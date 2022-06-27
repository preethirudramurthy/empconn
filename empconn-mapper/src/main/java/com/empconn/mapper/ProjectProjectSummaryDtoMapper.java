package com.empconn.mapper;

import java.util.List;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.springframework.beans.factory.annotation.Autowired;

import com.empconn.dto.ProjectSummaryDto;
import com.empconn.persistence.entities.Project;
import com.empconn.repositories.AllocationRepository;

@Mapper(componentModel = "spring", uses = { CommonQualifiedMapper.class })
public abstract class ProjectProjectSummaryDtoMapper {

	@Autowired
	AllocationRepository allocationRepository;

	@Mapping(source = "endDate", target = "endDate", qualifiedByName = "DateToLocalDate")
	@Mapping(source = "startDate", target = "startDate", qualifiedByName = "DateToLocalDate")
	@Mapping(target = "noOfResources", expression = "java(allocationRepository.findNumberOfResourcesForProject(source.getProjectId()))")
	@Mapping(source = "horizontal.name", target = "horizontal")
	@Mapping(source = "currentStatus", target = "status", qualifiedByName = "currentStatusToDtoPinStatus")
	@Mapping(source = "name", target = "projectName")
	@Mapping(source = "account.vertical.name", target = "vertical")
	@Mapping(source = "account.name", target = "accountName")
	@Mapping(source = "projectSubCategory.name", target = "subCategory")
	public abstract ProjectSummaryDto projectToProjectSummaryDto(Project source);

	public abstract List<ProjectSummaryDto> projectsToProjectSummaryDtos(List<Project> projects);

}
