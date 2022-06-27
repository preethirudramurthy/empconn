package com.empconn.dto.allocation;

import java.time.LocalDate;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.datatype.jsr310.deser.LocalDateDeserializer;
import com.fasterxml.jackson.datatype.jsr310.ser.LocalDateSerializer;
import com.empconn.persistence.entities.Project;
import com.empconn.persistence.entities.WorkGroup;

public class DeallocationResourceListRequestDto {

	private List<String> verticalIdList;
	private List<String> accountIdList;
	private List<String> projectIdList;
	private List<String> titleIdList;
	private List<String> workgroup;
	@JsonDeserialize(using=LocalDateDeserializer.class)
	@JsonSerialize(using=LocalDateSerializer.class)
	@JsonFormat(shape = JsonFormat.Shape.STRING, pattern="yyyy-MM-dd")
	private LocalDate releaseDateBefore;
	private List<String> orgLocationIdList;
	private Boolean billable;
	private String reporteeType;
	private List<String> managerId;

	@JsonIgnore
	private List<Project> allProjects;

	@JsonIgnore
	private List<WorkGroup> allWorkgroups;

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
	public List<String> getWorkgroup() {
		return workgroup;
	}
	public void setWorkgroup(List<String> workgroup) {
		this.workgroup = workgroup;
	}
	public LocalDate getReleaseDateBefore() {
		return releaseDateBefore;
	}
	public void setReleaseDateBefore(LocalDate releaseDateBefore) {
		this.releaseDateBefore = releaseDateBefore;
	}
	public List<String> getOrgLocationIdList() {
		return orgLocationIdList;
	}
	public void setOrgLocationIdList(List<String> orgLocationIdList) {
		this.orgLocationIdList = orgLocationIdList;
	}
	public Boolean getBillable() {
		return billable;
	}
	public void setBillable(Boolean billable) {
		this.billable = billable;
	}
	public String getReporteeType() {
		return reporteeType;
	}
	public void setReporteeType(String reporteeType) {
		this.reporteeType = reporteeType;
	}
	public List<String> getManagerId() {
		return managerId;
	}
	public void setManagerId(List<String> managerId) {
		this.managerId = managerId;
	}
	public List<Project> getAllProjects() {
		return allProjects;
	}
	public void setAllProjects(List<Project> allProjects) {
		this.allProjects = allProjects;
	}
	public List<WorkGroup> getAllWorkgroups() {
		return allWorkgroups;
	}
	public void setAllWorkgroups(List<WorkGroup> allWorkgroups) {
		this.allWorkgroups = allWorkgroups;
	}
}
