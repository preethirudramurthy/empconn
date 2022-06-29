package com.empconn.persistence.entities;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQuery;
import javax.persistence.OneToMany;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;

/**
 * The persistent class for the "nd_request" database table.
 *
 */
@Entity
@Table(name = "nd_request")
@NamedQuery(name = "NdRequest.findAll", query = "SELECT n FROM NdRequest n")
public class NdRequest extends Auditable<Long> implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	@SequenceGenerator(name = "ND_REQUEST_NDREQUESTID_GENERATOR", sequenceName = "ND_REQUEST_ID_SEQUENCE", allocationSize = 1)
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "ND_REQUEST_NDREQUESTID_GENERATOR")
	@Column(name = "nd_request_id")
	private Long ndRequestId;

	@Column(name = "is_active")
	private boolean isActive;

	@Column(name = "is_billable")
	private Boolean isBillable;

	@Column(name = "percentage")
	private Integer percentage;

	@Column(name = "release_date")
	private Date releaseDate;

	@Column(name = "start_date")
	private Date startDate;

	@ManyToOne(cascade = CascadeType.MERGE)
	@JoinColumn(name = "employee_id")
	private Employee employee1;

	@ManyToOne(cascade = CascadeType.MERGE)
	@JoinColumn(name = "reporting_manager_id")
	private Employee employee2;

	@ManyToOne
	@JoinColumn(name = "project_id")
	private Project project;

	// bi-directional many-to-one association to NdRequestSalesforceIdentifier
	@OneToMany(mappedBy = "ndRequest", cascade = CascadeType.ALL)
	private List<NdRequestSalesforceIdentifier> ndRequestSalesforceIdentifiers;


	public Long getNdRequestId() {
		return this.ndRequestId;
	}

	public void setNdRequestId(Long ndRequestId) {
		this.ndRequestId = ndRequestId;
	}

	@Override
	public boolean getIsActive() {
		return this.isActive;
	}

	@Override
	public void setIsActive(boolean isActive) {
		this.isActive = isActive;
	}

	public Boolean getIsBillable() {
		return this.isBillable;
	}

	public void setIsBillable(Boolean isBillable) {
		this.isBillable = isBillable;
	}

	public Integer getPercentage() {
		return this.percentage;
	}

	public void setPercentage(Integer percentage) {
		this.percentage = percentage;
	}

	public Date getReleaseDate() {
		return this.releaseDate;
	}

	public void setReleaseDate(Date releaseDate) {
		this.releaseDate = releaseDate;
	}

	public Date getStartDate() {
		return this.startDate;
	}

	public void setStartDate(Date startDate) {
		this.startDate = startDate;
	}

	public Employee getEmployee1() {
		return this.employee1;
	}

	public void setEmployee1(Employee employee1) {
		this.employee1 = employee1;
	}

	public Employee getEmployee2() {
		return this.employee2;
	}

	public void setEmployee2(Employee employee2) {
		this.employee2 = employee2;
	}

	public Project getProject() {
		return this.project;
	}

	public void setProject(Project project) {
		this.project = project;
	}

	public List<NdRequestSalesforceIdentifier> getNdRequestSalesforceIdentifiers() {
		return this.ndRequestSalesforceIdentifiers;
	}

	public void setNdRequestSalesforceIdentifiers(List<NdRequestSalesforceIdentifier> ndRequestSalesforceIdentifiers) {
		this.ndRequestSalesforceIdentifiers = ndRequestSalesforceIdentifiers;
	}

	public NdRequestSalesforceIdentifier addNdRequestSalesforceIdentifier(
			NdRequestSalesforceIdentifier ndRequestSalesforceIdentifier) {
		getNdRequestSalesforceIdentifiers().add(ndRequestSalesforceIdentifier);
		ndRequestSalesforceIdentifier.setNdRequest(this);

		return ndRequestSalesforceIdentifier;
	}

	public NdRequestSalesforceIdentifier removeNdRequestSalesforceIdentifier(
			NdRequestSalesforceIdentifier ndRequestSalesforceIdentifier) {
		getNdRequestSalesforceIdentifiers().remove(ndRequestSalesforceIdentifier);
		ndRequestSalesforceIdentifier.setNdRequest(null);

		return ndRequestSalesforceIdentifier;
	}
}