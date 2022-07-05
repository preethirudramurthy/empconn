package com.empconn.service;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;
import org.thymeleaf.util.StringUtils;

import com.empconn.constants.ApplicationConstants;
import com.empconn.dto.AccountDropdownDto;
import com.empconn.dto.MasterResourceDto;
import com.empconn.dto.ProjectDropdownDto;
import com.empconn.dto.manager.GetResourcesResponseDto;
import com.empconn.enums.AccountCategory;
import com.empconn.enums.AccountStatus;
import com.empconn.enums.ProjectStatus;
import com.empconn.enums.UserRoles;
import com.empconn.mapper.AccountUnitValueMapper;
import com.empconn.mapper.ChecklistUnitValueMapper;
import com.empconn.mapper.DepartmentUnitValueMapper;
import com.empconn.mapper.EmployeeToMasterResourceDtoMapper;
import com.empconn.mapper.HorizontalUnitValueMapper;
import com.empconn.mapper.LocationUnitValueMapper;
import com.empconn.mapper.PrimarySkillUnitValueMapper;
import com.empconn.mapper.ProjectSubCategoryUnitValueMapper;
import com.empconn.mapper.ProjectUnitValueMapper;
import com.empconn.mapper.SecondarySkillUnitValueMapper;
import com.empconn.mapper.TitleUnitValueMapper;
import com.empconn.mapper.VerticalUnitValueMapper;
import com.empconn.mapper.WorkGroupMapper;
import com.empconn.persistence.entities.Account;
import com.empconn.persistence.entities.Checklist;
import com.empconn.persistence.entities.Department;
import com.empconn.persistence.entities.Employee;
import com.empconn.persistence.entities.Horizontal;
import com.empconn.persistence.entities.Location;
import com.empconn.persistence.entities.LocationHr;
import com.empconn.persistence.entities.PrimarySkill;
import com.empconn.persistence.entities.Project;
import com.empconn.persistence.entities.ProjectSubCategory;
import com.empconn.persistence.entities.SecondarySkill;
import com.empconn.persistence.entities.Title;
import com.empconn.persistence.entities.Vertical;
import com.empconn.persistence.entities.WorkGroup;
import com.empconn.repositories.AccountRepository;
import com.empconn.repositories.ChecklistRespository;
import com.empconn.repositories.DepartmentRepository;
import com.empconn.repositories.EmployeeRepository;
import com.empconn.repositories.HorizontalRepository;
import com.empconn.repositories.LocationHrRepository;
import com.empconn.repositories.LocationRepository;
import com.empconn.repositories.PrimarySkillRepository;
import com.empconn.repositories.ProjectLocationRespository;
import com.empconn.repositories.ProjectRepository;
import com.empconn.repositories.ProjectSubCategoryRepository;
import com.empconn.repositories.SecondarySkillRepository;
import com.empconn.repositories.TitleRepository;
import com.empconn.repositories.VerticalRepository;
import com.empconn.repositories.WorkGroupRepository;
import com.empconn.repositories.specification.ProjectSpecification;
import com.empconn.security.SecurityUtil;
import com.empconn.util.RolesUtil;
import com.empconn.utilities.IterableUtils;
import com.empconn.vo.UnitValue;

@Service
@Transactional
public class MasterService {

	private static final String ACTIVE = "ACTIVE";

	private static final Logger logger = LoggerFactory.getLogger(MasterService.class);

	@Autowired
	private VerticalRepository verticalRepository;

	@Autowired
	private PrimarySkillRepository primarySkillRepository;

	@Autowired
	private HorizontalRepository horizontalRepository;

	@Autowired
	private TitleRepository titleRepository;

	@Autowired
	private EmployeeRepository employeeRepository;

	@Autowired
	private ProjectSubCategoryRepository projectSubCategoryRepository;

	@Autowired
	private LocationRepository locationRepository;

	@Autowired
	private SecondarySkillRepository secondarySkillRepository;

	@Autowired
	private VerticalUnitValueMapper verticalUnitValueMapper;

	@Autowired
	private PrimarySkillUnitValueMapper primarySkillUnitValueMapper;

	@Autowired
	private SecondarySkillUnitValueMapper secondarySkillUnitValueMapper;

	@Autowired
	private TitleUnitValueMapper titleUnitValueMapper;

