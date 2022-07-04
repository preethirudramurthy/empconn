package com.empconn.service;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;

import com.empconn.constants.ApplicationConstants;
import com.empconn.dto.MailForEarmarkProjectDto;
import com.empconn.dto.earmark.AvailableResourceDto;
import com.empconn.dto.earmark.AvailableResourceReqDto;
import com.empconn.dto.earmark.DropdownGDMDto;
import com.empconn.dto.earmark.DropdownManagerDto;
import com.empconn.dto.earmark.EarmarkAvailabilityDto;
import com.empconn.dto.earmark.EarmarkDetailsDto;
import com.empconn.dto.earmark.EarmarkEngineersGdmReqDto;
import com.empconn.dto.earmark.EarmarkEngineersManagerReqDto;
import com.empconn.dto.earmark.EarmarkEngineersRmgReqDto;
import com.empconn.dto.earmark.EarmarkItemDto;
import com.empconn.dto.earmark.EarmarkOppurtunityDto;
import com.empconn.dto.earmark.EarmarkProjectDto;
import com.empconn.dto.earmark.EarmarkSummaryDto;
import com.empconn.dto.earmark.EarmarkedDropdownReqDto;
import com.empconn.dto.earmark.GdmManagerDropdownReqDto;
import com.empconn.dto.earmark.ManagerInfoDto;
import com.empconn.dto.earmark.UpdateEarmarkDto;
import com.empconn.email.EmailService;
import com.empconn.exception.EmpConnException;
import com.empconn.exception.PreConditionFailedException;
import com.empconn.mapper.AllocationAvailableResourceDtoMapper;
import com.empconn.mapper.AllocationMapper;
import com.empconn.mapper.CommonQualifiedMapper;
import com.empconn.mapper.EarmarkToEarmarkDetailsDtoMapper;
import com.empconn.mapper.EmployeeToMasterInfoDtoMapper;
import com.empconn.mapper.ProjectDtoToEarmarkToEarmarkMapper;
import com.empconn.persistence.entities.Allocation;
import com.empconn.persistence.entities.Earmark;
import com.empconn.persistence.entities.Employee;
import com.empconn.persistence.entities.Opportunity;
import com.empconn.persistence.entities.Project;
import com.empconn.persistence.entities.ProjectLocation;
import com.empconn.persistence.entities.SalesforceIdentifier;
import com.empconn.repositories.AllocationRepository;
import com.empconn.repositories.EarmarkRepository;
import com.empconn.repositories.EarmarkSalesforceIdentifierRepository;
import com.empconn.repositories.EmployeeRepository;
import com.empconn.repositories.OpportunityRepository;
import com.empconn.repositories.ProjectRepository;
import com.empconn.repositories.specification.AllEngineersSpecification;
import com.empconn.repositories.specification.GDMSpecification;
import com.empconn.repositories.specification.ProjectSpec;
import com.empconn.security.SecurityUtil;
import com.empconn.util.RolesUtil;
import com.empconn.vo.UnitValue;

@Service
public class EarmarkService {
	private static final Logger logger = LoggerFactory.getLogger(EarmarkService.class);

	@Autowired
	private EarmarkRepository earmarkRepository;

	@Autowired
	private ProjectDtoToEarmarkToEarmarkMapper projectDtoToEarmarkToEarmarkMapper;

	@Autowired
	private EmployeeRepository employeeRepository;

	@Autowired
	private EmployeeToMasterInfoDtoMapper employeeToMasterInfoDtoMapper;

	@Autowired
	private OpportunityRepository opportunityRepository;

	@Autowired
	private EmailService emailService;

	@Autowired
	private EarmarkToEarmarkDetailsDtoMapper earmarkToEarmarkDetailsDtoMapper;

	@Autowired
	private SecurityUtil jwtEmployeeUtil;

	@Autowired
	private ProjectRepository projectRepository;

	@Autowired
	private AllocationRepository allocationRepository;


	@Autowired
	private SalesforceIdentifierService sfService;

	@Autowired
	private AllocationAvailableResourceDtoMapper allocationAvailableResourceDtoMapper;

	@Autowired
	private AllocationMapper allocationMapper;

	@Autowired
	EarmarkedEngineerProjectService earmarkedEngineerProjectService;

	@Autowired
	EarmarkedEngineerOpportunityService earmarkedEngineerOpportunityService;

	@Autowired
	EarmarkSalesforceIdentifierRepository earmarkSalesforceIdentifierRepository;

