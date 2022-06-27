package com.empconn.dto;

import java.math.BigDecimal;
import java.util.Map;

public class ForecastReportResponseDto {

	private String title;
	private Map<String,BigDecimal> monthYearCount;

	public String getTitle() {
		return title;
	}
	public void setTitle(String title) {
		this.title = title;
	}
	public Map<String, BigDecimal> getMonthYearCount() {
		return monthYearCount;
	}
	public void setMonthYearCount(Map<String, BigDecimal> monthYearCount) {
		this.monthYearCount = monthYearCount;
	}

}
