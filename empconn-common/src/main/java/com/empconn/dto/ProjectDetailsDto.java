package com.empconn.dto;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.datatype.jsr310.deser.LocalDateDeserializer;
import com.fasterxml.jackson.datatype.jsr310.deser.LocalDateTimeDeserializer;
import com.fasterxml.jackson.datatype.jsr310.ser.LocalDateSerializer;
import com.fasterxml.jackson.datatype.jsr310.ser.LocalDateTimeSerializer;

public class ProjectDetailsDto {

	private String accountId;
	private String projectId;
	private String projectName;
	private String vertical;
	private String horizontal;
	private String accountName;
	private String category;
	private String subCategory;
	@JsonProperty
	private Boolean isSubProject;
	private String projectCode;
	private String parentProjectName;
	private List<String> salesforceIdList;
	private String description;

	@JsonDeserialize(using = LocalDateDeserializer.class)
	@JsonSerialize(using = LocalDateSerializer.class)
	@JsonFormat(pattern = "yyyy-MM-dd")
	private LocalDate startDate;
	@JsonDeserialize(using = LocalDateDeserializer.class)
	@JsonSerialize(using = LocalDateSerializer.class)
	@JsonFormat(pattern = "yyyy-MM-dd")
	private LocalDate endDate;
	@JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd'T'HH:mm:ss.SSS'Z'")
	@JsonDeserialize(using = LocalDateTimeDeserializer.class)
	@JsonSerialize(using = LocalDateTimeSerializer.class)
	private LocalDateTime initiationMeetingDate;

	private String projectTokLink;
	private List<String> techList;
	private List<String> osList;
	private List<String> dbList;
	private String devGdm;
	private String qaGdm;
	private String status;
	private String onHoldComment;
	private List<String> subProjectNameList;
	private List<LocationManagersPDto> locationList;
	private String mapId;

	public String getAccountId() {
		return accountId;
	}

	public void setAccountId(String accountId) {
		this.accountId = accountId;
	}

	public String getProjectId() {
		return projectId;
	}

	public void setProjectId(String projectId) {
		this.projectId = projectId;
	}

	public String getProjectName() {
		return projectName;
	}

	public void setProjectName(String projectName) {
		this.projectName = projectName;
	}

	public String getVertical() {
		return vertical;
	}

	public void setVertical(String vertical) {
		this.vertical = vertical;
	}

	public String getHorizontal() {
		return horizontal;
	}

	public void setHorizontal(String horizontal) {
		this.horizontal = horizontal;
	}

	public String getAccountName() {
		return accountName;
	}

	public void setAccountName(String accountName) {
		this.accountName = accountName;
	}

	public String getCategory() {
		return category;
	}

	public void setCategory(String category) {
		this.category = category;
	}

	public String getSubCategory() {
		return subCategory;
	}

	public void setSubCategory(String subCategory) {
		this.subCategory = subCategory;
	}

	public Boolean getIsSubProject() {
		return isSubProject;
	}

	public void setIsSubProject(Boolean isSubProject) {
		this.isSubProject = isSubProject;
	}

	public String getProjectCode() {
		return projectCode;
	}

	public void setProjectCode(String projectCode) {
		this.projectCode = projectCode;
	}

	public String getParentProjectName() {
		return parentProjectName;
	}

	public void setParentProjectName(String parentProjectName) {
		this.parentProjectName = parentProjectName;
	}

	public List<String> getSalesforceIdList() {
		return salesforceIdList;
	}

	public void setSalesforceIdList(List<String> salesforceIdList) {
		this.salesforceIdList = salesforceIdList;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public LocalDate getStartDate() {
		return startDate;
	}

	public void setStartDate(LocalDate startDate) {
		this.startDate = startDate;
	}

	public LocalDate getEndDate() {
		return endDate;
	}

	public void setEndDate(LocalDate endDate) {
		this.endDate = endDate;
	}

	public LocalDateTime getInitiationMeetingDate() {
		return initiationMeetingDate;
	}

	public void setInitiationMeetingDate(LocalDateTime initiationMeetingDate) {
		this.initiationMeetingDate = initiationMeetingDate;
	}

	public String getProjectTokLink() {
		return projectTokLink;
	}

	public void setProjectTokLink(String projectTokLink) {
		this.projectTokLink = projectTokLink;
	}

	public List<String> getTechList() {
		return techList;
	}

	public void setTechList(List<String> techList) {
		this.techList = techList;
	}

	public List<String> getOsList() {
		return osList;
	}

	public void setOsList(List<String> osList) {
		this.osList = osList;
	}

	public List<String> getDbList() {
		return dbList;
	}

	public void setDbList(List<String> dbList) {
		this.dbList = dbList;
	}

	public String getDevGdm() {
		return devGdm;
	}

	public void setDevGdm(String devGdm) {
		this.devGdm = devGdm;
	}

	public String getQaGdm() {
		return qaGdm;
	}

	public void setQaGdm(String qaGdm) {
		this.qaGdm = qaGdm;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public String getOnHoldComment() {
		return onHoldComment;
	}

	public void setOnHoldComment(String onHoldComment) {
		this.onHoldComment = onHoldComment;
	}

	public List<String> getSubProjectNameList() {
		return subProjectNameList;
	}

	public void setSubProjectNameList(List<String> subProjectNameList) {
		this.subProjectNameList = subProjectNameList;
	}

	public List<LocationManagersPDto> getLocationList() {
		return locationList;
	}

	public void setLocationList(List<LocationManagersPDto> locationList) {
		this.locationList = locationList;
	}

	public String getMapId() {
		return mapId;
	}

	public void setMapId(String mapId) {
		this.mapId = mapId;
	}

}
