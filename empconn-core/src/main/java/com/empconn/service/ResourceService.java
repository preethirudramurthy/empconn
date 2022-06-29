package com.empconn.service;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.data.domain.Sort.Order;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.empconn.constants.ApplicationConstants;
import com.empconn.dto.ResourceStatusDto;
import com.empconn.dto.ResourceViewRequestDto;
import com.empconn.dto.UpdateUserRoleDto;
import com.empconn.dto.UserInfoDto;
import com.empconn.dto.UsersRoleValidityRequestDto;
import com.empconn.dto.UsersRoleValidityResponseDto;
import com.empconn.dto.allocation.ResourceViewResponseDto;
import com.empconn.exception.EmpConnException;
import com.empconn.mapper.EmployeeUserInfoDtoMapper;
import com.empconn.mapper.ResourceViewMapper;
import com.empconn.mapper.RoleEmployeeRoleMapper;
import com.empconn.persistence.entities.Allocation;
import com.empconn.persistence.entities.AllocationStatus;
import com.empconn.persistence.entities.Earmark;
import com.empconn.persistence.entities.Employee;
import com.empconn.persistence.entities.EmployeeRole;
import com.empconn.persistence.entities.Project;
import com.empconn.persistence.entities.ProjectLocation;
import com.empconn.persistence.entities.Role;
import com.empconn.repositories.AllocationRepository;
import com.empconn.repositories.AllocationStatusRepository;
import com.empconn.repositories.EmployeeRepository;
import com.empconn.repositories.ProjectLocationRespository;
import com.empconn.repositories.ProjectRepository;
import com.empconn.repositories.RoleRepository;
import com.empconn.repositories.specification.ResourceViewSpecification;
import com.empconn.security.SecurityUtil;
import com.empconn.util.RolesUtil;
import com.empconn.vo.UnitValue;

@Service
public class ResourceService {

	@Autowired
	private AllocationRepository allocationRepository;
	@Autowired
	private ResourceViewMapper resourceViewmapper;
	@Autowired
	private SecurityUtil securityUtil;

	@Autowired
	private ProjectLocationRespository projectLocationRespository;

	@Autowired
	private ProjectRepository projectRespository;

	@Autowired
	private AllocationStatusRepository allocationStatusRepository;

	@Autowired
	private CommonBenchService benchService;

	@Autowired
	private EarmarkService earmarkService;

	@Autowired
	private EmployeeRepository employeeRepository;

	@Autowired
	private EmployeeUserInfoDtoMapper employeeUserInfoDtoMapper;

	@Autowired
	RoleRepository roleRepository;

	@Autowired
	RoleEmployeeRoleMapper roleEmployeeRoleMapper;

	public List<ResourceViewResponseDto> getResourceList(ResourceViewRequestDto resourceViewReqDto) {

		final Set<String> myProjects = getMyProjects(resourceViewReqDto);
		if (myProjects != null && myProjects.size() > 0)
			resourceViewReqDto.getProhectIdsForManager().addAll(myProjects);
		if (resourceViewReqDto.getGdmIdListId() != null && !resourceViewReqDto.getGdmIdListId().isEmpty()
				&& resourceViewReqDto.getGdmIdListId().size() > 0) {

			final Set<Project> projectGdms = projectRespository.findGdmForProject(
					resourceViewReqDto.getGdmIdListId().stream().map(Long::valueOf).collect(Collectors.toList())

			);

			resourceViewReqDto.getProjectsByGdms().addAll(projectGdms);
		}

		return resourceViewmapper.allocationToAvailableResourceDtoList(
				allocationRepository.findAll(new ResourceViewSpecification(resourceViewReqDto)));
	}

