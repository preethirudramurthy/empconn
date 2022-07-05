package com.empconn.service;

import java.util.Collection;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;

import com.empconn.constants.Roles;
import com.empconn.dto.ChecklistDto;
import com.empconn.dto.ChecklistPmoDto;
import com.empconn.dto.IsValidDto;
import com.empconn.dto.IsValidSalesforceDto;
import com.empconn.dto.MyPinDto;
import com.empconn.dto.PinCountDto;
import com.empconn.dto.PinDetailsDto;
import com.empconn.dto.PinStatusChangeCommentDto;
import com.empconn.dto.PinStatusChangeDto;
import com.empconn.dto.ResourceRequirementDto;
import com.empconn.dto.SavePinDto;
import com.empconn.dto.SavedPinDto;
import com.empconn.dto.ValidatedPinDto;
import com.empconn.enums.AccountCategory;
import com.empconn.enums.AccountStatus;
import com.empconn.enums.ProjectStatus;
import com.empconn.exception.EmpConnException;
import com.empconn.exception.PreConditionFailedException;
import com.empconn.map.MapHandler;
import com.empconn.mapper.LocationUnitValueMapper;
import com.empconn.mapper.ProjectChecklistDtoMapper;
import com.empconn.mapper.ProjectCommentDtoMapper;
import com.empconn.mapper.ProjectDtoMapper;
import com.empconn.mapper.ProjectPinMapper;
import com.empconn.mapper.ProjectResourceDtoMapper;
import com.empconn.mapper.RoleEmployeeRoleMapper;
import com.empconn.mapper.RoleUnitValueMapper;
import com.empconn.persistence.entities.Account;
import com.empconn.persistence.entities.Employee;
import com.empconn.persistence.entities.Project;
import com.empconn.persistence.entities.ProjectChecklist;
import com.empconn.persistence.entities.ProjectComment;
import com.empconn.persistence.entities.ProjectLocation;
import com.empconn.persistence.entities.ProjectResource;
import com.empconn.persistence.entities.ProjectResourcesSecondarySkill;
import com.empconn.persistence.entities.SalesforceIdentifier;
import com.empconn.repositories.AccountRepository;
import com.empconn.repositories.ProjectChecklistRepository;
import com.empconn.repositories.ProjectCommentRepository;
import com.empconn.repositories.ProjectLocationRespository;
import com.empconn.repositories.ProjectRepository;
import com.empconn.repositories.ProjectResourceRepository;
import com.empconn.repositories.ProjectResourcesSecondarySkillRepository;
import com.empconn.repositories.ProjectReviewRepository;
import com.empconn.repositories.RoleRepository;
import com.empconn.repositories.SalesforceIdentifierRepository;
import com.empconn.security.SecurityUtil;
import com.empconn.utilities.IterableUtils;
import com.empconn.vo.UnitValue;

@Service
public class PinService {

	private static final Logger logger = LoggerFactory.getLogger(PinService.class);

	@Autowired
	private ProjectRepository projectRepository;

	@Autowired
	private ProjectResourceDtoMapper projectResourceDtoMapper;

	@Autowired
	private ProjectResourceRepository projectResourceRepository;

	@Autowired
	private ProjectChecklistRepository projectChecklistRepository;

	@Autowired
	private SalesforceIdentifierRepository salesforceIdentifierRepository;

	@Autowired
	private ProjectReviewRepository projectReviewRepository;

	@Autowired
	private ProjectLocationRespository projectLocationRepository;

	@Autowired
	private ProjectChecklistDtoMapper projectChecklistDtoMapper;

	@Autowired
	ProjectLocationRespository projectLocationRespository;

	@Autowired
	LocationUnitValueMapper locationUnitValueMapper;

	@Autowired
	ProjectDtoMapper projectDtoMapper;

	@Autowired
	private ProjectPinMapper pinUtilValueMapper;

	@Autowired
	private ProjectCommentRepository projectCommentRepository;

	@Autowired
	ProjectCommentDtoMapper projectCommentDtoMapper;

