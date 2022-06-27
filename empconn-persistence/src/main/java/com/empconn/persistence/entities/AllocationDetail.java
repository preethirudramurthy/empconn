package com.empconn.persistence.entities;

import java.io.Serializable;
import java.sql.Timestamp;
import java.time.LocalDate;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQuery;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

import org.hibernate.annotations.Formula;

import com.empconn.utilities.TimeUtils;

/**
 * The persistent class for the allocation_detail database table.
 *
 */
@Entity
@Table(name = "allocation_detail")
@NamedQuery(name = "AllocationDetail.findAll", query = "SELECT a FROM AllocationDetail a")
public class AllocationDetail extends Auditable<Long> implements Serializable {

	public AllocationDetail(Integer allocatedPercentage, Date startDate, Allocation allocation, Long createdBy,
			Boolean isActive) {
		super();
		this.allocatedPercentage = allocatedPercentage;
		this.startDate = startDate;
		this.allocation = allocation;
		this.setCreatedBy(createdBy);
		this.setIsActive(isActive);
		this.setCreatedOn(TimeUtils.getToday());
	}

	public AllocationDetail(Integer allocatedPercentage, LocalDate startDate, Long createdBy) {
		super();
		this.allocatedPercentage = allocatedPercentage;
		this.startDate = Timestamp.valueOf(startDate.atStartOfDay());
		this.setCreatedBy(createdBy);
		this.setIsActive(Boolean.TRUE);
		this.setCreatedOn(TimeUtils.getToday());
	}

	private static final long serialVersionUID = 1L;

	@Id
	@SequenceGenerator(name = "ALLOCATION_ALLOCATIONDETAILID_GENERATOR", sequenceName = "ALLOCATION_DETAIL_ID_SEQUENCE", allocationSize = 1)
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "ALLOCATION_ALLOCATIONDETAILID_GENERATOR")
	@Column(name = "allocation_detail_id")
	private Long allocationDetailId;

	@Column(name = "allocated_percentage")
	private Integer allocatedPercentage;

	@Column(name = "deallocated_on")
	private Date deallocatedOn;

	@Temporal(TemporalType.DATE)
	@Column(name = "start_date")
	private Date startDate;

	// bi-directional many-to-one association to Allocation
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "allocation_id")
	private Allocation allocation;

	// bi-directional many-to-one association to Employee
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "deallocated_by_id")
	private Employee deallocatedBy;

	@Formula(value = "current_date - start_date")
	private Integer allocationAge;

	public AllocationDetail() {
	}

	public Integer getAllocationAge() {
		return allocationAge;
	}

	public void setAllocationAge(Integer allocationAge) {
		this.allocationAge = allocationAge;
	}

	public Long getAllocationDetailId() {
		return this.allocationDetailId;
	}

	public void setAllocationDetailId(Long allocationDetailId) {
		this.allocationDetailId = allocationDetailId;
	}

	public Integer getAllocatedPercentage() {
		return this.allocatedPercentage;
	}

	public void setAllocatedPercentage(Integer allocatedPercentage) {
		this.allocatedPercentage = allocatedPercentage;
	}

	public Date getDeallocatedOn() {
		return deallocatedOn;
	}

	public void setDeallocatedOn(Date deallocatedOn) {
		this.deallocatedOn = deallocatedOn;
	}

	public Date getStartDate() {
		return this.startDate;
	}

	public void setStartDate(Date startDate) {
		this.startDate = startDate;
	}

	public Allocation getAllocation() {
		return this.allocation;
	}

	public void setAllocation(Allocation allocation) {
		this.allocation = allocation;
	}

	public Employee getDeallocatedBy() {
		return deallocatedBy;
	}

	public void setDeallocatedBy(Employee deallocatedBy) {
		this.deallocatedBy = deallocatedBy;
	}

	@Override
	public String toString() {
		return "AllocationDetail [allocationDetailId=" + allocationDetailId + ", allocatedPercentage="
				+ allocatedPercentage + ", deallocatedOn=" + deallocatedOn + ", startDate=" + startDate
				+ ", allocation=" + allocation + ", deallocatedBy=" + deallocatedBy + ", isActive=" + getIsActive()
				+ "]";
	}

	public void deactivate(Employee loggedInEmployee) {
		setIsActive(false);
		setDeallocatedBy(loggedInEmployee);
		setDeallocatedOn(new Date());
	}

}