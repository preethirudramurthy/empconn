package com.empconn.dto;

import javax.validation.constraints.NotEmpty;

public class VerticalDto {

	@NotEmpty
	private String verticalName;

	public String getVerticalName() {
		return verticalName;
	}

	public void setVerticalName(String verticalName) {
		this.verticalName = verticalName;
	}

}
