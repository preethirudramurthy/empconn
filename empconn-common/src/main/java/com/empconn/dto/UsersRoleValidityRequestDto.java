package com.empconn.dto;

import java.util.List;

import javax.validation.constraints.NotEmpty;

public class UsersRoleValidityRequestDto {

	@NotEmpty
	private List<@NotEmpty String> resourceIds;

	@NotEmpty
	private String role;

	public List<String> getResourceIds() {
		return resourceIds;
	}

	public void setResourceIds(List<String> resourceIds) {
		this.resourceIds = resourceIds;
	}

	public String getRole() {
		return role;
	}

	public void setRole(String role) {
		this.role = role;
	}

}
