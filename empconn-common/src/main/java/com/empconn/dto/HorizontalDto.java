package com.empconn.dto;

import javax.validation.constraints.NotEmpty;

public class HorizontalDto {

	@NotEmpty
	private String horizontalName;

	public String getHorizontalName() {
		return horizontalName;
	}

	public void setHorizontalName(String horizontalName) {
		this.horizontalName = horizontalName;
	}

}
