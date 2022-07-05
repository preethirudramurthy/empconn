package com.empconn.service;

import java.util.*;
import java.util.stream.Collectors;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;

import com.empconn.constants.ApplicationConstants;
import com.empconn.constants.Roles;
import com.empconn.dto.IsValidDto;
import com.empconn.dto.PinStatusChangeCommentDto;
import com.empconn.dto.PinStatusChangeDto;
import com.empconn.dto.PinStatusChangedDto;
import com.empconn.dto.ProjectDetailsDto;
import com.empconn.dto.ProjectDropdownDto;
import com.empconn.dto.ProjectEndDateChangeDto;
import com.empconn.dto.ProjectEndDateChangedDto;
import com.empconn.dto.ProjectForAllocationDto;
import com.empconn.dto.ProjectStatusChangeCommentDto;
import com.empconn.dto.ProjectSummaryDto;
import com.empconn.dto.ProjectSummarySearchDto;
import com.empconn.dto.UpdateLocationManagersDto;
import com.empconn.dto.UpdateProjectDetailsDto;
import com.empconn.dto.allocation.ChangeReleaseDateDto;
import com.empconn.dto.allocation.ChangeReleaseDateListDto;
import com.empconn.enums.ProjectStatus;
import com.empconn.enums.UserRoles;
import com.empconn.exception.EmpConnException;
import com.empconn.map.MapHandler;
import com.empconn.map.MapService;
import com.empconn.mapper.CommonQualifiedMapper;
import com.empconn.mapper.ProjectCommentDtoMapper;
import com.empconn.mapper.ProjectDetailsMapper;
import com.empconn.mapper.ProjectProjectSummaryDtoMapper;
import com.empconn.mapper.ProjectUnitValueMapper;
import com.empconn.persistence.entities.Allocation;
import com.empconn.persistence.entities.Earmark;
import com.empconn.persistence.entities.Employee;
import com.empconn.persistence.entities.EmployeeRole;
import com.empconn.persistence.entities.Project;
import com.empconn.persistence.entities.ProjectComment;
import com.empconn.persistence.entities.ProjectLocation;
import com.empconn.repositories.AllocationRepository;
import com.empconn.repositories.EmployeeRepository;
import com.empconn.repositories.ProjectLocationRespository;
import com.empconn.repositories.ProjectRepository;
import com.empconn.repositories.specification.ProjectSpec;
import com.empconn.repositories.specification.ProjectSpecification;
import com.empconn.security.SecurityUtil;
import com.empconn.successfactor.service.SFIntegrationService;
import com.empconn.successfactors.dto.ProjectChangeDto;
import com.empconn.utilities.DateUtils;
import com.empconn.vo.UnitValue;

@Component
public class ProjectService {

	private static final String EDIT_PROJECT = "EDIT-PROJECT";

	@Autowired
	private ProjectRepository projectRepository;

	@Autowired
	private AllocationRepository allocationRepository;

	@Autowired
	private ProjectUnitValueMapper projectUnitValueMapper;

	@Autowired
	private ProjectDetailsMapper projectDetailsMapper;

	@Autowired
	ProjectProjectSummaryDtoMapper projectProjectSummaryDtoMapper;

	@Autowired
	ProjectLocationRespository projectLocationRespository;

	@Autowired
	PinService pinService;

	@Autowired
	MapService mapService;

	@Autowired
	MapHandler mapHandler;

	@Autowired
	private SecurityUtil jwtEmployeeUtil;

	@Autowired
	PinProjectMailService pinProjectMailService;

	@Autowired
	ProjectCommentDtoMapper projectCommentDtoMapper;

	@Autowired
	private ProjectNameChangeService projectNameChangeService;

	@Autowired
	private EmployeeRepository employeeRepository;

	@Autowired
	private SFIntegrationService sfIntegrationService;

	@Autowired
	private EarmarkService earmarkService;

