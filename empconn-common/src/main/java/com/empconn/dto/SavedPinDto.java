package com.empconn.dto;

public class SavedPinDto {

	private String ProjectId;

	public SavedPinDto(Long projectId) {
		super();
		ProjectId = Long.toString(projectId);
	}

	public String getProjectId() {
		return ProjectId;
	}

	public void setProjectId(String projectId) {
		ProjectId = projectId;
	}

}
