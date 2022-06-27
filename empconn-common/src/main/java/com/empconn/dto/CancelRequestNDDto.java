package com.empconn.dto;

import java.util.Date;

public class CancelRequestNDDto {
	
	private String empId;
	private String resourceName;
	private String requestedAccount;
	private String requestedProject;
	private Integer allocation;
	private String allocStartDate;
	private String tentativeReleaseEndDate;
	public String getEmpId() {
		return empId;
	}
	public void setEmpId(String empId) {
		this.empId = empId;
	}
	public String getResourceName() {
		return resourceName;
	}
	public void setResourceName(String resourceName) {
		this.resourceName = resourceName;
	}
	public String getRequestedAccount() {
		return requestedAccount;
	}
	public void setRequestedAccount(String requestedAccount) {
		this.requestedAccount = requestedAccount;
	}
	public String getRequestedProject() {
		return requestedProject;
	}
	public void setRequestedProject(String requestedProject) {
		this.requestedProject = requestedProject;
	}
	public Integer getAllocation() {
		return allocation;
	}
	public void setAllocation(Integer allocation) {
		this.allocation = allocation;
	}
	public String getAllocStartDate() {
		return allocStartDate;
	}
	public void setAllocStartDate(String allocStartDate) {
		this.allocStartDate = allocStartDate;
	}
	public String getTentativeReleaseEndDate() {
		return tentativeReleaseEndDate;
	}
	public void setTentativeReleaseEndDate(String tentativeReleaseEndDate) {
		this.tentativeReleaseEndDate = tentativeReleaseEndDate;
	}

}
