package com.empconn.persistence.entities;

import java.io.Serializable;

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
 * The persistent class for the allocation_feedback database table.
 *
 */
@Entity
@Table(name = "allocation_feedback")
@NamedQuery(name = "AllocationFeedback.findAll", query = "SELECT a FROM AllocationFeedback a")
public class AllocationFeedback extends Auditable<Long> implements Serializable {
	private static final long serialVersionUID = 1L;

	public AllocationFeedback(String softSkillFeedback, Integer softSkillRating, String techFeedback,
			Integer techRating, Allocation allocation, Long createdBy, Boolean isActive) {
		super();
		this.softSkillFeedback = softSkillFeedback;
		this.softSkillRating = softSkillRating;
		this.techFeedback = techFeedback;
		this.techRating = techRating;
		this.allocation = allocation;
		this.setCreatedBy(createdBy);
		this.setIsActive(isActive);
	}

	@Id
	@SequenceGenerator(name = "ALLOCATION_ALLOCATIONFEEDBACKID_GENERATOR", sequenceName = "ALLOCATION_FEEDBACK_ID_SEQUENCE", allocationSize = 1)
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "ALLOCATION_ALLOCATIONFEEDBACKID_GENERATOR")
	@Column(name = "allocation_feedback_id")
	private Long allocationFeedbackId;

	@Column(name = "soft_skill_feedback")
	private String softSkillFeedback;

	@Column(name = "soft_skill_rating")
	private Integer softSkillRating;

	@Column(name = "tech_feedback")
	private String techFeedback;

	@Column(name = "tech_rating")
	private Integer techRating;

	// bi-directional many-to-one association to Allocation
	@ManyToOne
	@JoinColumn(name = "allocation_id")
	private Allocation allocation;

	public AllocationFeedback() {
	}

	public Long getAllocationFeedbackId() {
		return this.allocationFeedbackId;
	}

	public void setAllocationFeedbackId(Long allocationFeedbackId) {
		this.allocationFeedbackId = allocationFeedbackId;
	}

	public String getSoftSkillFeedback() {
		return this.softSkillFeedback;
	}

	public void setSoftSkillFeedback(String softSkillFeedback) {
		this.softSkillFeedback = softSkillFeedback;
	}

	public Integer getSoftSkillRating() {
		return this.softSkillRating;
	}

	public void setSoftSkillRating(Integer softSkillRating) {
		this.softSkillRating = softSkillRating;
	}

	public String getTechFeedback() {
		return this.techFeedback;
	}

	public void setTechFeedback(String techFeedback) {
		this.techFeedback = techFeedback;
	}

	public Integer getTechRating() {
		return this.techRating;
	}

	public void setTechRating(Integer techRating) {
		this.techRating = techRating;
	}

	public Allocation getAllocation() {
		return this.allocation;
	}

	public void setAllocation(Allocation allocation) {
		this.allocation = allocation;
	}

	public void deactivate() {
		setIsActive(false);
	}

}