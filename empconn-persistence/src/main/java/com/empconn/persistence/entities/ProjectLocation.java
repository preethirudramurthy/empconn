package com.empconn.persistence.entities;

import java.io.Serializable;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
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
import org.hibernate.annotations.Where;

/**
 * The persistent class for the project_location database table.
 *
 */
@Entity
@Table(name = "project_location")
@NamedQuery(name = "ProjectLocation.findAll", query = "SELECT p FROM ProjectLocation p")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
public class ProjectLocation extends Auditable<Long> implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	@SequenceGenerator(name = "PROJECT_LOCATION_PROJECTLOCATIONID_GENERATOR", sequenceName = "PROJECT_LOCATION_ID_SEQUENCE", allocationSize = 1)
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "PROJECT_LOCATION_PROJECTLOCATIONID_GENERATOR")
	@Column(name = "project_location_id")
	private Long projectLocationId;

	// bi-directional many-to-one association to Employee
	@ManyToOne
	@JoinColumn(name = "dev_manager_id")
	private Employee employee1;

	// bi-directional many-to-one association to Employee
	@ManyToOne
	@JoinColumn(name = "qe_manager_id")
	private Employee employee2;

	// bi-directional many-to-one association to Employee
	@ManyToOne
	@JoinColumn(name = "ui_manager_id")
	private Employee employee3;

	// bi-directional many-to-one association to Employee
	@ManyToOne
	@JoinColumn(name = "manager1_id")
	private Employee employee4;

	// bi-directional many-to-one association to Employee
	@ManyToOne
	@JoinColumn(name = "manager2_id")
	private Employee employee5;

	// bi-directional many-to-one association to Location
	@ManyToOne
	@JoinColumn(name = "location_id")
	private Location location;

	// bi-directional many-to-one association to Project
	@ManyToOne
	@JoinColumn(name = "project_id")
	private Project project;

	// bi-directional many-to-one association to ProjectResource
	@OneToMany(mappedBy = "projectLocation")
	@Where(clause = "is_active = true")
	private Set<ProjectResource> projectResources;

	//bi-directional many-to-one association to SyncProjectManager
	@OneToMany(mappedBy="projectLocation")
	private List<SyncProjectManager> syncProjectManagers;


	public Long getProjectLocationId() {
		return this.projectLocationId;
	}

	public void setProjectLocationId(Long projectLocationId) {
		this.projectLocationId = projectLocationId;
	}

	public Employee getEmployee1() {
		return this.employee1;
	}

	public void setEmployee1(Employee employee1) {
		this.employee1 = employee1;
	}

	public Employee getEmployee2() {
		return this.employee2;
	}

	public void setEmployee2(Employee employee2) {
		this.employee2 = employee2;
	}

	public Employee getEmployee3() {
		return this.employee3;
	}

	public void setEmployee3(Employee employee3) {
		this.employee3 = employee3;
	}

	public Employee getEmployee4() {
		return this.employee4;
	}

	public void setEmployee4(Employee employee4) {
		this.employee4 = employee4;
	}

	public Employee getEmployee5() {
		return this.employee5;
	}

	public void setEmployee5(Employee employee5) {
		this.employee5 = employee5;
	}

	public Location getLocation() {
		return this.location;
	}

	public void setLocation(Location location) {
		this.location = location;
	}

	public Project getProject() {
		return this.project;
	}

	public void setProject(Project project) {
		this.project = project;
	}

	public Set<ProjectResource> getProjectResources() {
		return this.projectResources;
	}

	public void setProjectResources(Set<ProjectResource> projectResources) {
		this.projectResources = projectResources;
	}

	public ProjectResource addProjectResource(ProjectResource projectResource) {
		getProjectResources().add(projectResource);
		projectResource.setProjectLocation(this);

		return projectResource;
	}

	public ProjectResource removeProjectResource(ProjectResource projectResource) {
		getProjectResources().remove(projectResource);
		projectResource.setProjectLocation(null);

		return projectResource;
	}

	public Map<String, Employee> getAllManagers() {
		final Map<String, Employee> allManagers = new LinkedHashMap<>();
		allManagers.computeIfAbsent("DEV", val -> getEmployee1());
		allManagers.computeIfAbsent("QA", val -> getEmployee2());
		allManagers.computeIfAbsent("UI", val -> getEmployee3());
		allManagers.computeIfAbsent("SUPPORT1", val -> getEmployee4());
		allManagers.computeIfAbsent("SUPPORT2", val -> getEmployee5());

		return allManagers;
	}

	public List<SyncProjectManager> getSyncProjectManagers() {
		return this.syncProjectManagers;
	}

	public void setSyncProjectManagers(List<SyncProjectManager> syncProjectManagers) {
		this.syncProjectManagers = syncProjectManagers;
	}

	public SyncProjectManager addSyncProjectManager(SyncProjectManager syncProjectManager) {
		getSyncProjectManagers().add(syncProjectManager);
		syncProjectManager.setProjectLocation(this);

		return syncProjectManager;
	}

	public SyncProjectManager removeSyncProjectManager(SyncProjectManager syncProjectManager) {
		getSyncProjectManagers().remove(syncProjectManager);
		syncProjectManager.setProjectLocation(null);

		return syncProjectManager;
	}
}