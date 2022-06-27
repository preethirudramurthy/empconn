package com.empconn.mapper;

import java.util.List;
import java.util.Set;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import com.empconn.persistence.entities.PrimarySkill;
import com.empconn.vo.UnitValue;

@Mapper(componentModel = "spring")
public interface PrimarySkillUnitValueMapper {

	@Mapping(source = "primarySkillId", target = "id")
	@Mapping(source = "name", target = "value")
	UnitValue primarySkillToUnitValue(PrimarySkill source);

	Set<UnitValue> primarySkillsToUnitValues(Set<PrimarySkill> primarySkills);
	List<UnitValue> primarySkillsToUnitValues(List<PrimarySkill> primarySkills);

	@Mapping(source = "id", target = "primarySkillId")
	@Mapping(source = "value", target = "name")
	PrimarySkill unitValueToPrimarySkill(UnitValue destination);

}
