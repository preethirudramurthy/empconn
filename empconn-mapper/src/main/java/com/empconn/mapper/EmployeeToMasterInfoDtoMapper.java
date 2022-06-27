package com.empconn.mapper;

import java.util.Set;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import com.empconn.dto.earmark.ManagerInfoDto;
import com.empconn.persistence.entities.Employee;

@Mapper(componentModel = "spring")
public interface EmployeeToMasterInfoDtoMapper {

	@Mapping(source = "employeeId", target = "id")
	@Mapping(source = "title.name", target = "title")
	@Mapping(target = "value", expression = "java(source.getFirstName() + \" \" + source.getLastName())")
	ManagerInfoDto employeeToMasterInfoDto(Employee source);

	Set<ManagerInfoDto> employeesToMastersDto(Set<Employee> source);


}