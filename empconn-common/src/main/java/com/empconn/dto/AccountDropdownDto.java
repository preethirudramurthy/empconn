package com.empconn.dto;

import java.util.List;

public class AccountDropdownDto {

	private List<String> verticalIdList;
	private boolean ignoreRole;

	public List<String> getVerticalIdList() {
		return verticalIdList;
	}

	public void setVerticalIdList(List<String> verticalIdList) {
		this.verticalIdList = verticalIdList;
	}

	public boolean isIgnoreRole() {
		return ignoreRole;
	}

	public void setIgnoreRole(boolean ignoreRole) {
		this.ignoreRole = ignoreRole;
	}

}
