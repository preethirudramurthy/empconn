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
import com.empconn.vo.UnitValue;

public class PinDetailsDto {
	private AccountInfoDto accountInfo;
	private String projectId;
	private String projectName;
	private UnitValue subCategory;
	private UnitValue horizontal;
	@JsonProperty
	private Boolean isSubProject;
	private UnitValue parentProject;
	private List<String> salesforceIdList;

	@JsonDeserialize(using = LocalDateDeserializer.class)
	@JsonSerialize(using = LocalDateSerializer.class)
	@JsonFormat(pattern = "yyyy-MM-dd")
	private LocalDate startDate;

	@JsonDeserialize(using = LocalDateDeserializer.class)
	@JsonSerialize(using = LocalDateSerializer.class)
	@JsonFormat(pattern = "yyyy-MM-dd")
	private LocalDate endDate;

	private String description;
	private List<String> techList;
	private List<String> osList;
	private List<String> dbList;
	private UnitValue devGDM;
	private UnitValue qaGDM;
	private UnitValue businessManager;
	private List<LocationManagersRRUnitDto> locationList;
	private List<CheckListItemDto> checkListItemList;
	private PreApprovalCheckDto preApprovalChecks;
	private List<GdmCommentDto> gdmCommentList;
	private String pinStatus;

	@JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd'T'HH:mm:ss.SSS'Z'")
	@JsonDeserialize(using = LocalDateTimeDeserializer.class)
	@JsonSerialize(using = LocalDateTimeSerializer.class)
	private LocalDateTime createdDate;

	public AccountInfoDto getAccountInfo() {
		return accountInfo;
	}

	public void setAccountInfo(AccountInfoDto accountInfo) {
		this.accountInfo = accountInfo;
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

	public UnitValue getSubCategory() {
		return subCategory;
	}

	public void setSubCategory(UnitValue subCategory) {
		this.subCategory = subCategory;
	}

	public UnitValue getHorizontal() {
		return horizontal;
	}

	public void setHorizontal(UnitValue horizontal) {
		this.horizontal = horizontal;
	}

	public Boolean getIsSubProject() {
		return isSubProject;
	}

	public void setIsSubProject(Boolean isSubProject) {
		this.isSubProject = isSubProject;
	}

	public UnitValue getParentProject() {
		return parentProject;
	}

	public void setParentProject(UnitValue parentProject) {
		this.parentProject = parentProject;
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

	public LocalDate getEndDate() {
		return endDate;
	}

	public void setEndDate(LocalDate endDate) {
		this.endDate = endDate;
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

	public UnitValue getDevGDM() {
		return devGDM;
	}

	public void setDevGDM(UnitValue devGDM) {
		this.devGDM = devGDM;
	}

	public UnitValue getQaGDM() {
		return qaGDM;
	}

	public void setQaGDM(UnitValue qaGDM) {
		this.qaGDM = qaGDM;
	}

	public UnitValue getBusinessManager() {
		return businessManager;
	}

	public void setBusinessManager(UnitValue businessManager) {
		this.businessManager = businessManager;
	}

	public List<LocationManagersRRUnitDto> getLocationList() {
		return locationList;
	}

	public void setLocationList(List<LocationManagersRRUnitDto> locationList) {
		this.locationList = locationList;
	}

	public List<CheckListItemDto> getCheckListItemList() {
		return checkListItemList;
	}

	public void setCheckListItemList(List<CheckListItemDto> checkListItemList) {
		this.checkListItemList = checkListItemList;
	}

	public PreApprovalCheckDto getPreApprovalChecks() {
		return preApprovalChecks;
	}

	public void setPreApprovalChecks(PreApprovalCheckDto preApprovalChecks) {
		this.preApprovalChecks = preApprovalChecks;
	}

	public List<GdmCommentDto> getGdmCommentList() {
		return gdmCommentList;
	}

	public void setGdmCommentList(List<GdmCommentDto> gdmCommentList) {
		this.gdmCommentList = gdmCommentList;
	}

	public String getPinStatus() {
		return pinStatus;
	}

	public void setPinStatus(String pinStatus) {
		this.pinStatus = pinStatus;
	}

	public LocalDateTime getCreatedDate() {
		return createdDate;
	}

	public void setCreatedDate(LocalDateTime createdDate) {
		this.createdDate = createdDate;
	}

}
