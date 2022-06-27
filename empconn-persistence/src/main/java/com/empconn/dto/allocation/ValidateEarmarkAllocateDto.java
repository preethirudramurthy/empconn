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

public class ValidateEarmarkAllocateDto {
	private String earmarkId;
	private String projectId;
	private String resourceId;
	private String projectLocationId;
	private String workgroup;
	private String reportingManagerId;
	private List<String> requestSalesforceIdList;
	private Integer percentage;
	@JsonDeserialize(using = LocalDateDeserializer.class)
	@JsonSerialize(using = LocalDateSerializer.class)
	@JsonFormat(pattern = "yyyy-MM-dd")
	private LocalDate startDate;
	@JsonFormat(pattern = "yyyy-MM-dd'T'HH:mm:ss.SSS'Z'")
	@JsonDeserialize(using = LocalDateTimeDeserializer.class)
	@JsonSerialize(using = LocalDateTimeSerializer.class)
	private LocalDateTime releaseDate;
	private boolean billable;
	private boolean isPrimaryManager;
	private int index;
	private List<String> earmarkSalesforceIdList;

	public int getIndex() {
		return index;
	}

	public void setIndex(int index) {
		this.index = index;
	}

	public List<String> getEarmarkSalesforceIdList() {
		return earmarkSalesforceIdList;
	}

	public void setEarmarkSalesforceIdList(List<String> earmarkSalesforceIdList) {
		this.earmarkSalesforceIdList = earmarkSalesforceIdList;
	}

	public String getEarmarkId() {
		return earmarkId;
	}

	public void setEarmarkId(String earmarkId) {
		this.earmarkId = earmarkId;
	}

	public String getProjectId() {
		return projectId;
	}

	public void setProjectId(String projectId) {
		this.projectId = projectId;
	}

	public String getResourceId() {
		return resourceId;
	}

	public void setResourceId(String resourceId) {
		this.resourceId = resourceId;
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

	public List<String> getRequestSalesforceIdList() {
		return requestSalesforceIdList;
	}

	public void setRequestSalesforceIdList(List<String> requestSalesforceIdList) {
		this.requestSalesforceIdList = requestSalesforceIdList;
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

	public boolean isBillable() {
		return billable;
	}

	public void setBillable(boolean billable) {
		this.billable = billable;
	}

	public boolean getIsPrimaryManager() {
		return isPrimaryManager;
	}

	public void setIsPrimaryManager(boolean isPrimaryManager) {
		this.isPrimaryManager = isPrimaryManager;
	}

}