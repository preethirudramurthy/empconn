package com.empconn.dto.allocation;

import java.util.List;

public class SwitchOverRequestWrapperDto {
	
	private List<SwitchOverRequestDto> allocationList;

	public List<SwitchOverRequestDto> getAllocationList() {
		return allocationList;
	}

	public void setAllocationList(List<SwitchOverRequestDto> allocationList) {
		this.allocationList = allocationList;
	}

}
