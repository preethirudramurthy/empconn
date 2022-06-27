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
 * The persistent class for the project_checklist database table.
 *
 */
@Entity
@Table(name="project_checklist")
@NamedQuery(name="ProjectChecklist.findAll", query="SELECT p FROM ProjectChecklist p")
public class ProjectChecklist extends Auditable<Long> implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	@SequenceGenerator(name="PROJECT_CHECKLIST_PROJECTCHECKLISTID_GENERATOR", sequenceName="PROJECT_CHECKLIST_ID_SEQUENCE", allocationSize = 1)
	@GeneratedValue(strategy=GenerationType.SEQUENCE, generator="PROJECT_CHECKLIST_PROJECTCHECKLISTID_GENERATOR")
	@Column(name="project_checklist_id")
	private Long projectChecklistId;

	private String comment;

	@Column(name="is_selected")
	private Boolean isSelected;

	//bi-directional many-to-one association to Checklist
	@ManyToOne
	@JoinColumn(name="checklist_id")
	private Checklist checklist;

	//bi-directional many-to-one association to Project
	@ManyToOne
	@JoinColumn(name="project_id")
	private Project project;

	public ProjectChecklist() {
	}

	public Long getProjectChecklistId() {
		return this.projectChecklistId;
	}

	public void setProjectChecklistId(Long projectChecklistId) {
		this.projectChecklistId = projectChecklistId;
	}

	public String getComment() {
		return this.comment;
	}

	public void setComment(String comment) {
		this.comment = comment;
	}

	public Boolean getIsSelected() {
		return this.isSelected;
	}

	public void setIsSelected(Boolean isSelected) {
		this.isSelected = isSelected;
	}

	public Checklist getChecklist() {
		return this.checklist;
	}

	public void setChecklist(Checklist checklist) {
		this.checklist = checklist;
	}

	public Project getProject() {
		return this.project;
	}

	public void setProject(Project project) {
		this.project = project;
	}

}