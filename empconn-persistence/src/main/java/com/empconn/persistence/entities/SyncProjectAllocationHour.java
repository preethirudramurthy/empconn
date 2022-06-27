package com.empconn.persistence.entities;

import java.io.Serializable;
import java.sql.Timestamp;

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


/**
 * The persistent class for the sync_project_allocation database table.
 *
 */
@Entity
@Table(name="sync_project_allocation_hours")
@NamedQuery(name="SyncProjectAllocationHour.findAll", query="SELECT s FROM SyncProjectAllocationHour s")
public class SyncProjectAllocationHour extends Auditable<Long> implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	@SequenceGenerator(name="SYNC_PROJECT_SYNCPROJECTALLOCATIONHOURID_GENERATOR", sequenceName="SYNC_PROJECT_ALLOCATION_HOURS_ID_SEQUENCE", allocationSize = 1)
	@GeneratedValue(strategy=GenerationType.SEQUENCE, generator="SYNC_PROJECT_SYNCPROJECTALLOCATIONHOURID_GENERATOR")
	@Column(name="sync_project_allocation_hours_id")
	private Long syncProjectAllocationHoursId;

	@Column(name="created_by")
	private Long createdBy;

	@Column(name="created_on")
	private Timestamp createdOn;

	@Column(name="is_active")
	private Boolean isActive;

	@Column(name="modified_by")
	private Long modifiedBy;

	@Column(name="modified_on")
	private Timestamp modifiedOn;

	private String status;

	//bi-directional many-to-one association to Allocation
	@ManyToOne
	@JoinColumn(name="allocation_id")
	private Allocation allocation;

	public SyncProjectAllocationHour() {
	}



	public SyncProjectAllocationHour(Long createdBy, Long modifiedBy, Allocation allocation) {
		super();
		this.createdBy = createdBy;
		this.modifiedBy = modifiedBy;
		this.allocation = allocation;
		this.status = "Pending";
		setIsActive(true);
	}



	public Long getSyncProjectAllocationHoursId() {
		return this.syncProjectAllocationHoursId;
	}

	public void setSyncProjectAllocationHoursId(Long syncProjectAllocationHoursId) {
		this.syncProjectAllocationHoursId = syncProjectAllocationHoursId;
	}

	@Override
	public Long getCreatedBy() {
		return this.createdBy;
	}

	@Override
	public void setCreatedBy(Long createdBy) {
		this.createdBy = createdBy;
	}

	@Override
	public Timestamp getCreatedOn() {
		return this.createdOn;
	}

	@Override
	public void setCreatedOn(Timestamp createdOn) {
		this.createdOn = createdOn;
	}

	@Override
	public Boolean getIsActive() {
		return this.isActive;
	}

	@Override
	public void setIsActive(Boolean isActive) {
		this.isActive = isActive;
	}

	@Override
	public Long getModifiedBy() {
		return this.modifiedBy;
	}

	@Override
	public void setModifiedBy(Long modifiedBy) {
		this.modifiedBy = modifiedBy;
	}

	@Override
	public Timestamp getModifiedOn() {
		return this.modifiedOn;
	}

	@Override
	public void setModifiedOn(Timestamp modifiedOn) {
		this.modifiedOn = modifiedOn;
	}

	public String getStatus() {
		return this.status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public Allocation getAllocation() {
		return this.allocation;
	}

	public void setAllocation(Allocation allocation) {
		this.allocation = allocation;
	}

}