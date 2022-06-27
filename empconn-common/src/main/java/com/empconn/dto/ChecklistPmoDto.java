package com.empconn.dto;

import java.util.List;

import javax.validation.Valid;
import javax.validation.constraints.NotEmpty;

public class ChecklistPmoDto {

	@NotEmpty(message = "projectId required")
	private String projectId;
	@Valid
	private List<CheckItemDto> checkItemList;
	private PreApprovalCheckDto preApprovalCheck;

	public ChecklistPmoDto() {
		super();
	}

	public ChecklistPmoDto(String projectId, List<CheckItemDto> checkItemList, PreApprovalCheckDto preApprovalCheck) {
		super();
		this.projectId = projectId;
		this.checkItemList = checkItemList;
		this.preApprovalCheck = preApprovalCheck;
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

	public PreApprovalCheckDto getPreApprovalCheck() {
		return preApprovalCheck;
	}

	public void setPreApprovalCheck(PreApprovalCheckDto preApprovalCheck) {
		this.preApprovalCheck = preApprovalCheck;
	}

}
