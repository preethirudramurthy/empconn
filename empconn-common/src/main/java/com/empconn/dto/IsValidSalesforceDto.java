package com.empconn.dto;

import java.util.List;

import javax.validation.constraints.NotEmpty;

public class IsValidSalesforceDto {

	private String projectId;
	@NotEmpty
	private List<@NotEmpty String> salesforceIdList;

	public IsValidSalesforceDto(String projectId, List<String> salesforceIdList) {
		super();
		this.projectId = projectId;
		this.salesforceIdList = salesforceIdList;
	}

	public IsValidSalesforceDto() {
		super();
	}

	public String getProjectId() {
		return projectId;
	}

	public void setProjectId(String projectId) {
		this.projectId = projectId;
	}

	public List<String> getSalesforceIdList() {
		return salesforceIdList;
	}

	public void setSalesforceIdList(List<String> salesforceIdList) {
		this.salesforceIdList = salesforceIdList;
	}

}
