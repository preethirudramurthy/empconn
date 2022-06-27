package com.empconn.mapper;

import java.util.List;
import java.util.Set;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import com.empconn.persistence.entities.ProjectSubCategory;
import com.empconn.vo.UnitValue;

@Mapper(componentModel = "spring")
public interface ProjectSubCategoryUnitValueMapper {

	@Mapping(source = "projectSubCategoryId", target = "id")
	@Mapping(source = "name", target = "value")
	UnitValue projectSubCategoryToUnitValue(ProjectSubCategory source);

	Set<UnitValue> projectSubCategoriesToUnitValues(Set<ProjectSubCategory> projectSubCategorys);
	List<UnitValue> projectSubCategoriesToUnitValues(List<ProjectSubCategory> projectSubCategorys);

	@Mapping(source = "id", target = "projectSubCategoryId")
	@Mapping(source = "value", target = "name")
	ProjectSubCategory unitValueToProjectSubCategory(UnitValue destination);
}