package com.empconn.dto;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.datatype.jsr310.deser.LocalDateDeserializer;
import com.fasterxml.jackson.datatype.jsr310.deser.LocalDateTimeDeserializer;
import com.fasterxml.jackson.datatype.jsr310.ser.LocalDateSerializer;
import com.fasterxml.jackson.datatype.jsr310.ser.LocalDateTimeSerializer;

public class UpdateProjectDetailsDto {

	private String accountId;
	private String accountName;
	private String category;
	List<String> dbList;
	private String description;
	private String devGdm;
	@JsonDeserialize(using = LocalDateDeserializer.class)
	@JsonSerialize(using = LocalDateSerializer.class)
	@JsonFormat(pattern = "yyyy-MM-dd")
	private LocalDate endDate;
	List<String> errors;
	private String horizontal;
	@JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd'T'HH:mm:ss.SSS'Z'")
	@JsonDeserialize(using = LocalDateTimeDeserializer.class)
	@JsonSerialize(using = LocalDateTimeSerializer.class)
	private LocalDateTime initiationMeetingDate;
	private boolean isSubProject;
	List<UpdateLocationManagersDto> locationList;
	private String mapId;
	private String onHoldComment;
	List<String> osList;
	private String parentProjectName;
	private String projectCode;
	private String projectId;
	private String projectName;
	private String projectTokLink;
	private String qaGdm;
	List<String> salesforceIdList;
	@JsonDeserialize(using = LocalDateDeserializer.class)
	@JsonSerialize(using = LocalDateSerializer.class)
	@JsonFormat(pattern = "yyyy-MM-dd")
	private LocalDate startDate;
	private String status;
	private String subCategory;
	private boolean subProject;
	List<String> subProjectNameList;
	List<String> techList;
	private String vertical;

	private List<String> managerAssignResources;

	public UpdateProjectDetailsDto() {
		super();
	}

	public UpdateProjectDetailsDto(String accountId, String accountName, String category, List<String> dbList,
			String description, String devGdm, LocalDate endDate, List<String> errors, String horizontal,
			LocalDateTime initiationMeetingDate, boolean isSubProject, List<UpdateLocationManagersDto> locationList,
			String mapId, String onHoldComment, List<String> osList, String parentProjectName, String projectCode,
			String projectId, String projectName, String projectTokLink, String qaGdm, List<String> salesforceIdList,
			LocalDate startDate, String status, String subCategory, boolean subProject, List<String> subProjectNameList,
			List<String> techList, String vertical) {
		super();
		this.accountId = accountId;
		this.accountName = accountName;
		this.category = category;
		this.dbList = dbList;
		this.description = description;
		this.devGdm = devGdm;
		this.endDate = endDate;
		this.errors = errors;
		this.horizontal = horizontal;
		this.initiationMeetingDate = initiationMeetingDate;
		this.isSubProject = isSubProject;
		this.locationList = locationList;
		this.mapId = mapId;
		this.onHoldComment = onHoldComment;
		this.osList = osList;
		this.parentProjectName = parentProjectName;
		this.projectCode = projectCode;
		this.projectId = projectId;
		this.projectName = projectName;
		this.projectTokLink = projectTokLink;
		this.qaGdm = qaGdm;
		this.salesforceIdList = salesforceIdList;
		this.startDate = startDate;
		this.status = status;
		this.subCategory = subCategory;
		this.subProject = subProject;
		this.subProjectNameList = subProjectNameList;
		this.techList = techList;
		this.vertical = vertical;
	}

	public String getAccountId() {
		return accountId;
	}

	public void setAccountId(String accountId) {
		this.accountId = accountId;
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

	public List<String> getDbList() {
		return dbList;
	}

	public void setDbList(List<String> dbList) {
		this.dbList = dbList;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public String getDevGdm() {
		return devGdm;
	}

	public void setDevGdm(String devGdm) {
		this.devGdm = devGdm;
	}

	public LocalDate getEndDate() {
		return endDate;
	}

	public void setEndDate(LocalDate endDate) {
		this.endDate = endDate;
	}

	public List<String> getErrors() {
		return errors;
	}

	public void setErrors(List<String> errors) {
		this.errors = errors;
	}

	public String getHorizontal() {
		return horizontal;
	}

	public void setHorizontal(String horizontal) {
		this.horizontal = horizontal;
	}

	public LocalDateTime getInitiationMeetingDate() {
		return initiationMeetingDate;
	}

	public void setInitiationMeetingDate(LocalDateTime initiationMeetingDate) {
		this.initiationMeetingDate = initiationMeetingDate;
	}

	public boolean isSubProject() {
		return isSubProject;
	}

	public void setIsSubProject(boolean isSubProject) {
		this.isSubProject = isSubProject;
	}

	public List<UpdateLocationManagersDto> getLocationList() {
		return locationList;
	}

	public void setLocationList(List<UpdateLocationManagersDto> locationList) {
		this.locationList = locationList;
	}

	public String getMapId() {
		return mapId;
	}

	public void setMapId(String mapId) {
		this.mapId = mapId;
	}

	public String getOnHoldComment() {
		return onHoldComment;
	}

	public void setOnHoldComment(String onHoldComment) {
		this.onHoldComment = onHoldComment;
	}

	public List<String> getOsList() {
		return osList;
	}

	public void setOsList(List<String> osList) {
		this.osList = osList;
	}

	public String getParentProjectName() {
		return parentProjectName;
	}

	public void setParentProjectName(String parentProjectName) {
		this.parentProjectName = parentProjectName;
	}

	public String getProjectCode() {
		return projectCode;
	}

	public void setProjectCode(String projectCode) {
		this.projectCode = projectCode;
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

	public String getProjectTokLink() {
		return projectTokLink;
	}

	public void setProjectTokLink(String projectTokLink) {
		this.projectTokLink = projectTokLink;
	}

	public String getQaGdm() {
		return qaGdm;
	}

	public void setQaGdm(String qaGdm) {
		this.qaGdm = qaGdm;
	}

	public List<String> getSalesforceIdList() {
		return salesforceIdList;
	}

	public void setSalesforceIdList(List<String> salesforceIdList) {
		this.salesforceIdList = salesforceIdList;
	}

	public LocalDate getStartDate() {
		return startDate;
	}

	public void setStartDate(LocalDate startDate) {
		this.startDate = startDate;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public String getSubCategory() {
		return subCategory;
	}

	public void setSubCategory(String subCategory) {
		this.subCategory = subCategory;
	}

	public boolean subProject() {
		return subProject;
	}

	public void setSubProject(boolean subProject) {
		this.subProject = subProject;
	}

	public List<String> getSubProjectNameList() {
		return subProjectNameList;
	}

	public void setSubProjectNameList(List<String> subProjectNameList) {
		this.subProjectNameList = subProjectNameList;
	}

	public List<String> getTechList() {
		return techList;
	}

	public void setTechList(List<String> techList) {
		this.techList = techList;
	}

	public String getVertical() {
		return vertical;
	}

	public void setVertical(String vertical) {
		this.vertical = vertical;
	}

	public List<String> getManagerAssignResources() {
		return managerAssignResources;
	}

	public void setManagerAssignResources(List<String> managerAssignResources) {
		this.managerAssignResources = managerAssignResources;
	}

}
