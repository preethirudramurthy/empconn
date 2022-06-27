package com.empconn.dto;

public class ResourceInformationDto {
	private String location;
	private String title;
	private int numberOfResources;
	private int allocationPercentage;
	private String startDate;
	private String endDate;
	private String primarySkills;

	public ResourceInformationDto() {
		super();
		// TODO Auto-generated constructor stub
	}

	public ResourceInformationDto(String location, String title, int numberOfResources, int allocationPercentage,
			String startDate, String endDate, String primarySkills) {
		super();
		this.location = location;
		this.title = title;
		this.numberOfResources = numberOfResources;
		this.allocationPercentage = allocationPercentage;
		this.startDate = startDate;
		this.endDate = endDate;
		this.primarySkills = primarySkills;
	}

	public String getLocation() {
		return location;
	}

	public void setLocation(String location) {
		this.location = location;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public int getNumberOfResources() {
		return numberOfResources;
	}

	public void setNumberOfResources(int numberOfResources) {
		this.numberOfResources = numberOfResources;
	}

	public int getAllocationPercentage() {
		return allocationPercentage;
	}

	public void setAllocationPercentage(int allocationPercentage) {
		this.allocationPercentage = allocationPercentage;
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

	public String getPrimarySkills() {
		return primarySkills;
	}

	public void setPrimarySkills(String primarySkills) {
		this.primarySkills = primarySkills;
	}
}
