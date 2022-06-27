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
 * The persistent class for the manager_change database table.
 *
 */
@Entity
@Table(name="manager_change")
@NamedQuery(name="ManagerChange.findAll", query="SELECT m FROM ManagerChange m")
public class ManagerChange extends Auditable<Long> implements Serializable {

	public ManagerChange(Date effectiveStartDate, Boolean isGdm, String status, Employee employee,
			Employee newManager, Long createdBy) {
		super();
		this.effectiveStartDate = effectiveStartDate;
		this.isGdm = isGdm;
		this.status = status;
		this.employee = employee;
		this.newManager = newManager;
		this.setIsActive(true);
		this.setCreatedBy(createdBy);
		this.setCreatedOn(TimeUtils.getToday());
	}

	private static final long serialVersionUID = 1L;

	@Id
	@SequenceGenerator(name = "MANAGER_CHANGE_MANAGERCHANGEID_GENERATOR", sequenceName = "MANAGER_CHANGE_ID_SEQUENCE", allocationSize = 1)
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "MANAGER_CHANGE_MANAGERCHANGEID_GENERATOR")
	@Column(name="manager_change_id")
	private Long managerChangeId;

	@Temporal(TemporalType.DATE)
	@Column(name="effective_start_date")
	private Date effectiveStartDate;

	@Column(name="is_gdm")
	private Boolean isGdm;

	@Column(name="status")
	private String status;

	//bi-directional many-to-one association to Employee
	@ManyToOne
	@JoinColumn(name="employee_id")
	private Employee employee;

	//bi-directional many-to-one association to Employee
	@ManyToOne
	@JoinColumn(name="new_manager_id")
	private Employee newManager;

	public ManagerChange() {
	}

	public Long getManagerChangeId() {
		return this.managerChangeId;
	}

	public void setManagerChangeId(Long managerChangeId) {
		this.managerChangeId = managerChangeId;
	}

	public Date getEffectiveStartDate() {
		return this.effectiveStartDate;
	}

	public void setEffectiveStartDate(Date effectiveStartDate) {
		this.effectiveStartDate = effectiveStartDate;
	}

	public Boolean getIsGdm() {
		return this.isGdm;
	}

	public void setIsGdm(Boolean isGdm) {
		this.isGdm = isGdm;
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

	public Employee getNewManager() {
		return this.newManager;
	}

	public void setNewManager(Employee newManager) {
		this.newManager = newManager;
	}

}