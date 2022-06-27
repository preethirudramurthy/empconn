package com.empconn.dto.ndallocation;

import java.time.LocalDateTime;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.datatype.jsr310.deser.LocalDateTimeDeserializer;
import com.fasterxml.jackson.datatype.jsr310.ser.LocalDateTimeSerializer;

public class NDAllocateRequestDto {
	private Boolean billable;
	private Boolean isPrimaryManager;
	private Integer percentage;
	private Integer projectLocationId;

	@JsonFormat(pattern = "yyyy-MM-dd'T'HH:mm:ss.SSS'Z'")
	@JsonDeserialize(using = LocalDateTimeDeserializer.class)
	@JsonSerialize(using = LocalDateTimeSerializer.class)
	private LocalDateTime releaseDate;

	private Integer reportingManagerId;
	private Long requestId;
	private List<String>  requestSalesforceIdList;
	private Long resourceId;
	private String startDate;
	private String workgroup;

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
	public Integer getPercentage() {
		return percentage;
	}
	public void setPercentage(Integer percentage) {
		this.percentage = percentage;
	}
	public Integer getProjectLocationId() {
		return projectLocationId;
	}
	public void setProjectLocationId(Integer projectLocationId) {
		this.projectLocationId = projectLocationId;
	}
	public LocalDateTime getReleaseDate() {
		return releaseDate;
	}
	public void setReleaseDate(LocalDateTime releaseDate) {
		this.releaseDate = releaseDate;
	}
	public Integer getReportingManagerId() {
		return reportingManagerId;
	}
	public void setReportingManagerId(Integer reportingManagerId) {
		this.reportingManagerId = reportingManagerId;
	}
	public Long getRequestId() {
		return requestId;
	}
	public void setRequestId(Long requestId) {
		this.requestId = requestId;
	}
	public List<String> getRequestSalesforceIdList() {
		return requestSalesforceIdList;
	}
	public void setRequestSalesforceIdList(List<String> requestSalesforceIdList) {
		this.requestSalesforceIdList = requestSalesforceIdList;
	}
	public Long getResourceId() {
		return resourceId;
	}
	public void setResourceId(Long resourceId) {
		this.resourceId = resourceId;
	}
	public String getStartDate() {
		return startDate;
	}
	public void setStartDate(String startDate) {
		this.startDate = startDate;
	}
	public String getWorkgroup() {
		return workgroup;
	}
	public void setWorkgroup(String workgroup) {
		this.workgroup = workgroup;
	}
}
