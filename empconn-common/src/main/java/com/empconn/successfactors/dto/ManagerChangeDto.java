package com.empconn.successfactors.dto;

import java.util.Date;

import org.apache.commons.lang3.StringUtils;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.empconn.constants.ApplicationConstants;
import com.empconn.successfactors.interfaces.SuccessFactorsOutboundData;
import com.empconn.utilities.DateUtils;

public class ManagerChangeDto implements SuccessFactorsOutboundData {

	private String empId;
	private String newManagerId;
	@JsonFormat(pattern = "yyyy-MM-dd")
	private Date date;

	public String getEmpId() {
		return empId;
	}

	public String getNewManagerId() {
		return newManagerId;
	}

	public Date getDate() {
		return date;
	}

	public ManagerChangeDto() {
		super();
		empId = null;
		newManagerId = null;
		date= null;
	}

	public ManagerChangeDto(String empId, String newManagerId, Date date) {
		super();
		this.empId = empId;
		this.newManagerId = newManagerId;
		this.date = date;
	}

	@Override
	public String toCsv() {
		final String[] array = new String[] {StringUtils.trimToEmpty(empId), StringUtils.trimToEmpty(newManagerId), DateUtils.toString(date, ApplicationConstants.DATE_FORMAT_YYYYMMDD)};
		return String.join( ",", array);
	}


	@Override
	public String description() {
		return "Manager Change";
	}

	public void setEmpId(String empId) {
		this.empId = empId;
	}

	public void setNewManagerId(String newManagerId) {
		this.newManagerId = newManagerId;
	}

	public void setDate(Date date) {
		this.date = date;
	}
}
