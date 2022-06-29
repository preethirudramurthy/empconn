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

import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

/**
 * The persistent class for the title database table.
 *
 */
@Entity
@NamedQuery(name="Title.findAll", query="SELECT t FROM Title t")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
public class Title extends NamedAuditable<Long> implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	@SequenceGenerator(name="TITLE_TITLEID_GENERATOR", sequenceName="TITLE_ID_SEQUENCE", allocationSize = 1)
	@GeneratedValue(strategy=GenerationType.SEQUENCE, generator="TITLE_TITLEID_GENERATOR")
	@Column(name="title_id")
	private Integer titleId;

	//bi-directional many-to-one association to Employee
	@OneToMany(mappedBy="title")
	private Set<Employee> employees;

	//bi-directional many-to-one association to ProjectResource
	@OneToMany(mappedBy="title")
	private Set<ProjectResource> projectResources;

	public Integer getTitleId() {
		return this.titleId;
	}

	public void setTitleId(Integer titleId) {
		this.titleId = titleId;
	}

	public Set<Employee> getEmployees() {
		return this.employees;
	}

	public void setEmployees(Set<Employee> employees) {
		this.employees = employees;
	}

	public Employee addEmployee(Employee employee) {
		getEmployees().add(employee);
		employee.setTitle(this);

		return employee;
	}

	public Employee removeEmployee(Employee employee) {
		getEmployees().remove(employee);
		employee.setTitle(null);

		return employee;
	}

	public Set<ProjectResource> getProjectResources() {
		return this.projectResources;
	}

	public void setProjectResources(Set<ProjectResource> projectResources) {
		this.projectResources = projectResources;
	}

	public ProjectResource addProjectResource(ProjectResource projectResource) {
		getProjectResources().add(projectResource);
		projectResource.setTitle(this);

		return projectResource;
	}

	public ProjectResource removeProjectResource(ProjectResource projectResource) {
		getProjectResources().remove(projectResource);
		projectResource.setTitle(null);

		return projectResource;
	}

}