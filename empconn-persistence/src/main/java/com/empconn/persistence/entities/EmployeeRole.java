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


/**
 * The persistent class for the employee_roles database table.
 *
 */
@Entity
@Table(name="employee_roles")
@NamedQuery(name="EmployeeRole.findAll", query="SELECT e FROM EmployeeRole e")
public class EmployeeRole extends Auditable<Long> implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	@SequenceGenerator(name="EMPLOYEE_ROLES_EMPLOYEEROLESID_GENERATOR", sequenceName="EMPLOYEE_ROLES_ID_SEQUENCE", allocationSize = 1)
	@GeneratedValue(strategy=GenerationType.SEQUENCE, generator="EMPLOYEE_ROLES_EMPLOYEEROLESID_GENERATOR")
	@Column(name="employee_roles_id")
	private Long employeeRolesId;

	//bi-directional many-to-one association to Employee
	@ManyToOne
	@JoinColumn(name="employee_id")
	private Employee employee;

	//bi-directional many-to-one association to Role
	@ManyToOne
	@JoinColumn(name="role_id")
	private Role role;

	public EmployeeRole() {
	}

	public Long getEmployeeRolesId() {
		return this.employeeRolesId;
	}

	public void setEmployeeRolesId(Long employeeRolesId) {
		this.employeeRolesId = employeeRolesId;
	}

	public Employee getEmployee() {
		return this.employee;
	}

	public void setEmployee(Employee employee) {
		this.employee = employee;
	}

	public Role getRole() {
		return this.role;
	}

	public void setRole(Role role) {
		this.role = role;
	}

}