	@Autowired
	private SecurityUtil jwtEmployeeUtil;

	@Autowired
	private SalesforceIdentifierService salesforceIdentifierService;

	@Autowired
	ProjectResourcesSecondarySkillRepository projectResourcesSecondarySkillRepository;

	@Autowired
	private RoleUnitValueMapper roleUnitValueMapper;

	@Autowired
	private RoleRepository roleRepository;

	@Autowired
	MapHandler mapHandler;

	@Value("${link.url.application}")
	private String applicationUrl;

	@Autowired
	PinProjectMailService pinProjectMailService;

	@Autowired
	AccountRepository accountRepository;

	@Autowired
	ProjectService projectService;

	@Autowired
	private SyncToTimesheetService syncToTimesheetService;

	@Autowired
	RoleEmployeeRoleMapper roleEmployeeRoleMapper;

	@Autowired
	ResourceService resourceService;

	public void approvePin(PinStatusChangeDto approveDto) {
		final Project project = projectRepository.findByProjectId(approveDto.getProjectId());

		if (!CollectionUtils.isEmpty(approveDto.getManagerAssignResources()))
			resourceService.assignUserRole(approveDto.getManagerAssignResources(), Roles.MANAGER.name());
		if (!CollectionUtils.isEmpty(approveDto.getGdmAssignResources()))
			resourceService.assignUserRole(approveDto.getGdmAssignResources(), Roles.GDM.name());

		project.setCurrentStatus(ProjectStatus.PMO_APPROVED.name());
		project.setApprovedBy(jwtEmployeeUtil.getLoggedInEmployee().getEmployeeId());
		project.setApprovedOn(new java.sql.Timestamp(System.currentTimeMillis()));
		final Account account = project.getAccount();
		if (!account.getStatus().equals(AccountStatus.ACTIVE.name())) {
			account.setStatus(AccountStatus.ACTIVE.name());
		}
		final Project saveProject = projectRepository.save(project);
		pinProjectMailService.sendEmailForPinApprove(project);
		mapHandler.integrateWithMap(saveProject, "PMO-APPROVE");
		syncToTimesheetService.syncAccountAndProject(saveProject, account);
	}

	public void saveResourceRequirement(ResourceRequirementDto resourceRequirementDto) {
		final Set<ProjectResource> projectResources = projectResourceDtoMapper
				.resourceItemListToProjectResources(resourceRequirementDto.getResourceRequirementList());
		final Iterable<ProjectResource> savedResources = projectResourceRepository.saveAll(projectResources);
		softDeleteOnSaveResourceRequirement(savedResources, Long.valueOf(resourceRequirementDto.getProjectId()));
	}

	private void softDeleteOnSaveResourceRequirement(Iterable<ProjectResource> savedResources, Long projectId) {
		final Set<ProjectResource> savedResourcesSet = IterableUtils.toSet(savedResources);
		final Set<Long> projectLocationIds = projectLocationRepository.findLocationIdsForProjectId(projectId);
		if (CollectionUtils.isEmpty(savedResourcesSet)) {
			projectResourceRepository.softDeleteAllProjectResourcesForLocations(projectLocationIds);
			final Set<Long> deletedResourceIds = projectResourceRepository
					.findSoftDeletedProjectResourcesForProjectLocations(projectLocationIds);
			if (!CollectionUtils.isEmpty(deletedResourceIds)) {
				projectResourcesSecondarySkillRepository
						.softDeleteAllSecondariesForProjectResourceIds(deletedResourceIds);
			}
		} else {
			final Set<Long> savedResourceIds = savedResourcesSet.stream().map(ProjectResource::getProjectResourcesId)
					.collect(Collectors.toSet());
			projectResourceRepository.softDeleteProjectResourcesForLocations(savedResourceIds, projectLocationIds);
			final Set<Long> deletedResourceIds = projectResourceRepository
					.findSoftDeletedProjectResourcesForProjectLocations(projectLocationIds);
			final Set<Long> savedSecondaryIds = savedResourcesSet.stream().map(s -> {
				if (!CollectionUtils.isEmpty(s.getProjectResourcesSecondarySkills()))
					return s.getProjectResourcesSecondarySkills().stream()
							.map(ProjectResourcesSecondarySkill::getProjectResourcesSecondarySkillId)
							.collect(Collectors.toSet());
				else
					return null;

			}).filter(Objects::nonNull).flatMap(Collection::stream).collect(Collectors.toSet());
			if (CollectionUtils.isEmpty(savedSecondaryIds))
				projectResourcesSecondarySkillRepository.softDeleteAllSecondariesForProjectResourceIds(Stream
						.of(savedResourceIds, deletedResourceIds).flatMap(Set::stream).collect(Collectors.toSet()));
			else
				projectResourcesSecondarySkillRepository.softDeleteSecondariesForProjectResourceIds(savedSecondaryIds,
						Stream.of(savedResourceIds, deletedResourceIds).flatMap(Set::stream)
								.collect(Collectors.toSet()));
		}
	}

