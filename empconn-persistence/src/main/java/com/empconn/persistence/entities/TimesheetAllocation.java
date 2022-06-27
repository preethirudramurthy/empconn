package com.empconn.persistence.entities;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.NamedQuery;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;


/**
 * The persistent class for the timesheet_allocation database table.
 *
 */
@Entity
@Table(name="timesheet_allocation")
@NamedQuery(name="TimesheetAllocation.findAll", query="SELECT s FROM TimesheetAllocation s")
public class TimesheetAllocation extends Auditable<Long> implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	@SequenceGenerator(name="TIMESHEET_TIMESHEETALLOCATIONTID_GENERATOR", sequenceName="TIMESHEET_ALLOCATION_ID_SEQUENCE", allocationSize = 1)
	@GeneratedValue(strategy=GenerationType.SEQUENCE, generator="TIMESHEET_TIMESHEETALLOCATIONTID_GENERATOR")
	@Column(name="timesheet_allocation_id")
	private Long timesheetAllocationId;

	@Column(name="allocated_percentage")
	private Integer allocatedPercentage;

	@Column(name="allocation_id")
	private Long allocationId;

	public TimesheetAllocation() {
	}


	public Long getTimesheetAllocationId() {
		return this.timesheetAllocationId;
	}

	public void setTimesheetAllocationId(Long timesheetAllocationId) {
		this.timesheetAllocationId = timesheetAllocationId;
	}

	public Integer getAllocatedPercentage() {
		return this.allocatedPercentage;
	}

	public void setAllocatedPercentage(Integer allocatedPercentage) {
		this.allocatedPercentage = allocatedPercentage;
	}

	public Long getAllocationId() {
		return this.allocationId;
	}

	public void setAllocationId(Long allocationId) {
		this.allocationId = allocationId;
	}

}