	@Autowired
	private SyncToTimesheetService syncToTimesheetService;

	@Autowired
	private AllocationHoursService allocationHoursService;

	@Autowired
	private EditReleaseDateService editReleaseDateService;

	@Autowired
	private CommonQualifiedMapper commonQualifiedMapper;

	@Autowired
	private ResourceService resourceService;

	public IsValidDto isValidNewProjectInAccount(String projectName, String accountId) {
		final Set<Project> projectsInAccount = projectRepository.findProjectsInAccountForNameValidation(projectName,
				Integer.parseInt(accountId));
		if (projectsInAccount.isEmpty())
			return new IsValidDto(true);
		return new IsValidDto(false);
	}

	public boolean isValidProjectInAccountForProject(String projectName, Long projectId, Integer accountId) {
		final Set<Project> projectsInAccount = projectRepository
				.findProjectsInAccountForNameValidationOtherThanProject(projectName, projectId, accountId);
		return (projectsInAccount.isEmpty());
	}

	public List<UnitValue> getProjects(String accountId, Boolean isActive, Boolean onlyFutureReleaseDate,
			Boolean withBench, String partial) {
		final ProjectForAllocationDto request = new ProjectForAllocationDto(accountId, isActive, onlyFutureReleaseDate,
				withBench, partial);
		final Set<UnitValue> values = projectUnitValueMapper.projectsToUnitValues(projectRepository
				.findAll(ProjectSpecification.getProjectsSpec(request, jwtEmployeeUtil.getLoggedInEmployee())));
		return values.stream().sorted((p1, p2) -> p1.getValue().compareToIgnoreCase(p2.getValue())).collect(Collectors.toList());
	}

	public List<UnitValue> getProjects(ProjectDropdownDto request) {
		final Set<UnitValue> values = projectUnitValueMapper
				.projectsToUnitValues(projectRepository.findAll(ProjectSpecification.getProjectsSpec(request)));
		return values.stream().sorted((p1, p2) -> p1.getValue().compareToIgnoreCase(p2.getValue())).collect(Collectors.toList());
	}

	public ProjectDetailsDto getProjectDetails(String projectId) {
		final Optional<Project> project = projectRepository.findById(Long.parseLong(projectId));
		return project.map(value -> projectDetailsMapper.projectToProjectDetailsDto(value)).orElse(null);
	}

	public List<ProjectSummaryDto> getProjectSummaryList(ProjectSummarySearchDto searchDto) {
		final String leadUserRole = getLeaduserRole(jwtEmployeeUtil.getLoggedInEmployee());
		if (leadUserRole.equals(UserRoles.PMO.name()))
			return pmoProjectSummary(searchDto);
		else if (leadUserRole.equals(UserRoles.RMG.name()))
			return rmgProjectSummary(searchDto);
		else if (leadUserRole.equals(UserRoles.GDM.name()))
			return gdmProjectSummary(searchDto);
		else
			return managerProjectSummary();
	}

	private List<ProjectSummaryDto> pmoProjectSummary(ProjectSummarySearchDto searchDto) {
		final Set<String> pmoStatuses;
		if (searchDto.getIncludeInactive() == null || !searchDto.getIncludeInactive())
			pmoStatuses = Arrays.stream(ProjectSpec.searchStatusesWithOutInactive).collect(Collectors.toSet());
		else
			pmoStatuses = Arrays.stream(ProjectSpec.searchStatuses).collect(Collectors.toSet());
		final Specification<Project> pmoSpec = ProjectSpec.filterCurrentStatusIn(pmoStatuses);
		final Specification<Project> pmoSearchSpec = pmoSpec.and(getProjectSearchSpecification(searchDto));
		final List<Project> projects = projectRepository.findAll(pmoSearchSpec);
		return projectProjectSummaryDtoMapper.projectsToProjectSummaryDtos(projects);
	}

