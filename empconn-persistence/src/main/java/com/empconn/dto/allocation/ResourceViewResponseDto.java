package com.empconn.dto.allocation;

import java.time.LocalDate;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.datatype.jsr310.deser.LocalDateDeserializer;
import com.fasterxml.jackson.datatype.jsr310.ser.LocalDateSerializer;

public class ResourceViewResponseDto {
	private Long allocationId;
	private String empCode;
	private String empName;
	private String title;
	private String projectLocation;
	private List<String> primarySkillList;
	private List<String> secondarySkillList;
	private String accountName;
	private String vertical;
	private String projectName;
	private Integer percentage;

	@JsonDeserialize(using=LocalDateDeserializer.class)
	@JsonSerialize(using=LocalDateSerializer.class)
	@JsonFormat(shape = JsonFormat.Shape.STRING, pattern="yyyy-MM-dd")
	private LocalDate startDate;
	@JsonDeserialize(using=LocalDateDeserializer.class)
	@JsonSerialize(using=LocalDateSerializer.class)
	@JsonFormat(shape = JsonFormat.Shape.STRING, pattern="yyyy-MM-dd")
	private LocalDate releaseDate;
	private String reportingManager;
	private String isPrimary;
	private Boolean billable;
	private String qaGdm;
	private String devGdm;
	private String primryManagerName;


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
	public String getTitle() {
		return title;
	}
	public void setTitle(String title) {
		this.title = title;
	}
	public String getProjectLocation() {
		return projectLocation;
	}
	public void setProjectLocation(String projectLocation) {
		this.projectLocation = projectLocation;
	}
	public List<String> getPrimarySkillList() {
		return primarySkillList;
	}
	public void setPrimarySkillList(List<String> primarySkillList) {
		this.primarySkillList = primarySkillList;
	}
	public List<String> getSecondarySkillList() {
		return secondarySkillList;
	}
	public void setSecondarySkillList(List<String> secondarySkillList) {
		this.secondarySkillList = secondarySkillList;
	}
	public String getAccountName() {
		return accountName;
	}
	public void setAccountName(String accountName) {
		this.accountName = accountName;
	}
	public String getVertical() {
		return vertical;
	}
	public void setVertical(String vertical) {
		this.vertical = vertical;
	}
	public String getProjectName() {
		return projectName;
	}
	public void setProjectName(String projectName) {
		this.projectName = projectName;
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
	public LocalDate getReleaseDate() {
		return releaseDate;
	}
	public void setReleaseDate(LocalDate releaseDate) {
		this.releaseDate = releaseDate;
	}

	public String getReportingManager() {
		return reportingManager;
	}
	public void setReportingManager(String reportingManager) {
		this.reportingManager = reportingManager;
	}
	public String getIsPrimary() {
		return isPrimary;
	}
	public void setIsPrimary(String isPrimary) {
		this.isPrimary = isPrimary;
	}
	public Boolean getBillable() {
		return billable;
	}
	public void setBillable(Boolean billable) {
		this.billable = billable;
	}
	public String getQaGdm() {
		return qaGdm;
	}
	public void setQaGdm(String qaGdm) {
		this.qaGdm = qaGdm;
	}
	public String getDevGdm() {
		return devGdm;
	}
	public void setDevGdm(String devGdm) {
		this.devGdm = devGdm;
	}
	public String getPrimryManagerName() {
		return primryManagerName;
	}
	public void setPrimryManagerName(String primryManagerName) {
		this.primryManagerName = primryManagerName;
	}

}
