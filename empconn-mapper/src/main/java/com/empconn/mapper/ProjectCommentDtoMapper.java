package com.empconn.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Named;
import org.springframework.beans.factory.annotation.Autowired;

import com.empconn.dto.PinStatusChangeCommentDto;
import com.empconn.persistence.entities.Project;
import com.empconn.persistence.entities.ProjectComment;
import com.empconn.repositories.ProjectRepository;
import com.empconn.security.SecurityUtil;

@Mapper(componentModel = "spring")
public abstract class ProjectCommentDtoMapper {

	@Autowired
	ProjectRepository projectRepository;

	@Autowired
	SecurityUtil jwtEmployeeUtil;

	@Mapping(source = "dto.comment", target = "value")
	@Mapping(source = "dto.projectId", target = "project", qualifiedByName = "projectIdToProject")
	@Mapping(target = "createdBy", expression = "java(jwtEmployeeUtil.getLoggedInEmployee().getEmployeeId())")
	@Mapping(target = "createdOn", expression = "java(new java.sql.Timestamp(System.currentTimeMillis()))")
	@Mapping(target = "isActive", constant = "true")
	public abstract ProjectComment commentDtoToProjectComment(PinStatusChangeCommentDto dto, String status);

	@Named("projectIdToProject")
	public Project projectIdToProject(String projectId) {
		if (null != projectId) {
			return projectRepository.findByProjectId(Long.valueOf(projectId));
		}
		return null;
	}

}
