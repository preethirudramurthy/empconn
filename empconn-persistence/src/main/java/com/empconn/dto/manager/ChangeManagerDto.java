package com.empconn.dto.manager;

public class ChangeManagerDto {

	private Long allocationId;
	private Long newReportingMangerId;
	private Boolean isPrimary;

	public Long getAllocationId() {
		return allocationId;
	}
	public void setAllocationId(Long allocationId) {
		this.allocationId = allocationId;
	}
	public Long getNewReportingMangerId() {
		return newReportingMangerId;
	}
	public void setNewReportingMangerId(Long newReportingMangerId) {
		this.newReportingMangerId = newReportingMangerId;
	}
	public Boolean getIsPrimary() {
		return isPrimary;
	}
	public void setIsPrimary(Boolean isPrimary) {
		this.isPrimary = isPrimary;
	}



}
