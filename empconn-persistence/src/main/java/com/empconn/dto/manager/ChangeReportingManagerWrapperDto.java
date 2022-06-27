package com.empconn.dto.manager;

import java.util.List;

public class ChangeReportingManagerWrapperDto {

	private List<ChangeReportingManagerDto> changeManagerList;

	public List<ChangeReportingManagerDto> getChangeManagerList() {
		return changeManagerList;
	}

	public void setChangeManagerList(List<ChangeReportingManagerDto> changeManagerList) {
		this.changeManagerList = changeManagerList;
	}
}
