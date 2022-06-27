package com.empconn.dto;

import java.util.Date;
import java.util.List;

public class RequestDto {
	private String resourceId;
	private String projectId;
	private Integer percentage;
	private List<String> extraSalesforceIdList;
	private Date startDate;
	private Date releaseDate;
	private Boolean billable;
	private String reportingManagerId;
	private Boolean isPrimaryManager;
	private Boolean isActive;

	public Boolean getIsActive() {
		return isActive;
	}

	public void setIsActive(Boolean isActive) {
		this.isActive = isActive;
	}

	public String getResourceId() {
		return resourceId;
	}

	public void setResourceId(String resourceId) {
		this.resourceId = resourceId;
	}

	public String getProjectId() {
		return projectId;
	}

	public void setProjectId(String projectId) {
		this.projectId = projectId;
	}

	public Integer getPercentage() {
		return percentage;
	}

	public void setPercentage(Integer percentage) {
		this.percentage = percentage;
	}

	public List<String> getExtraSalesforceIdList() {
		return extraSalesforceIdList;
	}

	public void setExtraSalesforceIdList(List<String> extraSalesforceIdList) {
		this.extraSalesforceIdList = extraSalesforceIdList;
	}

	public Date getStartDate() {
		return startDate;
	}

	public void setStartDate(Date startDate) {
		this.startDate = startDate;
	}

	public Date getReleaseDate() {
		return releaseDate;
	}

	public void setReleaseDate(Date releaseDate) {
		this.releaseDate = releaseDate;
	}

	public Boolean getBillable() {
		return billable;
	}

	public void setBillable(Boolean billable) {
		this.billable = billable;
	}

	public String getReportingManagerId() {
		return reportingManagerId;
	}

	public void setReportingManagerId(String reportingManagerId) {
		this.reportingManagerId = reportingManagerId;
	}

	public Boolean getIsPrimaryManager() {
		return isPrimaryManager;
	}

	public void setIsPrimaryManager(Boolean isPrimaryManager) {
		this.isPrimaryManager = isPrimaryManager;
	}

}