	private List<ProjectSummaryDto> rmgProjectSummary(ProjectSummarySearchDto searchDto) {
		final Specification<Project> rmgSpec = ProjectSpec.filterCurrentStatusIn(
				Arrays.stream(ProjectSpec.searchStatusesWithOutInactive).collect(Collectors.toSet()));
		final Specification<Project> rmgSearchSpec = rmgSpec.and(getProjectSearchSpecification(searchDto));
		final List<Project> projects = projectRepository.findAll(rmgSearchSpec);
		return projectProjectSummaryDtoMapper.projectsToProjectSummaryDtos(projects);
	}

	private List<ProjectSummaryDto> gdmProjectSummary(ProjectSummarySearchDto searchDto) {
		final Long empId = jwtEmployeeUtil.getLoggedInEmployee().getEmployeeId();
		final Set<Long> projectIds = projectLocationRespository.findProjectsWhereOneOfManagerIs(empId);
		final Set<String> gdmStatuses;
		if (searchDto.getIncludeInactive() == null || !searchDto.getIncludeInactive())
			gdmStatuses = Arrays.stream(ProjectSpec.searchStatusesWithOutInactive).collect(Collectors.toSet());
		else
			gdmStatuses = Arrays.stream(ProjectSpec.searchStatuses).collect(Collectors.toSet());
		final Specification<Project> gdmSpec = ProjectSpec.isDevGdm(empId).or(ProjectSpec.isQaGdm(empId));
		final Specification<Project> gdmManagerSpec = ProjectSpec.filterProjectIn(projectIds).or(gdmSpec);
		final Specification<Project> gdmManagerStatusSpec = gdmManagerSpec
				.and(ProjectSpec.filterCurrentStatusIn(gdmStatuses));
		final Specification<Project> finalSearchSpec = gdmManagerStatusSpec
				.and(getProjectSearchSpecification(searchDto));
		final List<Project> projects = projectRepository.findAll(finalSearchSpec);
		return projectProjectSummaryDtoMapper.projectsToProjectSummaryDtos(projects);
	}

	private List<ProjectSummaryDto> managerProjectSummary() {
		final Set<Long> projectIds = projectLocationRespository
				.findProjectsWhereOneOfManagerIs(jwtEmployeeUtil.getLoggedInEmployee().getEmployeeId());
		final List<Project> managerProjects = projectRepository.findProjectSummaryForManager(projectIds);
		return projectProjectSummaryDtoMapper.projectsToProjectSummaryDtos(managerProjects);
	}

	public String getLeaduserRole(Employee loginUser) {
		final Set<String> userRoles = loginUser.getEmployeeRoles().stream().filter(EmployeeRole::getIsActive)
				.map(r -> r.getRole().getName()).collect(Collectors.toSet());
		if (userRoles.contains(UserRoles.PMO.name()) || userRoles.contains(UserRoles.PMO_ADMIN.name()))
			return UserRoles.PMO.name();
		else if (userRoles.contains(UserRoles.RMG.name()) || userRoles.contains(UserRoles.RMG_ADMIN.name()))
			return UserRoles.RMG.name();
		else if (userRoles.contains(UserRoles.GDM.name()))
			return UserRoles.GDM.name();
		else
			return UserRoles.MANAGER.name();
	}

