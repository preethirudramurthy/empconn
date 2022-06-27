package com.empconn.successfactors.dto;

import java.util.Date;

import org.apache.commons.lang3.StringUtils;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.empconn.constants.ApplicationConstants;
import com.empconn.successfactors.interfaces.SuccessFactorsOutboundData;
import com.empconn.utilities.DateUtils;

public class GdmChangeDto implements SuccessFactorsOutboundData {

	private String empId;
	private String typeOfRelation;
	private String newManagerId;
	@JsonFormat(pattern = "yyyy-MM-dd")
	private Date date;


	public GdmChangeDto() {
		super();
		empId = null;
		typeOfRelation = null;
		newManagerId = null;
		date = null;
	}


	public GdmChangeDto(String empId, String typeOfRelation, String newManagerId, Date date) {
		super();
		this.empId = empId;
		this.typeOfRelation = typeOfRelation;
		this.newManagerId = newManagerId;
		this.date = date;
	}


	public String getEmpId() {
		return empId;
	}


	public String getTypeOfRelation() {
		return typeOfRelation;
	}


	public String getNewManagerId() {
		return newManagerId;
	}


	public Date getDate() {
		return date;
	}


	@Override
	public String toCsv() {
		final String[] array = new String[] {StringUtils.trimToEmpty(empId), StringUtils.trimToEmpty(typeOfRelation), StringUtils.trimToEmpty(newManagerId), DateUtils.toString(date, ApplicationConstants.DATE_FORMAT_YYYYMMDD)};
		return String.join( ",", array);
	}

	@Override
	public String description() {
		return "Gdm Change";
	}

	public void setEmpId(String empId) {
		this.empId = empId;
	}

	public void setTypeOfRelation(String typeOfRelation) {
		this.typeOfRelation = typeOfRelation;
	}

	public void setNewManagerId(String newManagerId) {
		this.newManagerId = newManagerId;
	}

	public void setDate(Date date) {
		this.date = date;
	}

}
