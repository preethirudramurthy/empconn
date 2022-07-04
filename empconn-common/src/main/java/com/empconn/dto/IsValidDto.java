package com.empconn.dto;

import com.fasterxml.jackson.annotation.JsonProperty;

public class IsValidDto {
	
	@JsonProperty
	private boolean isValid;

	public IsValidDto(boolean isValid) {
		super();
		this.isValid = isValid;
	}

	public IsValidDto() {
		super();
	}

	public boolean getIsValid() {
		return isValid;
	}

	public void setIsValid(boolean isValid) {
		this.isValid = isValid;
	}

	
	
	

}
