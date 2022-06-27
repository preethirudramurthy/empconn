package com.empconn.dto.earmark;

import java.time.LocalDate;
import java.util.List;
import java.util.Objects;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.datatype.jsr310.deser.LocalDateDeserializer;
import com.fasterxml.jackson.datatype.jsr310.ser.LocalDateSerializer;

public class EarmarkItemDto implements Comparable<EarmarkItemDto> {
	private String earmarkId;
	private String empCode;
	private String empName;
	private String title;
	private List<String> primarySkillList;
	private List<String> secondarySkillList;
	private String allocatedAccountName;
	private String allocatedProjectName;
	private Integer availablePercentage;

	@JsonDeserialize(using = LocalDateDeserializer.class)
	@JsonSerialize(using = LocalDateSerializer.class)
	@JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd")
	private LocalDate availableFrom;

	private String earmarkAccountName;
	private String earmarkProjectName;
	private Integer earmarkPercentage;
	private List<String> salesforceIdList;

	@JsonDeserialize(using = LocalDateDeserializer.class)
	@JsonSerialize(using = LocalDateSerializer.class)
	@JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd")
	private LocalDate startDate;
	@JsonDeserialize(using = LocalDateDeserializer.class)
	@JsonSerialize(using = LocalDateSerializer.class)
	@JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd")
	private LocalDate endDate;

	// not required in front end
	private Integer earmarkAccountId;
	private Integer earmarkVerticalId;
	private Long devGdmId;
	private Long qaGdmId;
	private Long businessManagerId;
	private Boolean billable;
	private Long opportunityId;
	private String opportunityName;
	private Long projectId;

	public String getEarmarkId() {
		return earmarkId;
	}

	public void setEarmarkId(String earmarkId) {
		this.earmarkId = earmarkId;
	}

	public String getEmpCode() {
		return empCode;
	}

	public void setEmpCode(String empCode) {
		this.empCode = empCode;
	}

	public String getEmpName() {
		return empName;
	}

