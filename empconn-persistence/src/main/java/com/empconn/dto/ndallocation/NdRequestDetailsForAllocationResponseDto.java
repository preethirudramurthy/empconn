package com.empconn.dto.ndallocation;

import java.util.List;
import java.util.Map;

import com.empconn.dto.allocation.AllocationSummaryDto;
import com.empconn.dto.allocation.ExistingAllocationDto;
import com.empconn.vo.UnitValue;

public class NdRequestDetailsForAllocationResponseDto {

	private Long ndRequestId;
	private Long resourceId;
	private String empCode;
	private String empName;
	private String title;
	private Long projectId;
	private String projectName;
	private Integer requestedPercentage;
	private Integer availablePercentage;
	private Boolean billable;

	private String projectEndDate;

	private List<String> projectSalesforceIdList;
	private List<String> ndRequestSalesforceIdList;
	private List<ExistingAllocationDto> existingAllocationsInThisProject;

	private String movementDate;
	private UnitValue reportingManager;
	private List<Map<String, String>> managerList;
	private List<AllocationSummaryDto> allocationSummary;

	public Long getNdRequestId() {
		return ndRequestId;
	}
	public void setNdRequestId(Long ndRequestId) {
		this.ndRequestId = ndRequestId;
	}
	public Long getResourceId() {
		return resourceId;
	}
	public void setResourceId(Long resourceId) {
		this.resourceId = resourceId;
	}
	public String getEmpCode() {
		return empCode;
	}
	public void setEmpCode(String empCode) {
		this.empCode = empCode;
	}
	public String getEmpName() {
		return empName;
	}
	public void setEmpName(String empName) {
		this.empName = empName;
	}
	public String getTitle() {
		return title;
	}
	public void setTitle(String title) {
		this.title = title;
	}
	public Long getProjectId() {
		return projectId;
	}
	public void setProjectId(Long projectId) {
		this.projectId = projectId;
	}
	public String getProjectName() {
		return projectName;
	}
	public void setProjectName(String projectName) {
		this.projectName = projectName;
	}
	public Integer getRequestedPercentage() {
		return requestedPercentage;
	}
	public void setRequestedPercentage(Integer requestedPercentage) {
		this.requestedPercentage = requestedPercentage;
	}
	public Integer getAvailablePercentage() {
		return availablePercentage;
	}
	public void setAvailablePercentage(Integer availablePercentage) {
		this.availablePercentage = availablePercentage;
	}
	public Boolean getBillable() {
		return billable;
	}
	public void setBillable(Boolean billable) {
		this.billable = billable;
	}
	public String getProjectEndDate() {
		return projectEndDate;
	}
	public void setProjectEndDate(String projectEndDate) {
		this.projectEndDate = projectEndDate;
	}
	public List<String> getProjectSalesforceIdList() {
		return projectSalesforceIdList;
	}
	public void setProjectSalesforceIdList(List<String> projectSalesforceIdList) {
		this.projectSalesforceIdList = projectSalesforceIdList;
	}
	public List<String> getNdRequestSalesforceIdList() {
		return ndRequestSalesforceIdList;
	}
	public void setNdRequestSalesforceIdList(List<String> ndRequestSalesforceIdList) {
		this.ndRequestSalesforceIdList = ndRequestSalesforceIdList;
	}
	public List<ExistingAllocationDto> getExistingAllocationsInThisProject() {
		return existingAllocationsInThisProject;
	}
	public void setExistingAllocationsInThisProject(List<ExistingAllocationDto> existingAllocationsInThisProject) {
		this.existingAllocationsInThisProject = existingAllocationsInThisProject;
	}
	public String getMovementDate() {
		return movementDate;
	}
	public void setMovementDate(String movementDate) {
		this.movementDate = movementDate;
	}
	public UnitValue getReportingManager() {
		return reportingManager;
	}
	public void setReportingManager(UnitValue reportingManager) {
		this.reportingManager = reportingManager;
	}
	public List<Map<String, String>> getManagerList() {
		return managerList;
	}
	public void setManagerList(List<Map<String, String>> managerList) {
		this.managerList = managerList;
	}
	public List<AllocationSummaryDto> getAllocationSummary() {
		return allocationSummary;
	}
	public void setAllocationSummary(List<AllocationSummaryDto> allocationSummary) {
		this.allocationSummary = allocationSummary;
	}

}
