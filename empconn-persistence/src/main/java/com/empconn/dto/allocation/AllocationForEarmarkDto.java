package com.empconn.dto.allocation;

import java.time.LocalDate;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.datatype.jsr310.deser.LocalDateDeserializer;
import com.fasterxml.jackson.datatype.jsr310.ser.LocalDateSerializer;

public class AllocationForEarmarkDto {

	private String earmarkId;
	private Integer earmarkPercentage;
	private String earmarkProjectName;
	private String empCode;
	private String empName;
	private String title;
	private boolean isOpp;
	private Integer availablePercentage;
	@JsonDeserialize(using = LocalDateDeserializer.class)
	@JsonSerialize(using = LocalDateSerializer.class)
	@JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd")
	private LocalDate availableFrom;
	private boolean billable;

	private List<String> primarySkillList;
	@JsonDeserialize(using = LocalDateDeserializer.class)
	@JsonSerialize(using = LocalDateSerializer.class)
	@JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd")
	private LocalDate startDate;
	@JsonDeserialize(using = LocalDateDeserializer.class)
	@JsonSerialize(using = LocalDateSerializer.class)
	@JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd")
	private LocalDate endDate;

	private String devGdm;
	private String qaGdm;
	private String earmarkManager;
	private List<String> secondarySkillList;

	public String getEarmarkId() {
		return earmarkId;
	}

	public void setEarmarkId(String earmarkId) {
		this.earmarkId = earmarkId;
	}

	public Integer getEarmarkPercentage() {
		return earmarkPercentage;
	}

	public void setEarmarkPercentage(Integer earmarkPercentage) {
		this.earmarkPercentage = earmarkPercentage;
	}

	public String getEarmarkProjectName() {
		return earmarkProjectName;
	}

	public void setEarmarkProjectName(String earmarkProjectName) {
		this.earmarkProjectName = earmarkProjectName;
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

	public boolean getIsOpp() {
		return isOpp;
	}

	public void setIsOpp(boolean isOpp) {
		this.isOpp = isOpp;
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

	public boolean isBillable() {
		return billable;
	}

	public void setBillable(boolean billable) {
		this.billable = billable;
	}

	public List<String> getPrimarySkillList() {
		return primarySkillList;
	}

	public void setPrimarySkillList(List<String> primarySkillList) {
		this.primarySkillList = primarySkillList;
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

	public String getDevGdm() {
		return devGdm;
	}

	public void setDevGdm(String devGdm) {
		this.devGdm = devGdm;
	}

	public String getQaGdm() {
		return qaGdm;
	}

	public void setQaGdm(String qaGdm) {
		this.qaGdm = qaGdm;
	}

	public String getEarmarkManager() {
		return earmarkManager;
	}

	public void setEarmarkManager(String earmarkManager) {
		this.earmarkManager = earmarkManager;
	}

	public List<String> getSecondarySkillList() {
		return secondarySkillList;
	}

	public void setSecondarySkillList(List<String> secondarySkillList) {
		this.secondarySkillList = secondarySkillList;
	}

}
