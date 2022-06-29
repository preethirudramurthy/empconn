package com.empconn.dto;

import com.fasterxml.jackson.annotation.JsonProperty;

public class IsValidDto {
	
	@JsonProperty
	private Boolean isValid;

	public IsValidDto(Boolean isValid) {
		super();
		this.isValid = isValid;
	}

	public IsValidDto() {
		super();
	}

	public Boolean getIsValid() {
		return isValid;
	}

	public void setIsValid(Boolean isValid) {
		this.isValid = isValid;
	}

	
	
	

}
