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
 * The persistent class for the project_resources_secondary_skill database table.
 *
 */
@Entity
@Table(name="project_resources_secondary_skill")
@NamedQuery(name="ProjectResourcesSecondarySkill.findAll", query="SELECT p FROM ProjectResourcesSecondarySkill p")
public class ProjectResourcesSecondarySkill extends Auditable<Long> implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	@SequenceGenerator(name="PROJECT_RESOURCES_SECONDARY_SKILL_PROJECTRESOURCESSECONDARYSKILLID_GENERATOR", sequenceName="PROJECT_RESOURCES_SECONDARY_SKILL_ID_SEQUENCE", allocationSize = 1)
	@GeneratedValue(strategy=GenerationType.SEQUENCE, generator="PROJECT_RESOURCES_SECONDARY_SKILL_PROJECTRESOURCESSECONDARYSKILLID_GENERATOR")
	@Column(name="project_resources_secondary_skill_id")
	private Long projectResourcesSecondarySkillId;

	//bi-directional many-to-one association to ProjectResource
	@ManyToOne
	@JoinColumn(name="project_resources_id")
	private ProjectResource projectResource;

	//bi-directional many-to-one association to SecondarySkill
	@ManyToOne
	@JoinColumn(name="secondary_skill_id")
	private SecondarySkill secondarySkill;


	public Long getProjectResourcesSecondarySkillId() {
		return this.projectResourcesSecondarySkillId;
	}

	public void setProjectResourcesSecondarySkillId(Long projectResourcesSecondarySkillId) {
		this.projectResourcesSecondarySkillId = projectResourcesSecondarySkillId;
	}

	public ProjectResource getProjectResource() {
		return this.projectResource;
	}

	public void setProjectResource(ProjectResource projectResource) {
		this.projectResource = projectResource;
	}

	public SecondarySkill getSecondarySkill() {
		return this.secondarySkill;
	}

	public void setSecondarySkill(SecondarySkill secondarySkill) {
		this.secondarySkill = secondarySkill;
	}

}