	public void saveChecklist(ChecklistDto checklistDto) {
		final Project project = projectRepository.findByProjectId(Long.valueOf(checklistDto.getProjectId()));
		final Set<ProjectChecklist> projectChecklists = projectChecklistDtoMapper
				.checklistDtoToProjectChecklistSet(checklistDto.getCheckItemList(), project);
		final Iterable<ProjectChecklist> savedChecklists = projectChecklistRepository.saveAll(projectChecklists);
		softDeleteOnSaveProjectCheckList(savedChecklists, Long.parseLong(checklistDto.getProjectId()));
	}

	public void saveChecklistPmo(ChecklistPmoDto checklistPmoDto) {
		final Project project = projectDtoMapper.checklistPmoDtoToProject(checklistPmoDto);
		final Project savedProject = projectRepository.save(project);
		softDeleteOnSaveProjectCheckList(savedProject.getProjectChecklists(), project.getProjectId());
	}

	private void softDeleteOnSaveProjectCheckList(Iterable<ProjectChecklist> savedChecklists, Long projectId) {
		final Set<Long> savedIds = IterableUtils.toSet(savedChecklists).stream()
				.map(ProjectChecklist::getProjectChecklistId).collect(Collectors.toSet());
		projectChecklistRepository.softDeleteProjectChecklistsForProject(savedIds, projectId);
	}

	public void submitPin(PinStatusChangeDto submitDto) {
		final Project project = projectRepository.findByProjectId(submitDto.getProjectId());
		if (project.getCurrentStatus().equalsIgnoreCase(ProjectStatus.OPEN.name())) {
			doPinSubmit(project);
		} else if (project.getCurrentStatus().equalsIgnoreCase(ProjectStatus.SENT_BACK.name())) {
			doPinReSubmit(project);
		}
	}

	public void doPinSubmit(Project project) {
		if (isPinSubmitByProjectGdm(project)) {
			projectRepository.changeCurrentStatus(project.getProjectId(), ProjectStatus.GDM_REVIEWED.name());
			pinProjectMailService.sendEmailForPinStatusChange(project, ProjectStatus.GDM_REVIEWED.name());
		} else {
			projectRepository.changeCurrentStatus(project.getProjectId(), ProjectStatus.INITIATED.name());
			pinProjectMailService.sendEmailForPinStatusChange(project, ProjectStatus.INITIATED.name());
		}
	}

	public boolean isPinSubmitByProjectGdm(Project project) {
		final Employee loginUser = jwtEmployeeUtil.getLoggedInEmployee();

		boolean ret = false;
		if (project.getEmployee1() != null && project.getEmployee1().getEmployeeId().equals(loginUser.getEmployeeId()))
			ret = true;
		else ret = project.getEmployee1() == null && project.getEmployee2() != null
				&& project.getEmployee2().getEmployeeId().equals(loginUser.getEmployeeId());
		return ret;
	}

