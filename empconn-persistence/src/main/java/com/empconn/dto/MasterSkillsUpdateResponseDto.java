package com.empconn.dto;

import java.util.List;

import com.empconn.vo.UnitValue;

public class MasterSkillsUpdateResponseDto {
	private UnitValue primarySkill;
	private List<UnitValue> secondarySkills;

	public UnitValue getPrimarySkill() {
		return primarySkill;
	}

	public List<UnitValue> getSecondarySkills() {
		return secondarySkills;
	}

	public void setPrimarySkill(UnitValue primarySkill) {
		this.primarySkill = primarySkill;
	}

	public void setSecondarySkills(List<UnitValue> secondarySkills) {
		this.secondarySkills = secondarySkills;
	}

}
