package com.empconn.dto.allocation;

public class IsValidEarmarkAllocationDto {

	private boolean invalidLocationWorkgroup;
	private boolean invalidSalesforceIdList;
	private boolean invalidAllocationPercentage;
	private boolean invalidReleaseDateAfterProjectDate;
	private boolean invalidReleaseDateOnWeekend;

	public boolean isInvalidLocationWorkgroup() {
		return invalidLocationWorkgroup;
	}

	public void setInvalidLocationWorkgroup(boolean invalidLocationWorkgroup) {
		this.invalidLocationWorkgroup = invalidLocationWorkgroup;
	}

	public boolean isInvalidSalesforceIdList() {
		return invalidSalesforceIdList;
	}

	public void setInvalidSalesforceIdList(boolean invalidSalesforceIdList) {
		this.invalidSalesforceIdList = invalidSalesforceIdList;
	}

	public boolean isInvalidAllocationPercentage() {
		return invalidAllocationPercentage;
	}

	public void setInvalidAllocationPercentage(boolean invalidAllocationPercentage) {
		this.invalidAllocationPercentage = invalidAllocationPercentage;
	}

	public boolean isInvalidReleaseDateAfterProjectDate() {
		return invalidReleaseDateAfterProjectDate;
	}

	public void setInvalidReleaseDateAfterProjectDate(boolean invalidReleaseDateAfterProjectDate) {
		this.invalidReleaseDateAfterProjectDate = invalidReleaseDateAfterProjectDate;
	}

	public boolean isInvalidReleaseDateOnWeekend() {
		return invalidReleaseDateOnWeekend;
	}

	public void setInvalidReleaseDateOnWeekend(boolean invalidReleaseDateOnWeekend) {
		this.invalidReleaseDateOnWeekend = invalidReleaseDateOnWeekend;
	}

}
