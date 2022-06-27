package com.empconn.dto;

import java.util.List;

public class ValidatedPinDto {
	
	private String projectId;
	private List<LocationManagersDto> locationList;
	public String getProjectId() {
		return projectId;
	}
	public void setProjectId(String projectId) {
		this.projectId = projectId;
	}
	public List<LocationManagersDto> getLocationList() {
		return locationList;
	}
	public void setLocationList(List<LocationManagersDto> locationList) {
		this.locationList = locationList;
	}
	
	

}
