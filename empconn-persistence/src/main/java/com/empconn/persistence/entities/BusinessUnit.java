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
 * The persistent class for the business_unit database table.
 *
 */
@Entity
@Table(name="business_unit")
@NamedQuery(name="BusinessUnit.findAll", query="SELECT b FROM BusinessUnit b")
public class BusinessUnit extends NamedAuditable<Long> implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	@SequenceGenerator(name="BUSINESS_UNIT_BUSINESSUNITID_GENERATOR", sequenceName="BUSINESS_UNIT_ID_SEQUENCE", allocationSize = 1)
	@GeneratedValue(strategy=GenerationType.SEQUENCE, generator="BUSINESS_UNIT_BUSINESSUNITID_GENERATOR")
	@Column(name="business_unit_id")
	private Integer businessUnitId;

	//bi-directional many-to-one association to Employee
	@OneToMany(mappedBy="businessUnit")
	private Set<Employee> employees;

	public BusinessUnit() {
	}

	public Integer getBusinessUnitId() {
		return this.businessUnitId;
	}

	public void setBusinessUnitId(Integer businessUnitId) {
		this.businessUnitId = businessUnitId;
	}

	public Set<Employee> getEmployees() {
		return this.employees;
	}

	public void setEmployees(Set<Employee> employees) {
		this.employees = employees;
	}

	public Employee addEmployee(Employee employee) {
		getEmployees().add(employee);
		employee.setBusinessUnit(this);

		return employee;
	}

	public Employee removeEmployee(Employee employee) {
		getEmployees().remove(employee);
		employee.setBusinessUnit(null);

		return employee;
	}

}