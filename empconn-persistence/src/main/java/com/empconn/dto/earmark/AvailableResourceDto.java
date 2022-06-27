package com.empconn.dto.earmark;

import java.time.LocalDate;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.datatype.jsr310.deser.LocalDateDeserializer;
import com.fasterxml.jackson.datatype.jsr310.ser.LocalDateSerializer;

public class AvailableResourceDto {

	private String allocationId;
	private String resourceId;
	private String empCode;
	private String empName;
	private String title;
	private String account;
	private String project;
	private String location;

	@JsonDeserialize(using = LocalDateDeserializer.class)
	@JsonSerialize(using = LocalDateSerializer.class)
	@JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd")
	private LocalDate availableFrom;

	private Integer availablePercentage;
	private Integer benchAge;
	private List<String> primarySkillSetList;
	private List<String> secondarySkillSetList;
	private List<String> earmarkProjectNameList;
	private String allocationStatus;

	public String getAllocationId() {
		return allocationId;
	}

	public void setAllocationId(String allocationId) {
		this.allocationId = allocationId;
	}

	public String getResourceId() {
		return resourceId;
	}

	public void setResourceId(String resourceId) {
		this.resourceId = resourceId;
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

	public String getAccount() {
		return account;
	}

	public void setAccount(String account) {
		this.account = account;
	}

	public String getProject() {
		return project;
	}

	public void setProject(String project) {
		this.project = project;
	}

	public LocalDate getAvailableFrom() {
		return availableFrom;
	}

	public void setAvailableFrom(LocalDate availableFrom) {
		this.availableFrom = availableFrom;
	}

	public Integer getAvailablePercentage() {
		return availablePercentage;
	}

	public void setAvailablePercentage(Integer availablePercentage) {
		this.availablePercentage = availablePercentage;
	}

	public Integer getBenchAge() {
		return benchAge;
	}

	public void setBenchAge(Integer benchAge) {
		this.benchAge = benchAge;
	}

	public List<String> getPrimarySkillSetList() {
		return primarySkillSetList;
	}

	public void setPrimarySkillSetList(List<String> primarySkillSetList) {
		this.primarySkillSetList = primarySkillSetList;
	}

	public List<String> getSecondarySkillSetList() {
		return secondarySkillSetList;
	}

	public void setSecondarySkillSetList(List<String> secondarySkillSetList) {
		this.secondarySkillSetList = secondarySkillSetList;
	}

	public List<String> getEarmarkProjectNameList() {
		return earmarkProjectNameList;
	}

	public void setEarmarkProjectNameList(List<String> earmarkProjectNameList) {
		this.earmarkProjectNameList = earmarkProjectNameList;
	}

	public String getAllocationStatus() {
		return allocationStatus;
	}

	public void setAllocationStatus(String allocationStatus) {
		this.allocationStatus = allocationStatus;
	}

	public String getLocation() {
		return location;
	}

	public void setLocation(String location) {
		this.location = location;
	}
}
