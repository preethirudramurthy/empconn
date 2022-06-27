package com.empconn.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.springframework.beans.factory.annotation.Autowired;

import com.empconn.persistence.entities.Checklist;
import com.empconn.security.SecurityUtil;

@Mapper(componentModel = "spring")
public abstract class ChecklistMapper {

	@Autowired
	SecurityUtil jwtEmployeeUtil;

	@Mapping(target = "name", source = "source")
	@Mapping(target = "isActive", constant = "true")
	@Mapping(target = "createdBy", expression = "java(jwtEmployeeUtil.getLoggedInEmployee().getEmployeeId())")
	public abstract Checklist stringToChecklist(String source);

}
