package com.empconn.mapper;

import java.util.List;
import java.util.Set;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import com.empconn.dto.FMSEmployeeDetailsDto;
import com.empconn.persistence.entities.Employee;

@Mapper(componentModel = "spring")
public abstract class EmployeeToFMSEMployeeDetailsDtoMapper extends BaseMapper {

	@Mapping(source = "empCode", target = "employeeNumber")
	@Mapping(source = "email", target = "emailId")
	@Mapping(source = "location.name", target = "locationName")
	@Mapping(source = "title.name", target = "designationName")
	@Mapping(source = "businessUnit.name", target = "businessUnit")
	@Mapping(source = "division.name", target = "division")
	@Mapping(source = "department.name", target = "department")
	@Mapping(source = "primaryAllocation.project.account.name", target = "accountName")
	@Mapping(source = "primaryAllocation.project.name", target = "projectName")
	@Mapping(source = "primaryAllocation.project.account.vertical.name", target = "verticalName")
	@Mapping(source = "primaryAllocation.project.horizontal.name", target = "horizontalName")
	public abstract FMSEmployeeDetailsDto employeeToFmsEmployeeDetailsDto(Employee employee);

	public abstract List<FMSEmployeeDetailsDto> employeesToFmsEmployeesDetailsDto(Set<Employee> employees);

}
