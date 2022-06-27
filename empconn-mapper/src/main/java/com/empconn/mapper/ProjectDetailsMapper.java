package com.empconn.mapper;

import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import org.apache.commons.lang3.StringUtils;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Named;

import com.empconn.dto.LocationManagersPDto;
import com.empconn.dto.ProjectDetailsDto;
import com.empconn.enums.ProjectStatus;
import com.empconn.persistence.entities.Project;
import com.empconn.persistence.entities.ProjectComment;
import com.empconn.persistence.entities.ProjectLocation;

@Mapper(componentModel = "spring", uses = { ProjectPinMapper.class, CommonQualifiedMapper.class })
public abstract class ProjectDetailsMapper {

	@Mapping(source = "account.accountId", target = "accountId")
	@Mapping(source = "name", target = "projectName")
	@Mapping(source = "account.vertical.name", target = "vertical")
	@Mapping(source = "horizontal.name", target = "horizontal")
	@Mapping(source = "account.name", target = "accountName")
	@Mapping(source = "account.category", target = "category")
	@Mapping(source = "projectSubCategory.name", target = "subCategory")
	@Mapping(source = "source", target = "projectCode", qualifiedByName = "customizeProjectId")
	@Mapping(source = "project.name", target = "parentProjectName")
	@Mapping(source = "salesforceIdentifiers", target = "salesforceIdList", qualifiedByName = "salesforceIdentifiersToStringList")
	@Mapping(source = "endDate", target = "endDate", qualifiedByName = "DateToLocalDate")
	@Mapping(source = "startDate", target = "startDate", qualifiedByName = "DateToLocalDate")
	@Mapping(target = "initiationMeetingDate", expression = "java(ProjectPinMapper.getInitiationMeetingDeadline(source))")
	@Mapping(source = "database", target = "dbList", qualifiedByName = "commaStringsTostringList")
	@Mapping(source = "operatingSystem", target = "osList", qualifiedByName = "commaStringsTostringList")
	@Mapping(source = "technology", target = "techList", qualifiedByName = "commaStringsTostringList")
	@Mapping(source = "employee1", target = "devGdm", qualifiedByName = "employeeToFullName")
	@Mapping(source = "employee2", target = "qaGdm", qualifiedByName = "employeeToFullName")
	@Mapping(source = "currentStatus", target = "status", qualifiedByName = "currentStatusToDtoPinStatus")
	@Mapping(source = "projectLocations", target = "locationList")
	@Mapping(source = "projectComments", target = "onHoldComment", qualifiedByName = "projectCommentsToLatestOnholdComment")
	@Mapping(source = "projects", target = "subProjectNameList", qualifiedByName = "subProjectsToSubProjectsNameList")
	public abstract ProjectDetailsDto projectToProjectDetailsDto(Project source);

	@Mapping(source = "employee5", target = "manager2", qualifiedByName = "employeeToUnitValue")
	@Mapping(source = "employee4", target = "manager1", qualifiedByName = "employeeToUnitValue")
	@Mapping(source = "employee3", target = "uiManager", qualifiedByName = "employeeToUnitValue")
	@Mapping(source = "employee2", target = "qaManager", qualifiedByName = "employeeToUnitValue")
	@Mapping(source = "employee1", target = "devManager", qualifiedByName = "employeeToUnitValue")
	@Mapping(source = "location.name", target = "locationName")
	public abstract LocationManagersPDto projectLocationsToLocationManagersRRUnitDto(ProjectLocation projectLocation);

	@Named("customizeProjectId")
	String customizeProjectName(Project project) {
		String projectId = null;
		if (project != null) {
			final Calendar calendar = new GregorianCalendar();
			calendar.setTime(project.getStartDate());
			final String accountName = project.getAccount().getName() == null ? ""
					: StringUtils.substring(project.getAccount().getName(), 0, 3);
			final String projectName = project.getName() == null ? "" : StringUtils.substring(project.getName(), 0, 3);
			projectId = calendar.get(Calendar.YEAR) + accountName + projectName + "_" + project.getProjectId();
		}
		return projectId;
	}

	@Named("subProjectsToSubProjectsNameList")
	public List<String> subProjectsToSubProjectsNameList(Set<Project> projects) {
		if (projects == null) {
			return null;
		}
		return projects.stream().map(Project::getName).collect(Collectors.toList());
	}

	@Named("projectCommentsToLatestOnholdComment")
	public String projectCommentsToLatestOnholdComment(Set<ProjectComment> comments) {
		final ProjectComment comment = CommonQualifiedMapper.latestCommentForProjectStatus(comments,
				ProjectStatus.PROJECT_ON_HOLD.name());
		if (comment != null)
			return comment.getValue();
		return null;
	}

}
