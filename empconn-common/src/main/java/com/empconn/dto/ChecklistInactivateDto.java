package com.empconn.dto;

import javax.validation.constraints.NotEmpty;

public class ChecklistInactivateDto {

	@NotEmpty
	private String checklist;

	public String getChecklist() {
		return checklist;
	}

	public void setChecklist(String checklist) {
		this.checklist = checklist;
	}

}
