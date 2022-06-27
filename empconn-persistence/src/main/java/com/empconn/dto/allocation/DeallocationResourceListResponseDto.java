package com.empconn.dto.allocation;

import java.time.LocalDate;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.datatype.jsr310.deser.LocalDateDeserializer;
import com.fasterxml.jackson.datatype.jsr310.ser.LocalDateSerializer;

public class DeallocationResourceListResponseDto {

	private Long allocationId;
	private String empCode;
	private String empName;
	private String projectName;
	private String projectLocation;
	private String title;
	private String reportingMangerName;
	private String primaryManagerName;
	private Integer percentage;
	private Boolean billable;

	@JsonDeserialize(using=LocalDateDeserializer.class)
	@JsonSerialize(using=LocalDateSerializer.class)
	@JsonFormat(shape = JsonFormat.Shape.STRING, pattern="yyyy-MM-dd")
	private LocalDate allocationStartDate;
	@JsonDeserialize(using=LocalDateDeserializer.class)
	@JsonSerialize(using=LocalDateSerializer.class)
	@JsonFormat(shape = JsonFormat.Shape.STRING, pattern="yyyy-MM-dd")
	private LocalDate allocationReleaseDate;
	public Long getAllocationId() {
		return allocationId;
	}
	public void setAllocationId(Long allocationId) {
		this.allocationId = allocationId;
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
	public String getProjectName() {
		return projectName;
	}
	public void setProjectName(String projectName) {
		this.projectName = projectName;
	}
	public String getProjectLocation() {
		return projectLocation;
	}
	public void setProjectLocation(String projectLocation) {
		this.projectLocation = projectLocation;
	}
	public String getTitle() {
		return title;
	}
	public void setTitle(String title) {
		this.title = title;
	}
	public String getReportingMangerName() {
		return reportingMangerName;
	}
	public void setReportingMangerName(String reportingMangerName) {
		this.reportingMangerName = reportingMangerName;
	}
	public String getPrimaryManagerName() {
		return primaryManagerName;
	}
	public void setPrimaryManagerName(String primaryManagerName) {
		this.primaryManagerName = primaryManagerName;
	}
	public Integer getPercentage() {
		return percentage;
	}
	public void setPercentage(Integer percentage) {
		this.percentage = percentage;
	}
	public Boolean getBillable() {
		return billable;
	}
	public void setBillable(Boolean billable) {
		this.billable = billable;
	}
	public LocalDate getAllocationStartDate() {
		return allocationStartDate;
	}
	public void setAllocationStartDate(LocalDate allocationStartDate) {
		this.allocationStartDate = allocationStartDate;
	}
	public LocalDate getAllocationReleaseDate() {
		return allocationReleaseDate;
	}
	public void setAllocationReleaseDate(LocalDate allocationReleaseDate) {
		this.allocationReleaseDate = allocationReleaseDate;
	}

}