	@Autowired
	private EmployeeToMasterResourceDtoMapper employeeMasterResourceDtoMapper;

	@Autowired
	private HorizontalUnitValueMapper horizontalUnitValueMapper;

	@Autowired
	private ProjectSubCategoryUnitValueMapper projectSubCategoryUnitValueMapper;

	@Autowired
	private LocationUnitValueMapper locationUnitValueMapper;

	@Autowired
	private ChecklistRespository checklistRespository;

	@Autowired
	private ChecklistUnitValueMapper checklistUnitValueMapper;

	@Autowired
	private ProjectUnitValueMapper projectUnitValueMapper;

	@Autowired
	private AccountUnitValueMapper accountUnitValueMapper;

	@Autowired
	private AccountRepository accountRepository;

	@Autowired
	private ProjectRepository projectRepository;

	@Autowired
	private SecurityUtil jwtEmployeeUtil;

	@Autowired
	private WorkGroupRepository workGroupRepository;

	@Autowired
	private WorkGroupMapper workGroupMapper;

	@Autowired
	private DepartmentRepository departmentRepository;

	@Autowired
	private DepartmentUnitValueMapper departmentUnitValueMapper;

	@Autowired
	private ProjectLocationRespository projectLocationRespository;

	@Autowired
	private ProjectService projectService;

	@Autowired
	private LocationHrRepository locationHrRepository;

	public List<UnitValue> getVerticals() {
		logger.debug("Get the list of verticals");
		final List<Vertical> verticals = IterableUtils.toList(verticalRepository.findByIsActiveOrderByName(true));
		return verticalUnitValueMapper.verticalsToUnitValues(verticals);
	}

	public List<UnitValue> getNDDepartments() {
		logger.debug("Get the list of nd-department");
		final List<Department> departments = IterableUtils.toList(departmentRepository.findNDDepartment());
		return departmentUnitValueMapper.departmentsToUnitValues(departments);
	}

	public List<UnitValue> getPrimarySkills(String partialName) {
		logger.debug("Get the list of matching Primary Skills");
		final List<PrimarySkill> primarySkills = IterableUtils
				.toList(primarySkillRepository.findByIsActiveTrueAndNameContainingIgnoreCaseOrderByName(partialName));
		return primarySkillUnitValueMapper.primarySkillsToUnitValues(primarySkills);

	}

	public List<UnitValue> getTitles(String partialName) {
		logger.debug("Get the list of matching titles");
		final List<Title> titles = IterableUtils
				.toList(titleRepository.findByNameContainingIgnoreCaseOrderByName(partialName));
		return titleUnitValueMapper.titlesToUnitValues(titles);

	}

	public List<UnitValue> getProjectCategories() {
		logger.debug("Get the list of Categories");
		return AccountCategory.getUnitValues();
	}

	public List<UnitValue> getChecklists() {
		final List<Checklist> checklists = checklistRespository
				.findByIsActiveTrue(Sort.by(Sort.Direction.ASC, "checklistId"));
		return checklistUnitValueMapper.checklistsToUnitValues(checklists);
	}

	public List<UnitValue> getHorizontals() {
		final List<Horizontal> horizontals = IterableUtils
				.toList(horizontalRepository.findByIsActiveTrue(Sort.by(Sort.Direction.ASC, "name")));
		return horizontalUnitValueMapper.horizontalsToUnitValues(horizontals);
	}

	public List<UnitValue> getSubCategories() {
		final List<ProjectSubCategory> projectSubCategories = IterableUtils
				.toList(projectSubCategoryRepository.findByIsActiveTrue(Sort.by(Sort.Direction.ASC, "name")));
		return projectSubCategoryUnitValueMapper.projectSubCategoriesToUnitValues(projectSubCategories);
	}

	public List<UnitValue> getOrgLocations() {
		final List<Location> locations = IterableUtils.toList(locationRepository.findAllExcludingInternalLocations());
		return locationUnitValueMapper.locationsToUnitValues(locations);
	}


	public Set<MasterResourceDto> getNDResources(String partial) {
		final Set<Employee> employees = IterableUtils.toSet(employeeRepository.findByMatchingNameForND(partial))
				.stream().filter(e -> !RolesUtil.isSystemUser(e)).collect(Collectors.toSet());
		return employeeMasterResourceDtoMapper.employeesToMasterResourcesDto(employees);
	}

