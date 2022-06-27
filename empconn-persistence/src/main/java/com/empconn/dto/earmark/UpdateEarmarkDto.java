package com.empconn.dto.earmark;

public class UpdateEarmarkDto {
	
	private String earmarkId;
	private Integer allocationPercentage;
	private Boolean billable;
	private Boolean clientInterviewNeeded;
	public String getEarmarkId() {
		return earmarkId;
	}
	public void setEarmarkId(String earmarkId) {
		this.earmarkId = earmarkId;
	}
	public Integer getAllocationPercentage() {
		return allocationPercentage;
	}
	public void setAllocationPercentage(Integer allocationPercentage) {
		this.allocationPercentage = allocationPercentage;
	}
	public Boolean getBillable() {
		return billable;
	}
	public void setBillable(Boolean billable) {
		this.billable = billable;
	}
	public Boolean getClientInterviewNeeded() {
		return clientInterviewNeeded;
	}
	public void setClientInterviewNeeded(Boolean clientInterviewNeeded) {
		this.clientInterviewNeeded = clientInterviewNeeded;
	}

	
}
