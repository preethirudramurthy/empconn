package com.empconn.dto.ndallocation;

import java.util.List;

public class NDAllocateResponseDto {
	private List<String> failedRequestIdList;
	public List<String> getFailedRequestIdList() {
		return failedRequestIdList;
	}
	public void setFailedRequestIdList(List<String> failedRequestIdList) {
		this.failedRequestIdList = failedRequestIdList;
	}
}
