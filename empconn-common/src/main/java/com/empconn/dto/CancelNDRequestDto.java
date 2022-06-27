package com.empconn.dto;

import java.util.List;

public class CancelNDRequestDto {
	private List<Long> requestIdList;

	public List<Long> getRequestIdList() {
		return requestIdList;
	}

	public void setRequestIdList(List<Long> requestIdList) {
		this.requestIdList = requestIdList;
	}
}
