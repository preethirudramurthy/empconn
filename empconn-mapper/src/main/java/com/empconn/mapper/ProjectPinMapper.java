package com.empconn.mapper;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Named;
import org.springframework.beans.factory.annotation.Autowired;

import com.empconn.constants.ApplicationConstants;
import com.empconn.dto.AccountInfoDto;
import com.empconn.dto.AccountLocationContactDto;
import com.empconn.dto.AccountLocationDto;
import com.empconn.dto.ChecklistDto;
import com.empconn.dto.GdmCommentDto;
import com.empconn.dto.LocationManagersRRUnitDto;
import com.empconn.dto.PinDetailsDto;
import com.empconn.dto.PinStatusChangeCommentDto;
import com.empconn.dto.PreApprovalCheckDto;
import com.empconn.dto.ResourceItemUnitDto;
import com.empconn.enums.ProjectStatus;
import com.empconn.persistence.entities.Account;
import com.empconn.persistence.entities.ClientLocation;
import com.empconn.persistence.entities.Contact;
import com.empconn.persistence.entities.Employee;
import com.empconn.persistence.entities.Project;
import com.empconn.persistence.entities.ProjectComment;
import com.empconn.persistence.entities.ProjectLocation;
import com.empconn.persistence.entities.ProjectResource;
import com.empconn.persistence.entities.ProjectResourcesSecondarySkill;
import com.empconn.persistence.entities.SalesforceIdentifier;
import com.empconn.repositories.EmployeeRepository;
import com.empconn.vo.UnitValue;

@Mapper(componentModel = "spring", uses = { ProjectCheckListMapper.class, CommonQualifiedMapper.class })
public abstract class ProjectPinMapper {
	@Autowired
	EmployeeRepository employeeRepository;

	@Mapping(source = "source", target = "preApprovalChecks", qualifiedByName = "projectToPreApprovalCheckDto")
	@Mapping(source = "database", target = "dbList", qualifiedByName = "commaStringsTostringList")
	@Mapping(source = "operatingSystem", target = "osList", qualifiedByName = "commaStringsTostringList")
	@Mapping(source = "technology", target = "techList", qualifiedByName = "commaStringsTostringList")
	@Mapping(source = "projectComments", target = "gdmCommentList", qualifiedByName = "projectCommentsToGdmCommentList")
	@Mapping(source = "projectLocations", target = "locationList")
	@Mapping(source = "createdOn", target = "createdDate", qualifiedByName = "TimestampToLocalDateTime")
	@Mapping(source = "endDate", target = "endDate", qualifiedByName = "DateToLocalDate")
	@Mapping(source = "startDate", target = "startDate", qualifiedByName = "DateToLocalDate")
	@Mapping(source = "projectChecklists", target = "checkListItemList")
	@Mapping(source = "projectSubCategory.projectSubCategoryId", target = "subCategory.id")
	@Mapping(source = "projectSubCategory.name", target = "subCategory.value")
	@Mapping(source = "salesforceIdentifiers", target = "salesforceIdList", qualifiedByName = "salesforceIdentifiersToStringList")
	@Mapping(source = "currentStatus", target = "pinStatus", qualifiedByName = "currentStatusToDtoPinStatus")
	@Mapping(source = "source.project", target = "parentProject", qualifiedByName = "projectToUnitValue")
	@Mapping(source = "name", target = "projectName")
	@Mapping(source = "horizontal", target = "horizontal", qualifiedByName = "horizontalToUnitValue")
	@Mapping(source = "employee1", target = "devGDM", qualifiedByName = "employeeToUnitValue")
	@Mapping(source = "employee2", target = "qaGDM", qualifiedByName = "employeeToUnitValue")
	@Mapping(source = "employee3", target = "businessManager", qualifiedByName = "employeeToUnitValue")
	@Mapping(source = "account", target = "accountInfo", qualifiedByName = "projectAccountToAccountInfo")
	public abstract PinDetailsDto projectToPinDetailsDto(Project source);

	@Mapping(source = "vertical", target = "vertical", qualifiedByName = "verticalToUnitValue")
	@Mapping(source = "category", target = "category", qualifiedByName = "categoryToUnitValue")
	@Mapping(source = "name", target = "accountName")
	@Mapping(source = "createdOn", target = "createdDate", qualifiedByName = "TimestampToLocalDateTime")
	@Mapping(source = "endDate", target = "endDate", qualifiedByName = "DateToLocalDate")
	@Mapping(source = "startDate", target = "startDate", qualifiedByName = "DateToLocalDate")
	@Mapping(source = "clientLocations", target = "accountLocationList")
	@Mapping(source = "clientWebsiteLink", target = "websiteLink")
	@Named("projectAccountToAccountInfo")
	public abstract AccountInfoDto accountToAccountInfoDto(Account source);

	@Mapping(target = "initiationMeetingDeadline", expression = "java(getInitiationMeetingDeadline(source))")
	@Mapping(source = "sendNotificationToPinGroup", target = "sendNotificationToPinGroup")
	@Mapping(source = "projectKickoffIsRequired", target = "kickOffMeetingRequired")
	@Mapping(source = "account.accountTokLink", target = "accountTOKLink")
	@Mapping(source = "projectTokLink", target = "projectTOKLink")
	@Named("projectToPreApprovalCheckDto")
	public abstract PreApprovalCheckDto projectToPreApprovalCheckDto(Project source);

