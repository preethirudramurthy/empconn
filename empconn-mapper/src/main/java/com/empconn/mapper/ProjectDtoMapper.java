package com.empconn.mapper;

import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;

import org.mapstruct.AfterMapping;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;
import org.mapstruct.Named;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.CollectionUtils;

import com.empconn.dto.CheckListInformationDto;
import com.empconn.dto.ChecklistPmoDto;
import com.empconn.dto.MyPinDto;
import com.empconn.dto.PreApprovalCheckDto;
import com.empconn.dto.ProjectInformationDto;
import com.empconn.dto.ResourceInformationDto;
import com.empconn.dto.SavePinDto;
import com.empconn.dto.ValidatedPinDto;
import com.empconn.persistence.entities.Account;
import com.empconn.persistence.entities.Employee;
import com.empconn.persistence.entities.Horizontal;
import com.empconn.persistence.entities.Project;
import com.empconn.persistence.entities.ProjectChecklist;
import com.empconn.persistence.entities.ProjectLocation;
import com.empconn.persistence.entities.ProjectResource;
import com.empconn.persistence.entities.ProjectSubCategory;
import com.empconn.persistence.entities.SalesforceIdentifier;
import com.empconn.repositories.AccountRepository;
import com.empconn.repositories.EmployeeRepository;
import com.empconn.repositories.HorizontalRepository;
import com.empconn.repositories.ProjectRepository;
import com.empconn.repositories.ProjectSubCategoryRepository;
import com.empconn.security.SecurityUtil;
import com.empconn.utilities.IterableUtils;

@Mapper(componentModel = "spring", uses = { ProjectLocationDtoMapper.class, CommonQualifiedMapper.class })
public abstract class ProjectDtoMapper {

	@Autowired
	ProjectRepository projectRepository;

	@Autowired
	ProjectLocationDtoMapper projectLocationDtoMapper;

	@Autowired
	ProjectChecklistDtoMapper projectChecklistDtoMapper;

	@Autowired
	ProjectSubCategoryRepository projectSubCategoryRepository;

	@Autowired
	HorizontalRepository horizontalRepository;

	@Autowired
	EmployeeRepository employeeRepository;

	@Autowired
	AccountRepository accountRepository;

	@Autowired
	SecurityUtil jwtEmployeeUtil;

	@Autowired
	ProjectToSaveAccountDtoMapper projectToSaveAccountDtoMapper;

	@Mapping(source = "kickOffMeetingRequired", target = "projectKickoffIsRequired")
	@Mapping(source = "projectTOKLink", target = "projectTokLink")
	@Mapping(source = "accountTOKLink", target = "account.accountTokLink")
	public abstract void preApprovalCheckDtoToProject(PreApprovalCheckDto preApprovalCheckDto,
			@MappingTarget Project project);

	public Project checklistPmoDtoToProject(ChecklistPmoDto checklistPmoDto) {
		final Project project = projectRepository.findByProjectId(Long.parseLong(checklistPmoDto.getProjectId()));
		preApprovalCheckDtoToProject(checklistPmoDto.getPreApprovalCheck(), project);
		final Set<ProjectChecklist> projectChecklistSet = projectChecklistDtoMapper
				.checklistDtoToProjectChecklistSet(checklistPmoDto.getCheckItemList(), project);
		project.setProjectChecklists(projectChecklistSet);
		return project;
	}

	@Mapping(source = "name", target = "projectName")
	@Mapping(source = "projectSubCategory.name", target = "projectCategory")
	@Mapping(source = "currentStatus", target = "pinStatus", qualifiedByName = "currentStatusToDtoPinStatus")
	@Mapping(source = "account.name", target = "accountName")
	@Mapping(source = "startDate", target = "startDate", qualifiedByName = "DateToLocalDate")
	@Mapping(source = "createdBy", target = "createdBy", qualifiedByName = "employeeIdToEmployeeFullName")
	public abstract MyPinDto projectToMypinDto(Project project);

	public abstract List<MyPinDto> projectToMypinDtos(List<Project> project);

