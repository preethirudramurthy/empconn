package com.empconn.dto;

import java.util.List;

public class UserDto {

	private String resourceId;
	private String loginId;
	private String empCode;
	private String fullName;
	private boolean isActive;
	private List<String> roleList;

	public UserDto() {
		super();
	}

	public UserDto(String resourceId, String loginId, String empCode, String fullName, boolean isActive,
			List<String> roleList) {
		super();
		this.resourceId = resourceId;
		this.loginId = loginId;
		this.empCode = empCode;
		this.fullName = fullName;
		this.isActive = isActive;
		this.roleList = roleList;
	}

	public String getResourceId() {
		return resourceId;
	}

	public void setResourceId(String resourceId) {
		this.resourceId = resourceId;
	}

	public String getLoginId() {
		return loginId;
	}

	public void setLoginId(String loginId) {
		this.loginId = loginId;
	}

	public String getEmpCode() {
		return empCode;
	}

	public void setEmpCode(String empCode) {
		this.empCode = empCode;
	}

	public String getFullName() {
		return fullName;
	}

	public void setFullName(String fullName) {
		this.fullName = fullName;
	}

	public boolean getIsActive() {
		return isActive;
	}

	public void setIsActive(boolean isActive) {
		this.isActive = isActive;
	}

	public List<String> getRoleList() {
		return roleList;
	}

	public void setRoleList(List<String> roleList) {
		this.roleList = roleList;
	}

}