	public List<UnitValue> getMyHorizontals() {
		final Employee loginEmployee = jwtEmployeeUtil.getLoggedInEmployee();
		final String leadUserRole = projectService.getLeaduserRole(loginEmployee);
		if (leadUserRole.equals(UserRoles.GDM.name())) {
			final List<Project> projects = projectService
					.getProjectsForUserAsGdmOrManager(loginEmployee.getEmployeeId());
			final Set<Horizontal> horizontals = projects.stream().map(Project::getHorizontal)
					.collect(Collectors.toSet());
			final List<Horizontal> h = new ArrayList<>(horizontals);
			final List<UnitValue> values = horizontalUnitValueMapper.horizontalsToUnitValues(h);
			values.sort((p1, p2) -> p1.getValue().compareToIgnoreCase(p2.getValue()));
			return values;
		} else {
			final Iterable<Horizontal> horizontals = horizontalRepository
					.findByIsActiveTrue(Sort.by(Sort.Direction.ASC, "name"));
			return horizontalUnitValueMapper.horizontalsToUnitValues(IterableUtils.toList(horizontals));
		}
	}

	public List<UnitValue> getMyVerticalDropdown(final boolean ignoreRole) {
		final Employee loginEmployee = jwtEmployeeUtil.getLoggedInEmployee();
		final String leadUserRole = projectService.getLeaduserRole(loginEmployee);
		if (ignoreRole) {
			final Iterable<Vertical> varticals = verticalRepository
					.findByIsActiveTrue(Sort.by(Sort.Direction.ASC, "name"));
			return verticalUnitValueMapper.verticalsToUnitValues(IterableUtils.toList(varticals));
		} else {
			if (leadUserRole.equals(UserRoles.GDM.name())) {
				final List<Project> projects = projectService
						.getProjectsForUserAsGdmOrManager(loginEmployee.getEmployeeId());
				final Set<Vertical> varticals = projects.stream().map(p -> p.getAccount().getVertical())
						.collect(Collectors.toSet());
				final List<Vertical> v = new ArrayList<>(varticals);
				final List<UnitValue> values = verticalUnitValueMapper.verticalsToUnitValues(v);
				values.sort((p1, p2) -> p1.getValue().compareToIgnoreCase(p2.getValue()));
				return values;
			} else {
				final List<Vertical> verticals = verticalRepository.findByIsActiveOrderByName(true);
				return verticalUnitValueMapper.verticalsToUnitValues(verticals);
			}
		}
	}

	public List<UnitValue> getMySubCategories() {
		final Employee loginEmployee = jwtEmployeeUtil.getLoggedInEmployee();
		final String leadUserRole = projectService.getLeaduserRole(loginEmployee);
		if (leadUserRole.equals(UserRoles.GDM.name())) {
			final List<Project> projects = projectService
					.getProjectsForUserAsGdmOrManager(loginEmployee.getEmployeeId());
			final Set<ProjectSubCategory> subCategories = projects.stream().map(Project::getProjectSubCategory)
					.collect(Collectors.toSet());
			final List<ProjectSubCategory> sc = new ArrayList<>(subCategories);
			final List<UnitValue> values = projectSubCategoryUnitValueMapper.projectSubCategoriesToUnitValues(sc);
			values.sort((p1, p2) -> p1.getValue().compareToIgnoreCase(p2.getValue()));
			return values;
		} else {
			final Iterable<ProjectSubCategory> horizontals = projectSubCategoryRepository
					.findByIsActiveTrue(Sort.by(Sort.Direction.ASC, "name"));
			return projectSubCategoryUnitValueMapper
					.projectSubCategoriesToUnitValues(IterableUtils.toList(horizontals));
		}
	}

