package com.empconn.mapper;

import java.util.List;
import java.util.Set;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import com.empconn.persistence.entities.SecondarySkill;
import com.empconn.vo.UnitValue;

@Mapper(componentModel = "spring")
public interface SecondarySkillUnitValueMapper {

	@Mapping(source = "secondarySkillId", target = "id")
	@Mapping(source = "name", target = "value")
	UnitValue secondarySkillToUnitValue(SecondarySkill source);

	Set<UnitValue> secondarySkillsToUnitValues(Set<SecondarySkill> secondarySkills);
	List<UnitValue> secondarySkillsToUnitValues(List<SecondarySkill> secondarySkills);

	@Mapping(source = "id", target = "secondarySkillId")
	@Mapping(source = "value", target = "name")
	SecondarySkill unitValueToSecondarySkill(UnitValue destination);

}
