package com.empconn.dto;

import java.util.List;

import javax.validation.Valid;
import javax.validation.constraints.NotEmpty;

public class ChecklistDto {
	
	@NotEmpty(message = "projectId required")
	private String projectId;
	
	@Valid
	private List<CheckItemDto> checkItemList;
	
	public ChecklistDto() {
		super();
	}
	public ChecklistDto(String projectId, List<CheckItemDto> checkItemList) {
		super();
		this.projectId = projectId;
		this.checkItemList = checkItemList;
	}
	public String getProjectId() {
		return projectId;
	}
	public void setProjectId(String projectId) {
		this.projectId = projectId;
	}
	public List<CheckItemDto> getCheckItemList() {
		return checkItemList;
	}
	public void setCheckItemList(List<CheckItemDto> checkItemList) {
		this.checkItemList = checkItemList;
	}
	@Override
	public String toString() {
		return "ChecklistDto [projectId=" + projectId + ", checkItemList=" + checkItemList + "]";
	}
	
	
	

}
