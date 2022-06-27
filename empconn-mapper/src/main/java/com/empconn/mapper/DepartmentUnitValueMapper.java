package com.empconn.mapper;

import java.util.List;
import java.util.Set;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import com.empconn.persistence.entities.Department;
import com.empconn.vo.UnitValue;

@Mapper(componentModel = "spring")
public interface DepartmentUnitValueMapper {
	@Mapping(source = "departmentId", target = "id")
	@Mapping(source = "name", target = "value")
	UnitValue departmentToUnitValue(Department source);

	Set<UnitValue> departmentsToUnitValues(Set<Department> departments);
	List<UnitValue> departmentsToUnitValues(List<Department> departments);

	@Mapping(source = "id", target = "departmentId")
	@Mapping(source = "value", target = "name")
	Department unitValueTodepartment(UnitValue destination);
}
