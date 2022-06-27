package com.empconn.persistence.entities;

import java.io.Serializable;
import java.util.Set;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.NamedQuery;
import javax.persistence.OneToMany;
import javax.persistence.SequenceGenerator;


/**
 * The persistent class for the checklist database table.
 *
 */
@Entity
@NamedQuery(name="Checklist.findAll", query="SELECT c FROM Checklist c")
public class Checklist extends Auditable<Long> implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	@SequenceGenerator(name="CHECKLIST_CHECKLISTID_GENERATOR", sequenceName="CHECKLIST_ID_SEQUENCE", allocationSize = 1)
	@GeneratedValue(strategy=GenerationType.SEQUENCE, generator="CHECKLIST_CHECKLISTID_GENERATOR")
	@Column(name="checklist_id")
	private Integer checklistId;

	private String name;

	//bi-directional many-to-one association to ProjectChecklist
	@OneToMany(mappedBy="checklist")
	private Set<ProjectChecklist> projectChecklists;

	public Checklist() {
	}

	public Integer getChecklistId() {
		return this.checklistId;
	}

	public void setChecklistId(Integer checklistId) {
		this.checklistId = checklistId;
	}

	public String getName() {
		return this.name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public Set<ProjectChecklist> getProjectChecklists() {
		return this.projectChecklists;
	}

	public void setProjectChecklists(Set<ProjectChecklist> projectChecklists) {
		this.projectChecklists = projectChecklists;
	}

	public ProjectChecklist addProjectChecklist(ProjectChecklist projectChecklist) {
		getProjectChecklists().add(projectChecklist);
		projectChecklist.setChecklist(this);

		return projectChecklist;
	}

	public ProjectChecklist removeProjectChecklist(ProjectChecklist projectChecklist) {
		getProjectChecklists().remove(projectChecklist);
		projectChecklist.setChecklist(null);

		return projectChecklist;
	}

}