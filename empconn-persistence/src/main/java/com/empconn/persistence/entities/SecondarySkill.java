package com.empconn.persistence.entities;

import java.io.Serializable;
import java.util.Set;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQuery;
import javax.persistence.OneToMany;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;

import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;


/**
 * The persistent class for the secondary_skill database table.
 *
 */
@Entity
@Table(name="secondary_skill")
@NamedQuery(name="SecondarySkill.findAll", query="SELECT s FROM SecondarySkill s")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
public class SecondarySkill extends Auditable<Long> implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	@SequenceGenerator(name="SECONDARY_SKILL_SECONDARYSKILLID_GENERATOR", sequenceName="SECONDARY_SKILL_ID_SEQUENCE", allocationSize = 1)
	@GeneratedValue(strategy=GenerationType.SEQUENCE, generator="SECONDARY_SKILL_SECONDARYSKILLID_GENERATOR")
	@Column(name="secondary_skill_id")
	private Integer secondarySkillId;

	private String name;

	//bi-directional many-to-one association to ProjectResourcesSecondarySkill
	@OneToMany(mappedBy="secondarySkill")
	private Set<ProjectResourcesSecondarySkill> projectResourcesSecondarySkills;

	//bi-directional many-to-one association to PrimarySkill
	@ManyToOne
	@JoinColumn(name="primary_skill_id")
	private PrimarySkill primarySkill;

	public SecondarySkill() {
	}

	public Integer getSecondarySkillId() {
		return this.secondarySkillId;
	}

	public void setSecondarySkillId(Integer secondarySkillId) {
		this.secondarySkillId = secondarySkillId;
	}

	public String getName() {
		return this.name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public Set<ProjectResourcesSecondarySkill> getProjectResourcesSecondarySkills() {
		return this.projectResourcesSecondarySkills;
	}

	public void setProjectResourcesSecondarySkills(Set<ProjectResourcesSecondarySkill> projectResourcesSecondarySkills) {
		this.projectResourcesSecondarySkills = projectResourcesSecondarySkills;
	}

	public ProjectResourcesSecondarySkill addProjectResourcesSecondarySkill(ProjectResourcesSecondarySkill projectResourcesSecondarySkill) {
		getProjectResourcesSecondarySkills().add(projectResourcesSecondarySkill);
		projectResourcesSecondarySkill.setSecondarySkill(this);

		return projectResourcesSecondarySkill;
	}

	public ProjectResourcesSecondarySkill removeProjectResourcesSecondarySkill(ProjectResourcesSecondarySkill projectResourcesSecondarySkill) {
		getProjectResourcesSecondarySkills().remove(projectResourcesSecondarySkill);
		projectResourcesSecondarySkill.setSecondarySkill(null);

		return projectResourcesSecondarySkill;
	}

	public PrimarySkill getPrimarySkill() {
		return this.primarySkill;
	}

	public void setPrimarySkill(PrimarySkill primarySkill) {
		this.primarySkill = primarySkill;
	}

}