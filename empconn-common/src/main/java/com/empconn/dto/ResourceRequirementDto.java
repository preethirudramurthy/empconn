package com.empconn.dto;

import java.util.List;

import javax.validation.Valid;

public class ResourceRequirementDto {

	private String projectId;

	@Valid
	private List<ResourceItemDto> resourceRequirementList;

	public ResourceRequirementDto(String projectId, List<ResourceItemDto> resourceRequirementList) {
		super();
		this.projectId = projectId;
		this.resourceRequirementList = resourceRequirementList;
	}

	public ResourceRequirementDto() {
		super();
	}

	public String getProjectId() {
		return projectId;
	}

	public void setProjectId(String projectId) {
		this.projectId = projectId;
	}

	public List<ResourceItemDto> getResourceRequirementList() {
		return resourceRequirementList;
	}

	public void setResourceRequirementList(List<ResourceItemDto> resourceRequirementList) {
		this.resourceRequirementList = resourceRequirementList;
	}

	@Override
	public String toString() {
		return "ResourceRequirementDto [projectId=" + projectId + ", resourceRequirementList=" + resourceRequirementList
				+ "]";
	}

}