	private Specification<Project> getProjectSearchSpecification(ProjectSummarySearchDto searchDto) {
		Specification<Project> searchSpec = ProjectSpec.filterSoftDeleted();
		if (searchDto.getAccountId() != null) {
			searchSpec = searchSpec.and(ProjectSpec.filterAccountId(Integer.valueOf(searchDto.getAccountId())));
		}
		if (searchDto.getFromStartDate() != null && searchDto.getToStartDate() != null
				&& searchDto.getFromStartDate().compareTo(searchDto.getToStartDate()) <= 0) {
			searchSpec = searchSpec.and(ProjectSpec.filterStartDateBetween(
					CommonQualifiedMapper.localDateToDate(searchDto.getFromStartDate()),
					CommonQualifiedMapper.localDateToDate(searchDto.getToStartDate())));
		}
		if (searchDto.getVerticalId() != null) {
			searchSpec = searchSpec.and(ProjectSpec.filterVertical(Integer.valueOf(searchDto.getVerticalId())));
		}
		if (!CollectionUtils.isEmpty(searchDto.getHorizontalId())) {
			final Set<Integer> horizontalIds = searchDto.getHorizontalId().stream().map(Integer::valueOf)
					.collect(Collectors.toSet());
			searchSpec = searchSpec.and(ProjectSpec.filterHorizontal(horizontalIds));
		}
		if (!CollectionUtils.isEmpty(searchDto.getSubCategoryId())) {
			final Set<Integer> subCategoryIds = searchDto.getSubCategoryId().stream().map(Integer::valueOf)
					.collect(Collectors.toSet());
			searchSpec = searchSpec.and(ProjectSpec.filterSubCategory(subCategoryIds));
		}
		return searchSpec;
	}

	public Project getBenchProject(boolean isDelivery) {
		if (isDelivery) {
			return projectRepository.findByAccountCategoryAndName(ApplicationConstants.INTERNAL_CATEGORY,
					ApplicationConstants.DELIVERY_BENCH_PROJECT_NAME);
		} else {
			return projectRepository.findByAccountCategoryAndName(ApplicationConstants.INTERNAL_CATEGORY,
					ApplicationConstants.NON_DELIVERY_BENCH_PROJECT_NAME);
		}
	}

	public List<Project> getProjectsForUserAsGdmOrManager(Long empId) {
		final Set<Long> projectIds = projectLocationRespository.findProjectsWhereOneOfManagerIs(empId);
		final Specification<Project> gdmSpec = ProjectSpec.isDevGdm(empId).or(ProjectSpec.isQaGdm(empId));
		final Specification<Project> gdmManagerSpec = ProjectSpec.filterProjectIn(projectIds).or(gdmSpec);
		final Specification<Project> gdmManagerStatusSpec = gdmManagerSpec.and(ProjectSpec
				.filterCurrentStatusIn(Arrays.stream(ProjectSpec.searchStatuses).collect(Collectors.toSet())));
		final Specification<Project> finalGdmSpec = gdmManagerStatusSpec.and(ProjectSpec.filterSoftDeleted());
		return projectRepository.findAll(finalGdmSpec);
	}

	@Transactional
	public PinStatusChangedDto activateProject(PinStatusChangeDto dto) {
		final PinStatusChangedDto changedDto = new PinStatusChangedDto();
		final Project project = projectRepository.findByProjectId(dto.getProjectId());
		String oldStatus = "";
		if (project.getCurrentStatus().equalsIgnoreCase(ProjectStatus.PROJECT_ON_HOLD.toString()))
			oldStatus = ApplicationConstants.ON_HOLD;
		if (project.getCurrentStatus().equalsIgnoreCase(ProjectStatus.PROJECT_ON_HOLD.toString())) {
			project.setCurrentStatus(ProjectStatus.PMO_APPROVED.toString());
			final Project saveProject = projectRepository.save(project);
			changedDto.setNewState(ProjectStatus.PMO_APPROVED.toString());
			pinProjectMailService.mailForProjectStatusChangeActive(project, ApplicationConstants.ACTIVE, oldStatus);
			mapHandler.integrateWithMap(saveProject, EDIT_PROJECT);
			syncToTimesheetService.syncProject(saveProject);
		} else {
			throw new EmpConnException("CheckProjectStatus");
		}
		return changedDto;
	}

