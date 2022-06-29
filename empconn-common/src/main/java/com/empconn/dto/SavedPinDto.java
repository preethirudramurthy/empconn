package com.empconn.dto;

public class SavedPinDto {

	private String projectId;

	public SavedPinDto(Long projectId) {
		super();
		this.projectId = Long.toString(projectId);
	}

	public String getProjectId() {
		return projectId;
	}

	public void setProjectId(String projectId) {
		this.projectId = projectId;
	}

}