	@Mapping(source = "dbList", target = "database", qualifiedByName = "stringListToCommaStrings")
	@Mapping(source = "osList", target = "operatingSystem", qualifiedByName = "stringListToCommaStrings")
	@Mapping(source = "techList", target = "technology", qualifiedByName = "stringListToCommaStrings")
	@Mapping(target = "salesforceIdentifiers", expression = "java(getSalesforceIdentifiersForProject(savePinDto.getSalesforceIdList(), project))")
	@Mapping(target = "modifiedBy", expression = "java(jwtEmployeeUtil.getLoggedInEmployee().getEmployeeId())")
	@Mapping(target = "modifiedOn", expression = "java(new java.sql.Timestamp(System.currentTimeMillis()))")
	@Mapping(source = "locationList", target = "projectLocations")
	@Mapping(source = "accountId", target = "account", qualifiedByName = "accountIdToAccount")
	@Mapping(source = "businessManagerId", target = "employee3", qualifiedByName = "employeeIdToEmployee")
	@Mapping(source = "qaGDMId", target = "employee2", qualifiedByName = "employeeIdToEmployee")
	@Mapping(source = "devGDMId", target = "employee1", qualifiedByName = "employeeIdToEmployee")
	@Mapping(source = "parentProjectId", target = "project", qualifiedByName = "parentProjectIdToProject")
	@Mapping(source = "horizontalId", target = "horizontal", qualifiedByName = "horizontalIdToHorizontal")
	@Mapping(source = "subCategoryId", target = "projectSubCategory", qualifiedByName = "subCategoryIdToProjectSubCategory")
	public abstract Project savePinDtoToProject(SavePinDto savePinDto, @MappingTarget Project project);

	@Mapping(source = "dbList", target = "database", qualifiedByName = "stringListToCommaStrings")
	@Mapping(source = "osList", target = "operatingSystem", qualifiedByName = "stringListToCommaStrings")
	@Mapping(source = "techList", target = "technology", qualifiedByName = "stringListToCommaStrings")
	@Mapping(target = "salesforceIdentifiers", expression = "java(getSalesforceIdentifiersForProject(savePinDto.getSalesforceIdList(), project))")
	@Mapping(target = "createdBy", expression = "java(jwtEmployeeUtil.getLoggedInEmployee().getEmployeeId())")
	@Mapping(target = "createdOn", expression = "java(new java.sql.Timestamp(System.currentTimeMillis()))")
	@Mapping(source = "locationList", target = "projectLocations")
	@Mapping(source = "accountId", target = "account", qualifiedByName = "accountIdToAccount")
	@Mapping(source = "businessManagerId", target = "employee3", qualifiedByName = "employeeIdToEmployee")
	@Mapping(source = "qaGDMId", target = "employee2", qualifiedByName = "employeeIdToEmployee")
	@Mapping(source = "devGDMId", target = "employee1", qualifiedByName = "employeeIdToEmployee")
	@Mapping(source = "parentProjectId", target = "project", qualifiedByName = "parentProjectIdToProject")
	@Mapping(source = "horizontalId", target = "horizontal", qualifiedByName = "horizontalIdToHorizontal")
	@Mapping(source = "subCategoryId", target = "projectSubCategory", qualifiedByName = "subCategoryIdToProjectSubCategory")
	@Mapping(target = "projectChecklists", expression = "java(projectToSaveAccountDtoMapper.getDefaultChecklistForProject(project))")
	@Mapping(target = "isActive", constant = "true")
	public abstract Project savePinDtoToProject(SavePinDto savePinDto);

	@Mapping(source = "projectLocations", target = "locationList")
	public abstract ValidatedPinDto projectToValidatedPinDto(Project project);

	@Mapping(target = "createdBy", expression = "java(jwtEmployeeUtil.getLoggedInEmployee().getEmployeeId())")
	@Mapping(target = "createdOn", expression = "java(new java.sql.Timestamp(System.currentTimeMillis()))")
	@Mapping(target = "modifiedOn", ignore = true)
	@Mapping(target = "modifiedBy", ignore = true)
	@Mapping(source = "project", target = "project")
	@Mapping(target = "isActive", constant = "true")
	@Mapping(source = "source", target = "value")
	public abstract SalesforceIdentifier salesforceToSalesforceIdentifier(String source, Project project);

	public Set<SalesforceIdentifier> getSalesforceIdentifiersForProject(List<String> salesforceIdList,
			Project project) {
		if (CollectionUtils.isEmpty(salesforceIdList))
			return null;
		final Set<SalesforceIdentifier> set = new HashSet<>();
		IterableUtils.removeIfNullOrEmpty(salesforceIdList);
		if (CollectionUtils.isEmpty(project.getSalesforceIdentifiers())) {
			for (final String sf : salesforceIdList) {
				set.add(salesforceToSalesforceIdentifier(sf, project));
			}
			return set;
		} else {
			for (final String sf : salesforceIdList) {
				final Optional<SalesforceIdentifier> sfExist = project.getSalesforceIdentifiers().stream()
						.filter(s -> s.getIsActive() == true && s.getValue().equals(sf)).findFirst();
				if (sfExist.isPresent())
					set.add(sfExist.get());
				else
					set.add(salesforceToSalesforceIdentifier(sf, project));
			}
			return set;
		}
	}

