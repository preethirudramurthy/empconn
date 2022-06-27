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
 * The persistent class for the sync_project database table.
 *
 */
@Entity
@Table(name="sync_project")
@NamedQuery(name="SyncProject.findAll", query="SELECT s FROM SyncProject s")
public class SyncProject implements Serializable {
	private static final long serialVersionUID = 1L;

	public SyncProject(Long createdBy, String status, Project project) {
		super();
		this.setCreatedBy(createdBy);
		this.setCreatedOn(TimeUtils.getCreatedOn());
		this.setIsActive(true);
		this.status = status;
		this.project = project;
	}

	public SyncProject(Long createdBy, String status, Project project, Boolean isActive) {
		super();
		this.setCreatedBy(createdBy);
		this.setCreatedOn(TimeUtils.getCreatedOn());
		this.setIsActive(isActive);
		this.status = status;
		this.project = project;
	}


	@Id
	@SequenceGenerator(name="SYNC_PROJECT_SYNCPROJECTID_GENERATOR", sequenceName="SYNC_PROJECT_ID_SEQUENCE", allocationSize = 1)
	@GeneratedValue(strategy=GenerationType.SEQUENCE, generator="SYNC_PROJECT_SYNCPROJECTID_GENERATOR")
	@Column(name="sync_project_id")
	private Long syncProjectId;

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

	//bi-directional many-to-one association to Project
	@ManyToOne
	@JoinColumn(name="project_id")
	private Project project;

	public SyncProject() {
	}

	public Long getSyncProjectId() {
		return this.syncProjectId;
	}

	public void setSyncProjectId(Long syncProjectId) {
		this.syncProjectId = syncProjectId;
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

}