package com.empconn.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.springframework.beans.factory.annotation.Autowired;

import com.empconn.persistence.entities.ProjectSubCategory;
import com.empconn.security.SecurityUtil;

@Mapper(componentModel = "spring")
public abstract class ProjectSubcategoryMapper {

	@Autowired
	SecurityUtil jwtEmployeeUtil;

	@Mapping(target = "name", source = "source")
	@Mapping(target = "isActive", constant = "true")
	@Mapping(target = "createdBy", expression = "java(jwtEmployeeUtil.getLoggedInEmployee().getEmployeeId())")
	public abstract ProjectSubCategory stringToProjectSubCategory(String source);

}
