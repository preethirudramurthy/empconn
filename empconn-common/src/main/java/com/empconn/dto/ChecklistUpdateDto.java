package com.empconn.dto;

import javax.validation.constraints.NotEmpty;

public class ChecklistUpdateDto {

	private String oldChecklist;
	@NotEmpty
	private String newChecklist;

	public String getOldChecklist() {
		return oldChecklist;
	}

	public void setOldChecklist(String oldChecklist) {
		this.oldChecklist = oldChecklist;
	}

	public String getNewChecklist() {
		return newChecklist;
	}

	public void setNewChecklist(String newChecklist) {
		this.newChecklist = newChecklist;
	}

}
