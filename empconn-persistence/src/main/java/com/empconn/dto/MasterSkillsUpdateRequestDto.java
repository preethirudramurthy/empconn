package com.empconn.dto;

import java.util.List;

import javax.validation.constraints.NotEmpty;

public class MasterSkillsUpdateRequestDto {

	@NotEmpty
	private String primarySkill;
	private List<@NotEmpty String> secondarySkills;

	public MasterSkillsUpdateRequestDto() {
		super();
	}

	public MasterSkillsUpdateRequestDto(String primarySkill, List<String> secondarySkills) {
		super();
		this.primarySkill = primarySkill;
		this.secondarySkills = secondarySkills;
	}

	public String getPrimarySkill() {
		return primarySkill;
	}

	public List<String> getSecondarySkills() {
		return secondarySkills;
	}

	public void setPrimarySkill(String primarySkill) {
		this.primarySkill = primarySkill;
	}

	public void setSecondarySkills(List<String> secondarySkills) {
		this.secondarySkills = secondarySkills;
	}

}
