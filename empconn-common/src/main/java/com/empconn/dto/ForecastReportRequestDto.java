package com.empconn.dto;

import java.util.List;

public class ForecastReportRequestDto {

	private String monthYear;
	private List<String> titleIdList;
	private List<String> primarySkillIdList;
	private List<String> secondarySkillIdList;
	private List<String> orgLocationIdList;
	private List<String> verticalIdList;
	private String title;
	private String inside_monthYear;

	public String getMonthYear() {
		return monthYear;
	}
	public void setMonthYear(String monthYear) {
		this.monthYear = monthYear;
	}
	public List<String> getTitleIdList() {
		return titleIdList;
	}
	public void setTitleIdList(List<String> titleIdList) {
		this.titleIdList = titleIdList;
	}
	public List<String> getPrimarySkillIdList() {
		return primarySkillIdList;
	}
	public void setPrimarySkillIdList(List<String> primarySkillIdList) {
		this.primarySkillIdList = primarySkillIdList;
	}
	public List<String> getSecondarySkillIdList() {
		return secondarySkillIdList;
	}
	public void setSecondarySkillIdList(List<String> secondarySkillIdList) {
		this.secondarySkillIdList = secondarySkillIdList;
	}
	public List<String> getOrgLocationIdList() {
		return orgLocationIdList;
	}
	public void setOrgLocationIdList(List<String> orgLocationIdList) {
		this.orgLocationIdList = orgLocationIdList;
	}
	public List<String> getVerticalIdList() {
		return verticalIdList;
	}
	public void setVerticalIdList(List<String> verticalIdList) {
		this.verticalIdList = verticalIdList;
	}
	public String getTitle() {
		return title;
	}
	public void setTitle(String title) {
		this.title = title;
	}
	public String getInside_monthYear() {
		return inside_monthYear;
	}
	public void setInside_monthYear(String inside_monthYear) {
		this.inside_monthYear = inside_monthYear;
	}
}
