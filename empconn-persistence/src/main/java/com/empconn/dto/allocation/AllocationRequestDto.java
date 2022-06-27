package com.empconn.dto.allocation;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.datatype.jsr310.deser.LocalDateDeserializer;
import com.fasterxml.jackson.datatype.jsr310.deser.LocalDateTimeDeserializer;
import com.fasterxml.jackson.datatype.jsr310.ser.LocalDateSerializer;
import com.fasterxml.jackson.datatype.jsr310.ser.LocalDateTimeSerializer;

public class AllocationRequestDto {

	public AllocationRequestDto() {

	}

	public AllocationRequestDto(SwitchOverRequestDto switchOverRequestDto) {
		super();
		this.allocationId = switchOverRequestDto.getAllocationId();
		this.resourceId = switchOverRequestDto.getResourceId();
		this.projectLocationId = switchOverRequestDto.getProjectLocationId();
		this.workgroup = switchOverRequestDto.getWorkgroup();
		this.reportingManagerId = switchOverRequestDto.getReportingManagerId();
		this.extraSalesforceIdList = switchOverRequestDto.getExtraSalesforceIdList();
		this.percentage = switchOverRequestDto.getPercentage();
		this.startDate = switchOverRequestDto.getStartDate();
		this.releaseDate = switchOverRequestDto.getReleaseDate();
		this.billable = switchOverRequestDto.getBillable();
		this.isPrimaryManager = switchOverRequestDto.getIsPrimaryManager();
		this.projectId = switchOverRequestDto.getProjectId();
	}

	public AllocationRequestDto(EarmarkAllocationRequestDto earmarkAllocationRequestDto) {
		super();
		this.allocationId = earmarkAllocationRequestDto.getAllocationId();
		this.resourceId = Long.valueOf(earmarkAllocationRequestDto.getResourceId());
		this.projectLocationId = earmarkAllocationRequestDto.getProjectLocationId();
		this.workgroup = earmarkAllocationRequestDto.getWorkgroup();
		this.reportingManagerId = earmarkAllocationRequestDto.getReportingManagerId();
		this.extraSalesforceIdList = earmarkAllocationRequestDto.getEarmarkSalesforceIdList();
		this.percentage = earmarkAllocationRequestDto.getPercentage();
		this.startDate = earmarkAllocationRequestDto.getStartDate();
		this.releaseDate = earmarkAllocationRequestDto.getReleaseDate();
		this.billable = earmarkAllocationRequestDto.getBillable();
		this.isPrimaryManager = earmarkAllocationRequestDto.getIsPrimaryManager();
		this.projectId = earmarkAllocationRequestDto.getProjectId();
	}

	public AllocationRequestDto(NDBenchAllocationRequestDto ndBenchAllocationRequestDto) {
		super();
		this.allocationId = ndBenchAllocationRequestDto.getAllocationId();
		this.resourceId = ndBenchAllocationRequestDto.getResourceId();
		this.projectLocationId = ndBenchAllocationRequestDto.getProjectLocationId();
		this.workgroup = ndBenchAllocationRequestDto.getWorkgroup();
		this.reportingManagerId = ndBenchAllocationRequestDto.getReportingManagerId();
		this.extraSalesforceIdList = ndBenchAllocationRequestDto.getExtraSalesforceIdList();
		this.percentage = ndBenchAllocationRequestDto.getPercentage();
		this.startDate = ndBenchAllocationRequestDto.getStartDate();
		this.releaseDate = ndBenchAllocationRequestDto.getReleaseDate();
		this.billable = ndBenchAllocationRequestDto.getBillable();
		this.isPrimaryManager = ndBenchAllocationRequestDto.getIsPrimaryManager();
		this.projectId = ndBenchAllocationRequestDto.getProjectId();
	}

	private Long allocationId;
	private Long resourceId;
	private String projectLocationId;
	private String workgroup;
	private String reportingManagerId;
	private List<String> extraSalesforceIdList;
	private Integer percentage;

	@JsonDeserialize(using = LocalDateDeserializer.class)
	@JsonSerialize(using = LocalDateSerializer.class)
	@JsonFormat(pattern = "yyyy-MM-dd")
	private LocalDate startDate;

	@JsonFormat(pattern = "yyyy-MM-dd'T'HH:mm:ss.SSS'Z'")
	@JsonDeserialize(using = LocalDateTimeDeserializer.class)
	@JsonSerialize(using = LocalDateTimeSerializer.class)
	private LocalDateTime releaseDate;

	private Boolean billable;

	public Boolean getBillable() {
		return billable;
	}

	public void setBillable(Boolean billable) {
		this.billable = billable;
	}

	public Boolean getIsPrimaryManager() {
		return isPrimaryManager;
	}

	public void setIsPrimaryManager(Boolean isPrimaryManager) {
		this.isPrimaryManager = isPrimaryManager;
	}

	private Boolean isPrimaryManager;
	private String projectId;

	public Long getAllocationId() {
		return allocationId;
	}

	public void setAllocationId(Long allocationId) {
		this.allocationId = allocationId;
	}

	public String getProjectLocationId() {
		return projectLocationId;
	}

	public void setProjectLocationId(String projectLocationId) {
		this.projectLocationId = projectLocationId;
	}

	public String getWorkgroup() {
		return workgroup;
	}

	public void setWorkgroup(String workgroup) {
		this.workgroup = workgroup;
	}

	public String getReportingManagerId() {
		return reportingManagerId;
	}

	public void setReportingManagerId(String reportingManagerId) {
		this.reportingManagerId = reportingManagerId;
	}

	public List<String> getExtraSalesforceIdList() {
		return extraSalesforceIdList;
	}

	public void setExtraSalesforceIdList(List<String> extraSalesforceIdList) {
		this.extraSalesforceIdList = extraSalesforceIdList;
	}

	public Integer getPercentage() {
		return percentage;
	}

	public void setPercentage(Integer percentage) {
		this.percentage = percentage;
	}

	public LocalDate getStartDate() {
		return startDate;
	}

	public void setStartDate(LocalDate startDate) {
		this.startDate = startDate;
	}

	public LocalDateTime getReleaseDate() {
		return releaseDate;
	}

	public void setReleaseDate(LocalDateTime releaseDate) {
		this.releaseDate = releaseDate;
	}

	public String getProjectId() {
		return projectId;
	}

	public void setProjectId(String projectId) {
		this.projectId = projectId;
	}

	public Long getResourceId() {
		return resourceId;
	}

	public void setResourceId(Long resourceId) {
		this.resourceId = resourceId;
	}

}
