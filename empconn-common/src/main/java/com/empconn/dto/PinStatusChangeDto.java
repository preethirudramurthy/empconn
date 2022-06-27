package com.empconn.dto;

import java.util.List;

public class PinStatusChangeDto {
	private Long projectId;
	private String status;
	private List<String> gdmAssignResources;
	private List<String> managerAssignResources;

	public Long getProjectId() {
		return projectId;
	}

	public void setProjectId(Long projectId) {
		this.projectId = projectId;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public List<String> getGdmAssignResources() {
		return gdmAssignResources;
	}

	public void setGdmAssignResources(List<String> gdmAssignResources) {
		this.gdmAssignResources = gdmAssignResources;
	}

	public List<String> getManagerAssignResources() {
		return managerAssignResources;
	}

	public void setManagerAssignResources(List<String> managerAssignResources) {
		this.managerAssignResources = managerAssignResources;
	}

}
