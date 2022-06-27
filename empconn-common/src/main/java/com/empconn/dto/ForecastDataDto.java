package com.empconn.dto;

import java.util.Arrays;
import java.util.List;

public class ForecastDataDto implements Reporter {

	private String empCode;
	private String empFullName;
	private String title;
	private String location;
	private String vertical;
	private String primarySkillSet;
	private String secondSkillSet;
	private String projectName;
	private Integer percentage;
	private String startDate;
	private String releaseDate;

	@Override
	public List<String> headers() {
		final String[] headers = { "Emp ID", "Employee Name", "Title", "Location", "Vertical", "Skill Leve 1", "Skill Level 2"
				, "Project", "Alloc. %", "Date of Movement", "Release Date" };
		return Arrays.asList(headers);
	}

	@Override
	public List<String> fieldNames() {
		final String[] fieldNames = { "empCode", "empFullName", "title", "location", "vertical", "primarySkillSet", "secondSkillSet",
				"projectName", "percentage", "startDate", "releaseDate" };
		return Arrays.asList(fieldNames);
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
	public String getVertical() {
		return vertical;
	}

	public void setVertical(String vertical) {
		this.vertical = vertical;
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
	public String getStartDate() {
		return startDate;
	}
	public void setStartDate(String startDate) {
		this.startDate = startDate;
	}
	public String getReleaseDate() {
		return releaseDate;
	}
	public void setReleaseDate(String releaseDate) {
		this.releaseDate = releaseDate;
	}

}
