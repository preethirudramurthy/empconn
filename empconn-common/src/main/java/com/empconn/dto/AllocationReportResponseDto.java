package com.empconn.dto;

import java.util.Arrays;
import java.util.List;

public class AllocationReportResponseDto implements Reporter {

	private String accountName;
	private String allocationDetailId;
	private String empCode;
	private String empFullName;
	private String gdmFullName;
	private String location;
	private Integer percentage;
	private String primarySkillSet;
	private String projectName;
	private String reportingManager;
	private String secondSkillSet;
	private String title;
	private String workgroup;

	//@JsonDeserialize(using = LocalDateDeserializer.class)
	//@JsonSerialize(using = LocalDateSerializer.class)
	//@JsonFormat(pattern = "dd-MMM-YYYY")
	private String releaseDate;

	//@JsonDeserialize(using = LocalDateDeserializer.class)
	//@JsonSerialize(using = LocalDateSerializer.class)
	//@JsonFormat(pattern = "dd-MMM-YYYY")
	private String startDate;

	@Override
	public List<String> headers() {
		final String[] headers = { "Emp ID", "Employee Name", "Title", "Location", "Account", "Project", "Workgroup",
				"Alloc. %", "Skill Leve 1", "Skill Level 2", "Manager", "GDM", "Date of Movement", "Release Date" };
		return Arrays.asList(headers);
	}

	@Override
	public List<String> fieldNames() {
		final String[] fieldNames = { "empCode", "empFullName", "title", "location", "accountName", "projectName",
				"workgroup", "percentage", "primarySkillSet", "secondSkillSet", "reportingManager", "gdmFullName",
				"startDate", "releaseDate" };
		return Arrays.asList(fieldNames);
	}

	public String getAccountName() {
		return accountName;
	}

	public void setAccountName(String accountName) {
		this.accountName = accountName;
	}

	public String getAllocationDetailId() {
		return allocationDetailId;
	}

	public void setAllocationDetailId(String allocationDetailId) {
		this.allocationDetailId = allocationDetailId;
	}

	public String getEmpCode() {
		return empCode;
	}

	public void setEmpCode(String empCode) {
		this.empCode = empCode;
	}

	public String getEmpFullName() {
		return empFullName;
	}

	public void setEmpFullName(String empFullName) {
		this.empFullName = empFullName;
	}

	public String getGdmFullName() {
		return gdmFullName;
	}

	public void setGdmFullName(String gdmFullName) {
		this.gdmFullName = gdmFullName;
	}

	public String getLocation() {
		return location;
	}

	public void setLocation(String location) {
		this.location = location;
	}

	public Integer getPercentage() {
		return percentage;
	}

	public void setPercentage(Integer percentage) {
		this.percentage = percentage;
	}

	public String getPrimarySkillSet() {
		return primarySkillSet;
	}

	public void setPrimarySkillSet(String primarySkillSet) {
		this.primarySkillSet = primarySkillSet;
	}

	public String getProjectName() {
		return projectName;
	}

	public void setProjectName(String projectName) {
		this.projectName = projectName;
	}

	public String getReportingManager() {
		return reportingManager;
	}

	public void setReportingManager(String reportingManager) {
		this.reportingManager = reportingManager;
	}

	public String getSecondSkillSet() {
		return secondSkillSet;
	}

	public void setSecondSkillSet(String secondSkillSet) {
		this.secondSkillSet = secondSkillSet;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public String getWorkgroup() {
		return workgroup;
	}

	public void setWorkgroup(String workgroup) {
		this.workgroup = workgroup;
	}

	public String getReleaseDate() {
		return releaseDate;
	}

	public void setReleaseDate(String releaseDate) {
		this.releaseDate = releaseDate;
	}

	public String getStartDate() {
		return startDate;
	}

	public void setStartDate(String startDate) {
		this.startDate = startDate;
	}

}
