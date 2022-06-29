package com.empconn.dto;

import java.io.Serializable;
import java.time.LocalDate;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.datatype.jsr310.deser.LocalDateDeserializer;
import com.fasterxml.jackson.datatype.jsr310.ser.LocalDateSerializer;

public class AllocationReportRequestDto implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = -1725897145896417407L;
	private List<String> verticalIdList;
	private List<String> accountIdList;
	private List<String> projectIdList;
	private List<String> titleIdList;
	private List<String> workgroupList;
	private List<String> orgLocationIdList;

	@JsonDeserialize(using = LocalDateDeserializer.class)
	@JsonSerialize(using = LocalDateSerializer.class)
	@JsonFormat(pattern = "yyyy-MM-dd")
	private LocalDate allocationFrom;

	private List<String> primarySkillIdList;
	private List<String> secondarySkillIdList;
	private boolean onlyActive;

	public List<String> getVerticalIdList() {
		return verticalIdList;
	}

	public void setVerticalIdList(List<String> verticalIdList) {
		this.verticalIdList = verticalIdList;
	}

	public List<String> getAccountIdList() {
		return accountIdList;
	}

	public void setAccountIdList(List<String> accountIdList) {
		this.accountIdList = accountIdList;
	}

	public List<String> getProjectIdList() {
		return projectIdList;
	}

	public void setProjectIdList(List<String> projectIdList) {
		this.projectIdList = projectIdList;
	}

	public List<String> getTitleIdList() {
		return titleIdList;
	}

	public void setTitleIdList(List<String> titleIdList) {
		this.titleIdList = titleIdList;
	}

	public List<String> getWorkgroupList() {
		return workgroupList;
	}

	public void setWorkgroupList(List<String> workgroupList) {
		this.workgroupList = workgroupList;
	}

	public List<String> getOrgLocationIdList() {
		return orgLocationIdList;
	}

	public void setOrgLocationIdList(List<String> orgLocationIdList) {
		this.orgLocationIdList = orgLocationIdList;
	}

	public LocalDate getAllocationFrom() {
		return allocationFrom;
	}

	public void setAllocationFrom(LocalDate allocationFrom) {
		this.allocationFrom = allocationFrom;
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

	public boolean isOnlyActive() {
		return onlyActive;
	}

	public void setOnlyActive(boolean onlyActive) {
		this.onlyActive = onlyActive;
	}

}
