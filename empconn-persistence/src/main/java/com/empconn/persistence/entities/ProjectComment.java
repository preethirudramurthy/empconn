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
 * The persistent class for the project_comment database table.
 * 
 */
@Entity
@Table(name = "project_comment")
@NamedQuery(name = "ProjectComment.findAll", query = "SELECT p FROM ProjectComment p")
public class ProjectComment extends Auditable<Long> implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	@SequenceGenerator(name = "PROJECT_COMMENT_PROJECTCOMMENTID_GENERATOR", sequenceName = "PROJECT_COMMENT_ID_SEQUENCE", allocationSize = 1)
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "PROJECT_COMMENT_PROJECTCOMMENTID_GENERATOR")
	@Column(name = "project_comment_id")
	private Long projectCommentId;

	private String status;

	private String value;

	// bi-directional many-to-one association to Project
	@ManyToOne
	@JoinColumn(name = "project_id")
	private Project project;

	public Long getProjectCommentId() {
		return this.projectCommentId;
	}

	public void setProjectCommentId(Long projectCommentId) {
		this.projectCommentId = projectCommentId;
	}

	public String getStatus() {
		return this.status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public String getValue() {
		return this.value;
	}

	public void setValue(String value) {
		this.value = value;
	}

	public Project getProject() {
		return this.project;
	}

	public void setProject(Project project) {
		this.project = project;
	}

}