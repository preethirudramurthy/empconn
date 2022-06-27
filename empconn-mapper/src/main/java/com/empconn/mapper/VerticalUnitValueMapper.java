package com.empconn.mapper;

import java.util.List;
import java.util.Set;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import com.empconn.persistence.entities.Vertical;
import com.empconn.vo.UnitValue;

@Mapper(componentModel = "spring")
public interface VerticalUnitValueMapper {

	@Mapping(source = "verticalId", target = "id")
	@Mapping(source = "name", target = "value")
	UnitValue verticalToUnitValue(Vertical source);

	Set<UnitValue> verticalsToUnitValues(Set<Vertical> verticals);
	List<UnitValue> verticalsToUnitValues(List<Vertical> verticals);

	@Mapping(source = "id", target = "verticalId")
	@Mapping(source = "value", target = "name")
	Vertical unitValueToVertical(UnitValue destination);
}