	@Transactional
	public PinStatusChangedDto deactivateProject(PinStatusChangeDto dto) {
		final PinStatusChangedDto changedDto = new PinStatusChangedDto();
		final Project project = projectRepository.findByProjectId(dto.getProjectId());
		String oldStatus = "";
		if (project.getCurrentStatus().equalsIgnoreCase(ProjectStatus.PMO_APPROVED.toString()))
			oldStatus = ApplicationConstants.ACTIVE;
		else
			oldStatus = ApplicationConstants.ON_HOLD;

		if (project.getCurrentStatus().equalsIgnoreCase(ProjectStatus.PMO_APPROVED.toString())
				|| project.getCurrentStatus().equalsIgnoreCase(ProjectStatus.PROJECT_ON_HOLD.toString())) {
			project.getProjects().forEach(p -> {
				if (p.getCurrentStatus().equalsIgnoreCase(ProjectStatus.PMO_APPROVED.toString())) {
					throw new EmpConnException("AllSubprojectsInactive");
				}
			});
			final Set<Allocation> allocation = allocationRepository.findByProjectId(dto.getProjectId());
			if (!allocation.isEmpty()) {
				throw new EmpConnException("NoResourceAllocated");
			} else {
				project.setCurrentStatus(ProjectStatus.PROJECT_INACTIVE.toString());
				final Project saveProject = projectRepository.save(project);
				changedDto.setNewState(ProjectStatus.PROJECT_INACTIVE.toString());
				pinProjectMailService.mailForProjectStatusChangeInactive(project, ApplicationConstants.INACTIVE,
						oldStatus);
				mapHandler.integrateWithMap(saveProject, EDIT_PROJECT);
				syncToTimesheetService.syncProject(saveProject);
			}
		} else {
			throw new EmpConnException("AlreadyInActiveProject");
		}

		return changedDto;
	}

	@Transactional
	public ProjectStatusChangeCommentDto projectOnHold(PinStatusChangeCommentDto dto) {
		final ProjectStatusChangeCommentDto changedDto = new ProjectStatusChangeCommentDto();
		final Project project = projectRepository.findByProjectId(dto.getProjectId());
		String oldStatus = "";
		if (project.getCurrentStatus().equalsIgnoreCase(ProjectStatus.PMO_APPROVED.toString()))
			oldStatus = ApplicationConstants.ACTIVE;
		if (project.getCurrentStatus().equalsIgnoreCase(ProjectStatus.PMO_APPROVED.toString())) {

			final ProjectComment projectComment = projectCommentDtoMapper.commentDtoToProjectComment(dto,
					ProjectStatus.PROJECT_ON_HOLD.name());
			project.setCurrentStatus(ProjectStatus.PROJECT_ON_HOLD.name());
			project.addProjectComment(projectComment);

			final Project saveProject = projectRepository.save(project);

			changedDto.setNewState(ProjectStatus.PROJECT_ON_HOLD.toString());
			changedDto.setComment(dto.getComment());
			mapHandler.integrateWithMap(saveProject, EDIT_PROJECT);
			pinProjectMailService.mailForProjectStatusChangeOnHold(project, ApplicationConstants.ON_HOLD, oldStatus);
		} else {
			throw new EmpConnException("CheckProjectStatus");
		}
		return changedDto;
	}

