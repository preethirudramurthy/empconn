package com.empconn.dto;

import java.time.LocalDateTime;
import java.util.List;

import javax.validation.constraints.NotEmpty;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.datatype.jsr310.deser.LocalDateTimeDeserializer;
import com.fasterxml.jackson.datatype.jsr310.ser.LocalDateTimeSerializer;

import io.swagger.annotations.ApiModelProperty;

public class SavePinDto {

	private String accountId;
	private String projectId;
	private String name;
	private String subCategoryId;
	private String horizontalId;

	@JsonProperty
	private Boolean isSubProject;
	private String parentProjectId;
	private List<@NotEmpty String> salesforceIdList;

	@JsonFormat(pattern = "yyyy-MM-dd'T'HH:mm:ss.SSS'Z'")
	@JsonDeserialize(using = LocalDateTimeDeserializer.class)
	@JsonSerialize(using = LocalDateTimeSerializer.class)
	private LocalDateTime startDate;

	@JsonFormat(pattern = "yyyy-MM-dd'T'HH:mm:ss.SSS'Z'")
	@JsonDeserialize(using = LocalDateTimeDeserializer.class)
	@JsonSerialize(using = LocalDateTimeSerializer.class)
	private LocalDateTime endDate;

	@JsonFormat(pattern = "yyyy-MM-dd'T'HH:mm:ss.SSS'Z'")
	@JsonDeserialize(using = LocalDateTimeDeserializer.class)
	@JsonSerialize(using = LocalDateTimeSerializer.class)
	private LocalDateTime createdDate;

	private String description;
	private List<String> techList;
	private List<String> osList;
	private List<String> dbList;
	private String devGDMId;
	private String qaGDMId;
	private String businessManagerId;

	@ApiModelProperty(hidden = true)
	private String projectTOKLink;

	private List<LocationManagersValidateDto> locationList;

	public SavePinDto() {
		super();
	}

	public SavePinDto(String accountId, String projectId, String name, String subCategoryId, String horizontalId,
			Boolean isSubProject, String parentProjectId, List<String> salesforceIdList, LocalDateTime startDate,
			LocalDateTime endDate, LocalDateTime createdDate, String description, List<String> techList,
			List<String> osList, List<String> dbList, String devGDMId, String qaGDMId, String businessManagerId,
			String projectTOKLink, List<LocationManagersValidateDto> locationList) {
		super();
		this.accountId = accountId;
		this.projectId = projectId;
		this.name = name;
		this.subCategoryId = subCategoryId;
		this.horizontalId = horizontalId;
		this.isSubProject = isSubProject;
		this.parentProjectId = parentProjectId;
		this.salesforceIdList = salesforceIdList;
		this.startDate = startDate;
		this.endDate = endDate;
		this.createdDate = createdDate;
		this.description = description;
		this.techList = techList;
		this.osList = osList;
		this.dbList = dbList;
		this.devGDMId = devGDMId;
		this.qaGDMId = qaGDMId;
		this.businessManagerId = businessManagerId;
		this.projectTOKLink = projectTOKLink;
		this.locationList = locationList;
	}

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

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getSubCategoryId() {
		return subCategoryId;
	}

	public void setSubCategoryId(String subCategoryId) {
		this.subCategoryId = subCategoryId;
	}

	public String getHorizontalId() {
		return horizontalId;
	}

	public void setHorizontalId(String horizontalId) {
		this.horizontalId = horizontalId;
	}

	public Boolean getIsSubProject() {
		return isSubProject;
	}

	public void setIsSubProject(Boolean isSubProject) {
		this.isSubProject = isSubProject;
	}

	public String getParentProjectId() {
		return parentProjectId;
	}

	public void setParentProjectId(String parentProjectId) {
		this.parentProjectId = parentProjectId;
	}

	public List<String> getSalesforceIdList() {
		return salesforceIdList;
	}

	public void setSalesforceIdList(List<String> salesforceIdList) {
		this.salesforceIdList = salesforceIdList;
	}

	public LocalDateTime getStartDate() {
		return startDate;
	}

	public void setStartDate(LocalDateTime startDate) {
		this.startDate = startDate;
	}

	public LocalDateTime getEndDate() {
		return endDate;
	}

	public void setEndDate(LocalDateTime endDate) {
		this.endDate = endDate;
	}

	public LocalDateTime getCreatedDate() {
		return createdDate;
	}

	public void setCreatedDate(LocalDateTime createdDate) {
		this.createdDate = createdDate;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
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

	public String getDevGDMId() {
		return devGDMId;
	}

	public void setDevGDMId(String devGDMId) {
		this.devGDMId = devGDMId;
	}

	public String getQaGDMId() {
		return qaGDMId;
	}

	public void setQaGDMId(String qaGDMId) {
		this.qaGDMId = qaGDMId;
	}

	public String getBusinessManagerId() {
		return businessManagerId;
	}

	public void setBusinessManagerId(String businessManagerId) {
		this.businessManagerId = businessManagerId;
	}

	public String getProjectTOKLink() {
		return projectTOKLink;
	}

	public void setProjectTOKLink(String projectTOKLink) {
		this.projectTOKLink = projectTOKLink;
	}

	public List<LocationManagersValidateDto> getLocationList() {
		return locationList;
	}

	public void setLocationList(List<LocationManagersValidateDto> locationList) {
		this.locationList = locationList;
	}

}
