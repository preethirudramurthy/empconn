package com.empconn.persistence.entities;

import java.io.Serializable;
import java.util.List;
import java.util.Set;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.NamedQuery;
import javax.persistence.OneToMany;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;

import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

/**
 * The persistent class for the work_group database table.
 *
 */
@Entity
@Table(name="work_group")
@NamedQuery(name="WorkGroup.findAll", query="SELECT w FROM WorkGroup w order by hierarchy")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
public class WorkGroup extends Auditable<Long> implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	@SequenceGenerator(name="WORK_GROUP_WORKGROUPID_GENERATOR", sequenceName="WORK_GROUP_ID_SEQUENCE")
	@GeneratedValue(strategy=GenerationType.SEQUENCE, generator="WORK_GROUP_WORKGROUPID_GENERATOR")
	@Column(name="work_group_id")
	private Integer workGroupId;

	private String name;

	private Integer hierarchy;

	//bi-directional many-to-one association to Allocation
	@OneToMany(mappedBy="workGroup")
	private Set<Allocation> allocations;

	//bi-directional many-to-one association to SyncProjectManager
	@OneToMany(mappedBy="workGroup")
	private List<SyncProjectManager> syncProjectManagers;

	public WorkGroup() {
	}

	public Integer getWorkGroupId() {
		return this.workGroupId;
	}

	public void setWorkGroupId(Integer workGroupId) {
		this.workGroupId = workGroupId;
	}

	public String getName() {
		return this.name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public Integer getHierarchy() {
		return hierarchy;
	}

	public void setHierarchy(Integer hierarchy) {
		this.hierarchy = hierarchy;
	}

	public Set<Allocation> getAllocations() {
		return this.allocations;
	}

	public void setAllocations(Set<Allocation> allocations) {
		this.allocations = allocations;
	}

	public Allocation addAllocation(Allocation allocation) {
		getAllocations().add(allocation);
		allocation.setWorkGroup(this);

		return allocation;
	}

	public Allocation removeAllocation(Allocation allocation) {
		getAllocations().remove(allocation);
		allocation.setWorkGroup(null);

		return allocation;
	}

	public List<SyncProjectManager> getSyncProjectManagers() {
		return this.syncProjectManagers;
	}

	public void setSyncProjectManagers(List<SyncProjectManager> syncProjectManagers) {
		this.syncProjectManagers = syncProjectManagers;
	}

	public SyncProjectManager addSyncProjectManager(SyncProjectManager syncProjectManager) {
		getSyncProjectManagers().add(syncProjectManager);
		syncProjectManager.setWorkGroup(this);

		return syncProjectManager;
	}

	public SyncProjectManager removeSyncProjectManager(SyncProjectManager syncProjectManager) {
		getSyncProjectManagers().remove(syncProjectManager);
		syncProjectManager.setWorkGroup(null);

		return syncProjectManager;
	}
}