	@Transactional
	public ProjectEndDateChangedDto changeProjectEndDate(ProjectEndDateChangeDto dto) {
		final ProjectEndDateChangedDto changedDto = new ProjectEndDateChangedDto();
		final Project prj = projectRepository.findByProjectId(Long.valueOf(dto.getProjectId()));
		final Integer value = projectRepository.changeProjectEndDate(Long.valueOf(dto.getProjectId()),
				CommonQualifiedMapper.localDateToDate(dto.getEndDate()));
		final Set<Allocation> allocations = allocationRepository.findByProjectId(Long.valueOf(dto.getProjectId()),
				CommonQualifiedMapper.localDateToDate(dto.getEndDate()), prj.getEndDate());
		if (value > 0) {
			changedDto.setNewEndDate(dto.getEndDate());
			final ChangeReleaseDateDto changeReleaseDateDto = new ChangeReleaseDateDto();
			final List<ChangeReleaseDateListDto> changeReleaseDateList = new ArrayList<>();
			for (final Allocation allocation : allocations) {
				changeReleaseDateList
						.add(new ChangeReleaseDateListDto(allocation.getAllocationId().toString(), dto.getEndDate(),
								allocationHoursService.getCalculatedHours(
										commonQualifiedMapper.getAllocationStartDate(allocation),
										DateUtils.convertToDateViaInstant(dto.getEndDate()),
										commonQualifiedMapper.mergedAllocatedPercentage(allocation))));
			}
			changeReleaseDateDto.setChangeReleaseDateList(changeReleaseDateList);
			pinProjectMailService.mailForProjectEndDateChange(prj, allocations, dto);
			editReleaseDateService.changeReleaseDate(changeReleaseDateDto);

			syncToTimesheetService.syncProjectAllocationsForReleaseDate(prj, allocations);
		}

		for (final Allocation allocation : allocations) {
			final List<Earmark> earmarksList = allocation.getEarmarks().stream().filter(Earmark::getIsActive)
					.collect(Collectors.toList());

			for (final Earmark earmark : earmarksList) {
				if (dto.getEndDate().isAfter(com.empconn.utilities.DateUtils
						.convertToLocalDateViaMilisecond(earmark.getStartDate()))) {
					// unearmark
					earmarkService.unearmarkBySystem(earmark,
							ApplicationConstants.UNEARMARK_EDIT_RELEASE_DATE_PROJECT_COMMENT);
				}
			}

		}
		return changedDto;
	}

	@Transactional
	public void updateProjectDetails(UpdateProjectDetailsDto updateProjectDto) {
		Project project = projectRepository.findByProjectId(Long.valueOf(updateProjectDto.getProjectId()));
		final boolean isProjectNameIsChanged = !StringUtils.equals(project.getName(),
				updateProjectDto.getProjectName());
		final boolean isProjectDetailsChanged = isProjectDetailsChanged(updateProjectDto, project);

		if (isProjectDetailsChanged)
			project = getProjectWithUpdatedProjectDetails(project, updateProjectDto);

		if (!CollectionUtils.isEmpty(updateProjectDto.getLocationList()))
			project = getProjectWithAddedManagers(project, updateProjectDto);

		final Project savedProject = projectRepository.save(project);

		if (isProjectDetailsChanged)
			pinProjectMailService.mailForProjectDetailsChange(savedProject);

		if (!CollectionUtils.isEmpty(updateProjectDto.getManagerAssignResources()))
			resourceService.assignUserRole(updateProjectDto.getManagerAssignResources(), Roles.MANAGER.name());

		notifyAddedManagersWithMail(savedProject, updateProjectDto);

		if (isProjectNameIsChanged)
			doPostProjectNameUpdateIntegrations(updateProjectDto, savedProject);

		mapHandler.integrateWithMap(savedProject, EDIT_PROJECT);
	}

	private boolean isProjectDetailsChanged(UpdateProjectDetailsDto updateDetails, Project currentDetails) {
		if (!StringUtils.isEmpty(updateDetails.getProjectName())
				&& !StringUtils.equals(updateDetails.getProjectName(), currentDetails.getName()))
			return true;
		if (!StringUtils.isEmpty(updateDetails.getDescription())
				&& !StringUtils.equals(updateDetails.getDescription(), currentDetails.getDescription()))
			return true;

		if (!CollectionUtils.isEmpty(updateDetails.getTechList())
				&& !isStringListAndCommaStringsAreEqual(updateDetails.getTechList(), currentDetails.getTechnology()))
			return true;

		if (!CollectionUtils.isEmpty(updateDetails.getDbList())
				&& !isStringListAndCommaStringsAreEqual(updateDetails.getDbList(), currentDetails.getDatabase()))
			return true;

		return (!CollectionUtils.isEmpty(updateDetails.getOsList())
				&& !isStringListAndCommaStringsAreEqual(updateDetails.getOsList(), currentDetails.getOperatingSystem()));

	}