	public List<AvailableResourceDto> getResourceList(AvailableResourceReqDto availableResourceDto) {
		final List<Allocation> allocationResources = allocationRepository
				.findAll(new AllEngineersSpecification(availableResourceDto));
		return allocationAvailableResourceDtoMapper.allocationsToAvailableResourceDtos(allocationResources);
	}

	@Transactional
	public void unEarmark(List<String> earmarkIdList) {
		final String METHOD_NAME = "unEarmark";
		logger.info("{} starts execution successfully", METHOD_NAME);
		final List<Earmark> earmarks = getEarmarkDetails(earmarkIdList);
		unearmark(earmarks);
	}

	public void unearmark(List<Earmark> earmarks) {
		earmarks.forEach(e -> 
			e.setUnearmarkInfo(ApplicationConstants.USER, ApplicationConstants.UNEARMARK_USER_COMMENT)
		);

		// 1- Mark the status as false in Earmark table
		earmarkRepository.saveAll(earmarks);

		earmarkSalesforceIdentifierRepository
				.softDelete(earmarks.stream().map(Earmark::getEarmarkId).collect(Collectors.toList()));

		// allocationStatus when allocation is earmarked
		for (final Earmark earMark : earmarks) {
			mailForUnEarmark(earMark);
		}

	}

	@Transactional
	public List<Earmark> getEarmarkDetails(List<String> earmarkIdList) {
		return earmarkRepository
				.findByEarmarkIdIn(earmarkIdList.stream().map(Long::parseLong).collect(Collectors.toList()));
	}

	public EarmarkDetailsDto getMoreDetails(List<String> earmarkIdList) {
		Optional<Earmark> emOpt = earmarkRepository.findById(Long.valueOf(earmarkIdList.get(0)));
		if (emOpt.isPresent()) {
			final Earmark earList = emOpt.get();
			return earmarkToEarmarkDetailsDtoMapper.earmarksToEarmarkDetailsDtos(earList);
		}
		return null;
	}

	public void mailForUnEarmark(Earmark earmark) {
		final Map<String, Object> templateModel = new HashMap<>();
		final List<String> emailToList = new ArrayList<>();
		final List<String> emailCCList = new ArrayList<>();
		
		final Set<MailForEarmarkProjectDto> earmarkDtos = new HashSet<>(1);
		final Employee earmarkEmp = earmark.getAllocation().getEmployee();
		final MailForEarmarkProjectDto mailForEarMark = new MailForEarmarkProjectDto();
		mailForEarMark.setEmpCode(earmarkEmp.getEmpCode());
		mailForEarMark.setEmpName(earmarkEmp.getFullName());
		mailForEarMark.setTitle(earmarkEmp.getTitle().getName());
		mailForEarMark.setAccountName(CommonQualifiedMapper.earmarkToEarmarkAccountName(earmark));
		mailForEarMark.setProjectName(CommonQualifiedMapper.earmarkToEarmarkProjectName(earmark));
		mailForEarMark.setStartDate(new SimpleDateFormat(ApplicationConstants.DATE_FORMAT_DD_MMM_YYYY).format(earmark.getStartDate()));
		mailForEarMark.setEndDate(new SimpleDateFormat(ApplicationConstants.DATE_FORMAT_DD_MMM_YYYY).format(earmark.getEndDate()));
		mailForEarMark.setBillable(earmark.getBillable() ? "Yes" : "No");

		earmarkDtos.add(mailForEarMark);
		templateModel.put("earMark", earmarkDtos);
		emailService.send("resource-un-earmark", templateModel, emailToList.toArray(new String[emailToList.size()]),
				emailCCList.toArray(new String[emailCCList.size()]));

	}

	@Transactional
	public void saveEarmarkProject(EarmarkProjectDto earmarkProjectDto) {
		validateSaveEarmarkProject(earmarkProjectDto);
		final List<Earmark> earmarks = projectDtoToEarmarkToEarmarkMapper
				.earmarkProjectDtoToEarmarkList(earmarkProjectDto);
		earmarkRepository.saveAll(earmarks);
		mailForEarmark(earmarks);
	}

