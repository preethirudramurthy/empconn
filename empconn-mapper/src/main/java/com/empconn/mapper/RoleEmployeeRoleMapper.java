package com.empconn.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.springframework.beans.factory.annotation.Autowired;

import com.empconn.persistence.entities.EmployeeRole;
import com.empconn.persistence.entities.Role;
import com.empconn.security.SecurityUtil;

@Mapper(componentModel = "spring")
public abstract class RoleEmployeeRoleMapper {

	@Autowired
	SecurityUtil jwtEmployeeUtil;

	@Mapping(target = "role", source = "source")
	@Mapping(target = "createdBy", expression = "java(jwtEmployeeUtil.getLoggedInEmployee().getEmployeeId())")
	@Mapping(target = "isActive", constant = "true")
	@Mapping(target = "modifiedBy", ignore = true)
	@Mapping(target = "modifiedOn", ignore = true)
	@Mapping(target = "createdOn", ignore = true)
	public abstract EmployeeRole roleToEmployeeRole(Role source);
}
