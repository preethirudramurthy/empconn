package com.empconn.dto;

public class MailForEarmarkProjectDto {
	private String empCode;
	private String empName;
	private String title;
	private String accountName;
	private String projectName;
	private String startDate;
	private String endDate;
	private String billable;



	public MailForEarmarkProjectDto() {
		super();
	}
	public MailForEarmarkProjectDto(String empCode, String empName, String title, String accountName,
			String projectName, String startDate, String endDate, String billable) {
		super();
		this.empCode = empCode;
		this.empName = empName;
		this.title = title;
		this.accountName = accountName;
		this.projectName = projectName;
		this.startDate = startDate;
		this.endDate = endDate;
		this.billable = billable;
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
	public String getStartDate() {
		return startDate;
	}
	public void setStartDate(String startDate) {
		this.startDate = startDate;
	}
	public String getEndDate() {
		return endDate;
	}
	public void setEndDate(String endDate) {
		this.endDate = endDate;
	}
	public String getBillable() {
		return billable;
	}
	public void setBillable(String billable) {
		this.billable = billable;
	}



}
