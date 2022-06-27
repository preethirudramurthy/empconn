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

public class ResourceItemDto {

	private String rrId;
	@NotEmpty(message = "projectLocationId required")
	private String projectLocationId;
	@NotEmpty(message = "titleId required")
	private String titleId;
	private Integer noOfResources;

	@JsonFormat(pattern = "yyyy-MM-dd'T'HH:mm:ss.SSS'Z'")
	@JsonDeserialize(using = LocalDateTimeDeserializer.class)
	@JsonSerialize(using = LocalDateTimeSerializer.class)
	private LocalDateTime startDate;
	@JsonFormat(pattern = "yyyy-MM-dd'T'HH:mm:ss.SSS'Z'")
	@JsonDeserialize(using = LocalDateTimeDeserializer.class)
	@JsonSerialize(using = LocalDateTimeSerializer.class)
	private LocalDateTime endDate;

	private Integer percentage;
	@NotEmpty(message = "primarySkillId required")
	private String primarySkillId;
	private List<String> secondarySkillIdList;

	@JsonProperty
	private boolean billable;

	public ResourceItemDto() {
		super();
	}

	public ResourceItemDto(String rrId, String projectLocationId, String titleId, Integer noOfResources,
			LocalDateTime startDate, LocalDateTime endDate, Integer percentage, String primarySkillId,
			List<String> secondarySkillIdList, boolean billable) {
		super();
		this.rrId = rrId;
		this.projectLocationId = projectLocationId;
		this.titleId = titleId;
		this.noOfResources = noOfResources;
		this.startDate = startDate;
		this.endDate = endDate;
		this.percentage = percentage;
		this.primarySkillId = primarySkillId;
		this.secondarySkillIdList = secondarySkillIdList;
		this.billable = billable;
	}

	public String getRrId() {
		return rrId;
	}

	public void setRrId(String rrId) {
		this.rrId = rrId;
	}

	public String getProjectLocationId() {
		return projectLocationId;
	}

	public void setProjectLocationId(String projectLocationId) {
		this.projectLocationId = projectLocationId;
	}

	public String getTitleId() {
		return titleId;
	}

	public void setTitleId(String titleId) {
		this.titleId = titleId;
	}

	public Integer getNoOfResources() {
		return noOfResources;
	}

	public void setNoOfResources(Integer noOfResources) {
		this.noOfResources = noOfResources;
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

	public Integer getPercentage() {
		return percentage;
	}

	public void setPercentage(Integer percentage) {
		this.percentage = percentage;
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

	public boolean isBillable() {
		return billable;
	}

	public void setBillable(boolean billable) {
		this.billable = billable;
	}

	@Override
	public String toString() {
		return "ResourceItemDto [rrId=" + rrId + ", projectLocationId=" + projectLocationId + ", titleId=" + titleId
				+ ", noOfResources=" + noOfResources + ", startDate=" + startDate + ", endDate=" + endDate
				+ ", percentage=" + percentage + ", primarySkillId=" + primarySkillId + ", secondarySkillIdList="
				+ secondarySkillIdList + ", billable=" + billable + "]";
	}

}
