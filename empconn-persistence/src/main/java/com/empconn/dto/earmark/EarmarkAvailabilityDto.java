package com.empconn.dto.earmark;

import java.util.List;

public class EarmarkAvailabilityDto {
	
	private String fullName;
	private String empCode;
	private String title;
	private List<EarmarkSummaryDto> earmarkList;
	private List<ResourceAvailabilitySummaryDto> availabilityList;
	
	public String getFullName() {
		return fullName;
	}
	public void setFullName(String fullName) {
		this.fullName = fullName;
	}
	public String getEmpCode() {
		return empCode;
	}
	public void setEmpCode(String empCode) {
		this.empCode = empCode;
	}
	public String getTitle() {
		return title;
	}
	public void setTitle(String title) {
		this.title = title;
	}
	public List<EarmarkSummaryDto> getEarmarkList() {
		return earmarkList;
	}
	public void setEarmarkList(List<EarmarkSummaryDto> earmarkList) {
		this.earmarkList = earmarkList;
	}
	public List<ResourceAvailabilitySummaryDto> getAvailabilityList() {
		return availabilityList;
	}
	public void setAvailabilityList(List<ResourceAvailabilitySummaryDto> availabilityList) {
		this.availabilityList = availabilityList;
	}
	@Override
	public String toString() {
		return "EarmarkAvailabilityDto [fullName=" + fullName + ", empCode=" + empCode + ", title=" + title
				+ ", earmarkList=" + earmarkList + ", availabilityList=" + availabilityList + "]";
	}
	
	

}
