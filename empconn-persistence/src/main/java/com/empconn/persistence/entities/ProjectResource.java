package com.empconn.persistence.entities;

import java.io.Serializable;
import java.util.Date;
import java.util.Set;

import javax.persistence.CascadeType;
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
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

import org.hibernate.annotations.Where;

/**
 * The persistent class for the project_resources database table.
 *
 */
@Entity
@Table(name = "project_resources")
@NamedQuery(name = "ProjectResource.findAll", query = "SELECT p FROM ProjectResource p")
public class ProjectResource extends Auditable<Long> implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	@SequenceGenerator(name = "PROJECT_RESOURCES_PROJECTRESOURCESID_GENERATOR", sequenceName = "PROJECT_RESOURCES_ID_SEQUENCE", allocationSize = 1)
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "PROJECT_RESOURCES_PROJECTRESOURCESID_GENERATOR")
	@Column(name = "project_resources_id")
	private Long projectResourcesId;

	@Column(name = "allocation_percentage")
	private Integer allocationPercentage;

	@Temporal(TemporalType.DATE)
	@Column(name = "end_date")
	private Date endDate;

	@Column(name = "is_billable")
	private Boolean isBillable;

	@Column(name = "number_of_resources")
	private Integer numberOfResources;

	@Temporal(TemporalType.DATE)
	@Column(name = "start_date")
	private Date startDate;

	// bi-directional many-to-one association to PrimarySkill
	@ManyToOne
	@JoinColumn(name = "primary_skill_id")
	private PrimarySkill primarySkill;

	// bi-directional many-to-one association to ProjectLocation
	@ManyToOne
	@JoinColumn(name = "project_location_id")
	private ProjectLocation projectLocation;

	// bi-directional many-to-one association to Title
	@ManyToOne
	@JoinColumn(name = "title_id")
	private Title title;

	// bi-directional many-to-one association to ProjectResourcesSecondarySkill
	@OneToMany(mappedBy = "projectResource", cascade = CascadeType.ALL)
	@Where(clause = "is_active = true")
	private Set<ProjectResourcesSecondarySkill> projectResourcesSecondarySkills;

	public ProjectResource() {
	}

	public Long getProjectResourcesId() {
		return this.projectResourcesId;
	}

	public void setProjectResourcesId(Long projectResourcesId) {
		this.projectResourcesId = projectResourcesId;
	}

	public Integer getAllocationPercentage() {
		return this.allocationPercentage;
	}

	public void setAllocationPercentage(Integer allocationPercentage) {
		this.allocationPercentage = allocationPercentage;
	}

	public Date getEndDate() {
		return this.endDate;
	}

	public void setEndDate(Date endDate) {
		this.endDate = endDate;
	}

	public Boolean getIsBillable() {
		return this.isBillable;
	}

	public void setIsBillable(Boolean isBillable) {
		this.isBillable = isBillable;
	}

	public Integer getNumberOfResources() {
		return this.numberOfResources;
	}

	public void setNumberOfResources(Integer numberOfResources) {
		this.numberOfResources = numberOfResources;
	}

	public Date getStartDate() {
		return this.startDate;
	}

	public void setStartDate(Date startDate) {
		this.startDate = startDate;
	}

	public PrimarySkill getPrimarySkill() {
		return this.primarySkill;
	}

	public void setPrimarySkill(PrimarySkill primarySkill) {
		this.primarySkill = primarySkill;
	}

	public ProjectLocation getProjectLocation() {
		return this.projectLocation;
	}

	public void setProjectLocation(ProjectLocation projectLocation) {
		this.projectLocation = projectLocation;
	}

	public Title getTitle() {
		return this.title;
	}

	public void setTitle(Title title) {
		this.title = title;
	}

	public Set<ProjectResourcesSecondarySkill> getProjectResourcesSecondarySkills() {
		return this.projectResourcesSecondarySkills;
	}

	public void setProjectResourcesSecondarySkills(
			Set<ProjectResourcesSecondarySkill> projectResourcesSecondarySkills) {
		this.projectResourcesSecondarySkills = projectResourcesSecondarySkills;
	}

	public ProjectResourcesSecondarySkill addProjectResourcesSecondarySkill(
			ProjectResourcesSecondarySkill projectResourcesSecondarySkill) {
		getProjectResourcesSecondarySkills().add(projectResourcesSecondarySkill);
		projectResourcesSecondarySkill.setProjectResource(this);

		return projectResourcesSecondarySkill;
	}

	public ProjectResourcesSecondarySkill removeProjectResourcesSecondarySkill(
			ProjectResourcesSecondarySkill projectResourcesSecondarySkill) {
		getProjectResourcesSecondarySkills().remove(projectResourcesSecondarySkill);
		projectResourcesSecondarySkill.setProjectResource(null);

		return projectResourcesSecondarySkill;
	}

}