	private boolean isStringListAndCommaStringsAreEqual(List<String> list1, String s) {
		final List<String> list2 = CommonQualifiedMapper.commaStringsTostringList(s);
		if (list1 != null && list2 != null) {
			return (list1.size() == list2.size()) && new HashSet<>(list1).containsAll(list2);
		}
		return false;
	}

	private Project getProjectWithUpdatedProjectDetails(Project project, UpdateProjectDetailsDto updateProjectDto) {
		if (!isNullOrEmpty(updateProjectDto.getProjectName()))
			project.setName(updateProjectDto.getProjectName());
		if (!isNullOrEmpty(updateProjectDto.getDescription()))
			project.setDescription(updateProjectDto.getDescription());
		if (!updateProjectDto.getTechList().isEmpty())
			project.setTechnology(String.join(",", updateProjectDto.getTechList()));
		if (!updateProjectDto.getOsList().isEmpty())
			project.setOperatingSystem(String.join(",", updateProjectDto.getOsList()));
		if (!updateProjectDto.getDbList().isEmpty())
			project.setDatabase(String.join(",", updateProjectDto.getDbList()));

		return project;
	}

	private Project getProjectWithAddedManagers(Project project, UpdateProjectDetailsDto updateProjectDto) {
		for (final UpdateLocationManagersDto dto : updateProjectDto.getLocationList()) {
			final Optional<ProjectLocation> pl = project.getProjectLocations().stream()
					.filter(p -> p.getProjectLocationId().equals(Long.valueOf(dto.getProjectLocationId()))).findFirst();
			if (pl.isPresent()) {
				final ProjectLocation projectLocation = pl.get();
				if (dto.getDevManager() != null && dto.getDevManager().getIsEdited()) {
					final Employee devemp = employeeRepository
							.findByEmployeeId(Long.valueOf(dto.getDevManager().getId()));
					projectLocation.setEmployee1(devemp);
					syncToTimesheetService.syncProjectManager(projectLocation, devemp,
							ApplicationConstants.WORK_GROUP_DEV);
				}
				if (dto.getQaManager() != null && dto.getQaManager().getIsEdited()) {
					final Employee qaemp = employeeRepository
							.findByEmployeeId(Long.valueOf(dto.getQaManager().getId()));
					projectLocation.setEmployee2(qaemp);
					syncToTimesheetService.syncProjectManager(projectLocation, qaemp,
							ApplicationConstants.QA_WORK_GROUP);
				}
				if (dto.getUiManager() != null && dto.getUiManager().getIsEdited()) {
					final Employee uiemp = employeeRepository
							.findByEmployeeId(Long.valueOf(dto.getUiManager().getId()));
					projectLocation.setEmployee3(uiemp);
					syncToTimesheetService.syncProjectManager(projectLocation, uiemp,
							ApplicationConstants.WORK_GROUP_UI);
				}
				if (dto.getManager1() != null && dto.getManager1().getIsEdited()) {
					final Employee man1emp = employeeRepository
							.findByEmployeeId(Long.valueOf(dto.getManager1().getId()));
					projectLocation.setEmployee4(man1emp);
					syncToTimesheetService.syncProjectManager(projectLocation, man1emp,
							ApplicationConstants.WORK_GROUP_SUPPORT_1);
				}
				if (dto.getManager2() != null && dto.getManager2().getIsEdited()) {
					final Employee man2emp = employeeRepository
							.findByEmployeeId(Long.valueOf(dto.getManager2().getId()));
					projectLocation.setEmployee5(man2emp);
					syncToTimesheetService.syncProjectManager(projectLocation, man2emp,
							ApplicationConstants.WORK_GROUP_SUPPORT_2);
				}

			}
		}

		return project;
	}

