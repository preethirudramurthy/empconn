package com.empconn.mapper;

import java.util.Set;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import com.empconn.dto.EmployeeDataResponseDto;
import com.empconn.persistence.entities.Allocation;
import com.empconn.persistence.entities.Employee;

@Mapper(componentModel = "spring", uses = { CommonQualifiedMapper.class })
public abstract class AllocationToEmployeeDataResponseDto {

	@Mapping(source = "employee.title.name", target = "designation")
	@Mapping(source = "employee.email", target = "emailId")
	@Mapping(source = "employee.empCode", target = "employeeId")
	@Mapping(source = "employee.empCode", target = "employeeNumber")
	@Mapping(source = "employee.firstName", target = "firstName")
	@Mapping(source = "employee.lastName", target = "lastName")
	@Mapping(source = "employee.loginId", target = "loginId")
	@Mapping(source = "employee.middleName", target = "middleName")
	@Mapping(source = "employee.fullName", target = "name")
	@Mapping(source = "project.name", target = "project")
	@Mapping(source = "project.name", target = "projectName")
	@Mapping(source = "employee.location.name", target = "location")
	public abstract EmployeeDataResponseDto allocationToEmployeeDataResponseDto(Allocation source);

	public abstract Set<EmployeeDataResponseDto> allocationsToEmployeeDataResponseDto(Set<Allocation> source);

	@Mapping(source = "employee.title.name", target = "designation")
	@Mapping(source = "employee.email", target = "emailId")
	@Mapping(source = "employee.empCode", target = "employeeId")
	@Mapping(source = "employee.empCode", target = "employeeNumber")
	@Mapping(source = "employee.firstName", target = "firstName")
	@Mapping(source = "employee.lastName", target = "lastName")
	@Mapping(source = "employee.loginId", target = "loginId")
	@Mapping(source = "employee.middleName", target = "middleName")
	@Mapping(source = "employee.fullName", target = "name")
	@Mapping(source = "employee.location.name", target = "location")
	public abstract EmployeeDataResponseDto employeeToEmployeeDataResponseDto(Employee employee);

	public abstract Set<EmployeeDataResponseDto> employeesToEmployeeDataResponseDto(Set<Employee> employee);

}
