package com.empconn.dto.earmark;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.empconn.persistence.entities.Project;
import com.empconn.persistence.entities.WorkGroup;

public class DropdownManagerDto {

	List<String> verticalIdList;
	List<String> accountNameList;
	List<String> projectNameList;

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

	public List<String> getAccountNameList() {
		return accountNameList;
	}

	public void setAccountNameList(List<String> accountNameList) {
		this.accountNameList = accountNameList;
	}

	public List<String> getProjectNameList() {
		return projectNameList;
	}

	public void setProjectNameList(List<String> projectNameList) {
		this.projectNameList = projectNameList;
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
