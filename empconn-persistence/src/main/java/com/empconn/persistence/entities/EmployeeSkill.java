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
 * The persistent class for the employee_skills database table.
 *
 */
@Entity
@Table(name="employee_skills")
@NamedQuery(name="EmployeeSkill.findAll", query="SELECT e FROM EmployeeSkill e")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
public class EmployeeSkill extends Auditable<Long> implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	@SequenceGenerator(name="EMPLOYEE_SKILL_EMPLOYEESKILLID_GENERATOR", sequenceName="EMPLOYEE_SKILLS_ID_SEQUENCE", allocationSize = 1)
	@GeneratedValue(strategy=GenerationType.SEQUENCE, generator="EMPLOYEE_SKILL_EMPLOYEESKILLID_GENERATOR")
	@Column(name="employee_skills_id")
	private Long employeeSkillsId;

	//bi-directional many-to-one association to Employee
	@ManyToOne
	@JoinColumn(name="employee_id")
	private Employee employee;

	//bi-directional many-to-one association to SecondarySkill
	@ManyToOne
	@JoinColumn(name="secondary_skill_id")
	private SecondarySkill secondarySkill;

	public EmployeeSkill() {
	}

	public EmployeeSkill(Employee employee, SecondarySkill secondarySkill) {
		super();
		this.employee = employee;
		this.secondarySkill = secondarySkill;
	}

	public Long getEmployeeSkillsId() {
		return this.employeeSkillsId;
	}

	public void setEmployeeSkillsId(Long employeeSkillsId) {
		this.employeeSkillsId = employeeSkillsId;
	}

	public Employee getEmployee() {
		return this.employee;
	}

	public void setEmployee(Employee employee) {
		this.employee = employee;
	}

	public SecondarySkill getSecondarySkill() {
		return this.secondarySkill;
	}

	public void setSecondarySkill(SecondarySkill secondarySkill) {
		this.secondarySkill = secondarySkill;
	}

}
