package com.empconn.persistence.entities;

import java.io.Serializable;
import java.util.Date;

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
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

import com.empconn.utilities.TimeUtils;


/**
 * The persistent class for the project_change database table.
 *
 */
@Entity
@Table(name="project_change")
@NamedQuery(name="ProjectChange.findAll", query="SELECT p FROM ProjectChange p")
public class ProjectChange extends Auditable<Long> implements Serializable {
	private static final long serialVersionUID = 1L;


	public ProjectChange(Date effectiveStartDate, String status, Account account,
			Employee employee, Project project, Long createdBy) {
		super();
		this.effectiveStartDate = effectiveStartDate;
		this.status = status;
		this.account = account;
		this.employee = employee;
		this.project = project;
		this.setIsActive(true);
		this.setCreatedBy(createdBy);
		this.setCreatedOn(TimeUtils.getToday());
	}

	@Id
	@SequenceGenerator(name = "PROJECT_CHANGE_PROJECTCHANGEID_GENERATOR", sequenceName = "PROJECT_CHANGE_ID_SEQUENCE", allocationSize = 1)
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "PROJECT_CHANGE_PROJECTCHANGEID_GENERATOR")
	@Column(name="project_change_id")
	private Long projectChangeId;

	@Temporal(TemporalType.DATE)
	@Column(name="effective_start_date")
	private Date effectiveStartDate;

	@Column(name="status")
	private String status;

	//bi-directional many-to-one association to Account
	@ManyToOne
	@JoinColumn(name="account_id")
	private Account account;

	//bi-directional many-to-one association to Employee
	@ManyToOne
	@JoinColumn(name="employee_id")
	private Employee employee;

	//bi-directional many-to-one association to Project
	@ManyToOne
	@JoinColumn(name="project_id")
	private Project project;

	public ProjectChange() {
	}

	public Long getProjectChangeId() {
		return this.projectChangeId;
	}

	public void setProjectChangeId(Long projectChangeId) {
		this.projectChangeId = projectChangeId;
	}

	public Date getEffectiveStartDate() {
		return this.effectiveStartDate;
	}

	public void setEffectiveStartDate(Date effectiveStartDate) {
		this.effectiveStartDate = effectiveStartDate;
	}

	public String getStatus() {
		return this.status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public Account getAccount() {
		return this.account;
	}

	public void setAccount(Account account) {
		this.account = account;
	}

	public Employee getEmployee() {
		return this.employee;
	}

	public void setEmployee(Employee employee) {
		this.employee = employee;
	}

	public Project getProject() {
		return this.project;
	}

	public void setProject(Project project) {
		this.project = project;
	}

}