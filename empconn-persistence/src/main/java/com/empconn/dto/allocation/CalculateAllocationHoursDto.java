package com.empconn.dto.allocation;

import java.util.Date;

import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.format.annotation.DateTimeFormat.ISO;
import org.springframework.http.RequestEntity;

public class CalculateAllocationHoursDto {

	@DateTimeFormat(iso = ISO.DATE_TIME)
	private Date toDate;

	@DateTimeFormat(pattern="yyyy-MM-dd")
	private Date fromDate;

	private Integer percentage;

	public Date getToDate() {
		return toDate;
	}

	public void setToDate(Date toDate) {
		this.toDate = toDate;
	}

	public Date getFromDate() {
		return fromDate;
	}

	public void setFromDate(Date fromDate) {
		this.fromDate = fromDate;
	}

	public Integer getPercentage() {
		return percentage;
	}

	public void setPercentage(Integer percentage) {
		this.percentage = percentage;
	}


	public CalculateAllocationHoursDto getPerson(RequestEntity<CalculateAllocationHoursDto> requestEntity) {
		return requestEntity.getBody();
	}
}