	public List<UnitValue> getMyProjectDropdown(final ProjectDropdownDto requestparams) {
		final String METHOD_NAME = "getMyProjectDropdown";
		logger.info("{} starts execution requestparams : {}", METHOD_NAME, requestparams);
		Set<UnitValue> unitValueSetOfProjects = null;
		List<UnitValue> projectDropdownData = null;
		try {
			final Long loggedInEmployeeId = jwtEmployeeUtil.getLoggedInEmployee().getEmployeeId();
			final Employee employee = employeeRepository.findByEmployeeId(loggedInEmployeeId);

			logger.debug("{} >>>>>>>>>>>>>>>>>>>>>accountList : {}, isActive : {}, verticalList : {}", METHOD_NAME,
					requestparams.getAccountIdList(), requestparams.getIsActive(), requestparams.getVerticalIdList());

			Set<Long> projectIds = null;
			if (!requestparams.isIgnoreRole()) {
				if (!RolesUtil.isRMG(employee)) {
					if (RolesUtil.isGDMAndManager(employee)) {
						projectIds = projectRepository.findAll().stream()
								.filter(p -> ((p.getEmployee1() != null
								&& p.getEmployee1().getEmployeeId().equals(employee.getEmployeeId()))
										|| (p.getEmployee2() != null
										&& p.getEmployee2().getEmployeeId().equals(employee.getEmployeeId()))))
								.filter(p -> p.getAccount().getStatus().equals(ACTIVE)
										&& p.getIsActive()
										&& p.getAccount().getIsActive())
								.map(Project::getProjectId).collect(Collectors.toSet());
						projectIds.addAll(projectLocationRespository.findAll().stream()
								.filter(a -> a.getAllManagers().values().stream()
										.anyMatch(e -> e.getEmployeeId().equals(employee.getEmployeeId())))
								.filter(p -> p.getProject().getAccount().getStatus().equals(ACTIVE)
										&& p.getIsActive()
										&& p.getProject().getAccount().getIsActive())
								.map(p -> p.getProject().getProjectId()).collect(Collectors.toSet()));
					} else if (RolesUtil.isGDM(employee)) {
						projectIds = projectRepository.findAll().stream()
								.filter(p -> ((p.getEmployee1() != null
								&& p.getEmployee1().getEmployeeId().equals(employee.getEmployeeId()))
										|| (p.getEmployee2() != null
										&& p.getEmployee2().getEmployeeId().equals(employee.getEmployeeId()))))
								.filter(p -> p.getAccount().getStatus().equals(ACTIVE)
										&& p.getIsActive()
										&& p.getAccount().getIsActive())
								.map(Project::getProjectId).collect(Collectors.toSet());
					} else if (RolesUtil.isAManager(employee)) {
						projectIds = projectLocationRespository.findAll().stream()
								.filter(a -> a.getAllManagers().values().stream()
										.anyMatch(e -> e.getEmployeeId().equals(employee.getEmployeeId())))
								.filter(p -> p.getProject().getAccount().getStatus().equals(ACTIVE)
										&& p.getIsActive()
										&& p.getProject().getAccount().getIsActive())
								.map(p -> p.getProject().getProjectId()).collect(Collectors.toSet());
					}
				}
			}
			if (projectIds == null || projectIds.isEmpty()) {
				unitValueSetOfProjects = projectUnitValueMapper.projectsToUnitValues(new HashSet<>());
			}

			if (unitValueSetOfProjects == null || unitValueSetOfProjects.isEmpty()) {
				requestparams.setProjectIds(projectIds);
				final List<Project> projects = projectRepository
						.findAll(ProjectSpecification.getProjectsSpec(requestparams));
				final List<Project> nonNDBenchProjects = projects.stream()
						.filter(p -> null != p && !StringUtils
						.equalsIgnoreCase(ApplicationConstants.NON_DELIVERY_BENCH_PROJECT_NAME, p.getName()))
						.collect(Collectors.toList());
				unitValueSetOfProjects = projectUnitValueMapper.projectsToUnitValues(nonNDBenchProjects);
			}

			projectDropdownData = new ArrayList<>(unitValueSetOfProjects);
			projectDropdownData.sort((p1, p2) -> p1.getValue().compareToIgnoreCase(p2.getValue()));
		} catch (final Exception exception) {
			logger.error("{} Exception raised as : {}", METHOD_NAME, exception);
		}
		logger.info("{} exits with return data size : {}", METHOD_NAME, (projectDropdownData == null || projectDropdownData.isEmpty())?0:projectDropdownData.size());
		return projectDropdownData;
	}

