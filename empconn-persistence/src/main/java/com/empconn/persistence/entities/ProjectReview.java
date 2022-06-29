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
 * The persistent class for the project_review database table.
 *
 */
@Entity
@Table(name="project_review")
@NamedQuery(name="ProjectReview.findAll", query="SELECT p FROM ProjectReview p")
public class ProjectReview extends Auditable<Long> implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	@SequenceGenerator(name="PROJECT_REVIEW_PROJECTREVIEWID_GENERATOR", sequenceName="PROJECT_REVIEW_ID_SEQUENCE", allocationSize = 1)
	@GeneratedValue(strategy=GenerationType.SEQUENCE, generator="PROJECT_REVIEW_PROJECTREVIEWID_GENERATOR")
	@Column(name="project_review_id")
	private Long projectReviewId;

	private String comment;

	private String status;

	//bi-directional many-to-one association to Project
	@ManyToOne
	@JoinColumn(name="project_id")
	private Project project;

	public Long getProjectReviewId() {
		return this.projectReviewId;
	}

	public void setProjectReviewId(Long projectReviewId) {
		this.projectReviewId = projectReviewId;
	}

	public String getComment() {
		return this.comment;
	}

	public void setComment(String comment) {
		this.comment = comment;
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