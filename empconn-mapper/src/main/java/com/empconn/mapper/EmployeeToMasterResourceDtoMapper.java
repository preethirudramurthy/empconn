package com.empconn.mapper;

import java.util.Set;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Named;

import com.empconn.dto.MasterResourceDto;
import com.empconn.persistence.entities.Employee;
import com.empconn.persistence.entities.Title;

@Mapper(componentModel = "spring")
public interface EmployeeToMasterResourceDtoMapper {

	@Mapping(source = "employeeId", target = "id")
	@Mapping(source = "title", target = "title", qualifiedByName = "GetTitleName")
	@Mapping(target = "value", expression = "java(source.getFirstName() + \" \" + source.getLastName())")
	MasterResourceDto employeeToMasterResourceDto(Employee source);

	Set<MasterResourceDto> employeesToMasterResourcesDto(Set<Employee> source);

	@Named("GetTitleName")
	public static String titleToTitleName(Title title) {
		return title.getName();
	}
}