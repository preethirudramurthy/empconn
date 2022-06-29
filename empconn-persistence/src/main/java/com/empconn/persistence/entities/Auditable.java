package com.empconn.persistence.entities;

import java.sql.Timestamp;

import javax.persistence.Column;
import javax.persistence.MappedSuperclass;

import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

@MappedSuperclass
//@EntityListeners(AuditingEntityListener.class)
public class Auditable<U> {

	// @CreatedBy
	@Column(name = "created_by")
	private U createdBy;

	@CreationTimestamp
	@Column(name = "created_on")
	private Timestamp createdOn;

	// @LastModifiedBy
	@Column(name = "modified_by")
	private U modifiedBy;

	@UpdateTimestamp
	@Column(name = "modified_on")
	private Timestamp modifiedOn;

	@Column(name = "is_active")
	private boolean isActive;

	public U getCreatedBy() {
		return this.createdBy;
	}

	public void setCreatedBy(U createdBy) {
		this.createdBy = createdBy;
	}

	public Timestamp getCreatedOn() {
		return this.createdOn;
	}

	public void setCreatedOn(Timestamp createdOn) {
		this.createdOn = createdOn;
	}

	public U getModifiedBy() {
		return this.modifiedBy;
	}

	public void setModifiedBy(U modifiedBy) {
		this.modifiedBy = modifiedBy;
	}

	public Timestamp getModifiedOn() {
		return this.modifiedOn;
	}

	public void setModifiedOn(Timestamp modifiedOn) {
		this.modifiedOn = modifiedOn;
	}

	public boolean getIsActive() {
		return isActive;
	}

	public void setIsActive(boolean isActive) {
		this.isActive = isActive;
	}

}
