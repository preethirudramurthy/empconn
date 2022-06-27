package com.empconn.mapper;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Named;
import org.springframework.beans.factory.annotation.Autowired;

import com.empconn.constants.ApplicationConstants;
import com.empconn.dto.MasterSkillsResponseDto;
import com.empconn.dto.MasterSkillsUpdateResponseDto;
import com.empconn.persistence.entities.PrimarySkill;
import com.empconn.persistence.entities.SecondarySkill;
import com.empconn.security.SecurityUtil;
import com.empconn.vo.UnitValue;

@Mapper(componentModel = "spring")
public abstract class SkillsMapper {

	@Autowired
	SecurityUtil jwtEmployeeUtil;

	@Mapping(target = "isActive", constant = "true")
	@Mapping(target = "createdBy", expression = "java(jwtEmployeeUtil.getLoggedInEmployee().getEmployeeId())")
	public abstract PrimarySkill primarySkillNameToPrimarySkill(String name);

	@Mapping(target = "name", source = "secondarySkillName")
	@Mapping(target = "primarySkill", source = "primarySkill")
	@Mapping(target = "isActive", constant = "true")
	@Mapping(target = "createdBy", expression = "java(jwtEmployeeUtil.getLoggedInEmployee().getEmployeeId())")
	@Mapping(target = "modifiedBy", ignore = true)
	@Mapping(target = "modifiedOn", ignore = true)
	public abstract SecondarySkill primarySkillAndNameToSecondarySkillMapper(PrimarySkill primarySkill,
			String secondarySkillName);

	@Mapping(target = "secondarySkills", source = "secondarySkills", qualifiedByName = "secondarySkillsToUnitValues")
	@Mapping(target = "primarySkill", source = "primarySkill", qualifiedByName = "primarySkillToUnitValue")
	public abstract MasterSkillsUpdateResponseDto primarySkillAndSecondarySkillsToMasterSkillsUpdateResponseDto(
			PrimarySkill primarySkill, List<SecondarySkill> secondarySkills);

	@Mapping(target = "value", source = "name")
	@Mapping(target = "id", source = "primarySkillId")
	@Named("primarySkillToUnitValue")
	public abstract UnitValue primarySkillToUnitValue(PrimarySkill primarySkill);

	@Mapping(target = "value", source = "name")
	@Mapping(target = "id", source = "secondarySkillId")
	public abstract UnitValue secondarySkillToUnitValue(SecondarySkill secondarySkill);

	@Named("secondarySkillsToUnitValues")
	public abstract List<UnitValue> secondarySkillsToUnitValues(List<SecondarySkill> secondarySkills);

	@Mapping(target = "secondarySkills", source = "secondarySkills", qualifiedByName = "secondarySkillsToSecondarySkillList")
	@Mapping(target = "primarySkill", source = "name")
	public abstract MasterSkillsResponseDto primarySkillToMasterSkillResponseDto(PrimarySkill source);

	public abstract List<MasterSkillsResponseDto> primarySkillsToMasterSkillResponseDtos(List<PrimarySkill> source);

	@Named("secondarySkillsToSecondarySkillList")
	public List<String> secondarySkillsToSecondarySkillList(Set<SecondarySkill> secondarySkills) {
		return secondarySkills.stream()
				.filter(s -> s.getIsActive() && !s.getName().equals(ApplicationConstants.DEFAULT_SECONDARY_SKILL))
				.map(SecondarySkill::getName).collect(Collectors.toList());
	}
}
