package com.empconn.dto;

import java.util.Set;

public class EmployeeRoleAssignmentDto {

	private String empCode;
	private Set<String> roleNames;

	public EmployeeRoleAssignmentDto() {
		super();
	}

	public EmployeeRoleAssignmentDto(String empCode, Set<String> roleNames) {
		super();
		this.empCode = empCode;
		this.roleNames = roleNames;
	}

	public String getEmpCode() {
		return empCode;
	}

	public void setEmpCode(String empCode) {
		this.empCode = empCode;
	}

	public Set<String> getRoleNames() {
		return roleNames;
	}

	public void setRoleNames(Set<String> roleNames) {
		this.roleNames = roleNames;
	}

}
