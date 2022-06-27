package com.empconn.dto.allocation;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;


public class AllocationHourDto implements Serializable {

	public AllocationHourDto(BigDecimal billingHours, Integer billingHoursRounded, Date endDate, Integer networkDays,
			Date startDate, String month, Integer year, Integer maxHours) {
		super();
		this.billingHours = billingHours;
		this.billingHoursRounded = billingHoursRounded;
		this.endDate = endDate;
		this.networkDays = networkDays;
		this.startDate = startDate;
		this.month = month;
		this.year = year;
		this.maxHours = maxHours;
	}


	private static final long serialVersionUID = 1L;

	private Long allocationHoursId;

	private BigDecimal billingHours;

	private Integer billingHoursRounded;

	private Date endDate;

	private Integer networkDays;

	private Date startDate;

	private String month;

	private Integer year;

	private Integer maxHours;

	public AllocationHourDto() {
	}

	public Long getAllocationHoursId() {
		return this.allocationHoursId;
	}

	public void setAllocationHoursId(Long allocationHoursId) {
		this.allocationHoursId = allocationHoursId;
	}

	public BigDecimal getBillingHours() {
		return this.billingHours;
	}

	public void setBillingHours(BigDecimal billingHours) {
		this.billingHours = billingHours;
	}

	public Integer getBillingHoursRounded() {
		return this.billingHoursRounded;
	}

	public void setBillingHoursRounded(Integer billingHoursRounded) {
		this.billingHoursRounded = billingHoursRounded;
	}

	public Date getEndDate() {
		return this.endDate;
	}

	public void setEndDate(Date endDate) {
		this.endDate = endDate;
	}

	public Integer getNetworkDays() {
		return this.networkDays;
	}

	public void setNetworkDays(Integer networkDays) {
		this.networkDays = networkDays;
	}

	public Date getStartDate() {
		return this.startDate;
	}

	public void setStartDate(Date startDate) {
		this.startDate = startDate;
	}

	public String getMonth() {
		return month;
	}

	public void setMonth(String month) {
		this.month = month;
	}

	public Integer getYear() {
		return year;
	}

	public void setYear(Integer year) {
		this.year = year;
	}

	public Integer getMaxHours() {
		return maxHours;
	}


	public void setMaxHours(Integer maxHours) {
		this.maxHours = maxHours;
	}


	@Override
	public String toString() {
		return "AllocationHourDto [billingHours=" + billingHours + ", billingHoursRounded=" + billingHoursRounded
				+ ", endDate=" + endDate + ", networkDays=" + networkDays + ", startDate=" + startDate + "]";
	}


}