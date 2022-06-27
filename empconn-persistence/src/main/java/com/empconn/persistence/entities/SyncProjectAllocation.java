package com.empconn.persistence.entities;

import java.io.Serializable;
import java.sql.Timestamp;
import java.time.LocalDate;
import java.util.Date;

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
import javax.persistence.Temporal;
import javax.persistence.TemporalType;


/**
 * The persistent class for the sync_project_allocation database table.
 *
 */
@Entity
@Table(name="sync_project_allocation")
@NamedQuery(name="SyncProjectAllocation.findAll", query="SELECT s FROM SyncProjectAllocation s")
public class SyncProjectAllocation extends Auditable<Long> implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	@SequenceGenerator(name="SYNC_PROJECT_SYNCPROJECTALLOCATIONTID_GENERATOR", sequenceName="SYNC_PROJECT_ALLOCATION_ID_SEQUENCE", allocationSize = 1)
	@GeneratedValue(strategy=GenerationType.SEQUENCE, generator="SYNC_PROJECT_SYNCPROJECTALLOCATIONTID_GENERATOR")
	@Column(name="sync_project_allocation_id")
	private Long syncProjectAllocationId;

	@Temporal(TemporalType.DATE)
	@Column(name="date_of_movement")
	private Date dateOfMovement;

	private String status;

	//bi-directional many-to-one association to Allocation
	@ManyToOne
	@JoinColumn(name="allocation_id")
	private Allocation allocation;

	public SyncProjectAllocation() {
	}

	public SyncProjectAllocation(Long createdBy, Long modifiedBy, LocalDate dateOfMovement,
			Allocation allocation) {
		super();
		setCreatedBy(createdBy);
		setModifiedBy(modifiedBy);
		this.dateOfMovement = dateOfMovement != null?Timestamp.valueOf(dateOfMovement.atStartOfDay()):null;
		this.allocation = allocation;
		setStatus("Pending");
		setIsActive(true);
	}

	public SyncProjectAllocation(Long createdBy, Long modifiedBy, Date dateOfMovement,
			Allocation allocation) {
		super();
		setCreatedBy(createdBy);
		setModifiedBy(modifiedBy);
		this.dateOfMovement = dateOfMovement != null?new Timestamp(dateOfMovement.getTime()):null;
		this.allocation = allocation;
		setStatus("Pending");
		setIsActive(true);
	}

	public Long getSyncProjectAllocationId() {
		return this.syncProjectAllocationId;
	}

	public void setSyncProjectAllocationId(Long syncProjectAllocationId) {
		this.syncProjectAllocationId = syncProjectAllocationId;
	}

	public Date getDateOfMovement() {
		return this.dateOfMovement;
	}

	public void setDateOfMovement(Date dateOfMovement) {
		this.dateOfMovement = dateOfMovement;
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