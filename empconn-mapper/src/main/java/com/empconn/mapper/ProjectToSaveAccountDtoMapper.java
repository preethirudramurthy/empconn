package com.empconn.mapper;

import java.util.HashSet;
import java.util.Set;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.springframework.beans.factory.annotation.Autowired;

import com.empconn.dto.SaveAccountDto;
import com.empconn.persistence.entities.Checklist;
import com.empconn.persistence.entities.Project;
import com.empconn.persistence.entities.ProjectChecklist;
import com.empconn.repositories.ChecklistRespository;
import com.empconn.security.SecurityUtil;

@Mapper(componentModel = "spring")
public abstract class ProjectToSaveAccountDtoMapper {

	@Autowired
	SecurityUtil jwtEmployeeUtil;

	@Autowired
	ChecklistRespository checklistRespository;

	@Mapping(target = "createdBy", expression = "java(jwtEmployeeUtil.getLoggedInEmployee().getEmployeeId())")
	@Mapping(target = "createdOn", expression = "java(new java.sql.Timestamp(System.currentTimeMillis()))")
	@Mapping(target = "description", ignore = true)
	@Mapping(target = "startDate", ignore = true)
	@Mapping(target = "endDate", ignore = true)
	@Mapping(target = "currentStatus", expression = "java(com.empconn.enums.ProjectStatus.DRAFT.name())")
	@Mapping(source = "projectId", target = "projectId")
	@Mapping(source = "projectName", target = "name")
	@Mapping(target = "projectChecklists", expression = "java(getDefaultChecklistForProject(project))")
	@Mapping(target = "isActive", constant = "true")
	public abstract Project saveAccountDtoMapperToProject(SaveAccountDto dto);

	public Set<ProjectChecklist> getDefaultChecklistForProject(Project project) {
		final Set<Checklist> checklists = checklistRespository.findByIsActive(true);
		final Set<ProjectChecklist> projectChecklists = new HashSet<>();
		for (final Checklist checklist : checklists) {
			projectChecklists.add(checklistToProjectChecklist(checklist, project));
		}
		return projectChecklists;
	}

	@Mapping(target = "createdBy", expression = "java(jwtEmployeeUtil.getLoggedInEmployee().getEmployeeId())")
	@Mapping(target = "createdOn", expression = "java(new java.sql.Timestamp(System.currentTimeMillis()))")
	@Mapping(target = "modifiedBy", ignore = true)
	@Mapping(target = "modifiedOn", ignore = true)
	@Mapping(source = "project", target = "project")
	@Mapping(source = "checklist", target = "checklist")
	@Mapping(target = "isActive", constant = "true")
	@Mapping(target = "isSelected", constant = "true")
	public abstract ProjectChecklist checklistToProjectChecklist(Checklist checklist, Project project);
}
