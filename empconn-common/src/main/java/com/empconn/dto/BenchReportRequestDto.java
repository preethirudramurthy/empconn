package com.empconn.dto;

import java.util.List;

public class BenchReportRequestDto {

	private List<String> benchProjectIdList;
	private List<String> orgLocationIdList;
	private List<String> titleIdList;
	private List<String> primarySkillIdList;
	private List<String> secondarySkillIdList;
	private List<AvailablePercentageDto> availablePercentage;
	private List<BenchAgeDto> benchAge;

	public List<String> getBenchProjectIdList() {
		return benchProjectIdList;
	}

	public void setBenchProjectIdList(List<String> benchProjectIdList) {
		this.benchProjectIdList = benchProjectIdList;
	}

	public List<String> getOrgLocationIdList() {
		return orgLocationIdList;
	}

	public void setOrgLocationIdList(List<String> orgLocationIdList) {
		this.orgLocationIdList = orgLocationIdList;
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

	public void setPrimarySkillIdList(List<String> primarySkillId) {
		this.primarySkillIdList = primarySkillId;
	}

	public List<String> getSecondarySkillIdList() {
		return secondarySkillIdList;
	}

	public void setSecondarySkillIdList(List<String> secondarySkillIdList) {
		this.secondarySkillIdList = secondarySkillIdList;
	}

	public List<AvailablePercentageDto> getAvailablePercentage() {
		return availablePercentage;
	}

	public void setAvailablePercentage(List<AvailablePercentageDto> availablePercentage) {
		this.availablePercentage = availablePercentage;
	}

	public List<BenchAgeDto> getBenchAge() {
		return benchAge;
	}

	public void setBenchAge(List<BenchAgeDto> benchAge) {
		this.benchAge = benchAge;
	}

}
