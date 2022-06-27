package com.empconn.dto;

import java.util.List;
import java.util.Set;

import com.fasterxml.jackson.annotation.JsonProperty;

public class ProjectDropdownDto {

	public ProjectDropdownDto() {

	}

	private List<String> verticalIdList;
	private List<String> accountIdList;
	@JsonProperty
	private Boolean isActive;
	@JsonProperty
	private Boolean onlyFutureReleaseDate;
	private boolean ignoreRole;

	public ProjectDropdownDto(List<String> verticalIdList, List<String> accountIdList, Boolean isActive,
			Boolean onlyFutureReleaseDate, Boolean includeBench, Boolean includePracticeBench, String partial,
			Set<Long> projectIds) {
		super();
		this.verticalIdList = verticalIdList;
		this.accountIdList = accountIdList;
		this.isActive = isActive;
		this.onlyFutureReleaseDate = onlyFutureReleaseDate;
		this.includeBench = includeBench;
		this.includePracticeBench = includePracticeBench;
		this.partial = partial;
		this.projectIds = projectIds;
	}

	@JsonProperty
	private Boolean includeBench;
	private Boolean includePracticeBench;
	private String partial;
	private Set<Long> projectIds;

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

	public Boolean getIsActive() {
		return isActive;
	}

	public void setIsActive(Boolean isActive) {
		this.isActive = isActive;
	}

	public Boolean getOnlyFutureReleaseDate() {
		return onlyFutureReleaseDate;
	}

	public void setOnlyFutureReleaseDate(Boolean onlyFutureReleaseDate) {
		this.onlyFutureReleaseDate = onlyFutureReleaseDate;
	}

	public Boolean getIncludeBench() {
		return includeBench;
	}

	public void setIncludeBench(Boolean includeBench) {
		this.includeBench = includeBench;
	}

	public Boolean getIncludePracticeBench() {
		return includePracticeBench;
	}

	public void setIncludePracticeBench(Boolean includePracticeBench) {
		this.includePracticeBench = includePracticeBench;
	}

	public String getPartial() {
		return partial;
	}

	public void setPartial(String partial) {
		this.partial = partial;
	}

	@Override
	public String toString() {
		return "FormdataDto [verticalIdList=" + verticalIdList + ", accountIdList=" + accountIdList + ", isActive="
				+ isActive + ", onlyFutureReleaseDate=" + onlyFutureReleaseDate + ", withBench=" + includeBench
				+ ", partial=" + partial + "]";
	}

	public Set<Long> getProjectIds() {
		return projectIds;
	}

	public void setProjectIds(Set<Long> projectIds) {
		this.projectIds = projectIds;
	}

	public boolean isIgnoreRole() {
		return ignoreRole;
	}

	public void setIgnoreRole(boolean ignoreRole) {
		this.ignoreRole = ignoreRole;
	}

}