	public void doPinReSubmit(Project project) {
		projectRepository.changeCurrentStatus(project.getProjectId(), ProjectStatus.RESUBMITTED.name());
		pinProjectMailService.sendEmailForPinStatusChange(project, ProjectStatus.RESUBMITTED.name());
	}

	public PinDetailsDto getPinDetails(String projectId) {
		Optional<Project> projectOpt = projectRepository.findById(Long.valueOf(projectId));
		final Project project = projectOpt.orElse(null);
		return pinUtilValueMapper.projectToPinDetailsDto(project);
	}

	public void rejectPin(PinStatusChangeCommentDto rejectDto) {
		projectRepository.changeCurrentStatus(rejectDto.getProjectId(),
				ProjectStatus.PMO_REJECTED.name());
		final ProjectComment projectComment = projectCommentDtoMapper.commentDtoToProjectComment(rejectDto,
				ProjectStatus.PMO_REJECTED.name());
		projectCommentRepository.save(projectComment);
		final Project project = projectRepository.findByProjectId(rejectDto.getProjectId());
		pinProjectMailService.mailForRejectPin(project, rejectDto);
	}

	public void rejectPinOnReview(PinStatusChangeCommentDto rejectDto) {
		projectRepository.changeCurrentStatus(rejectDto.getProjectId(),
				ProjectStatus.GDM_REJECTED.name());
		final ProjectComment projectComment = projectCommentDtoMapper.commentDtoToProjectComment(rejectDto,
				ProjectStatus.GDM_REJECTED.name());
		projectCommentRepository.save(projectComment);
		final Project project = projectRepository.findByProjectId(rejectDto.getProjectId());
		pinProjectMailService.sendEmailForPinStatusChange(project, ProjectStatus.GDM_REJECTED.name());
	}

	@Transactional
	public SavedPinDto saveDraftPin(SavePinDto savePinDto) {
		if (savePinDto.getProjectId() != null) {
			final Project existProject = projectRepository.findByProjectId(Long.valueOf(savePinDto.getProjectId()));
			final Project project = projectDtoMapper.savePinDtoToProject(savePinDto, existProject);
			final Project savedProject = projectRepository.save(project);
			softDeleteOnSavePin(savedProject);
			return new SavedPinDto(savedProject.getProjectId());
		} else {
			final Project project = projectDtoMapper.savePinDtoToProject(savePinDto);
			project.setCurrentStatus(ProjectStatus.DRAFT.name());
			final Project savedProject = projectRepository.save(project);
			softDeleteOnSavePin(savedProject);
			return new SavedPinDto(savedProject.getProjectId());
		}

	}

	@Transactional
	public ValidatedPinDto validatePin(SavePinDto savePinDto) {
		validateSavePin(savePinDto);
		Project project;
		if (savePinDto.getProjectId() != null) {
			final Project existProject = projectRepository.findByProjectId(Long.valueOf(savePinDto.getProjectId()));
			project = projectDtoMapper.savePinDtoToProject(savePinDto, existProject);
			if (existProject.getCurrentStatus().equals(ProjectStatus.DRAFT.name()))
				project.setCurrentStatus(ProjectStatus.OPEN.name());
		} else {
			project = projectDtoMapper.savePinDtoToProject(savePinDto);
			project.setCurrentStatus(ProjectStatus.OPEN.name());
		}
		final Project savedProject = projectRepository.save(project);
		softDeleteOnSavePin(savedProject);
		return projectDtoMapper.projectToValidatedPinDto(savedProject);
	}

	private void validateSavePin(SavePinDto dto) {
		if (dto.getProjectId() != null)
			validateExistSavePin(dto);
		else
			validateNewSavePin(dto);
	}

