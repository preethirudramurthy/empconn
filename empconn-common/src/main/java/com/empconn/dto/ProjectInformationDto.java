package com.empconn.dto;

import java.util.Set;

public class ProjectInformationDto {
	private String projectName;
	private String vertical;
	private String subCategory;
	private String customerName;
	private String businessManagerName;
	private String projectManagerName;
	private String scope;
	private String initialMeetingDate;
	private String startDate;
	private String endDate;
	private Set<ResourceInformationDto> resourceInformation;
	private String initiatedBy;
	private String initiatedDate;
	private String approvedBy;

	private String approvedDate;
	private Set<CheckListInformationDto> checklist;
	public ProjectInformationDto() {
		super();
	}
	public ProjectInformationDto(String projectName, String vertical, String subCategory, String customerName,
			String businessManagerName, String projectManagerName, String scope, String initialMeetingDate,
			String startDate, String endDate, Set<ResourceInformationDto> resourceInformation, String initiatedBy,
			String initiatedDate, String approvedBy, String approvedDate, Set<CheckListInformationDto> checklist) {
		super();
		this.projectName = projectName;
		this.vertical = vertical;
		this.subCategory = subCategory;
		this.customerName = customerName;
		this.businessManagerName = businessManagerName;
		this.projectManagerName = projectManagerName;
		this.scope = scope;
		this.initialMeetingDate = initialMeetingDate;
		this.startDate = startDate;
		this.endDate = endDate;
		this.resourceInformation = resourceInformation;
		this.initiatedBy = initiatedBy;
		this.initiatedDate = initiatedDate;
		this.approvedBy = approvedBy;
		this.approvedDate = approvedDate;
		this.checklist = checklist;
	}
	public String getProjectName() {
		return projectName;
	}
	public void setProjectName(String projectName) {
		this.projectName = projectName;
	}
	public String getVertical() {
		return vertical;
	}
	public void setVertical(String vertical) {
		this.vertical = vertical;
	}
	public String getSubCategory() {
		return subCategory;
	}
	public void setSubCategory(String subCategory) {
		this.subCategory = subCategory;
	}
	public String getCustomerName() {
		return customerName;
	}
	public void setCustomerName(String customerName) {
		this.customerName = customerName;
	}
	public String getBusinessManagerName() {
		return businessManagerName;
	}
	public void setBusinessManagerName(String businessManagerName) {
		this.businessManagerName = businessManagerName;
	}
	public String getProjectManagerName() {
		return projectManagerName;
	}
	public void setProjectManagerName(String projectManagerName) {
		this.projectManagerName = projectManagerName;
	}
	public String getScope() {
		return scope;
	}
	public void setScope(String scope) {
		this.scope = scope;
	}
	public String getInitialMeetingDate() {
		return initialMeetingDate;
	}
	public void setInitialMeetingDate(String initialMeetingDate) {
		this.initialMeetingDate = initialMeetingDate;
	}
	public String getStartDate() {
		return startDate;
	}
	public void setStartDate(String startDate) {
		this.startDate = startDate;
	}
	public String getEndDate() {
		return endDate;
	}
	public void setEndDate(String endDate) {
		this.endDate = endDate;
	}
	public Set<ResourceInformationDto> getResourceInformation() {
		return resourceInformation;
	}
	public void setResourceInformation(Set<ResourceInformationDto> resourceInformation) {
		this.resourceInformation = resourceInformation;
	}
	public String getInitiatedBy() {
		return initiatedBy;
	}
	public void setInitiatedBy(String initiatedBy) {
		this.initiatedBy = initiatedBy;
	}
	public String getInitiatedDate() {
		return initiatedDate;
	}
	public void setInitiatedDate(String initiatedDate) {
		this.initiatedDate = initiatedDate;
	}
	public String getApprovedBy() {
		return approvedBy;
	}
	public void setApprovedBy(String approvedBy) {
		this.approvedBy = approvedBy;
	}
	public String getApprovedDate() {
		return approvedDate;
	}
	public void setApprovedDate(String approvedDate) {
		this.approvedDate = approvedDate;
	}
	public Set<CheckListInformationDto> getChecklist() {
		return checklist;
	}
	public void setChecklist(Set<CheckListInformationDto> checklist) {
		this.checklist = checklist;
	}
}
