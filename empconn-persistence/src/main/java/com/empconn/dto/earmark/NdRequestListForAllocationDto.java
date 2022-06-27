package com.empconn.dto.earmark;

import java.util.List;

public class NdRequestListForAllocationDto {
	private List<String> accountIdList;
	private List<String> locationIdList;
	private List<String> projectIdList;
	private List<String> titleIdList;

	public List<String> getAccountIdList() {
		return accountIdList;
	}

	public void setAccountIdList(List<String> accountIdList) {
		this.accountIdList = accountIdList;
	}

	public List<String> getLocationIdList() {
		return locationIdList;
	}

	public void setLocationIdList(List<String> locationIdList) {
		this.locationIdList = locationIdList;
	}

	public List<String> getProjectIdList() {
		return projectIdList;
	}

	public void setProjectIdList(List<String> projectIdList) {
		this.projectIdList = projectIdList;
	}

	public List<String> getTitleIdList() {
		return titleIdList;
	}

	public void setTitleIdList(List<String> titleIdList) {
		this.titleIdList = titleIdList;
	}

}
