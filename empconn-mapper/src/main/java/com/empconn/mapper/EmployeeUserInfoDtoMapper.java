package com.empconn.mapper;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Named;

import com.empconn.dto.UserInfoDto;
import com.empconn.persistence.entities.Employee;
import com.empconn.persistence.entities.EmployeeRole;

@Mapper(componentModel = "spring")
public abstract class EmployeeUserInfoDtoMapper {

	@Mapping(target = "roleList", source = "employeeRoles", qualifiedByName = "employeeRolesToRoles")
	@Mapping(target = "loginId", source = "email")
	@Mapping(target = "resourceId", source = "employeeId")
	abstract UserInfoDto employeeToUserInfoDto(Employee source);

	public abstract List<UserInfoDto> employeeToUserInfoDto(List<Employee> employees);

	@Named("employeeRolesToRoles")
	public List<String> employeeRolesToRoles(Set<EmployeeRole> employeeRoles) {
		return employeeRoles.stream().filter(EmployeeRole::getIsActive).map(e -> e.getRole().getName())
				.collect(Collectors.toList());

	}

}
