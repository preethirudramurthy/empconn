package com.empconn.dto.ndallocation;

public class IsValidNDResponseDto {
	private Boolean invalidLocationWorkgroup;
	private Boolean invalidSalesforceIdList;
	private Boolean invalidAllocationPercentage;
	private Boolean invalidReleaseDateAfterProjectDate;
	private Boolean invalidReleaseDateOnWeekend;
	public Boolean getInvalidLocationWorkgroup() {
		return invalidLocationWorkgroup;
	}
	public void setInvalidLocationWorkgroup(Boolean invalidLocationWorkgroup) {
		this.invalidLocationWorkgroup = invalidLocationWorkgroup;
	}
	public Boolean getInvalidSalesforceIdList() {
		return invalidSalesforceIdList;
	}
	public void setInvalidSalesforceIdList(Boolean invalidSalesforceIdList) {
		this.invalidSalesforceIdList = invalidSalesforceIdList;
	}
	public Boolean getInvalidAllocationPercentage() {
		return invalidAllocationPercentage;
	}
	public void setInvalidAllocationPercentage(Boolean invalidAllocationPercentage) {
		this.invalidAllocationPercentage = invalidAllocationPercentage;
	}
	public Boolean getInvalidReleaseDateAfterProjectDate() {
		return invalidReleaseDateAfterProjectDate;
	}
	public void setInvalidReleaseDateAfterProjectDate(Boolean invalidReleaseDateAfterProjectDate) {
		this.invalidReleaseDateAfterProjectDate = invalidReleaseDateAfterProjectDate;
	}
	public Boolean getInvalidReleaseDateOnWeekend() {
		return invalidReleaseDateOnWeekend;
	}
	public void setInvalidReleaseDateOnWeekend(Boolean invalidReleaseDateOnWeekend) {
		this.invalidReleaseDateOnWeekend = invalidReleaseDateOnWeekend;
	}
	
	
}
