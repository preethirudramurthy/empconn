package com.empconn.persistence.entities;

import java.io.Serializable;
import java.math.BigDecimal;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQuery;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;

import com.empconn.utilities.TimeUtils;

/**
 * The persistent class for the allocation_hours database table.
 *
 */
@Entity
@Table(name = "allocation_hours")
@NamedQuery(name = "AllocationHour.findAll", query = "SELECT a FROM AllocationHour a")
public class AllocationHour extends Auditable<Long> implements Serializable {

	public AllocationHour(BigDecimal billingHours, Long createdBy, String month, Integer year) {
		super();
		this.billingHours = billingHours;
		setCreatedBy(createdBy);
		setCreatedOn(TimeUtils.getCreatedOn());
		setIsActive(true);
		this.month = month;
		this.year = year;
	}

	private static final long serialVersionUID = 1L;

	@Id
	@SequenceGenerator(name = "ALLOCATION_ALLOCATIONHOURSID_GENERATOR", sequenceName = "ALLOCATION_HOURS_ID_SEQUENCE", allocationSize = 1)
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "ALLOCATION_ALLOCATIONHOURSID_GENERATOR")
	@Column(name = "allocation_hours_id")
	private Long allocationHoursId;

	@Column(name = "billing_hours")
	private BigDecimal billingHours;

	@Column(name = "billing_hours_rounded")
	private Integer billingHoursRounded;

	@Column(name = "max_hours")
	private Integer maxHours;

	@Column(name = "month")
	private String month;

	@Column(name = "year")
	private Integer year;

	// bi-directional many-to-one association to AllocationDetail
	@ManyToOne
	@JoinColumn(name = "allocation_id")
	private Allocation allocation;

	public AllocationHour() {
	}

	public Long getAllocationHoursId() {
		return this.allocationHoursId;
	}

	public void setAllocationHoursId(Long allocationHoursId) {
		this.allocationHoursId = allocationHoursId;
	}

	public BigDecimal getBillingHours() {
		return billingHours;
	}

	public void setBillingHours(BigDecimal billingHours) {
		this.billingHours = billingHours;
	}

	public Integer getBillingHoursRounded() {
		return billingHoursRounded;
	}

	public void setBillingHoursRounded(Integer billingHoursRounded) {
		this.billingHoursRounded = billingHoursRounded;
	}

	public Allocation getAllocation() {
		return this.allocation;
	}

	public void setAllocation(Allocation allocation) {
		this.allocation = allocation;
	}

	public Integer getMaxHours() {
		return maxHours;
	}

	public void setMaxHours(Integer maxHours) {
		this.maxHours = maxHours;
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

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((month == null) ? 0 : month.hashCode());
		result = prime * result + ((year == null) ? 0 : year.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		final AllocationHour other = (AllocationHour) obj;
		if (month == null) {
			if (other.month != null)
				return false;
		} else if (!month.equals(other.month))
			return false;
		if (year == null) {
			return other.year == null;
		} else return year.equals(other.year);
	}

	@Override
	public String toString() {
		return "AllocationHour [allocationHoursId=" + allocationHoursId + ", billingHours=" + billingHours
				+ ", billingHoursRounded=" + billingHoursRounded + ", maxHours=" + maxHours + ", month=" + month
				+ ", year=" + year + ", allocation=" + allocation + "]";
	}

	public void deactivate() {
		setIsActive(false);
	}

}