	private void validateExistSavePin(SavePinDto dto) {
		Optional<Account> acOpt = accountRepository.findById(Integer.valueOf(dto.getAccountId()));
		final Account account = acOpt.orElse(null);
		if (account != null) {

			if (!projectService.isValidProjectInAccountForProject(dto.getName(), Long.valueOf(dto.getProjectId()),
					account.getAccountId()))
				throw new EmpConnException("ProjectNameNotUniqueForAccount");

			if (account.getCategory().equalsIgnoreCase(AccountCategory.CLIENT.name())) {
				final IsValidSalesforceDto isValidSalesforceDto = new IsValidSalesforceDto(dto.getProjectId(),
						dto.getSalesforceIdList());
				if (isValidSalesForceIdsForTheProject(isValidSalesforceDto).getIsValid())
					throw new EmpConnException("SalesforceIdNotExistsInOtherProject");
			}
		}
	}

	private void validateNewSavePin(SavePinDto dto) {
		Optional<Account> acOpt = accountRepository.findById(Integer.valueOf(dto.getAccountId()));
		final Account account = acOpt.orElse(null);

		if (projectService.isValidNewProjectInAccount(dto.getName(), dto.getAccountId()).getIsValid())
			throw new EmpConnException("ProjectNameNotUniqueForAccount");

		if (account!= null && account.getCategory().equalsIgnoreCase(AccountCategory.CLIENT.name()) && 
				(!isValidSalesForceIds(dto.getSalesforceIdList()))){
				throw new EmpConnException("SalesforceIdNotExistsInOtherProject");
		}
	}

	public void softDeleteOnSavePin(Project project) {
		if (CollectionUtils.isEmpty(project.getProjectLocations())) {
			projectLocationRepository.softDeleteAllProjectLocationsForProject(project.getProjectId());
			final Set<Long> deletedLocationIds = projectLocationRepository
					.findSoftDeletedLocationIdsForProject(project.getProjectId());
			if (!CollectionUtils.isEmpty(deletedLocationIds)) {
				projectResourceRepository.softDeleteAllProjectResourcesForLocations(deletedLocationIds);
				final Set<Long> deletedResourceIds = projectResourceRepository
						.findSoftDeletedProjectResourcesForProjectLocations(deletedLocationIds);
				if (!CollectionUtils.isEmpty(deletedResourceIds)) {
					projectResourcesSecondarySkillRepository
							.softDeleteAllSecondariesForProjectResourceIds(deletedResourceIds);
				}
			}

		} else {
			final Set<Long> savedLocationIds = project.getProjectLocations().stream()
					.map(ProjectLocation::getProjectLocationId).collect(Collectors.toSet());
			projectLocationRepository.softDeleteProjectLocationsForProject(savedLocationIds, project.getProjectId());
			final Set<Long> deletedLocationIds = projectLocationRepository
					.findSoftDeletedLocationIdsForProject(project.getProjectId());
			if (!CollectionUtils.isEmpty(deletedLocationIds)) {
				projectResourceRepository.softDeleteAllProjectResourcesForLocations(deletedLocationIds);
				final Set<Long> deletedResourceIds = projectResourceRepository
						.findSoftDeletedProjectResourcesForProjectLocations(deletedLocationIds);
				if (!CollectionUtils.isEmpty(deletedResourceIds)) {
					projectResourcesSecondarySkillRepository
							.softDeleteAllSecondariesForProjectResourceIds(deletedResourceIds);
				}
			}
		}

		if (CollectionUtils.isEmpty(project.getSalesforceIdentifiers())) {
			salesforceIdentifierRepository.softDeleteAllSalesforcesForProject(project.getProjectId());
		} else {
			final Set<Long> savedSfIds = project.getSalesforceIdentifiers().stream()
					.map(SalesforceIdentifier::getSalesforceIdentifierId).collect(Collectors.toSet());
			salesforceIdentifierRepository.softDeleteSalesforcesForProject(savedSfIds, project.getProjectId());
		}

	}

	public void submitPinForApproval(PinStatusChangeDto submitApproveDto) {
		final Project project = projectRepository.findByProjectId(submitApproveDto.getProjectId());
		projectRepository.changeCurrentStatus(submitApproveDto.getProjectId(),
				ProjectStatus.GDM_REVIEWED.name());
		pinProjectMailService.sendEmailForPinStatusChange(project, ProjectStatus.GDM_REVIEWED.name());
	}

