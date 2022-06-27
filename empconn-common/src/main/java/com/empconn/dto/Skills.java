package com.empconn.dto;

import java.util.List;

import javax.validation.constraints.NotEmpty;

public class Skills {

	@NotEmpty
	private String primarySkill;

	private List<@NotEmpty String> secondarySkills;

	public String getPrimarySkill() {
		return primarySkill;
	}
	public void setPrimarySkill(String primarySkill) {
		this.primarySkill = primarySkill;
	}
	public List<String> getSecondarySkills() {
		return secondarySkills;
	}
	public void setSecondarySkills(List<String> secondarySkills) {
		this.secondarySkills = secondarySkills;
	}

}
