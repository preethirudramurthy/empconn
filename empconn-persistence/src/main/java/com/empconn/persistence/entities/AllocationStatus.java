package com.empconn.persistence.entities;

import java.io.Serializable;
import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.NamedQuery;
import javax.persistence.OneToMany;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;


/**
 * The persistent class for the allocation_status database table.
 *
 */
@Entity
@Table(name="allocation_status")
@NamedQuery(name="AllocationStatus.findAll", query="SELECT a FROM AllocationStatus a")
public class AllocationStatus extends Auditable<Long> implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	@SequenceGenerator(name="ALLOCATION_STATUS_ALLOCATIONSTATUSID_GENERATOR", sequenceName="ALLOCATION_STATUS_ID_SEQUENCE", allocationSize = 1)
	@GeneratedValue(strategy=GenerationType.SEQUENCE, generator="ALLOCATION_STATUS_ALLOCATIONSTATUSID_GENERATOR")
	@Column(name="allocation_status_id")
	private Long allocationStatusId;

	private String status;

	//bi-directional many-to-one association to Allocation
	@OneToMany(mappedBy="allocationStatus")
	private List<Allocation> allocations;

	public Long getAllocationStatusId() {
		return this.allocationStatusId;
	}

	public void setAllocationStatusId(Long allocationStatusId) {
		this.allocationStatusId = allocationStatusId;
	}

	public String getStatus() {
		return this.status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public List<Allocation> getAllocations() {
		return this.allocations;
	}

	public void setAllocations(List<Allocation> allocations) {
		this.allocations = allocations;
	}

	public Allocation addAllocation(Allocation allocation) {
		getAllocations().add(allocation);
		allocation.setAllocationStatus(this);

		return allocation;
	}

	public Allocation removeAllocation(Allocation allocation) {
		getAllocations().remove(allocation);
		allocation.setAllocationStatus(null);

		return allocation;
	}

}
