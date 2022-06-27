package com.empconn.successfactors.dto;

import java.util.Date;

import org.apache.commons.lang3.StringUtils;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.empconn.constants.ApplicationConstants;
import com.empconn.successfactors.interfaces.SuccessFactorsOutboundData;
import com.empconn.utilities.DateUtils;

public class ProjectChangeDto implements SuccessFactorsOutboundData {

	private String userId;
	@JsonFormat(pattern = "yyyy-MM-dd")
	private Date effectiveStartDate;
	private String project;
	private String account;

	public ProjectChangeDto() {
		super();
		userId = null;
		effectiveStartDate = null;
		project = null;
		account = null;
	}

	public ProjectChangeDto(String userId, Date effectiveStartDate, String project, String account) {
		super();
		this.userId = userId;
		this.effectiveStartDate = effectiveStartDate;
		this.project = project;
		this.account = account;
	}

	public String getUserId() {
		return userId;
	}

	public Date getEffectiveStartDate() {
		return effectiveStartDate;
	}

	public String getProject() {
		return project;
	}

	public String getAccount() {
		return account;
	}

	@Override
	public String toCsv() {
		final String[] array = new String[] {"",StringUtils.trimToEmpty(userId), DateUtils.toString(effectiveStartDate, ApplicationConstants.DATE_FORMAT_SLASH_MM_DD_YYYY), StringUtils.trimToEmpty(project), StringUtils.trimToEmpty(account)};
		return String.join( ",", array);
	}

	@Override
	public String description() {
		return "Project Change";
	}

	public void setUserId(String userId) {
		this.userId = userId;
	}

	public void setEffectiveStartDate(Date effectiveStartDate) {
		this.effectiveStartDate = effectiveStartDate;
	}

	public void setProject(String project) {
		this.project = project;
	}

	public void setAccount(String account) {
		this.account = account;
	}
}
