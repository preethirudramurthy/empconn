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

import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;


/**
 * The persistent class for the location_hr database table.
 *
 */
@Entity
@Table(name="location_hr")
@NamedQuery(name="LocationHr.findAll", query="SELECT l FROM LocationHr l")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
public class LocationHr extends Auditable<Long> implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	@SequenceGenerator(name = "LOCATIONHR_LOCATIONHRID_GENERATOR", sequenceName = "LOCATION_HR_ID_SEQUENCE", allocationSize = 1)
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "LOCATIONHR_LOCATIONHRID_GENERATOR")
	@Column(name="location_hr_id")
	private Integer locationHrId;

	//bi-directional many-to-one association to Employee
	@ManyToOne
	@JoinColumn(name="employee_id")
	private Employee employee;

	//bi-directional many-to-one association to Location
	@ManyToOne
	@JoinColumn(name="location_id")
	private Location location;

	public LocationHr() {
	}

	public Integer getLocationHrId() {
		return this.locationHrId;
	}

	public void setLocationHrId(Integer locationHrId) {
		this.locationHrId = locationHrId;
	}

	public Employee getEmployee() {
		return this.employee;
	}

	public void setEmployee(Employee employee) {
		this.employee = employee;
	}

	public Location getLocation() {
		return this.location;
	}

	public void setLocation(Location location) {
		this.location = location;
	}

}
