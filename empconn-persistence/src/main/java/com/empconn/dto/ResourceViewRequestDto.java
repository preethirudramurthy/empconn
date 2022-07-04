package com.empconn.dto;

import java.io.Serializable;
import java.time.LocalDate;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import com.empconn.persistence.entities.Project;
import com.empconn.persistence.entities.WorkGroup;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.datatype.jsr310.deser.LocalDateDeserializer;
import com.fasterxml.jackson.datatype.jsr310.ser.LocalDateSerializer;

public class ResourceViewRequestDto implements Serializable {

	
	private static final long serialVersionUID = 4054488143463125306L;
	private List<String> verticalIdList;
	private List<String> accountIdList;
	private List<String> projectIdList;
	private List<String> titleIdList;
	private List<String> primarySkillList;
	private List<String> secondarySkillIdList;
	private List<String> workgroup;
	@JsonDeserialize(using=LocalDateDeserializer.class)
	@JsonSerialize(using=LocalDateSerializer.class)
	@JsonFormat(shape = JsonFormat.Shape.STRING, pattern="yyyy-MM-dd")
	private LocalDate releaseDateBefore;
	private List<String> orgLocationIdList;
	private List<String> gdmIdListId;
	private List<String> managerId;

	@JsonIgnore
	private List<Project> allProjects;

	@JsonIgnore
	private List<WorkGroup> allWorkgroups;

	@JsonIgnore
	private Set<String> prohectIdsForManager= new HashSet<>();
	@JsonIgnore
	private Set<Project> projectsByGdms= new HashSet<>();





	public Set<Project> getProjectsByGdms() {
		return projectsByGdms;
	}
	public void setProjectsByGdms(Set<Project> projectsByGdms) {
		this.projectsByGdms = projectsByGdms;
	}
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
	public List<String> getPrimarySkillList() {
		return primarySkillList;
	}
	public void setPrimarySkillList(List<String> primarySkillList) {
		this.primarySkillList = primarySkillList;
	}
	public List<String> getSecondarySkillIdList() {
		return secondarySkillIdList;
	}
	public void setSecondarySkillIdList(List<String> secondarySkillIdList) {
		this.secondarySkillIdList = secondarySkillIdList;
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
	public Set<String> getProhectIdsForManager() {
		return prohectIdsForManager;
	}
	public void setProhectIdsForManager(Set<String> prohectIdsForManager) {
		this.prohectIdsForManager = prohectIdsForManager;
	}
	public List<String> getGdmIdListId() {
		return gdmIdListId;
	}
	public void setGdmIdListId(List<String> gdmIdListId) {
		this.gdmIdListId = gdmIdListId;
	}
	public List<String> getManagerId() {
		return managerId;
	}
	public void setManagerId(List<String> managerId) {
		this.managerId = managerId;
	}

}
