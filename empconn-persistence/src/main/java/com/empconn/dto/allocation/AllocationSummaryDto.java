package com.empconn.dto.allocation;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.empconn.persistence.entities.Employee;

public class AllocationSummaryDto {

	private String accountName;
	private String projectName;

	@JsonIgnore
	private Long projectId;

	@JsonIgnore
	private Employee reportingManager;

	public AllocationSummaryDto() {

	}

	public AllocationSummaryDto(String accountName, Long projectId, String projectName, Long allocationPercentage,
			Employee reportingManager) {
		super();
		this.accountName = accountName;
		this.projectId = projectId;
		this.projectName = projectName;
		this.allocationPercentage = allocationPercentage;
		this.reportingManager = reportingManager;
	}

	private Long allocationPercentage;
	private String reportingMangerName;
	private Boolean isPrimary;

	public String getAccountName() {
		return accountName;
	}

	public void setAccountName(String accountName) {
		this.accountName = accountName;
	}

	public String getProjectName() {
		return projectName;
	}

	public void setProjectName(String projectName) {
		this.projectName = projectName;
	}

	public Long getAllocationPercentage() {
		return allocationPercentage;
	}

	public void setAllocationPercentage(Long allocationPercentage) {
		this.allocationPercentage = allocationPercentage;
	}

	public String getReportingMangerName() {
		return reportingMangerName;
	}

	public void setReportingMangerName(String reportingMangerName) {
		this.reportingMangerName = reportingMangerName;
	}

	public Employee getReportingManager() {
		return reportingManager;
	}

	public void setReportingManager(Employee reportingManager) {
		this.reportingManager = reportingManager;
	}

	public Boolean getIsPrimary() {
		return isPrimary;
	}

	public void setIsPrimary(Boolean isPrimary) {
		this.isPrimary = isPrimary;
	}

	public Long getProjectId() {
		return projectId;
	}

	public void setProjectId(Long projectId) {
		this.projectId = projectId;
	}
}
