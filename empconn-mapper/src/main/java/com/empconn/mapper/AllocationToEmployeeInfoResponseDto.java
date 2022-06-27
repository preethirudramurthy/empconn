package com.empconn.mapper;

import java.util.Set;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import com.empconn.dto.EmployeeInfoResponseDto;
import com.empconn.persistence.entities.Allocation;

@Mapper(componentModel = "spring", uses = { CommonQualifiedMapper.class })
public abstract class AllocationToEmployeeInfoResponseDto {

	@Mapping(source = "employee.title.name", target = "designation")
	@Mapping(source = "employee.email", target = "emailId")
	@Mapping(source = "employee.empCode", target = "employeeId")
	@Mapping(source = "employee.empCode", target = "employeeNumber")
	@Mapping(source = "employee.firstName", target = "firstName")
	@Mapping(source = "employee.isManager", target = "isManager")
	@Mapping(source = "employee.lastName", target = "lastName")
	@Mapping(source = "employee.loginId", target = "loginId")
	@Mapping(source = "employee.middleName", target = "middleName")
	@Mapping(source = "employee.fullName", target = "name")
	@Mapping(source = "employee.primaryAllocation.reportingManagerId.title.name", target = "primaryManagerDesignation")
	@Mapping(source = "employee.primaryAllocation.reportingManagerId.email", target = "primaryManagerEmail")
	@Mapping(source = "employee.primaryAllocation.reportingManagerId.empCode", target = "primaryManagerEmployeeNumber")
	@Mapping(source = "employee.primaryAllocation.reportingManagerId.fullName", target = "primaryManagerName")
	@Mapping(source = "project.name", target = "project")
	@Mapping(source = "project.name", target = "projectName")
	@Mapping(source = "employee.location.name", target = "location")
	public abstract EmployeeInfoResponseDto allocationToEmployeeInfoResponseDto(Allocation source);

	public abstract Set<EmployeeInfoResponseDto> allocationsToEmployeeInfoResponseDto(Set<Allocation> source);

}
