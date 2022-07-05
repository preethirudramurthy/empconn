package com.empconn.mapper;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Named;
import org.springframework.util.CollectionUtils;

import com.empconn.dto.UserDto;
import com.empconn.persistence.entities.Employee;
import com.empconn.persistence.entities.EmployeeRole;

@Mapper(componentModel = "spring")
public interface EmployeeToUserDtoMapper {

	@Mapping(source = "employeeId", target = "resourceId")
	@Mapping(source = "fullName", target = "fullName")
	@Mapping(source = "employeeRoles", target = "roleList", qualifiedByName = "GetRoles")
	UserDto employeeToUserDto(Employee source);

	List<UserDto> employeesToUsersDto(List<Employee> employees);

	@Named("GetRoles")
	static List<String> getRoles(Set<EmployeeRole> employeeRoles) {
		if (!CollectionUtils.isEmpty(employeeRoles))
			return employeeRoles.stream().filter(
					er -> (er.getIsActive() && null != er.getRole().getName()))
					.map(er -> er.getRole().getName()).collect(Collectors.toList());

		return new ArrayList<>();
	}
}