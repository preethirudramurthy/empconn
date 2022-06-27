package com.empconn.mapper;

import java.util.List;
import java.util.Set;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Named;
import org.springframework.beans.factory.annotation.Autowired;

import com.empconn.constants.ApplicationConstants;
import com.empconn.persistence.entities.ProjectChange;
import com.empconn.security.SecurityUtil;
import com.empconn.successfactors.dto.ProjectChangeDto;

@Mapper(componentModel = "spring", uses = {CommonQualifiedMapper.class })
public abstract class ProjectChangeDtoToProjectChangeMapper {

	@Autowired
	SecurityUtil jwtEmployeeUtil;

	@Mapping(source = "userId", target = "employee.employeeId")
	@Mapping(source = "effectiveStartDate", target = "effectiveStartDate", qualifiedByName = "DateToLocalDate")
	@Mapping(target = "status", constant = "Pending")
	@Mapping(source = "project", target = "project.projectId")
	@Mapping(source = "account", target = "account.accountId")

	//Added audit-log fields
	@Mapping(target = "createdBy", expression = "java(jwtEmployeeUtil.getLoggedInEmployee().getEmployeeId())")
	@Mapping(target = "createdOn", expression = "java(new java.sql.Timestamp(System.currentTimeMillis()))")
	@Mapping(target = "modifiedBy", expression = "java(jwtEmployeeUtil.getLoggedInEmployee().getEmployeeId())")
	@Mapping(target = "modifiedOn", expression = "java(new java.sql.Timestamp(System.currentTimeMillis()))")
	@Mapping(target = "isActive", constant = "true")
	public abstract ProjectChange mapToEntity(ProjectChangeDto projectChangeDto);

	public abstract List<ProjectChange> mapToEntitys(List<ProjectChangeDto> projectChangeDtoSet);
	public abstract Set<ProjectChange> mapToEntitys(Set<ProjectChangeDto> projectChangeDtoSet);

	@Mapping(source = "employee.empCode", target = "userId")
	@Mapping(source = "effectiveStartDate", target = "effectiveStartDate")
	@Mapping(source = "projectChange", target = "project", qualifiedByName = "getProjectName")
	@Mapping(source = "projectChange", target = "account", qualifiedByName = "getAccountName")
	public abstract ProjectChangeDto mapToDto(ProjectChange projectChange);

	public abstract Set<ProjectChangeDto> mapToDtos(Set<ProjectChange> projectChangeSet);

	@Named("getProjectName")
	public static String getProjectName(ProjectChange projectChange) {
		if(projectChange.getProject().getName().equals(ApplicationConstants.NON_DELIVERY_BENCH_PROJECT_NAME)) {
			return projectChange.getEmployee().getDepartment().getName();
		}else {
			return projectChange.getProject().getName();
		}
	}

	@Named("getAccountName")
	public static String getAccountName(ProjectChange projectChange) {
		if(projectChange.getProject().getName().equals(ApplicationConstants.NON_DELIVERY_BENCH_PROJECT_NAME)) {
			return projectChange.getEmployee().getDepartment().getName();
		}else {
			return projectChange.getAccount().getName();
		}
	}
}
