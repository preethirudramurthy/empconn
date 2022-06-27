package com.empconn.dto.earmark;

public class EarmarkInfoDto {
	
	private String allocationId;
	private Boolean billable;
	private Boolean clientInterviewNeeded;
	public String getAllocationId() {
		return allocationId;
	}
	public void setAllocationId(String allocationId) {
		this.allocationId = allocationId;
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
