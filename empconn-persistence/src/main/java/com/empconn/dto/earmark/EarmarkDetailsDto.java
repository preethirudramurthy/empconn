package com.empconn.dto.earmark;

import java.time.LocalDate;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.datatype.jsr310.deser.LocalDateDeserializer;
import com.fasterxml.jackson.datatype.jsr310.ser.LocalDateSerializer;

public class EarmarkDetailsDto {
	
	private String empName;
	private String title;
	private String accountName;
	private String projectName;
	private Integer availablePercentage;
	
	@JsonDeserialize(using=LocalDateDeserializer.class)
	@JsonSerialize(using=LocalDateSerializer.class)
	@JsonFormat(shape = JsonFormat.Shape.STRING, pattern="yyyy-MM-dd")
	private LocalDate availableFrom;
	
	private String earmarkAccountName;
	private String earmarkProjectName;
	private Boolean isOpp;
	private List<String> salesforceIdList;
	private String managerName;

	@JsonDeserialize(using=LocalDateDeserializer.class)
	@JsonSerialize(using=LocalDateSerializer.class)
	@JsonFormat(shape = JsonFormat.Shape.STRING, pattern="yyyy-MM-dd")
	private LocalDate startDate;
	@JsonDeserialize(using=LocalDateDeserializer.class)
	@JsonSerialize(using=LocalDateSerializer.class)
	@JsonFormat(shape = JsonFormat.Shape.STRING, pattern="yyyy-MM-dd")
	private LocalDate endDate;
	
	private Integer earmarkPercentage;
	private Boolean billable;
	private Boolean clientInterviewNeeded;
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
	public Boolean getIsOpp() {
		return isOpp;
	}
	public void setIsOpp(Boolean isOpp) {
		this.isOpp = isOpp;
	}
	public List<String> getSalesforceIdList() {
		return salesforceIdList;
	}
	public void setSalesforceIdList(List<String> salesforceIdList) {
		this.salesforceIdList = salesforceIdList;
	}
	public String getManagerName() {
		return managerName;
	}
	public void setManagerName(String managerName) {
		this.managerName = managerName;
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
	public Integer getEarmarkPercentage() {
		return earmarkPercentage;
	}
	public void setEarmarkPercentage(Integer earmarkPercentage) {
		this.earmarkPercentage = earmarkPercentage;
	}
	public Boolean getBillable() {
		return billable;
	}
	public void setBillable(Boolean billable) {
		this.billable = billable;
	}
	public Boolean getClientInterviewNeeded() {
		return clientInterviewNeeded;
	}
	public void setClientInterviewNeeded(Boolean clientInterviewNeeded) {
		this.clientInterviewNeeded = clientInterviewNeeded;
	}
	
	
	

}