	public void validateSaveEarmarkProject(EarmarkProjectDto dto) {
		Optional<Project> pOpt = projectRepository.findById(Long.valueOf(dto.getProjectId()));
		final Project project = pOpt.isPresent()? pOpt.get() : null;
		if (project != null) {
			final List<String> projectSfList = project.getSalesforceIdentifiers().stream()
					.map(SalesforceIdentifier::getValue).collect(Collectors.toList());

			dto.getEarmarkList().stream().forEach(e -> {
				final Optional<List<Earmark>> earmark = earmarkRepository
						.findByAllocationAllocationIdAndProjectProjectIdAndIsActiveIsTrue(
								Long.valueOf(e.getAllocationId()), Long.valueOf(dto.getProjectId()));
				if (earmark.isPresent())
					throw new PreConditionFailedException("ResourceNotAlreadyEarmarkedForTheProject");
			});
			if (!CollectionUtils.isEmpty(dto.getSalesforceIdList())) {
				dto.getSalesforceIdList().removeAll(projectSfList);
				for (final String sf : dto.getSalesforceIdList()) {
					if (!sfService.isValidSalesforceIdForProject(sf, Long.valueOf(dto.getProjectId()))) {
						logger.debug("SalesForce ID is already being used for some other Project");
						throw new EmpConnException("SalesforceIdMustNotAlreadyExistProj");
					}
				}
			}
		}
	}

	@Transactional
	public void saveEarmarkForOppurtunity(EarmarkOppurtunityDto earmarkOppurtunityDto) {
		Opportunity opportunity = opportunityRepository.findByNameAndAccountNameAndIsActiveIsTrue(
				earmarkOppurtunityDto.getOpportunityName(), earmarkOppurtunityDto.getAccountName());
		validateSaveEarmarkOpportunity(earmarkOppurtunityDto, opportunity);

		if (opportunity == null) {
			opportunity = projectDtoToEarmarkToEarmarkMapper.earmarkOppurtunityDtoToOpportunity(earmarkOppurtunityDto);
			opportunity = opportunityRepository.save(opportunity);
		}

		final List<Earmark> earmarks = projectDtoToEarmarkToEarmarkMapper
				.earmarkOppurtunityDtoToEarmarkList(earmarkOppurtunityDto, opportunity);
		earmarkRepository.saveAll(earmarks);
		mailForEarmark(earmarks);
	}

	public void validateSaveEarmarkOpportunity(EarmarkOppurtunityDto dto, Opportunity opportunity) {
		if (!CollectionUtils.isEmpty(dto.getSalesforceIdList())) {
			for (final String sf : dto.getSalesforceIdList()) {
				if (opportunity != null) {
					if (!sfService.isValidSalesforceIdForOppurtunity(sf, opportunity.getOpportunityId())) {
						logger.debug("SalesForce ID is already being used for some other Opportunity");
						throw new EmpConnException("SalesforceIdMustNotAlreadyExistProj");
					}
				} else {
					if (!sfService.isValidSalesforceId(sf)) {
						logger.debug("SalesForce ID is already being used for some other existing Project");
						throw new EmpConnException("SalesforceIdMustNotAlreadyExistProj");
					}
				}

			}
		}
		if (opportunity != null) {
			dto.getEarmarkList().forEach(e -> {
				final Optional<List<Earmark>> alreadyEarmark = earmarkRepository
						.findByAllocationAndOppurtunity(Long.parseLong(e.getAllocationId()), dto.getOpportunityName());
				if (alreadyEarmark.isPresent()) {
					throw new EmpConnException("ResourceAlreadyEarmarkedForOpportunity");
				}
			});
		}
	}

	public Set<ManagerInfoDto> getManagerDropdown(DropdownManagerDto managerDropdownDto) {
		final Set<ManagerInfoDto> managerInfoDtos = new HashSet<>();
		final Set<Employee> empList = new HashSet<>();
		final Employee loginUser = jwtEmployeeUtil.getLoggedInEmployee();

		final List<Project> projects = projectRepository
				.findAll(ProjectSpec.getProjectManagerByAccountProjectVertical(managerDropdownDto));

		if (!projects.isEmpty()) {

			if (RolesUtil.isGDM(loginUser)) {
				projects.forEach(p -> {

					if ((p.getEmployee1() != null && p.getEmployee1().getEmployeeId().equals(loginUser.getEmployeeId())
							&& p.getEmployee2() == null)
							|| (p.getEmployee2() != null
									&& p.getEmployee2().getEmployeeId().equals(loginUser.getEmployeeId())
									&& p.getEmployee1() == null)) {
						p.getProjectLocations().stream().map(e -> empList.addAll(e.getAllManagers().values()))
								.collect(Collectors.toList());
					} else if ((p.getEmployee1() != null
							&& p.getEmployee1().getEmployeeId().equals(loginUser.getEmployeeId())
							&& p.getEmployee2() != null)) {
						empList.addAll(
								p.getProjectLocations().stream().flatMap(e -> e.getAllManagers().entrySet().stream())
										.filter(k -> !k.getKey().equals(ApplicationConstants.QA_WORK_GROUP))
										.map(Entry::getValue).collect(Collectors.toList()));

					} else if ((p.getEmployee2() != null
							&& p.getEmployee2().getEmployeeId().equals(loginUser.getEmployeeId())
							&& p.getEmployee1() != null)) {
						empList.addAll(
								p.getProjectLocations().stream().flatMap(e -> e.getAllManagers().entrySet().stream())
										.filter(k -> k.getKey().equals(ApplicationConstants.QA_WORK_GROUP))
										.map(Entry::getValue).collect(Collectors.toList()));
					}

				});

			} else {
				projects.forEach(p -> p.getProjectLocations().stream().map(e -> empList.addAll(e.getAllManagers().values()))
							.collect(Collectors.toList())
				);
			}
			managerInfoDtos.addAll(employeeToMasterInfoDtoMapper.employeesToMastersDto(empList));
		} else {
			final Set<Employee> employeeList = employeeRepository.findByIsManager(Boolean.TRUE);
			managerInfoDtos.addAll(employeeToMasterInfoDtoMapper.employeesToMastersDto(employeeList));
		}
		return managerInfoDtos;
	}

