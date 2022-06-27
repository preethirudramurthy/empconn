package com.empconn.dto.allocation;

import java.util.List;

public class CalculateAllocationHoursResponseDto {

	private List<EditReleaseDateAllocationHour> allocationHourList;

	public List<EditReleaseDateAllocationHour> getAllocationHourList() {
		return allocationHourList;
	}

	public void setAllocationHourList(List<EditReleaseDateAllocationHour> allocationHourList) {
		this.allocationHourList = allocationHourList;
	}

}
