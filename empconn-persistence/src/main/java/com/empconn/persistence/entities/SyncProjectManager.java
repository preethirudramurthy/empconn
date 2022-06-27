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

import com.empconn.utilities.TimeUtils;


/**
 * The persistent class for the sync_project_manager database table.
 *
 */
@Entity
@Table(name="sync_project_manager")
@NamedQuery(name="SyncProjectManager.findAll", query="SELECT s FROM SyncProjectManager s")
public class SyncProjectManager implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	@SequenceGenerator(name="SYNC_PROJECT_SYNCPROJECMANAGERTID_GENERATOR", sequenceName="SYNC_PROJECT_MANAGER_ID_SEQUENCE", allocationSize = 1)
	@GeneratedValue(strategy=GenerationType.SEQUENCE, generator="SYNC_PROJECT_SYNCPROJECMANAGERTID_GENERATOR")
	@Column(name="sync_project_manager_id")
	private Long syncProjectManagerId;

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

	//bi-directional many-to-one association to Employee
	@ManyToOne
	@JoinColumn(name="manager_id")
	private Employee managerId;

	private String status;

	//bi-directional many-to-one association to Project
	@ManyToOne
	@JoinColumn(name="project_id")
	private Project project;

	//bi-directional many-to-one association to ProjectLocation
	@ManyToOne
	@JoinColumn(name="project_location_id")
	private ProjectLocation projectLocation;

	//bi-directional many-to-one association to WorkGroup
	@ManyToOne
	@JoinColumn(name="work_group_id")
	private WorkGroup workGroup;

	public SyncProjectManager() {
	}


	public SyncProjectManager(Long createdBy, Employee managerId,
			Project project, ProjectLocation projectLocation, WorkGroup workGroup, String status) {
		super();
		this.createdBy = createdBy;
		setIsActive(true);
		this.managerId = managerId;
		this.project = project;
		this.projectLocation = projectLocation;
		this.workGroup = workGroup;
		this.status = status;
		this.setCreatedOn(TimeUtils.getCreatedOn());
	}


	public Long getSyncProjectManagerId() {
		return this.syncProjectManagerId;
	}

	public void setSyncProjectManagerId(Long syncProjectManagerId) {
		this.syncProjectManagerId = syncProjectManagerId;
	}

	public Long getCreatedBy() {
		return this.createdBy;
	}

	public void setCreatedBy(Long createdBy) {
		this.createdBy = createdBy;
	}

	public Timestamp getCreatedOn() {
		return this.createdOn;
	}

	public void setCreatedOn(Timestamp createdOn) {
		this.createdOn = createdOn;
	}

	public Boolean getIsActive() {
		return this.isActive;
	}

	public void setIsActive(Boolean isActive) {
		this.isActive = isActive;
	}

	public Long getModifiedBy() {
		return this.modifiedBy;
	}

	public void setModifiedBy(Long modifiedBy) {
		this.modifiedBy = modifiedBy;
	}

	public Timestamp getModifiedOn() {
		return this.modifiedOn;
	}

	public void setModifiedOn(Timestamp modifiedOn) {
		this.modifiedOn = modifiedOn;
	}

	public Employee getManagerId() {
		return this.managerId;
	}

	public void setManagerId(Employee managerId) {
		this.managerId = managerId;
	}

	public String getStatus() {
		return this.status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public Project getProject() {
		return this.project;
	}

	public void setProject(Project project) {
		this.project = project;
	}

	public ProjectLocation getProjectLocation() {
		return this.projectLocation;
	}

	public void setProjectLocation(ProjectLocation projectLocation) {
		this.projectLocation = projectLocation;
	}

	public WorkGroup getWorkGroup() {
		return this.workGroup;
	}

	public void setWorkGroup(WorkGroup workGroup) {
		this.workGroup = workGroup;
	}

}