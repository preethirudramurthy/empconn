package com.empconn.dto.ndallocation;

import java.util.List;

public class NDAllocateRequest {
	private List<NDAllocateRequestDto> allocationList;

	public List<NDAllocateRequestDto> getAllocationList() {
		return allocationList;
	}

	public void setAllocationList(List<NDAllocateRequestDto> allocationList) {
		this.allocationList = allocationList;
	}
	
}
