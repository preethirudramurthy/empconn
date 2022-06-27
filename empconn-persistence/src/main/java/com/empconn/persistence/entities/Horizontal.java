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
 * The persistent class for the horizontal database table.
 *
 */
@Entity
@NamedQuery(name="Horizontal.findAll", query="SELECT h FROM Horizontal h")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
public class Horizontal extends Auditable<Long> implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	@SequenceGenerator(name="HORIZONTAL_HORIZONTALID_GENERATOR", sequenceName="HORIZONTAL_ID_SEQUENCE", allocationSize = 1)
	@GeneratedValue(strategy=GenerationType.SEQUENCE, generator="HORIZONTAL_HORIZONTALID_GENERATOR")
	@Column(name="horizontal_id")
	private Integer horizontalId;

	private String name;

	//bi-directional many-to-one association to Project
	@OneToMany(mappedBy="horizontal")
	private Set<Project> projects;

	public Horizontal() {
	}

	public Integer getHorizontalId() {
		return this.horizontalId;
	}

	public void setHorizontalId(Integer horizontalId) {
		this.horizontalId = horizontalId;
	}

	public String getName() {
		return this.name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public Set<Project> getProjects() {
		return this.projects;
	}

	public void setProjects(Set<Project> projects) {
		this.projects = projects;
	}

	public Project addProject(Project project) {
		getProjects().add(project);
		project.setHorizontal(this);

		return project;
	}

	public Project removeProject(Project project) {
		getProjects().remove(project);
		project.setHorizontal(null);

		return project;
	}

}