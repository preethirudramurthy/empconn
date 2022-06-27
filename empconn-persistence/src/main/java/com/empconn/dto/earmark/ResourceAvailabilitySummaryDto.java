package com.empconn.dto.earmark;

import java.util.Date;

public class ResourceAvailabilitySummaryDto {

	private String accountName;
	private String projectName;

	// @JsonDeserialize(using = LocalDateDeserializer.class)
	// @JsonSerialize(using = LocalDateSerializer.class)
	// @JsonFormat(shape = JsonFormat.Shape.STRING, pattern="yyyy-MM-dd")
	private Date availableFrom;
	private Integer percentage;

	@Override
	public String toString() {
		return "ResourceAvailabilitySummaryDto [accountName=" + accountName + ", projectName=" + projectName
				+ ", availableFrom=" + availableFrom + ", percentage=" + percentage + "]";
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

	public Date getAvailableFrom() {
		return availableFrom;
	}

	public void setAvailableFrom(Date availableFrom) {
		this.availableFrom = availableFrom;
	}

	public Integer getPercentage() {
		return percentage;
	}

	public void setPercentage(Integer percentage) {
		this.percentage = percentage;
	}

}
