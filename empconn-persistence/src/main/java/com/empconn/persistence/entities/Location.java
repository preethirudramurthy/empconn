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
 * The persistent class for the location database table.
 *
 */
@Entity
@NamedQuery(name = "Location.findAll", query = "SELECT l FROM Location l order by hierarchy")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
public class Location extends NamedAuditable<Long> implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	@SequenceGenerator(name = "LOCATION_LOCATIONID_GENERATOR", sequenceName = "LOCATION_ID_SEQUENCE", allocationSize = 1)
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "LOCATION_LOCATIONID_GENERATOR")
	@Column(name = "location_id")
	private Integer locationId;

	private Integer hierarchy;

	// bi-directional many-to-one association to Employee
	@OneToMany(mappedBy = "location")
	private Set<Employee> employees;

	// bi-directional many-to-one association to ProjectLocation
	@OneToMany(mappedBy = "location")
	private Set<ProjectLocation> projectLocations;

	//bi-directional many-to-one association to LocationHr
	@OneToMany(mappedBy="location")
	private Set<LocationHr> locationHrs;

	public Integer getLocationId() {
		return this.locationId;
	}

	public void setLocationId(Integer locationId) {
		this.locationId = locationId;
	}

	public Set<Employee> getEmployees() {
		return this.employees;
	}

	public void setEmployees(Set<Employee> employees) {
		this.employees = employees;
	}

	public Employee addEmployee(Employee employee) {
		getEmployees().add(employee);
		employee.setLocation(this);

		return employee;
	}

	public Employee removeEmployee(Employee employee) {
		getEmployees().remove(employee);
		employee.setLocation(null);

		return employee;
	}

	public Set<ProjectLocation> getProjectLocations() {
		return this.projectLocations;
	}

	public void setProjectLocations(Set<ProjectLocation> projectLocations) {
		this.projectLocations = projectLocations;
	}

	public ProjectLocation addProjectLocation(ProjectLocation projectLocation) {
		getProjectLocations().add(projectLocation);
		projectLocation.setLocation(this);

		return projectLocation;
	}

	public ProjectLocation removeProjectLocation(ProjectLocation projectLocation) {
		getProjectLocations().remove(projectLocation);
		projectLocation.setLocation(null);

		return projectLocation;
	}

	public Integer getHierarchy() {
		return hierarchy;
	}

	public void setHierarchy(Integer hierarchy) {
		this.hierarchy = hierarchy;
	}

	public Set<LocationHr> getLocationHrs() {
		return locationHrs;
	}

	public void setLocationHrs(Set<LocationHr> locationHrs) {
		this.locationHrs = locationHrs;
	}

}