	private void notifyAddedManagersWithMail(Project project, UpdateProjectDetailsDto updateProjectDto) {
		for (final UpdateLocationManagersDto dto : updateProjectDto.getLocationList()) {
			final Optional<ProjectLocation> pl = project.getProjectLocations().stream()
					.filter(p -> p.getProjectLocationId().equals(Long.valueOf(dto.getProjectLocationId()))).findFirst();
			if (pl.isPresent()) {
				final ProjectLocation projectLocation = pl.get();
				if (dto.getDevManager() != null && dto.getDevManager().getIsEdited()
						&& projectLocation.getEmployee1() != null)
					pinProjectMailService.mailForProjectManagerChange(project, projectLocation.getEmployee1(),
							projectLocation, "DEV");

				if (dto.getQaManager() != null && dto.getQaManager().getIsEdited()
						&& projectLocation.getEmployee2() != null)
					pinProjectMailService.mailForProjectManagerChange(project, projectLocation.getEmployee2(),
							projectLocation, "QA");

				if (dto.getUiManager() != null && dto.getUiManager().getIsEdited()
						&& projectLocation.getEmployee3() != null) {
					pinProjectMailService.mailForProjectManagerChange(project, projectLocation.getEmployee3(),
							projectLocation, "UI");
				}
				if (dto.getManager1() != null && dto.getManager1().getIsEdited()
						&& projectLocation.getEmployee4() != null) {
					pinProjectMailService.mailForProjectManagerChange(project, projectLocation.getEmployee4(),
							projectLocation, "SUPPORT1");
				}
				if (dto.getManager2() != null && dto.getManager2().getIsEdited()
						&& projectLocation.getEmployee5() != null) {
					pinProjectMailService.mailForProjectManagerChange(project, projectLocation.getEmployee5(),
							projectLocation, "SUPPORT2");
				}

			}
		}

	}

	private void doPostProjectNameUpdateIntegrations(UpdateProjectDetailsDto updateProjectDto, Project project) {
		final List<String> employeesToUpdateProjectNameChange = projectNameChangeService
				.getEmployeeLoginMailIdsToUpdateProjectNameChange(Long.valueOf(updateProjectDto.getProjectId()),
						updateProjectDto.getProjectName());
		projectNameChangeService.process(project.getProjectId(), project.getName(), employeesToUpdateProjectNameChange);

		syncToTimesheetService.syncProject(project);
		sfCall(project);
	}

	public Set<UnitValue> getProjectLocations(String projectId) {
		return projectLocationRespository.findLocationsForProjectId(Long.parseLong(projectId));
	}

	public static boolean isNullOrEmpty(String str) {
		StringUtils.isEmpty("");
		return !(str != null && !str.isEmpty());
	}

	public void sfCall(Project project) {
		final Set<Allocation> allocations = allocationRepository.findByProjectId(project.getProjectId());
		final List<ProjectChangeDto> projectChangeDtos = new ArrayList<>();
		for (final Allocation a : allocations) {
			final Allocation primaryAllocation = a.getEmployee().getPrimaryAllocation();
			if (primaryAllocation.getAllocationId().equals(a.getAllocationId())) {
				final ProjectChangeDto projectChangeDto = new ProjectChangeDto(
						String.valueOf(a.getEmployee().getEmployeeId()), new Date(),
						String.valueOf(a.getProject().getProjectId()),
						String.valueOf(a.getProject().getAccount().getAccountId()));
				projectChangeDtos.add(projectChangeDto);
			}
		}
		sfIntegrationService.changeProjectOrAccountList(projectChangeDtos);
	}

	public Employee getProjectGdm(Project project) {
		if (project.getEmployee1() != null)
			return project.getEmployee1();
		else
			return project.getEmployee2();
	}

}
