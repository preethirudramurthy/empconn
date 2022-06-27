package com.empconn.dto;

import java.util.List;

import javax.validation.constraints.NotEmpty;

public class UpdateUserRoleDto {

	@NotEmpty
	private String resourceId;

	@NotEmpty
	private List<@NotEmpty String> roleList;

	public String getResourceId() {
		return resourceId;
	}

	public void setResourceId(String resourceId) {
		this.resourceId = resourceId;
	}

	public List<String> getRoleList() {
		return roleList;
	}

	public void setRoleList(List<String> roleList) {
		this.roleList = roleList;
	}

}
