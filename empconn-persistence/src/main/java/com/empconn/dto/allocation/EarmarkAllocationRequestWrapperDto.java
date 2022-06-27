package com.empconn.dto.allocation;

import java.util.List;

public class EarmarkAllocationRequestWrapperDto {

	private List<EarmarkAllocationRequestDto> allocationList;

	public List<EarmarkAllocationRequestDto> getAllocationList() {
		return allocationList;
	}

	public void setAllocationList(List<EarmarkAllocationRequestDto> allocationList) {
		this.allocationList = allocationList;
	}
}