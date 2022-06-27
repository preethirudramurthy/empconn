package com.empconn.dto.manager;

public class GetReporteeListResponseDto {

	public GetReporteeListResponseDto(Long allocationId, String empCode, String empName, String accountName,
			String projectName, String workgroup, String projectLocation) {
		super();
		this.allocationId = allocationId;
		this.empCode = empCode;
		this.empName = empName;
		this.accountName = accountName;
		this.projectName = projectName;
		this.workgroup = workgroup;
		this.projectLocation = projectLocation;
	}
	private Long allocationId;
	private String empCode;
	private String empName;
	private String accountName;
	private String projectName;
	private String workgroup;
	private String projectLocation;
	public Long getAllocationId() {
		return allocationId;
	}
	public void setAllocationId(Long allocationId) {
		this.allocationId = allocationId;
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
	public String getWorkgroup() {
		return workgroup;
	}
	public void setWorkgroup(String workgroup) {
		this.workgroup = workgroup;
	}
	public String getProjectLocation() {
		return projectLocation;
	}
	public void setProjectLocation(String projectLocation) {
		this.projectLocation = projectLocation;
	}

}