	public List<UnitValue> getMyAccountDropdown(final AccountDropdownDto requestparams) {
		final String METHOD_NAME = "getMyAccountDropdown";
		logger.info("{} starts execution requestparams : {}", METHOD_NAME, requestparams);
		Set<UnitValue> unitValueSet = new HashSet<>();
		List<UnitValue> values = new ArrayList<>();
		Set<Account> accounts;
		try {
			final List<Integer> verticalIdList = (requestparams.getVerticalIdList() != null)
					? requestparams.getVerticalIdList().stream().map(Integer::parseInt).collect(Collectors.toList())
							: null;
					final Employee employee = jwtEmployeeUtil.getLoggedInEmployee();

					if (requestparams.isIgnoreRole()) {
						if (CollectionUtils.isEmpty(verticalIdList)) {
							accounts = accountRepository.findAllByIsActiveAndStatusIgnoreCaseNot(Boolean.TRUE, AccountStatus.INACTIVE.name());
						} else {
							accounts = accountRepository.findByVerticalVerticalIdIn(verticalIdList);
						}
					} else {
						if (RolesUtil.isRMG(employee)) {
							if (CollectionUtils.isEmpty(verticalIdList)) {
								accounts = accountRepository.findAllByIsActiveAndStatusIgnoreCaseNot(Boolean.TRUE, AccountStatus.INACTIVE.name());
							} else {
								accounts = accountRepository.findByVerticalVerticalIdIn(verticalIdList);
							}
						} else if (RolesUtil.isGDMAndManager(employee)) {
							accounts = projectRepository.findAll().stream()
									.filter(p -> ((p.getEmployee1() != null
									&& p.getEmployee1().getEmployeeId().equals(employee.getEmployeeId()))
											|| (p.getEmployee2() != null
											&& p.getEmployee2().getEmployeeId().equals(employee.getEmployeeId()))))
									.filter(p -> (CollectionUtils.isEmpty(verticalIdList)
											|| verticalIdList.contains(p.getAccount().getVertical().getVerticalId()))
											&& p.getAccount().getStatus().equals(ACTIVE)
											&& p.getIsActive()
											&& Arrays.asList(ProjectStatus.PMO_APPROVED.name(),
													ProjectStatus.PROJECT_ON_HOLD.name()).contains(p.getCurrentStatus())
											&& p.getAccount().getIsActive())
									.map(Project::getAccount).collect(Collectors.toSet());
							accounts.addAll(projectLocationRespository.findAll().stream()
									.filter(a -> a.getAllManagers().values().stream()
											.anyMatch(e -> e.getEmployeeId().equals(employee.getEmployeeId())))
									.filter(p -> (CollectionUtils.isEmpty(verticalIdList) || verticalIdList
											.contains(p.getProject().getAccount().getVertical().getVerticalId()))
											&& p.getProject().getAccount().getStatus().equals(ACTIVE)
											&& p.getIsActive()
											&& Arrays
											.asList(ProjectStatus.PMO_APPROVED.name(),
													ProjectStatus.PROJECT_ON_HOLD.name())
											.contains(p.getProject().getCurrentStatus())
											&& p.getProject().getAccount().getIsActive())
									.map(p -> p.getProject().getAccount()).collect(Collectors.toSet()));
						} else if (RolesUtil.isGDM(employee)) {
							accounts = projectRepository.findAll().stream()
									.filter(p -> ((p.getEmployee1() != null
									&& p.getEmployee1().getEmployeeId().equals(employee.getEmployeeId()))
											|| (p.getEmployee2() != null
											&& p.getEmployee2().getEmployeeId().equals(employee.getEmployeeId()))))
									.filter(p -> (CollectionUtils.isEmpty(verticalIdList)
											|| verticalIdList.contains(p.getAccount().getVertical().getVerticalId()))
											&& p.getAccount().getStatus().equals(ACTIVE)
											&& p.getIsActive()
											&& Arrays.asList(ProjectStatus.PMO_APPROVED.name(),
													ProjectStatus.PROJECT_ON_HOLD.name()).contains(p.getCurrentStatus())
											&& p.getAccount().getIsActive())
									.map(Project::getAccount).collect(Collectors.toSet());
						} else if (RolesUtil.isAManager(employee)) {
							accounts = projectLocationRespository.findAll().stream()
									.filter(a -> a.getAllManagers().values().stream()
											.anyMatch(e -> e.getEmployeeId().equals(employee.getEmployeeId())))
									.filter(p -> (CollectionUtils.isEmpty(verticalIdList) || verticalIdList
											.contains(p.getProject().getAccount().getVertical().getVerticalId()))
											&& p.getProject().getAccount().getStatus().equals(ACTIVE)
											&& p.getIsActive()
											&& Arrays
											.asList(ProjectStatus.PMO_APPROVED.name(),
													ProjectStatus.PROJECT_ON_HOLD.name())
											.contains(p.getProject().getCurrentStatus())
											&& p.getProject().getAccount().getIsActive())
									.map(p -> p.getProject().getAccount()).collect(Collectors.toSet());
						} else {
							if (CollectionUtils.isEmpty(verticalIdList)) {
								accounts = accountRepository.findAllByIsActiveAndStatusIgnoreCaseNot(Boolean.TRUE, AccountStatus.INACTIVE.name());
							} else {
								accounts = accountRepository.findByVerticalVerticalIdIn(verticalIdList);
							}
						}
					}

					unitValueSet = accountUnitValueMapper.accountsToUnitValues(accounts);
					values = new ArrayList<>(unitValueSet);
					values.sort((p1, p2) -> p1.getValue().compareToIgnoreCase(p2.getValue()));
		} catch (final Exception exception) {
			logger.error("{} Exception raised as : {}", METHOD_NAME, exception.getMessage());
		}
		logger.info("{} exits with return data size : {}", METHOD_NAME, unitValueSet.size());
		return values;
	}

