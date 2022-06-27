package com.empconn.dto;

import java.util.List;

import com.empconn.vo.UnitValue;

public class UsersRoleValidityResponseDto {

	private Boolean isValid;
	private List<UnitValue> resources;

	public Boolean getIsValid() {
		return isValid;
	}

	public void setIsValid(Boolean isValid) {
		this.isValid = isValid;
	}

	public List<UnitValue> getResources() {
		return resources;
	}

	public void setResources(List<UnitValue> resources) {
		this.resources = resources;
	}

}
