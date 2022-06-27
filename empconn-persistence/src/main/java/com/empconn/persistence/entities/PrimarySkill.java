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
import javax.persistence.Table;

import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;


/**
 * The persistent class for the primary_skill database table.
 *
 */
@Entity
@Table(name="primary_skill")
@NamedQuery(name="PrimarySkill.findAll", query="SELECT p FROM PrimarySkill p")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
public class PrimarySkill extends Auditable<Long> implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	@SequenceGenerator(name="PRIMARY_SKILL_PRIMARYSKILLID_GENERATOR", sequenceName="PRIMARY_SKILL_ID_SEQUENCE", allocationSize = 1)
	@GeneratedValue(strategy=GenerationType.SEQUENCE, generator="PRIMARY_SKILL_PRIMARYSKILLID_GENERATOR")
	@Column(name="primary_skill_id")
	private Integer primarySkillId;

	private String name;

	//bi-directional many-to-one association to ProjectResource
	@OneToMany(mappedBy="primarySkill")
	private Set<ProjectResource> projectResources;

	//bi-directional many-to-one association to SecondarySkill
	@OneToMany(mappedBy="primarySkill")
	private Set<SecondarySkill> secondarySkills;

	public PrimarySkill() {
	}

	public Integer getPrimarySkillId() {
		return this.primarySkillId;
	}

	public void setPrimarySkillId(Integer primarySkillId) {
		this.primarySkillId = primarySkillId;
	}

	public String getName() {
		return this.name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public Set<ProjectResource> getProjectResources() {
		return this.projectResources;
	}

	public void setProjectResources(Set<ProjectResource> projectResources) {
		this.projectResources = projectResources;
	}

	public ProjectResource addProjectResource(ProjectResource projectResource) {
		getProjectResources().add(projectResource);
		projectResource.setPrimarySkill(this);

		return projectResource;
	}

	public ProjectResource removeProjectResource(ProjectResource projectResource) {
		getProjectResources().remove(projectResource);
		projectResource.setPrimarySkill(null);

		return projectResource;
	}

	public Set<SecondarySkill> getSecondarySkills() {
		return this.secondarySkills;
	}

	public void setSecondarySkills(Set<SecondarySkill> secondarySkills) {
		this.secondarySkills = secondarySkills;
	}

	public SecondarySkill addSecondarySkill(SecondarySkill secondarySkill) {
		getSecondarySkills().add(secondarySkill);
		secondarySkill.setPrimarySkill(this);

		return secondarySkill;
	}

	public SecondarySkill removeSecondarySkill(SecondarySkill secondarySkill) {
		getSecondarySkills().remove(secondarySkill);
		secondarySkill.setPrimarySkill(null);

		return secondarySkill;
	}

}