	public List<MyPinDto> getMyPinList() {
		final Employee loggedInEmployee = jwtEmployeeUtil.getLoggedInEmployee();
		final List<Project> myPinList = IterableUtils
				.toList(projectRepository.findMyPins(loggedInEmployee.getEmployeeId()));
		return projectDtoMapper.projectToMypinDtos(myPinList);
	}

	@Transactional
	public void cancelPin(PinStatusChangeDto cancelDto) {
		logger.debug("Cancel PIN with id {}", cancelDto.getProjectId());
		final long projectId = cancelDto.getProjectId();
		projectRepository.softDelete(projectId);
		projectChecklistRepository.softDeleteByProjectId(projectId);
		salesforceIdentifierRepository.softDeleteByProjectId(projectId);
		projectReviewRepository.softDeleteByProjectId(projectId);
		projectLocationRepository.softDeleteByProjectId(projectId);
	}

	public IsValidDto isValidSalesForceIdsForTheProject(IsValidSalesforceDto isValidSalesforceDto) {
		for (final String salesforceId : isValidSalesforceDto.getSalesforceIdList()) {
			if (!salesforceIdentifierService.isValidSalesforceIdForProject(salesforceId,
					Long.valueOf(isValidSalesforceDto.getProjectId())))
				return new IsValidDto(false);
		}
		return new IsValidDto(true);
	}

	public boolean isValidSalesForceIds(List<String> salesforceIdList) {
		for (final String salesforceId : salesforceIdList) {
			if (!salesforceIdentifierService.isValidSalesforceId(salesforceId))
				return false;
		}
		return true;
	}

	public List<MyPinDto> getPinsForApproval() {
		final List<Project> projects = projectRepository
				.findByCurrentStatusOrderByCreatedOnDesc(ProjectStatus.GDM_REVIEWED.name());
		return projectDtoMapper.projectToMypinDtos(projects);
	}

	public List<MyPinDto> getPinsForReviewList() {
		final List<Project> projects = projectRepository
				.getPinsForReviewList(jwtEmployeeUtil.getLoggedInEmployee().getEmployeeId());
		return projectDtoMapper.projectToMypinDtos(projects);
	}

	public PinCountDto getPinCount() {
		final PinCountDto pinCountDto = new PinCountDto();
		pinCountDto.setMyPins(projectRepository.getMyPinCount(jwtEmployeeUtil.getLoggedInEmployee().getEmployeeId()));
		pinCountDto.setPinsForReview(
				projectRepository.getPinsForReviewCount(jwtEmployeeUtil.getLoggedInEmployee().getEmployeeId()));
		pinCountDto.setPinsForApproval(
				projectRepository.countByCurrentStatusAndIsActiveIsTrue(ProjectStatus.GDM_REVIEWED.name()));
		return pinCountDto;
	}

	public void sendBackPinOnReview(PinStatusChangeCommentDto sendbackDto) {
		final Project project = projectRepository.findByProjectId(sendbackDto.getProjectId());
		if (!(project.getCurrentStatus().equals(ProjectStatus.INITIATED.name())
				|| project.getCurrentStatus().equals(ProjectStatus.RESUBMITTED.name())))
			throw new PreConditionFailedException("PinStatusNotInitiatedOrResubmitted");
		projectRepository.changeCurrentStatus(sendbackDto.getProjectId(), ProjectStatus.SENT_BACK.name());
		final ProjectComment projectComment = projectCommentDtoMapper.commentDtoToProjectComment(sendbackDto,
				ProjectStatus.SENT_BACK.name());
		projectCommentRepository.save(projectComment);
		pinProjectMailService.sendEmailForPinStatusChange(project, ProjectStatus.SENT_BACK.name());
	}

	public Set<UnitValue> getAllRoles() {
		return roleUnitValueMapper.rolesToUnitValues(roleRepository.findAll());
	}

}
