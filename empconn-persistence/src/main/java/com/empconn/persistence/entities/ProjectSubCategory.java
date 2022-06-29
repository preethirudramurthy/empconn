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


/**
 * The persistent class for the project_sub_category database table.
 *
 */
@Entity
@Table(name="project_sub_category")
@NamedQuery(name="ProjectSubCategory.findAll", query="SELECT p FROM ProjectSubCategory p")
public class ProjectSubCategory extends Auditable<Long> implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	@SequenceGenerator(name="PROJECT_SUB_CATEGORY_PROJECTSUBCATEGORYID_GENERATOR", sequenceName="PROJECT_SUB_CATEGORY_ID_SEQUENCE", allocationSize = 1)
	@GeneratedValue(strategy=GenerationType.SEQUENCE, generator="PROJECT_SUB_CATEGORY_PROJECTSUBCATEGORYID_GENERATOR")
	@Column(name="project_sub_category_id")
	private Integer projectSubCategoryId;

	private String name;

	//bi-directional many-to-one association to Project
	@OneToMany(mappedBy="projectSubCategory")
	private Set<Project> projects;


	public Integer getProjectSubCategoryId() {
		return this.projectSubCategoryId;
	}

	public void setProjectSubCategoryId(Integer projectSubCategoryId) {
		this.projectSubCategoryId = projectSubCategoryId;
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
		project.setProjectSubCategory(this);

		return project;
	}

	public Project removeProject(Project project) {
		getProjects().remove(project);
		project.setProjectSubCategory(null);

		return project;
	}

}