	public void updateEarmark(UpdateEarmarkDto updateEarmarkDto) {
		final String METHOD_NAME = "updateEarmark";
		logger.info("{} starts execution.", METHOD_NAME);
		final Long earmarkId = Long.parseLong(updateEarmarkDto.getEarmarkId());
		final Integer percentage = updateEarmarkDto.getAllocationPercentage();
		final boolean billable = updateEarmarkDto.getBillable();
		final boolean clientInterviewNeeded = updateEarmarkDto.getClientInterviewNeeded();
		logger.debug(" {} earmarkId : {}, percentage : {}, billable : {}, clientInterviewNeeded : {}", METHOD_NAME,
				earmarkId, percentage, billable, clientInterviewNeeded);
		earmarkRepository.updateEarmark(earmarkId, percentage, billable, clientInterviewNeeded);
	}

	public void mailForEarmark(List<Earmark> earMarks) {
		final Map<String, Object> templateModel = new HashMap<>();
		final List<String> emailToList = new ArrayList<>();
		final List<String> emailCCList = new ArrayList<>();
		
		final Set<MailForEarmarkProjectDto> earmarkDtos = new HashSet<>(earMarks.size());
		for (final Earmark earmark : earMarks) {
			final Employee earmarkEmp = earmark.getAllocation().getEmployee();

			final MailForEarmarkProjectDto mailForEarMark = new MailForEarmarkProjectDto();
			mailForEarMark.setEmpCode(earmarkEmp.getEmpCode());
			mailForEarMark.setEmpName(earmarkEmp.getFullName());
			mailForEarMark.setTitle(earmarkEmp.getTitle().getName());
			mailForEarMark.setAccountName(CommonQualifiedMapper.earmarkToEarmarkAccountName(earmark));
			mailForEarMark.setProjectName(CommonQualifiedMapper.earmarkToEarmarkProjectName(earmark));
			mailForEarMark.setStartDate(new SimpleDateFormat(ApplicationConstants.DATE_FORMAT_DD_MMM_YYYY).format(earmark.getStartDate()));
			mailForEarMark.setEndDate(new SimpleDateFormat(ApplicationConstants.DATE_FORMAT_DD_MMM_YYYY).format(earmark.getEndDate()));
			mailForEarMark.setBillable(earmark.getBillable() ? "Yes" : "No");
			earmarkDtos.add(mailForEarMark);
		}

		templateModel.put("earMark", earmarkDtos);
		emailService.send("resource-earmark-project", templateModel,
				emailToList.toArray(new String[emailToList.size()]),
				emailCCList.toArray(new String[emailCCList.size()]));

	}

	public List<ManagerInfoDto> getAllGdm(DropdownGDMDto request) {
		request.setAllProjects(projectRepository.getActiveAndOnHoldProjects());

		return employeeRepository.findAll(GDMSpecification.getAllGDMSpec(request)).stream().map(e -> {
			final ManagerInfoDto manager = new ManagerInfoDto();
			manager.setId(String.valueOf(e.getEmployeeId()));
			manager.setEmpCode(e.getEmpCode());
			manager.setTitle(e.getTitle().getName());
			manager.setValue(StringUtils.join(e.getFirstName(), " ", e.getLastName()));
			return manager;
		}).collect(Collectors.toList());
	}

