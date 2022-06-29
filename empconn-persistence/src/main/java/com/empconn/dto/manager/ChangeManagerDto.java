package com.empconn.dto.manager;

public class ChangeManagerDto {

	private Long allocationId;
	private Long newReportingMangerId;
	private boolean isPrimary;

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
	public boolean getIsPrimary() {
		return isPrimary;
	}
	public void setIsPrimary(boolean isPrimary) {
		this.isPrimary = isPrimary;
	}



}
