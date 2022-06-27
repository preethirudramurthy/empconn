package com.empconn.mapper;

import java.util.List;
import java.util.Set;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import com.empconn.persistence.entities.Project;
import com.empconn.vo.UnitValue;

@Mapper(componentModel = "spring")
public interface ProjectUnitValueMapper {

	@Mapping(source = "projectId", target = "id")
	@Mapping(source = "name", target = "value")
	UnitValue projectToUnitValue(Project source);

	Set<UnitValue> projectsToUnitValues(Set<Project> projects);

	Set<UnitValue> projectsToUnitValues(List<Project> projects);

	List<UnitValue> projectsToUnitValueList(List<Project> projects);

	@Mapping(source = "id", target = "projectId")
	@Mapping(source = "value", target = "name")
	Project unitValueToProject(UnitValue destination);
}