	@Mapping(source = "projectId", target = "projectId")
	@Mapping(source = "comment", target = "currentComment")
	public abstract Project pinStatusCDtoToProject(PinStatusChangeCommentDto source);

	@Mapping(source = "projectId", target = "projectId")
	@Mapping(source = "checkItemList", target = "projectChecklists")
	public abstract Project checkListDtoToProject(ChecklistDto source);

	@Mapping(source = "clientLocationId", target = "accountLocationId")
	@Mapping(source = "location", target = "locationName")
	@Mapping(source = "contacts", target = "accountLocationContactList")
	public abstract AccountLocationDto clientLocationToAccountLocationDto(ClientLocation source);

	@Mapping(source = "contactId", target = "accountLocationContactId")
	@Mapping(source = "name", target = "contactName")
	@Mapping(source = "email", target = "email")
	@Mapping(source = "phoneNumber", target = "phone")
	@Mapping(target = "isReadOnly", constant = "true")
	public abstract AccountLocationContactDto contactToAccountLocationContactDto(Contact source);

	@Mapping(source = "projectResources", target = "rrList")
	@Mapping(source = "employee5", target = "manager2", qualifiedByName = "employeeToUnitValue")
	@Mapping(source = "employee4", target = "manager1", qualifiedByName = "employeeToUnitValue")
	@Mapping(source = "employee3", target = "uiManager", qualifiedByName = "employeeToUnitValue")
	@Mapping(source = "employee2", target = "qaManager", qualifiedByName = "employeeToUnitValue")
	@Mapping(source = "employee1", target = "devManager", qualifiedByName = "employeeToUnitValue")
	@Mapping(source = "location", target = "location", qualifiedByName = "locationToUnitValue")
	public abstract LocationManagersRRUnitDto projectLocationsToLocationManagersRRUnitDto(
			ProjectLocation projectLocation);

	@Mapping(source = "projectResourcesSecondarySkills", target = "secondarySkillIdList")
	@Mapping(source = "primarySkill", target = "primarySkill", qualifiedByName = "primarySkillToUnitValue")
	@Mapping(source = "endDate", target = "endDate", qualifiedByName = "DateToLocalDate")
	@Mapping(source = "startDate", target = "startDate", qualifiedByName = "DateToLocalDate")
	@Mapping(source = "title", target = "title", qualifiedByName = "titleToUnitValue")
	@Mapping(source = "isBillable", target = "billable")
	@Mapping(source = "allocationPercentage", target = "percentage")
	@Mapping(source = "numberOfResources", target = "noOfResources")
	@Mapping(source = "projectResourcesId", target = "rrId")
	public abstract ResourceItemUnitDto projectResourceToResourceItemUnitDto(ProjectResource projectResource);

	@Mapping(source = "projectResourcesSecondarySkill.secondarySkill.name", target = "value")
	@Mapping(source = "projectResourcesSecondarySkill.secondarySkill.secondarySkillId", target = "id")
	public abstract UnitValue projectResourcesSecondarySkillToUnitValue(
			ProjectResourcesSecondarySkill projectResourcesSecondarySkill);

	@Named("salesforceIdentifiersToStringList")
	public List<String> salesforceIdentifiersToStringList(Set<SalesforceIdentifier> salesforceIdentifiers) {
		if (salesforceIdentifiers != null) {
			return salesforceIdentifiers.stream().filter(SalesforceIdentifier::getIsActive)
					.map(SalesforceIdentifier::getValue).collect(Collectors.toList());
		}
		return null;
	}

	@Named("projectCommentsToGdmCommentList")
	public List<GdmCommentDto> projectCommentsToGdmCommentList(Set<ProjectComment> projectComments) {
		final List<GdmCommentDto> gdmCommentDtos = new ArrayList<>();
		if (projectComments != null) {
			projectComments.stream().filter(c -> c.getStatus().equals(ProjectStatus.SENT_BACK.name()))
					.sorted((c1, c2) -> c1.getCreatedOn().compareTo(c2.getCreatedOn())).forEach(c -> {
						final Employee commentor = employeeRepository.findById(c.getCreatedBy()).get();
						gdmCommentDtos.add(new GdmCommentDto(commentor.getEmployeeId().toString(),
								CommonQualifiedMapper.employeeToFullName(commentor), c.getValue(),
								CommonQualifiedMapper.timestampToLocalDateTime(c.getCreatedOn())));
					});
		}
		return gdmCommentDtos;
	}

	public static LocalDateTime getInitiationMeetingDeadline(Project project) {
		if (project.getProjectKickoffIsRequired() != null && project.getProjectKickoffIsRequired() == true
				&& project.getStartDate() != null) {
			final LocalDate startDate = CommonQualifiedMapper.dateToLocalDate(project.getStartDate());
			return startDate.plusDays(ApplicationConstants.PROJECT_INIT_DEADLINE_BY).atStartOfDay();
		}
		return null;
	}

}
