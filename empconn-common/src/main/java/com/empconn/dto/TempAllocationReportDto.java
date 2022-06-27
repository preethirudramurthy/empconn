package com.empconn.dto;

import java.util.List;

public class TempAllocationReportDto {

	private String queryStringForExcel;
	private List<AllocationReportResponseDto> allocationList;

	public TempAllocationReportDto(String queryStringForExcel, List<AllocationReportResponseDto> allocationList) {
		super();
		this.queryStringForExcel = queryStringForExcel;
		this.allocationList = allocationList;
	}

	public String getQueryStringForExcel() {
		return queryStringForExcel;
	}

	public void setQueryStringForExcel(String queryStringForExcel) {
		this.queryStringForExcel = queryStringForExcel;
	}

	public List<AllocationReportResponseDto> getAllocationList() {
		return allocationList;
	}

	public void setAllocationList(List<AllocationReportResponseDto> allocationList) {
		this.allocationList = allocationList;
	}

}
