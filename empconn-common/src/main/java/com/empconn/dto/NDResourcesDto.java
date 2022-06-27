package com.empconn.dto;

import java.util.List;

public class NDResourcesDto {
	private String resourceId;
	private String empCode;
	private String empName;
	private String empLocation;
	private String title;
	private String department;
	private int availablePercentage;
	private Boolean isActive;
	private Long createdBy;
	private List<AllocationDto> allocationList;

	public Long getCreatedBy() {
		return createdBy;
	}

	public void setCreatedBy(Long createdBy) {
		this.createdBy = createdBy;
	}

	public Boolean getIsActive() {
		return isActive;
	}

	public void setIsActive(Boolean isActive) {
		this.isActive = isActive;
	}

	public List<AllocationDto> getAllocationList() {
		return allocationList;
	}

	public void setAllocationList(List<AllocationDto> allocationList) {
		this.allocationList = allocationList;
	}

	public String getResourceId() {
		return resourceId;
	}

	public void setResourceId(String resourceId) {
		this.resourceId = resourceId;
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

	public String getEmpLocation() {
		return empLocation;
	}

	public void setEmpLocation(String empLocation) {
		this.empLocation = empLocation;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public String getDepartment() {
		return department;
	}

	public void setDepartment(String department) {
		this.department = department;
	}

	public int getAvailablePercentage() {
		return availablePercentage;
	}

	public void setAvailablePercentage(int availablePercentage) {
		this.availablePercentage = availablePercentage;
	}

}
