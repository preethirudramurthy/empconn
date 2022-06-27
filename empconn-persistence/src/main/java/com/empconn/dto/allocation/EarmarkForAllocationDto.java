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

public class EarmarkForAllocationDto {

	private String earmarkId;
	private String allocationId;
	private String resourceId;
	private String empCode;
	private String empName;
	private String title;
	private String projectOppName;
	private boolean isOpp;
	private String earmarkPercentage;
	private String availablePercentage;
	private boolean billable;
	@JsonDeserialize(using = LocalDateDeserializer.class)
	@JsonSerialize(using = LocalDateSerializer.class)
	@JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd")
	private LocalDate projectEndDate;

	@JsonFormat(pattern = "yyyy-MM-dd'T'HH:mm:ss.SSS'Z'")
	@JsonDeserialize(using = LocalDateTimeDeserializer.class)
	@JsonSerialize(using = LocalDateTimeSerializer.class)
	private LocalDateTime earmarkReleaseDate;

	private List<String> projectSalesforceIdList;
	private List<String> earmarkSalesforceIdList;
	@JsonDeserialize(using = LocalDateDeserializer.class)
	@JsonSerialize(using = LocalDateSerializer.class)
	@JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd")
	private LocalDate movementDate;
	private List<AllocationSummaryDto> allocationSummary;
	private List<Map<String, String>> managerList;
	private ExistingAllocationDto existingAllocationInThisProject;

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

	public String getEarmarkId() {
		return earmarkId;
	}

	public void setEarmarkId(String earmarkId) {
		this.earmarkId = earmarkId;
	}

	public String getAllocationId() {
		return allocationId;
	}

	public void setAllocationId(String allocationId) {
		this.allocationId = allocationId;
	}

	public String getResourceId() {
		return resourceId;
	}

	public void setResourceId(String resourceId) {
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

	public String getProjectOppName() {
		return projectOppName;
	}

	public void setProjectOppName(String projectOppName) {
		this.projectOppName = projectOppName;
	}

	public boolean getIsOpp() {
		return isOpp;
	}

	public void setIsOpp(boolean isOpp) {
		this.isOpp = isOpp;
	}

	public String getEarmarkPercentage() {
		return earmarkPercentage;
	}

	public void setEarmarkPercentage(String earmarkPercentage) {
		this.earmarkPercentage = earmarkPercentage;
	}

	public String getAvailablePercentage() {
		return availablePercentage;
	}

	public void setAvailablePercentage(String availablePercentage) {
		this.availablePercentage = availablePercentage;
	}

	public boolean isBillable() {
		return billable;
	}

	public void setBillable(boolean billable) {
		this.billable = billable;
	}

	public LocalDate getProjectEndDate() {
		return projectEndDate;
	}

	public void setProjectEndDate(LocalDate projectEndDate) {
		this.projectEndDate = projectEndDate;
	}

	public LocalDateTime getEarmarkReleaseDate() {
		return earmarkReleaseDate;
	}

	public void setEarmarkReleaseDate(LocalDateTime earmarkReleaseDate) {
		this.earmarkReleaseDate = earmarkReleaseDate;
	}

	public List<String> getProjectSalesforceIdList() {
		return projectSalesforceIdList;
	}

	public void setProjectSalesforceIdList(List<String> projectSalesforceIdList) {
		this.projectSalesforceIdList = projectSalesforceIdList;
	}

	public List<String> getEarmarkSalesforceIdList() {
		return earmarkSalesforceIdList;
	}

	public void setEarmarkSalesforceIdList(List<String> earmarkSalesforceIdList) {
		this.earmarkSalesforceIdList = earmarkSalesforceIdList;
	}

	public LocalDate getMovementDate() {
		return movementDate;
	}

	public void setMovementDate(LocalDate movementDate) {
		this.movementDate = movementDate;
	}

	public ExistingAllocationDto getExistingAllocationInThisProject() {
		return existingAllocationInThisProject;
	}

	public void setExistingAllocationInThisProject(ExistingAllocationDto existingAllocationInThisProject) {
		this.existingAllocationInThisProject = existingAllocationInThisProject;
	}
}