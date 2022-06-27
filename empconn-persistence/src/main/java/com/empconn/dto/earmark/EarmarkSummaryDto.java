package com.empconn.dto.earmark;

import java.util.Date;
import java.util.List;

public class EarmarkSummaryDto {

	private String projectCode;
	private List<String> salesforceIdList;
	private String accountName;
	private String projectName;
	private String managerName;
	private Integer percentage;

	// @JsonDeserialize(using=LocalDateDeserializer.class)
	// @JsonSerialize(using=LocalDateSerializer.class)
	// @JsonFormat(shape = JsonFormat.Shape.STRING, pattern="yyyy-MM-dd")
	private Date startDate;

	// @JsonDeserialize(using=LocalDateDeserializer.class)
	// @JsonSerialize(using=LocalDateSerializer.class)
	// @JsonFormat(shape = JsonFormat.Shape.STRING, pattern="yyyy-MM-dd")
	private Date endDate;

	@Override
	public String toString() {
		return "EarmarkSummaryDto [projectCode=" + projectCode + ", salesforceIdList=" + salesforceIdList
				+ ", accountName=" + accountName + ", projectName=" + projectName + ", managerName=" + managerName
				+ ", percentage=" + percentage + ", startDate=" + startDate + ", endDate=" + endDate + "]";
	}

	public String getProjectCode() {
		return projectCode;
	}

	public void setProjectCode(String projectCode) {
		this.projectCode = projectCode;
	}

	public List<String> getSalesforceIdList() {
		return salesforceIdList;
	}

	public void setSalesforceIdList(List<String> salesforceIdList) {
		this.salesforceIdList = salesforceIdList;
	}

	public String getAccountName() {
		return accountName;
	}

	public void setAccountName(String accountName) {
		this.accountName = accountName;
	}

	public String getProjectName() {
		return projectName;
	}

	public void setProjectName(String projectName) {
		this.projectName = projectName;
	}

	public String getManagerName() {
		return managerName;
	}

	public void setManagerName(String managerName) {
		this.managerName = managerName;
	}

	public Integer getPercentage() {
		return percentage;
	}

	public void setPercentage(Integer percentage) {
		this.percentage = percentage;
	}

	public Date getStartDate() {
		return startDate;
	}

	public void setStartDate(Date startDate) {
		this.startDate = startDate;
	}

	public Date getEndDate() {
		return endDate;
	}

	public void setEndDate(Date endDate) {
		this.endDate = endDate;
	}

}
