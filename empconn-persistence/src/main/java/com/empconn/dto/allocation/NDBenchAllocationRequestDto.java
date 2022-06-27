package com.empconn.dto.allocation;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.datatype.jsr310.deser.LocalDateDeserializer;
import com.fasterxml.jackson.datatype.jsr310.ser.LocalDateSerializer;

public class NDBenchAllocationRequestDto {
	private final Long allocationId;
	private final Long resourceId;
	private final String projectLocationId;
	private final String workgroup;
	private final String reportingManagerId;
	private final List<String> extraSalesforceIdList;
	private final Integer percentage;

	@JsonFormat(pattern = "yyyy-MM-dd")
	@JsonDeserialize(using = LocalDateDeserializer.class)
	@JsonSerialize(using = LocalDateSerializer.class)
	private final LocalDate startDate;

	private final LocalDateTime releaseDate;
	private final Boolean billable;
	private final Boolean isPrimaryManager;
	private final String projectId;

	public NDBenchAllocationRequestDto(Long allocationId, Long resourceId, String projectLocationId, String workgroup,
			String reportingManagerId, List<String> extraSalesforceIdList, Integer percentage, LocalDate startDate,
			LocalDateTime releaseDate, Boolean billable, Boolean isPrimaryManager, String projectId) {
		super();
		this.allocationId = allocationId;
		this.resourceId = resourceId;
		this.projectLocationId = projectLocationId;
		this.workgroup = workgroup;
		this.reportingManagerId = reportingManagerId;
		this.extraSalesforceIdList = extraSalesforceIdList;
		this.percentage = percentage;
		this.startDate = startDate;
		this.releaseDate = releaseDate;
		this.billable = billable;
		this.isPrimaryManager = isPrimaryManager;
		this.projectId = projectId;
	}

	public Long getAllocationId() {
		return allocationId;
	}

	public Long getResourceId() {
		return resourceId;
	}

	public String getProjectLocationId() {
		return projectLocationId;
	}

	public String getWorkgroup() {
		return workgroup;
	}

	public String getReportingManagerId() {
		return reportingManagerId;
	}

	public List<String> getExtraSalesforceIdList() {
		return extraSalesforceIdList;
	}

	public Integer getPercentage() {
		return percentage;
	}

	public LocalDate getStartDate() {
		return startDate;
	}

	public LocalDateTime getReleaseDate() {
		return releaseDate;
	}

	public Boolean getBillable() {
		return billable;
	}

	public Boolean getIsPrimaryManager() {
		return isPrimaryManager;
	}

	public String getProjectId() {
		return projectId;
	}
}