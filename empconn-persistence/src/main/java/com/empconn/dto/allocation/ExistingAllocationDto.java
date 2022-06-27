package com.empconn.dto.allocation;

import java.time.LocalDate;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.datatype.jsr310.deser.LocalDateDeserializer;
import com.fasterxml.jackson.datatype.jsr310.ser.LocalDateSerializer;

public class ExistingAllocationDto {
	private String projectLocationId;
	private String projectLocationName;
	private String workgroup;
	private String reportingManagerId;
	private String reportingManagerName;
	private Boolean isPrimary;
	@JsonDeserialize(using = LocalDateDeserializer.class)
	@JsonSerialize(using = LocalDateSerializer.class)
	@JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd")
	private LocalDate releaseDate;

	public ExistingAllocationDto() {
		super();
	}

	public ExistingAllocationDto(String projectLocationId, String projectLocationName, String workgroup,
			String reportingManagerId, String reportingManagerName) {
		super();
		this.projectLocationId = projectLocationId;
		this.projectLocationName = projectLocationName;
		this.workgroup = workgroup;
		this.reportingManagerId = reportingManagerId;
		this.reportingManagerName = reportingManagerName;
	}

	public ExistingAllocationDto(String projectLocationId, String projectLocationName, String workgroup,
			String reportingManagerId, String reportingManagerName, Boolean isPrimary, LocalDate releaseDate) {
		super();
		this.projectLocationId = projectLocationId;
		this.projectLocationName = projectLocationName;
		this.workgroup = workgroup;
		this.reportingManagerId = reportingManagerId;
		this.reportingManagerName = reportingManagerName;
		this.isPrimary = isPrimary;
		this.releaseDate = releaseDate;
	}

	public String getProjectLocationId() {
		return projectLocationId;
	}

	public void setProjectLocationId(String projectLocationId) {
		this.projectLocationId = projectLocationId;
	}

	public String getProjectLocationName() {
		return projectLocationName;
	}

	public void setProjectLocationName(String projectLocationName) {
		this.projectLocationName = projectLocationName;
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

	public String getReportingManagerName() {
		return reportingManagerName;
	}

	public void setReportingManagerName(String reportingManagerName) {
		this.reportingManagerName = reportingManagerName;
	}

	public Boolean getIsPrimary() {
		return isPrimary;
	}

	public void setIsPrimary(Boolean isPrimary) {
		this.isPrimary = isPrimary;
	}

	public LocalDate getReleaseDate() {
		return releaseDate;
	}

	public void setReleaseDate(LocalDate releaseDate) {
		this.releaseDate = releaseDate;
	}

}
