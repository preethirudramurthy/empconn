package com.empconn.persistence.entities;

import java.io.Serializable;
import java.util.Set;

import javax.persistence.Cacheable;
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
 * The persistent class for the role database table.
 *
 */
@Entity
@NamedQuery(name="Role.findAll", query="SELECT r FROM Role r")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
public class Role extends Auditable<Long> implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	@SequenceGenerator(name="ROLE_ROLEID_GENERATOR", sequenceName="ROLE_ID_SEQUENCE", allocationSize = 1)
	@GeneratedValue(strategy=GenerationType.SEQUENCE, generator="ROLE_ROLEID_GENERATOR")
	@Column(name="role_id")
	private Integer roleId;

	private String name;

	//bi-directional many-to-one association to EmployeeRole
	@OneToMany(mappedBy="role")
	private Set<EmployeeRole> employeeRoles;

	public Role() {
	}

	public Integer getRoleId() {
		return this.roleId;
	}

	public void setRoleId(Integer roleId) {
		this.roleId = roleId;
	}

	public String getName() {
		return this.name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public Set<EmployeeRole> getEmployeeRoles() {
		return this.employeeRoles;
	}

	public void setEmployeeRoles(Set<EmployeeRole> employeeRoles) {
		this.employeeRoles = employeeRoles;
	}

	public EmployeeRole addEmployeeRole(EmployeeRole employeeRole) {
		getEmployeeRoles().add(employeeRole);
		employeeRole.setRole(this);

		return employeeRole;
	}

	public EmployeeRole removeEmployeeRole(EmployeeRole employeeRole) {
		getEmployeeRoles().remove(employeeRole);
		employeeRole.setRole(null);

		return employeeRole;
	}

}