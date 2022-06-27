package com.empconn.dto.earmark;

import java.time.LocalDateTime;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.datatype.jsr310.deser.LocalDateTimeDeserializer;
import com.fasterxml.jackson.datatype.jsr310.ser.LocalDateTimeSerializer;

public class AvailableResourceReqDto {

	private String resourceType;
	private List<String> titleId;
	private String primarySkillId;
	private List<String> secondarySkillIdList;
	private List<String> orgLocationIdList;
	private List<String> resourceId;
	@JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd'T'HH:mm:ss.SSS'Z'")
	@JsonDeserialize(using = LocalDateTimeDeserializer.class)
	@JsonSerialize(using = LocalDateTimeSerializer.class)
	private LocalDateTime availableFrom;

	private Integer availablePercentage;
	private Integer benchAgeLower;
	private Integer benchAgeHigher;

	public String getResourceType() {
		return resourceType;
	}

	public void setResourceType(String resourceType) {
		this.resourceType = resourceType;
	}

	public List<String> getTitleId() {
		return titleId;
	}

	public void setTitleId(List<String> titleId) {
		this.titleId = titleId;
	}

	public List<String> getOrgLocationIdList() {
		return orgLocationIdList;
	}

	public void setOrgLocationIdList(List<String> orgLocationIdList) {
		this.orgLocationIdList = orgLocationIdList;
	}

	public List<String> getResourceId() {
		return resourceId;
	}

	public void setResourceId(List<String> resourceId) {
		this.resourceId = resourceId;
	}

	public String getPrimarySkillId() {
		return primarySkillId;
	}

	public void setPrimarySkillId(String primarySkillId) {
		this.primarySkillId = primarySkillId;
	}

	public List<String> getSecondarySkillIdList() {
		return secondarySkillIdList;
	}

	public void setSecondarySkillIdList(List<String> secondarySkillIdList) {
		this.secondarySkillIdList = secondarySkillIdList;
	}

	public LocalDateTime getAvailableFrom() {
		return availableFrom;
	}

	public void setAvailableFrom(LocalDateTime availableFrom) {
		this.availableFrom = availableFrom;
	}

	public Integer getAvailablePercentage() {
		return availablePercentage;
	}

	public void setAvailablePercentage(Integer availablePercentage) {
		this.availablePercentage = availablePercentage;
	}

	public Integer getBenchAgeLower() {
		return benchAgeLower;
	}

	public void setBenchAgeLower(Integer benchAgeLower) {
		this.benchAgeLower = benchAgeLower;
	}

	public Integer getBenchAgeHigher() {
		return benchAgeHigher;
	}

	public void setBenchAgeHigher(Integer benchAgeHigher) {
		this.benchAgeHigher = benchAgeHigher;
	}

}