	private Set<String> getMyProjects(ResourceViewRequestDto filter) {
		final Employee loggedInUser = securityUtil.getLoggedInEmployee();
		final Boolean amIManager = RolesUtil.isAManager(loggedInUser);

		final Boolean amIGDM = RolesUtil.isGDM(loggedInUser);

		final Boolean amIGDMManager = RolesUtil.isGDMAndManager(loggedInUser);
		final Set<String> projectIdList = new HashSet<String>();
		if (amIManager || amIGDMManager) {

			final List<ProjectLocation> myProjectLocations = projectLocationRespository.findAll().stream()
					.filter(a -> a.getAllManagers().values().stream()
							.anyMatch(e -> e.getEmployeeId().equals(loggedInUser.getEmployeeId())))
					.collect(Collectors.toList());
			projectIdList.addAll(myProjectLocations.stream().map(s -> s.getProject().getProjectId().toString())
					.collect(Collectors.toSet()));
		}
		if (amIGDM)
			projectIdList.addAll(projectRespository.findAll().stream()
					.filter(a -> (a.getEmployee1() == null ? false
							: a.getEmployee1().getEmployeeId().equals(loggedInUser.getEmployeeId()))
							|| (a.getEmployee2() == null ? false
									: a.getEmployee2().getEmployeeId().equals(loggedInUser.getEmployeeId())))
					.map(s -> s.getProjectId().toString()).collect(Collectors.toSet()));

		return projectIdList;
	}

	@Transactional
	public void goOnLongLeave(ResourceStatusDto requestDto) {
		final Set<Allocation> allocations = allocationRepository
				.findByEmployeeEmployeeIdAndIsActive(Long.valueOf(requestDto.getResourceId()), true);

		final Optional<String> allocated = allocations.stream().map(a -> a.getProject().getName())
				.filter(a -> !benchService.getBenchProjectNames().contains(a)).findAny();
		final AllocationStatus longLeave = allocationStatusRepository
				.findByStatus(ApplicationConstants.ALLOCATION_STATUS_LONG_LEAVE);

		if (allocated.isPresent()) {
			throw new EmpConnException("NotAllocatedToProject");
		} else {
			for (final Allocation allocation : allocations) {
				final List<Earmark> earmarksList = allocation.getEarmarks().stream().filter(Earmark::getIsActive)
						.collect(Collectors.toList());
				if (!earmarksList.isEmpty()) {
					for (final Earmark earmark : earmarksList) {
						earmarkService.unearmarkBySystem(earmark, ApplicationConstants.UNEARMARK_ON_LONG_LEAVE_COMMENT);
					}
					final Optional<Allocation> a = allocationRepository.findById(allocation.getAllocationId());
					a.get().setAllocationStatus(longLeave);
					allocationRepository.save(a.get());
				} else {
					allocation.setAllocationStatus(longLeave);
					allocationRepository.save(allocation);
				}
			}
		}
	}

	@Transactional
	public void goOnSabbatical(ResourceStatusDto requestDto) {
		final Set<Allocation> allocations = allocationRepository
				.findByEmployeeEmployeeIdAndIsActive(Long.valueOf(requestDto.getResourceId()), true);

		final Optional<String> allocated = allocations.stream().map(a -> a.getProject().getName())
				.filter(a -> !benchService.getBenchProjectNames().contains(a)).findAny();
		final AllocationStatus sbLeave = allocationStatusRepository
				.findByStatus(ApplicationConstants.ALLOCATION_STATUS_SABBATICAL);

		if (allocated.isPresent()) {
			throw new EmpConnException("NotAllocatedToProject");
		} else {
			for (final Allocation allocation : allocations) {
				final List<Earmark> earmarksList = allocation.getEarmarks().stream().filter(Earmark::getIsActive)
						.collect(Collectors.toList());
				if (!earmarksList.isEmpty()) {
					for (final Earmark earmark : earmarksList) {
						earmarkService.unearmarkBySystem(earmark,
								ApplicationConstants.UNEARMARK_ON_SABATICAL_LEAVE_COMMENT);
					}
					final Optional<Allocation> a = allocationRepository.findById(allocation.getAllocationId());
					a.get().setAllocationStatus(sbLeave);
					allocationRepository.save(a.get());
				} else {
					allocation.setAllocationStatus(sbLeave);
					allocationRepository.save(allocation);
				}
			}
		}
	}

