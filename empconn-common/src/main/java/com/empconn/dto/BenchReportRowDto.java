package com.empconn.dto;

import java.util.Arrays;
import java.util.List;

public class BenchReportRowDto implements Reporter {

	private String benchAllocationId;
	private String empCode;
	private String empFullName;
	private String title;
	private String location;
	private String projectName;
	private String reportingManager;
	private String primarySkillSet;
	private String secondSkillSet;
	private Integer percentage;
	private String startDate;
	private Integer benchAge;
	private String allocationStatus;
	private String earmarkProjectNames;
	private String lastProjectName;
	private String dateOfJoining;

	@Override
	public List<String> headers() {
		final String[] headers = { "Emp ID", "Emp Name", "Title", "Location", "Project", "Manager", "Skills Level 1",
				"Skills Level 2", "Available %", "Bench From Date", "Bench Aging", "Status", "Earmarked Projects",
				"Released From", "Date Of Joining" };

		return Arrays.asList(headers);
	}

	@Override
	public List<String> fieldNames() {
		final String[] fieldNames = { "empCode", "empFullName", "title", "location", "projectName", "reportingManager",
				"primarySkillSet", "secondSkillSet", "percentage", "startDate", "benchAge", "allocationStatus",
				"earmarkProjectNames", "lastProjectName", "dateOfJoining" };
		return Arrays.asList(fieldNames);
	}

	public String getBenchAllocationId() {
		return benchAllocationId;
	}

	public void setBenchAllocationId(String benchAllocationId) {
		this.benchAllocationId = benchAllocationId;
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

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public String getLocation() {
		return location;
	}

	public void setLocation(String location) {
		this.location = location;
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

	public String getPrimarySkillSet() {
		return primarySkillSet;
	}

	public void setPrimarySkillSet(String primarySkillSet) {
		this.primarySkillSet = primarySkillSet;
	}

	public String getSecondSkillSet() {
		return secondSkillSet;
	}

	public void setSecondSkillSet(String secondSkillSet) {
		this.secondSkillSet = secondSkillSet;
	}

	public Integer getPercentage() {
		return percentage;
	}

	public void setPercentage(Integer percentage) {
		this.percentage = percentage;
	}

	public String getStartDate() {
		return startDate;
	}

	public void setStartDate(String startDate) {
		this.startDate = startDate;
	}

	public Integer getBenchAge() {
		return benchAge;
	}

	public void setBenchAge(Integer benchAge) {
		this.benchAge = benchAge;
	}

	public String getAllocationStatus() {
		return allocationStatus;
	}

	public void setAllocationStatus(String allocationStatus) {
		this.allocationStatus = allocationStatus;
	}

	public String getEarmarkProjectNames() {
		return earmarkProjectNames;
	}

	public void setEarmarkProjectNames(String earmarkProjectNames) {
		this.earmarkProjectNames = earmarkProjectNames;
	}

	public String getLastProjectName() {
		return lastProjectName;
	}

	public void setLastProjectName(String lastProjectName) {
		this.lastProjectName = lastProjectName;
	}

	public String getDateOfJoining() {
		return dateOfJoining;
	}

	public void setDateOfJoining(String dateOfJoining) {
		this.dateOfJoining = dateOfJoining;
	}

}
