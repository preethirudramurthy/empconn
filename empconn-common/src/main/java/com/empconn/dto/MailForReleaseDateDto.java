package com.empconn.dto;

public class MailForReleaseDateDto {
	private String empCode;
	private String empName;
	private String accountName;
	private String projectName;
	private String releaseDate;
	
	public MailForReleaseDateDto() {
		super();
	}
	public MailForReleaseDateDto(String empCode, String empName, String accountName, String projectName,
			String releaseDate) {
		super();
		this.empCode = empCode;
		this.empName = empName;
		this.accountName = accountName;
		this.projectName = projectName;
		this.releaseDate = releaseDate;
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
	public String getReleaseDate() {
		return releaseDate;
	}
	public void setReleaseDate(String releaseDate) {
		this.releaseDate = releaseDate;
	}

	
}