	@Transactional
	public void goOnPureBench(ResourceStatusDto requestDto) {
		final Set<Allocation> allocations = allocationRepository
				.findByEmployeeEmployeeIdAndIsActive(Long.valueOf(requestDto.getResourceId()), true);
		final AllocationStatus pbLeave = allocationStatusRepository
				.findByStatus(ApplicationConstants.ALLOCATION_STATUS_PUREBENCH);
		allocations.stream().forEach(a -> a.setAllocationStatus(pbLeave));
		allocationRepository.saveAll(allocations);
	}

	public List<UserInfoDto> getAllUsers() {
		final List<Order> orders = new ArrayList<Order>();
		final Order statusOrder = new Order(Sort.Direction.DESC, "isActive");
		final Order fullNameOrder = new Order(Sort.Direction.ASC, "fullName");
		orders.add(statusOrder);
		orders.add(fullNameOrder);
		final List<Employee> employees = employeeRepository.findAll(Sort.by(orders));
		return employeeUserInfoDtoMapper.employeeToUserInfoDto(employees);
	}

	public void updateUserRoles(UpdateUserRoleDto dto) {
		final Employee employee = employeeRepository.findById(Long.valueOf(dto.getResourceId())).get();
		final List<String> currentRoles = employee.getEmployeeRoles().stream().filter(EmployeeRole::getIsActive)
				.map(e -> e.getRole().getName()).collect(Collectors.toList());

		final List<String> updateRoles = dto.getRoleList().stream()
				.filter(r -> !currentRoles.stream().filter(c -> c.equals(r)).findAny().isPresent())
				.collect(Collectors.toList());

		for (final EmployeeRole empRole : employee.getEmployeeRoles()) {
			if (empRole.getIsActive() && !dto.getRoleList().contains(empRole.getRole().getName()))
				empRole.setIsActive(false);

		}

		final List<Role> updateRoleList = roleRepository.findByNameIn(updateRoles);

		for (final Role role : updateRoleList)
			employee.addEmployeeRole(roleEmployeeRoleMapper.roleToEmployeeRole(role));

		employeeRepository.save(employee);

	}

	public UsersRoleValidityResponseDto isValidRoleForUsers(UsersRoleValidityRequestDto dto) {

		final Role role = roleRepository.findByNameIgnoreCase(dto.getRole());
		final List<Long> empIds = dto.getResourceIds().stream().map(Long::valueOf).collect(Collectors.toList());
		final List<Employee> employees = employeeRepository.findByEmployeeIdIn(empIds);

		final UsersRoleValidityResponseDto response = new UsersRoleValidityResponseDto();
		final List<UnitValue> invalidRoleUsers = new ArrayList<>();

		for (final Employee emp : employees) {
			if (!RolesUtil.isValidRoleForUser(emp, role))
				invalidRoleUsers.add(new UnitValue(emp.getEmployeeId(), emp.getFullName()));
		}
		response.setResources(invalidRoleUsers);
		response.setIsValid(!(invalidRoleUsers.size() > 0));
		return response;
	}

	public void assignUserRole(List<String> empIds, String roleName) {
		final List<Employee> employees = employeeRepository
				.findByEmployeeIdIn(empIds.stream().map(Long::valueOf).collect(Collectors.toList()));
		final Role role = roleRepository.findByName(roleName);

		for (final Employee employee : employees) {
			if (!RolesUtil.isValidRoleForUser(employee, role)) {
				employee.addEmployeeRole(roleEmployeeRoleMapper.roleToEmployeeRole(role));
				employeeRepository.save(employee);
			}
		}
	}
}
