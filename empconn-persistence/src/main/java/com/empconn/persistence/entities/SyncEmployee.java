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

import com.empconn.utilities.TimeUtils;


/**
 * The persistent class for the sync_employee database table.
 *
 */
@Entity
@Table(name="sync_employee")
@NamedQuery(name="SyncEmployee.findAll", query="SELECT s FROM SyncEmployee s")
public class SyncEmployee extends Auditable<Long> implements Serializable {
	private static final long serialVersionUID = 1L;

	public SyncEmployee(Long createdBy, String status, Employee employee) {
		super();
		this.setCreatedBy(createdBy);
		this.setCreatedOn(TimeUtils.getCreatedOn());
		this.setIsActive(true);
		this.status = status;
		this.employee = employee;
	}

	@Id
	@SequenceGenerator(name="SYNC_EMPLOYEE_SYNCEMPLOYEE_GENERATOR", sequenceName="SYNC_EMPLOYEE_ID_SEQUENCE", allocationSize = 1)
	@GeneratedValue(strategy=GenerationType.SEQUENCE, generator="SYNC_EMPLOYEE_SYNCEMPLOYEE_GENERATOR")
	@Column(name="sync_employee_id")
	private Long syncEmployeeId;

	private String status;

	//bi-directional many-to-one association to Employee
	@ManyToOne
	@JoinColumn(name="employee_id")
	private Employee employee;

	public SyncEmployee() {
	}

	public Long getSyncEmployeeId() {
		return this.syncEmployeeId;
	}

	public void setSyncEmployeeId(Long syncEmployeeId) {
		this.syncEmployeeId = syncEmployeeId;
	}

	public String getStatus() {
		return this.status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public Employee getEmployee() {
		return this.employee;
	}

	public void setEmployee(Employee employee) {
		this.employee = employee;
	}

}