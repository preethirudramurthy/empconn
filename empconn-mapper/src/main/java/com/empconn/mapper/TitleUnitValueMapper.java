package com.empconn.mapper;

import java.util.List;
import java.util.Set;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import com.empconn.persistence.entities.Title;
import com.empconn.vo.UnitValue;

@Mapper(componentModel = "spring")
public interface TitleUnitValueMapper {

	@Mapping(source = "titleId", target = "id")
	@Mapping(source = "name", target = "value")
	UnitValue titleToUnitValue(Title source);

	Set<UnitValue> titlesToUnitValues(Set<Title> titles);
	List<UnitValue> titlesToUnitValues(List<Title> titles);

	@Mapping(source = "id", target = "titleId")
	@Mapping(source = "value", target = "name")
	Title unitValueToTitle(UnitValue destination);
}