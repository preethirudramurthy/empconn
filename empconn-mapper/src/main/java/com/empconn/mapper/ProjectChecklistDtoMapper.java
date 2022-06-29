package com.empconn.mapper;

import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;
import org.mapstruct.Named;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.CollectionUtils;

import com.empconn.dto.CheckItemDto;
import com.empconn.persistence.entities.Checklist;
import com.empconn.persistence.entities.Project;
import com.empconn.persistence.entities.ProjectChecklist;
import com.empconn.repositories.ChecklistRespository;
import com.empconn.repositories.ProjectRepository;
import com.empconn.security.SecurityUtil;

@Mapper(componentModel = "spring")
public abstract class ProjectChecklistDtoMapper {

	@Autowired
	ChecklistRespository checklistRespository;

	@Autowired
	ProjectRepository projectRepository;

	@Autowired
	SecurityUtil jwtEmployeeUtil;

	@Mapping(target = "modifiedBy", expression = "java(jwtEmployeeUtil.getLoggedInEmployee().getEmployeeId())")
	@Mapping(target = "modifiedOn", expression = "java(new java.sql.Timestamp(System.currentTimeMillis()))")
	@Mapping(source = "checked", target = "isSelected")
	public abstract ProjectChecklist checkItemDtoToProjectChecklist(CheckItemDto checkItemDto,
			@MappingTarget ProjectChecklist projectChecklist);

	public Set<ProjectChecklist> checklistDtoToProjectChecklistSet(List<CheckItemDto> checkItemDtos, Project project) {
		final Set<ProjectChecklist> projectChecklists = new HashSet<>();
		if (CollectionUtils.isEmpty(checkItemDtos)) {
			for (final ProjectChecklist projectChecklist : project.getProjectChecklists()) {
				projectChecklists.add(checkItemDtoToProjectChecklist(new CheckItemDto(), projectChecklist));
			}
			return projectChecklists;
		}

		for (final ProjectChecklist projectChecklist : project.getProjectChecklists()) {
			final Optional<CheckItemDto> checkItem = checkItemDtos.stream().filter(c -> Integer
					.valueOf(c.getCheckListItemId()).equals(projectChecklist.getChecklist().getChecklistId()))
					.findFirst();
			if (checkItem.isPresent())
				projectChecklists.add(checkItemDtoToProjectChecklist(checkItem.get(), projectChecklist));
			else
				projectChecklists.add(checkItemDtoToProjectChecklist(new CheckItemDto(), projectChecklist));
		}
		return projectChecklists;
	}

	@Named("checklistIdToChecklist")
	public Checklist checklistIdToChecklist(String checklistId) {
		if (checklistId != null) {
			final Optional<Checklist> checklist = checklistRespository.findById(Integer.parseInt(checklistId));
			if (checklist.isPresent()) 
				return checklist.get();
		}
		return null;

	}

}
