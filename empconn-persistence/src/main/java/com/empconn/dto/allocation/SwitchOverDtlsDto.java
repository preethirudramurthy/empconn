package com.empconn.dto.allocation;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.datatype.jsr310.deser.LocalDateDeserializer;
import com.fasterxml.jackson.datatype.jsr310.deser.LocalDateTimeDeserializer;
import com.fasterxml.jackson.datatype.jsr310.ser.LocalDateSerializer;
import com.fasterxml.jackson.datatype.jsr310.ser.LocalDateTimeSerializer;

public class SwitchOverDtlsDto {

	@Override
	public String toString() {
		return "SwitchOverDtlsForAllocationDto [allocationId=" + allocationId + ", resourceId=" + resourceId
				+ ", empCode=" + empCode + ", empName=" + empName + ", title=" + title + ", availablePercentage="
				+ availablePercentage + ", projectEndDate=" + projectEndDate + "]";
	}
	private Long allocationId;
	private Long resourceId;
	private String empCode;
	private String empName;
	private String title;
	private Integer availablePercentage;

	@JsonFormat(pattern = "yyyy-MM-dd'T'HH:mm:ss.SSS'Z'")
	@JsonDeserialize(using = LocalDateTimeDeserializer.class)
	@JsonSerialize(using = LocalDateTimeSerializer.class)
	private LocalDateTime projectEndDate;

	private List<String> projectSalesforceIdList;

	private ExistingAllocationDto  existingAllocationInThisProject;
	@JsonDeserialize(using = LocalDateDeserializer.class)
	@JsonSerialize(using = LocalDateSerializer.class)
	@JsonFormat(pattern = "yyyy-MM-dd")
	private LocalDate movementDate;

	private List<Map<String, String>> managerList;
	public List<Map<String, String>> getManagerList() {
		return managerList;
	}
	public void setManagerList(List<Map<String, String>> managerList) {
		this.managerList = managerList;
	}

	private List<AllocationSummaryDto> allocationSummary;

	public List<AllocationSummaryDto> getAllocationSummary() {
		return allocationSummary;
	}
	public void setAllocationSummary(List<AllocationSummaryDto> allocationSummary) {
		this.allocationSummary = allocationSummary;
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
	public Integer getAvailablePercentage() {
		return availablePercentage;
	}
	public void setAvailablePercentage(Integer availablePercentage) {
		this.availablePercentage = availablePercentage;
	}

	public Long getAllocationId() {
		return allocationId;
	}
	public void setAllocationId(Long allocationId) {
		this.allocationId = allocationId;
	}
	public void setProjectEndDate(LocalDateTime projectEndDate) {
		this.projectEndDate = projectEndDate;
	}
	public List<String> getProjectSalesforceIdList() {
		return projectSalesforceIdList;
	}
	public void setProjectSalesforceIdList(List<String> projectSalesforceIdList) {
		this.projectSalesforceIdList = projectSalesforceIdList;
	}
	public LocalDate getMovementDate() {
		return movementDate;
	}
	public void setMovementDate(LocalDate movementDate) {
		this.movementDate = movementDate;
	}
	public LocalDateTime getProjectEndDate() {
		return projectEndDate;
	}
	public ExistingAllocationDto getExistingAllocationInThisProject() {
		return existingAllocationInThisProject;
	}
	public void setExistingAllocationInThisProject(ExistingAllocationDto existingAllocationInThisProject) {
		this.existingAllocationInThisProject = existingAllocationInThisProject;
	}


}
