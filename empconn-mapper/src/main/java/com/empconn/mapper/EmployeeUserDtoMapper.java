package com.empconn.mapper;

import java.util.List;
import java.util.Set;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import com.empconn.dto.UserDto;
import com.empconn.persistence.entities.Employee;
import com.empconn.persistence.entities.EmployeeRole;

@Mapper(componentModel = "spring")
public abstract class EmployeeUserDtoMapper {

	@Mapping(source = "employeeRoles", target = "roleList")
	@Mapping(target = "fullName", expression = "java(source.getFirstName())")
	@Mapping(source = "employeeId", target = "resourceId")
	public abstract UserDto employeeToUserDto(Employee source);

	public String employeeRoleToString(EmployeeRole empRole) {
		return empRole.getRole().getName();
	}

	public abstract List<UserDto> employeesToUserDtos(Set<Employee> employees);
}