	public EarmarkAvailabilityDto getEarmarkAvailability(String allocationId) {
		EarmarkAvailabilityDto availabilityDto = null;
		Optional<Allocation> allocOpt = allocationRepository.findById(Long.valueOf(allocationId));
		final Allocation allocation = allocOpt.isPresent()?allocOpt.get():null;
		final Optional<List<Earmark>> earmarksForAllocation = earmarkRepository
				.findByAllocationAllocationIdAndIsActiveIsTrue(Long.valueOf(allocationId));
		if (allocation != null) {
			availabilityDto = allocationMapper.allocationToToEarmarkAvailabilityDto(allocation);
			if (earmarksForAllocation.isPresent()) {
				final List<EarmarkSummaryDto> dtos = allocationMapper
						.earMarkToEarmarkSummaryDtoList(earmarksForAllocation.get());
				availabilityDto.setEarmarkList(dtos);
			}
		}
		return availabilityDto;
	}

	public void unearmarkBySystem(Earmark earmark, String comment) {
		logger.debug("unearmarkBySystem");

		earmark.setUnearmarkInfo(ApplicationConstants.SYSTEM, comment);
		earmarkRepository.save(earmark);

		mailForUnEarmarkBySystem(earmark);

	}

	public void mailForUnEarmarkBySystem(Earmark earmark) {
		final Map<String, Object> templateModel = new HashMap<>();

		final List<String> gdmEmails = (earmark.getProject() != null)
				? earmark.getProject().getGdms().values().stream().map(Employee::getEmail).collect(Collectors.toList())
				: earmark.getOpportunity().getGdms().values().stream().map(Employee::getEmail)
						.collect(Collectors.toList());

		final List<String> emailToList = new ArrayList<>();
		if (earmark.getEmployee2() != null)
			emailToList.add(earmark.getEmployee2().getEmail());

		if (!gdmEmails.isEmpty())
			emailToList.addAll(gdmEmails);

		final Set<MailForEarmarkProjectDto> earmarkDtos = new HashSet<>(1);
		final MailForEarmarkProjectDto mailForEarMark = new MailForEarmarkProjectDto(
				earmark.getAllocation().getEmployee().getEmpCode(), earmark.getAllocation().getEmployee().getFullName(),
				earmark.getAllocation().getEmployee().getTitle().getName(),
				earmark.getProject() != null ? earmark.getProject().getAccount().getName()
						: earmark.getOpportunity().getAccountName(),
				earmark.getProject() != null ? earmark.getProject().getName() : earmark.getOpportunity().getName(),
				new SimpleDateFormat(ApplicationConstants.DATE_FORMAT_DD_MMM_YYYY).format(earmark.getStartDate()),
				new SimpleDateFormat(ApplicationConstants.DATE_FORMAT_DD_MMM_YYYY).format(earmark.getEndDate()), earmark.getBillable() ? "Yes" : "No");
		earmarkDtos.add(mailForEarMark);
		templateModel.put("earMark", earmarkDtos);
		emailService.send("resource-un-earmark", templateModel, emailToList.toArray(new String[emailToList.size()]),
				null);

	}

	public List<EarmarkItemDto> getEarmarkListAsManager(EarmarkEngineersManagerReqDto dto) {
		if (dto.getIsOpp())
			return earmarkedEngineerOpportunityService.getEarmarkListAsManager(dto);
		else
			return earmarkedEngineerProjectService.getEarmarkListAsManager(dto);
	}

	public List<EarmarkItemDto> getEarmarkListAsGdmManager(EarmarkEngineersGdmReqDto dto) {
		if (dto.getIsOpp())
			return earmarkedEngineerOpportunityService.getEarmarkListAsGdmManager(dto);
		else
			return earmarkedEngineerProjectService.getEarmarkListAsGdmManager(dto);
	}

	public List<EarmarkItemDto> getEarmarkListAsRmg(EarmarkEngineersRmgReqDto dto) {
		if (dto.getIsOpp())
			return earmarkedEngineerOpportunityService.getEarmarkListAsRmg(dto);
		else
			return earmarkedEngineerProjectService.getEarmarkListAsRmg(dto);
	}

	public List<UnitValue> getEarmarkedVerticalDropdown(EarmarkedDropdownReqDto dto) {
		if (dto.getIsOpp())
			return earmarkedEngineerOpportunityService.getEarmarkedVerticalDropdown(dto);
		else
			return earmarkedEngineerProjectService.getEarmarkedVerticalDropdown(dto);
	}

