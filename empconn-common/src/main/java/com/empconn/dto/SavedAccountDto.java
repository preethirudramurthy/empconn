package com.empconn.dto;

public class SavedAccountDto {

	private String accountId;
	private String projectId;

	public SavedAccountDto(String accountId, String projectId) {
		super();
		this.accountId = accountId;
		this.projectId = projectId;
	}

	public SavedAccountDto() {
		super();
	}

	public String getAccountId() {
		return accountId;
	}

	public void setAccountId(String accountId) {
		this.accountId = accountId;
	}

	public String getProjectId() {
		return projectId;
	}

	public void setProjectId(String projectId) {
		this.projectId = projectId;
	}

}