	@Mapping(source = "name", target = "projectName")
	@Mapping(source = "account.vertical.name", target = "vertical")
	@Mapping(source = "projectSubCategory.name", target = "subCategory")
	@Mapping(source = "account.name", target = "customerName")
	@Mapping(source = "employee3.fullName", target = "businessManagerName")
	@Mapping(source = "employee3.fullName", target = "projectManagerName")
	@Mapping(source = "description", target = "scope")
	@Mapping(source = "startDate", target = "startDate", qualifiedByName = "DateToFormattedDate")
	@Mapping(source = "endDate", target = "endDate", qualifiedByName = "DateToFormattedDate")
	@Mapping(source = "createdBy", target = "initiatedBy")
	@Mapping(source = "createdOn", target = "initiatedDate", qualifiedByName = "TimestampToFormattedDate")
	@Mapping(source = "approvedBy", target = "approvedBy")
	@Mapping(source = "approvedOn", target = "approvedDate", qualifiedByName = "DateToFormattedDate")
	@Mapping(source = "projectLocations", target = "resourceInformation", qualifiedByName = "projectLocationToListOfResource")
	@Mapping(source = "projectChecklists", target = "checklist")
	public abstract ProjectInformationDto projectToProjectInformationDto(Project project);

	@Mapping(source = "projectLocation.location.name", target = "location")
	@Mapping(source = "title.name", target = "title")
	@Mapping(source = "endDate", target = "endDate", qualifiedByName = "DateToFormattedDate")
	@Mapping(source = "startDate", target = "startDate", qualifiedByName = "DateToFormattedDate")
	@Mapping(source = "primarySkill.name", target = "primarySkills")
	public abstract ResourceInformationDto projectResourceToResourceInformationDto(ProjectResource projectResource);

	@Mapping(target = "comments", expression = "java(getCommentFromChecklist(source))")
	@Mapping(source = "checklist.name", target = "description")
	public abstract CheckListInformationDto projectToCheckListInformationDto(ProjectChecklist source);

	public String getCommentFromChecklist(ProjectChecklist source) {
		return source.getIsSelected() != null && source.getIsSelected() ? source.getComment() : "NA";
	}

	@Named("projectLocationToListOfResource")
	public Set<ResourceInformationDto> projectLocationToListOfResource(Set<ProjectLocation> projectLocations) {
		final Set<ResourceInformationDto> resourceInformationDtos = new HashSet<ResourceInformationDto>();
		for (final ProjectLocation location : projectLocations) {
			if (location.getIsActive()) {
				for (final ProjectResource projectResource : location.getProjectResources()) {
					if (projectResource.getIsActive()) {
						resourceInformationDtos.add(projectResourceToResourceInformationDto(projectResource));
					}
				}
			}
		}

		return resourceInformationDtos;
	}

	@AfterMapping
	protected void asignMappingRelations(SavePinDto savePinDto, @MappingTarget Project project) {
		if (project.getProjectLocations() != null) {
			project.getProjectLocations().stream().forEach(pl -> pl.setProject(project));
		}
	}

	@Named("subCategoryIdToProjectSubCategory")
	ProjectSubCategory subCategoryIdToProjectSubCategory(String subCategoryId) {
		if (subCategoryId != null) {
			return projectSubCategoryRepository.findById(Integer.valueOf(subCategoryId)).get();
		}
		return null;
	}

	@Named("horizontalIdToHorizontal")
	Horizontal horizontalIdToHorizontal(String horizontalId) {
		if (horizontalId != null) {
			return horizontalRepository.findById(Integer.valueOf(horizontalId)).get();
		}
		return null;

	}

	@Named("parentProjectIdToProject")
	Project parentProjectIdToProject(String parentProjectId) {
		if (parentProjectId != null) {
			return projectRepository.findByProjectId(Long.valueOf(parentProjectId));
		}
		return null;

	}

	@Named("employeeIdToEmployee")
	Employee employeeIdToEmployee(String employeeId) {
		if (employeeId != null) {
			return employeeRepository.findById(Long.valueOf(employeeId)).get();
		}
		return null;
	}

	@Named("employeeIdToEmployeeFullName")
	public String employeeIdToEmployeeFullName(Long employeeId) {
		if (employeeId == null) {
			return null;
		}
		final Optional<Employee> employee = employeeRepository.findById(employeeId);
		if (employee.isPresent()) {
			return CommonQualifiedMapper.employeeToFullName(employee.get());
		} else
			return null;
	}

	@Named("accountIdToAccount")
	Account accountIdToAccount(String accountId) {
		if (accountId != null) {
			return accountRepository.findById(Integer.valueOf(accountId)).get();
		}
		return null;
	}

}
