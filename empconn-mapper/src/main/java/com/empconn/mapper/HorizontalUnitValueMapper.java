package com.empconn.mapper;

import java.util.List;
import java.util.Set;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import com.empconn.persistence.entities.Horizontal;
import com.empconn.vo.UnitValue;

@Mapper(componentModel = "spring")
public interface HorizontalUnitValueMapper {

	@Mapping(source = "horizontalId", target = "id")
	@Mapping(source = "name", target = "value")
	UnitValue horizontalToUnitValue(Horizontal source);

	Set<UnitValue> horizontalsToUnitValues(Set<Horizontal> horizontals);
	List<UnitValue> horizontalsToUnitValues(List<Horizontal> horizontals);

	@Mapping(source = "id", target = "horizontalId")
	@Mapping(source = "value", target = "name")
	Horizontal unitValueToHorizontal(UnitValue destination);
}