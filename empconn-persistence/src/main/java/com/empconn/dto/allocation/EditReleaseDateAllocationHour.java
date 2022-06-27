package com.empconn.dto.allocation;

import java.util.List;

public class EditReleaseDateAllocationHour {

	private Integer year;
	private List<EditReleaseMonthDto> monthList;
	public Integer getYear() {
		return year;
	}
	public void setYear(Integer year) {
		this.year = year;
	}
	public List<EditReleaseMonthDto> getMonthList() {
		return monthList;
	}
	public void setMonthList(List<EditReleaseMonthDto> monthList) {
		this.monthList = monthList;
	}

}