	public List<UnitValue> getEarmarkedAccountDropdown(EarmarkedDropdownReqDto dto) {
		if (dto.getIsOpp())
			return earmarkedEngineerOpportunityService.getEarmarkedAccountDropdown(dto);
		else
			return earmarkedEngineerProjectService.getEarmarkedAccountDropdown(dto);
	}

	public List<UnitValue> getEarmarkedProjectDropdown(EarmarkedDropdownReqDto dto) {
		if (dto.getIsOpp())
			return earmarkedEngineerOpportunityService.getEarmarkedOpportunityDropdown(dto);
		else
			return earmarkedEngineerProjectService.getEarmarkedProjectDropdown(dto);
	}

	public List<String> getEarmarkedSalesforceSearch(EarmarkedDropdownReqDto dto) {
		if (dto.getIsOpp())
			return earmarkedEngineerOpportunityService.getEarmarkedSalesforceSearch(dto);
		else
			return earmarkedEngineerProjectService.getEarmarkedSalesforceSearch(dto);
	}

	public List<ManagerInfoDto> getEarmarkedGdmDropdown(GdmManagerDropdownReqDto dto) {
		if (dto.getIsOpp())
			return earmarkedEngineerOpportunityService.getEarmarkedGdmDropdown(dto);
		else
			return earmarkedEngineerProjectService.getEarmarkedGdmDropdown(dto);
	}

	public List<ManagerInfoDto> getEarmarkedManagerDropdown(GdmManagerDropdownReqDto dto) {
		if (dto.getIsOpp())
			return earmarkedEngineerOpportunityService.getEarmarkedManagerDropdown(dto);
		else
			return earmarkedEngineerProjectService.getEarmarkedManagerDropdown(dto);
	}

	public Set<ManagerInfoDto> getSelectedManagerDropdown(String projectId) {

		Optional<Project> pOpt = projectRepository.findById(Long.valueOf(projectId));
		final Project project = pOpt.isPresent() ? pOpt.get() : null;
		if (project != null) {
			final List<Employee> managerList = getProjectManagersList(project);

			if (RolesUtil.isRMG(jwtEmployeeUtil.getLoggedInEmployee()))
				return getManagerInfoDtoList(managerList);

			if (RolesUtil.isGDM(jwtEmployeeUtil.getLoggedInEmployee())) {
				if (!isUserGdmOfProject(project))
					return getManagerInfoDtoIfUserIsProjectManager(managerList);
				return getManagerInfoDtoList(managerList);
			}

			if (RolesUtil.isAManager(jwtEmployeeUtil.getLoggedInEmployee())) {
				return getManagerInfoDtoIfUserIsProjectManager(managerList);
			}
		}

		// the user is allowed to get the data only for the above conditions
		return Collections.emptySet();
	}

	private Set<ManagerInfoDto> getManagerInfoDtoIfUserIsProjectManager(final List<Employee> managerList) {
		final Optional<Employee> checkUserIsManagerOfProject = managerList.stream()
				.filter(e -> e.getEmployeeId().equals(jwtEmployeeUtil.getLoggedInEmployeeId())).findAny();
		if (checkUserIsManagerOfProject.isPresent()) {
			final Employee gdmManager = checkUserIsManagerOfProject.get();
			final ManagerInfoDto gdmManagerDto = new ManagerInfoDto(gdmManager.getEmployeeId().toString(),
					gdmManager.getFullName(), gdmManager.getEmpCode(), gdmManager.getTitle().getName());
			return Arrays.asList(gdmManagerDto).stream().collect(Collectors.toSet());
		}
		return Collections.emptySet();
	}

	private Set<ManagerInfoDto> getManagerInfoDtoList(final List<Employee> managerList) {
		return managerList.stream().map(e -> new ManagerInfoDto(e.getEmployeeId().toString(), e.getFullName(),
				e.getEmpCode(), e.getTitle().getName())).collect(Collectors.toSet());
	}

	private List<Employee> getProjectManagersList(final Project project) {
		final List<Employee> managerList = new ArrayList<>();
		project.getProjectLocations().stream().filter(ProjectLocation::getIsActive)
				.forEach(pl -> managerList.addAll(pl.getAllManagers().values()));
		return managerList;
	}

	private boolean isUserGdmOfProject(Project project) {
		return project.getGdms().values().stream()
				.anyMatch(e -> e.getEmployeeId().equals(jwtEmployeeUtil.getLoggedInEmployeeId()));
	}

}
