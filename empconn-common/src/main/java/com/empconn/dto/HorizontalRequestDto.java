package com.empconn.dto;

import javax.validation.constraints.NotEmpty;

public class HorizontalRequestDto {

	private String oldHorizontal;

	@NotEmpty
	private String newHorizontal;

	public String getOldHorizontal() {
		return oldHorizontal;
	}

	public void setOldHorizontal(String oldHorizontal) {
		this.oldHorizontal = oldHorizontal;
	}

	public String getNewHorizontal() {
		return newHorizontal;
	}

	public void setNewHorizontal(String newHorizontal) {
		this.newHorizontal = newHorizontal;
	}

}