	public void setEmpName(String empName) {
		this.empName = empName;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public List<String> getPrimarySkillList() {
		return primarySkillList;
	}

	public void setPrimarySkillList(List<String> primarySkillList) {
		this.primarySkillList = primarySkillList;
	}

	public List<String> getSecondarySkillList() {
		return secondarySkillList;
	}

	public void setSecondarySkillList(List<String> secondarySkillList) {
		this.secondarySkillList = secondarySkillList;
	}

	public String getAllocatedAccountName() {
		return allocatedAccountName;
	}

	public void setAllocatedAccountName(String allocatedAccountName) {
		this.allocatedAccountName = allocatedAccountName;
	}

	public String getAllocatedProjectName() {
		return allocatedProjectName;
	}

	public void setAllocatedProjectName(String allocatedProjectName) {
		this.allocatedProjectName = allocatedProjectName;
	}

	public Integer getAvailablePercentage() {
		return availablePercentage;
	}

	public void setAvailablePercentage(Integer availablePercentage) {
		this.availablePercentage = availablePercentage;
	}

	public LocalDate getAvailableFrom() {
		return availableFrom;
	}

	public void setAvailableFrom(LocalDate availableFrom) {
		this.availableFrom = availableFrom;
	}

	public String getEarmarkAccountName() {
		return earmarkAccountName;
	}

	public void setEarmarkAccountName(String earmarkAccountName) {
		this.earmarkAccountName = earmarkAccountName;
	}

	public String getEarmarkProjectName() {
		return earmarkProjectName;
	}

	public void setEarmarkProjectName(String earmarkProjectName) {
		this.earmarkProjectName = earmarkProjectName;
	}

	public Integer getEarmarkPercentage() {
		return earmarkPercentage;
	}

	public void setEarmarkPercentage(Integer earmarkPercentage) {
		this.earmarkPercentage = earmarkPercentage;
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

	public Integer getEarmarkAccountId() {
		return earmarkAccountId;
	}

	public void setEarmarkAccountId(Integer earmarkAccountId) {
		this.earmarkAccountId = earmarkAccountId;
	}

	public Integer getEarmarkVerticalId() {
		return earmarkVerticalId;
	}

	public void setEarmarkVerticalId(Integer earmarkVerticalId) {
		this.earmarkVerticalId = earmarkVerticalId;
	}

	public Long getDevGdmId() {
		return devGdmId;
	}

	public void setDevGdmId(Long devGdmId) {
		this.devGdmId = devGdmId;
	}

	public Long getQaGdmId() {
		return qaGdmId;
	}

	public void setQaGdmId(Long qaGdmId) {
		this.qaGdmId = qaGdmId;
	}

	public Long getBusinessManagerId() {
		return businessManagerId;
	}

	public void setBusinessManagerId(Long businessManagerId) {
		this.businessManagerId = businessManagerId;
	}

	public Boolean getBillable() {
		return billable;
	}

	public void setBillable(Boolean billable) {
		this.billable = billable;
	}

	public Long getOpportunityId() {
		return opportunityId;
	}

	public void setOpportunityId(Long opportunityId) {
		this.opportunityId = opportunityId;
	}

	public String getOpportunityName() {
		return opportunityName;
	}

	public void setOpportunityName(String opportunityName) {
		this.opportunityName = opportunityName;
	}

	public Long getProjectId() {
		return projectId;
	}

	public void setProjectId(Long projectId) {
		this.projectId = projectId;
	}

	@Override
	public int compareTo(EarmarkItemDto earmarkItemDto) {
		return this.empName.compareTo(earmarkItemDto.empName);
	}

	@Override
	public int hashCode() {
		return Objects.hash(allocatedAccountName, allocatedProjectName, availableFrom, availablePercentage, billable,
				businessManagerId, devGdmId, earmarkAccountId, earmarkAccountName, earmarkId, earmarkPercentage,
				earmarkProjectName, earmarkVerticalId, empCode, empName, endDate, opportunityId, opportunityName,
				primarySkillList, projectId, qaGdmId, salesforceIdList, secondarySkillList, startDate, title);
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		final EarmarkItemDto other = (EarmarkItemDto) obj;
		return Objects.equals(allocatedAccountName, other.allocatedAccountName)
				&& Objects.equals(allocatedProjectName, other.allocatedProjectName)
				&& Objects.equals(availableFrom, other.availableFrom)
				&& Objects.equals(availablePercentage, other.availablePercentage)
				&& Objects.equals(billable, other.billable)
				&& Objects.equals(businessManagerId, other.businessManagerId)
				&& Objects.equals(devGdmId, other.devGdmId) && Objects.equals(earmarkAccountId, other.earmarkAccountId)
				&& Objects.equals(earmarkAccountName, other.earmarkAccountName)
				&& Objects.equals(earmarkId, other.earmarkId)
				&& Objects.equals(earmarkPercentage, other.earmarkPercentage)
				&& Objects.equals(earmarkProjectName, other.earmarkProjectName)
				&& Objects.equals(earmarkVerticalId, other.earmarkVerticalId) && Objects.equals(empCode, other.empCode)
				&& Objects.equals(empName, other.empName) && Objects.equals(endDate, other.endDate)
				&& Objects.equals(opportunityId, other.opportunityId)
				&& Objects.equals(opportunityName, other.opportunityName)
				&& Objects.equals(primarySkillList, other.primarySkillList)
				&& Objects.equals(projectId, other.projectId) && Objects.equals(qaGdmId, other.qaGdmId)
				&& Objects.equals(salesforceIdList, other.salesforceIdList)
				&& Objects.equals(secondarySkillList, other.secondarySkillList)
				&& Objects.equals(startDate, other.startDate) && Objects.equals(title, other.title);
	}

}