package com.empconn.dto;

public class FMSEmployeeDetailsDto {

	private String employeeNumber;
	private String firstName;
	private String middleName;
	private String lastName;
	private String emailId;
	private String locationName;
	private String designationName;
	private String businessUnit;
	private String division;
	private String department;
	private String accountName;
	private String projectName;
	private String verticalName;
	private String horizontalName;

	public FMSEmployeeDetailsDto(String employeeNumber, String firstName, String middleName, String lastName,
			String emailId, String locationName, String designationName, String businessUnit, String division,
			String department, String accountName, String projectName, String verticalName, String horizontalName) {
		super();
		this.employeeNumber = employeeNumber;
		this.firstName = firstName;
		this.middleName = middleName;
		this.lastName = lastName;
		this.emailId = emailId;
		this.locationName = locationName;
		this.designationName = designationName;
		this.businessUnit = businessUnit;
		this.division = division;
		this.department = department;
		this.accountName = accountName;
		this.projectName = projectName;
		this.verticalName = verticalName;
		this.horizontalName = horizontalName;
	}

	public FMSEmployeeDetailsDto() {
		super();
		this.employeeNumber=null;
		this.firstName=null;
		this.middleName=null;
		this.lastName=null;
		this.emailId=null;
		this.locationName=null;
		this.designationName=null;
		this.businessUnit=null;
		this.division=null;
		this.department=null;
		this.accountName=null;
		this.projectName=null;
		this.verticalName=null;
		this.horizontalName=null;
	}

	public String getEmployeeNumber() {
		return employeeNumber;
	}

	public String getFirstName() {
		return firstName;
	}

	public String getMiddleName() {
		return middleName;
	}

	public String getLastName() {
		return lastName;
	}

	public String getEmailId() {
		return emailId;
	}

	public String getLocationName() {
		return locationName;
	}

	public String getDesignationName() {
		return designationName;
	}

	public String getBusinessUnit() {
		return businessUnit;
	}

	public String getDivision() {
		return division;
	}

	public String getDepartment() {
		return department;
	}

	public String getAccountName() {
		return accountName;
	}

	public String getProjectName() {
		return projectName;
	}

	public String getVerticalName() {
		return verticalName;
	}

	public String getHorizontalName() {
		return horizontalName;
	}

	public void setEmployeeNumber(String employeeNumber) {
		this.employeeNumber = employeeNumber;
	}

	public void setFirstName(String firstName) {
		this.firstName = firstName;
	}

	public void setMiddleName(String middleName) {
		this.middleName = middleName;
	}

	public void setLastName(String lastName) {
		this.lastName = lastName;
	}

	public void setEmailId(String emailId) {
		this.emailId = emailId;
	}

	public void setLocationName(String locationName) {
		this.locationName = locationName;
	}

	public void setDesignationName(String designationName) {
		this.designationName = designationName;
	}

	public void setBusinessUnit(String businessUnit) {
		this.businessUnit = businessUnit;
	}

	public void setDivision(String division) {
		this.division = division;
	}

	public void setDepartment(String department) {
		this.department = department;
	}

	public void setAccountName(String accountName) {
		this.accountName = accountName;
	}

	public void setProjectName(String projectName) {
		this.projectName = projectName;
	}

	public void setVerticalName(String verticalName) {
		this.verticalName = verticalName;
	}

	public void setHorizontalName(String horizontalName) {
		this.horizontalName = horizontalName;
	}

}
