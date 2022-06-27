package com.empconn.dto;

import com.fasterxml.jackson.annotation.JsonProperty;

public class IsValidStatusDto {
	
	@JsonProperty
	private Boolean isValid;
	
	private String status;
	
	public IsValidStatusDto(Boolean isValid, String status) {
		super();
		this.isValid = isValid;
		this.status = status;
	}

	public IsValidStatusDto() {
		super();
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public Boolean getIsValid() {
		return isValid;
	}

	public void setIsValid(Boolean isValid) {
		this.isValid = isValid;
	}


}