	public Set<UnitValue> getAccounts() {
		final Set<Account> accounts = IterableUtils.toSet(accountRepository.findAllByIsActiveAndStatusIgnoreCaseNot(Boolean.TRUE, AccountStatus.INACTIVE.name()));
		return accountUnitValueMapper.accountsToUnitValues(accounts);
	}

	public Set<UnitValue> getProjectForAccount(ProjectDropdownDto requestparams) {
		final Set<Project> projects = projectRepository.findByAccount(
				requestparams.getAccountIdList().stream().map(Integer::parseInt).collect(Collectors.toList()),
				requestparams.getIsActive());
		return projectUnitValueMapper.projectsToUnitValues(projects);
	}

	public List<UnitValue> getWorkgroupDropdown() {
		final List<WorkGroup> workGroups = IterableUtils
				.toList(workGroupRepository.findByIsActiveOrderByHierarchyAsc(Boolean.TRUE));
		return workGroupMapper.workGroupsToUnitValues(workGroups);
	}

	public Set<String> getLocationHr(Integer locationId) {
		final Set<LocationHr> findByLocation = locationHrRepository.findByLocationLocationId(locationId);
		return findByLocation.stream().map(e -> e.getEmployee().getEmail()).collect(Collectors.toSet());
	}

	public List<GetResourcesResponseDto> getAllResources(String partial) {
		final List<Employee> emps = employeeRepository.findEmployeesMatchingNameOrEmpCode(partial, partial);
		return emps.stream().filter(e -> !RolesUtil.isSystemUser(e))
				.map(e -> new GetResourcesResponseDto(e.getEmployeeId(), e.getFullName(), e.getEmpCode(),
						e.getTitle().getName()))
				.collect(Collectors.toList());
	}

	public List<MasterResourceDto> getDeliveryResources(String partial) {
		final List<Employee> emps = employeeRepository.findMatchingDeliveryResources(partial, partial);
		return emps.stream().filter(e -> !RolesUtil.isSystemUser(e))
				.map(e -> new MasterResourceDto(e.getEmployeeId().toString(), e.getFullName(), e.getEmpCode(),
						e.getTitle().getName()))
				.collect(Collectors.toList());
	}

	public List<UnitValue> getSecondarySkills(List<String> primarySkillIds) {
		final List<Integer> primarySkillIdList = primarySkillIds.stream().map(Integer::valueOf)
				.collect(Collectors.toList());
		final List<SecondarySkill> secondarySkills = secondarySkillRepository
				.findByPrimarySkillPrimarySkillIdInAndIsActiveTrueAndNameNotOrderByName(primarySkillIdList,
						ApplicationConstants.DEFAULT_SECONDARY_SKILL);
		return secondarySkillUnitValueMapper.secondarySkillsToUnitValues(secondarySkills);
	}

	public List<UnitValue> getSecondarySkills(String primarySkillId) {
		logger.debug("Get the list of secondary skills belonging to the given primary skill");
		return getSecondarySkills(Collections.singletonList(primarySkillId));
	}

}
