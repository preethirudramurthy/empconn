package com.empconn.dto;

import java.util.List;

public class BenchReportResponseDto {

	private Integer physicalBenchCount;
	private Float totalAvailable;
	private List<BenchReportRowDto> benchReportRowList;

	public Integer getPhysicalBenchCount() {
		return physicalBenchCount;
	}

	public void setPhysicalBenchCount(Integer physicalBenchCount) {
		this.physicalBenchCount = physicalBenchCount;
	}

	public Float getTotalAvailable() {
		return totalAvailable;
	}

	public void setTotalAvailable(Float totalAvailable) {
		this.totalAvailable = totalAvailable;
	}

	public List<BenchReportRowDto> getBenchReportRowList() {
		return benchReportRowList;
	}

	public void setBenchReportRowList(List<BenchReportRowDto> benchReportRowList) {
		this.benchReportRowList = benchReportRowList;
	}

}
