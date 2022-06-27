package com.empconn.mapper;

import java.util.List;
import java.util.Set;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import com.empconn.persistence.entities.WorkGroup;
import com.empconn.vo.UnitValue;

@Mapper(componentModel = "spring")
public interface WorkgroupUnitValueMapper {

	@Mapping(source = "workGroupId", target = "id")
	@Mapping(source = "name", target = "value")
	UnitValue workgroupToUnitValue(WorkGroup source);

	Set<UnitValue> workgoupsToUnitValues(List<WorkGroup> workgroups);

}