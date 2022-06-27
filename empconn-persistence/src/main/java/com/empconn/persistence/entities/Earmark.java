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
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

import com.empconn.utilities.TimeUtils;

/**
 * The persistent class for the earmark database table.
 *
 */
@Entity
@NamedQuery(name = "Earmark.findAll", query = "SELECT e FROM Earmark e")
public class Earmark extends Auditable<Long> implements Serializable {
	private static final long serialVersionUID = 1L;

	public Earmark(Earmark earmark) {
		super();
		this.billable = earmark.getBillable();
		this.endDate = earmark.getEndDate();
		this.isClientInterviewNeeded = earmark.getIsClientInterviewNeeded();
		this.percentage = earmark.getPercentage();
		this.startDate = earmark.getStartDate();
		this.employee2 = earmark.getEmployee2();
		this.opportunity = earmark.getOpportunity();
		this.project = earmark.getProject();
		setCreatedOn(TimeUtils.getCreatedOn());
		setIsActive(true);
	}

	@Id
	@SequenceGenerator(name = "EARMARK_EARMARKID_GENERATOR", sequenceName = "EARMARK_ID_SEQUENCE", allocationSize = 1)
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "EARMARK_EARMARKID_GENERATOR")
	@Column(name = "earmark_id")
	private Long earmarkId;

	@Column(name = "billable")
	private Boolean billable;

	@Temporal(TemporalType.DATE)
	@Column(name = "end_date")
	private Date endDate;

	@Column(name = "is_client_interview_needed")
	private Boolean isClientInterviewNeeded;

	@Column(name = "percentage")
	private Integer percentage;

	@Temporal(TemporalType.DATE)
	@Column(name = "start_date")
	private Date startDate;

	// bi-directional many-to-one association to Employee
	@ManyToOne(cascade = CascadeType.ALL)
	@JoinColumn(name = "allocation_id")
	private Allocation allocation;

	// bi-directional many-to-one association to Employee
	@ManyToOne
	@JoinColumn(name = "manager_id")
	private Employee employee2;

	// bi-directional many-to-one association to Opportunity
	@ManyToOne(cascade = CascadeType.ALL)
	@JoinColumn(name = "opportunity_id")
	private Opportunity opportunity;

	// bi-directional many-to-one association to Project
	@ManyToOne
	@JoinColumn(name = "project_id")
	private Project project;

	// bi-directional many-to-one association to EarmarkSalesforceIdentifier
	@OneToMany(mappedBy = "earmark", cascade = CascadeType.ALL)
	private List<EarmarkSalesforceIdentifier> earmarkSalesforceIdentifiers;

	@Column(name = "unearmark_by")
	private String unearmarkBy;

	@Column(name = "unearmark_comment")
	private String unearmarkComment;

	public Earmark() {
	}

	public Long getEarmarkId() {
		return this.earmarkId;
	}

	public void setEarmarkId(Long earmarkId) {
		this.earmarkId = earmarkId;
	}

	public Boolean getBillable() {
		return this.billable;
	}

	public void setBillable(Boolean billable) {
		this.billable = billable;
	}

	public Date getEndDate() {
		return this.endDate;
	}

	public void setEndDate(Date endDate) {
		this.endDate = endDate;
	}

	public Boolean getIsClientInterviewNeeded() {
		return this.isClientInterviewNeeded;
	}

	public void setIsClientInterviewNeeded(Boolean isClientInterviewNeeded) {
		this.isClientInterviewNeeded = isClientInterviewNeeded;
	}

	public Integer getPercentage() {
		return this.percentage;
	}

	public void setPercentage(Integer percentage) {
		this.percentage = percentage;
	}

	public Date getStartDate() {
		return this.startDate;
	}

	public void setStartDate(Date startDate) {
		this.startDate = startDate;
	}

	public Allocation getAllocation() {
		return allocation;
	}

	public void setAllocation(Allocation allocation) {
		this.allocation = allocation;
	}

	public Employee getEmployee2() {
		return this.employee2;
	}

	public void setEmployee2(Employee employee2) {
		this.employee2 = employee2;
	}

	public Opportunity getOpportunity() {
		return this.opportunity;
	}

	public void setOpportunity(Opportunity opportunity) {
		this.opportunity = opportunity;
	}

	public Project getProject() {
		return this.project;
	}

	public void setProject(Project project) {
		this.project = project;
	}

	public List<EarmarkSalesforceIdentifier> getEarmarkSalesforceIdentifiers() {
		return this.earmarkSalesforceIdentifiers;
	}

	public void setEarmarkSalesforceIdentifiers(List<EarmarkSalesforceIdentifier> earmarkSalesforceIdentifiers) {
		this.earmarkSalesforceIdentifiers = earmarkSalesforceIdentifiers;
	}

	public String getUnearmarkBy() {
		return unearmarkBy;
	}

	public void setUnearmarkBy(String unearmarkBy) {
		this.unearmarkBy = unearmarkBy;
	}

	public String getUnearmarkComment() {
		return unearmarkComment;
	}

	public void setUnearmarkComment(String unearmarkComment) {
		this.unearmarkComment = unearmarkComment;
	}

	public void setUnearmarkInfo(String unearmarkBy, String unearmarkComment) {
		this.setIsActive(false);
		this.unearmarkBy = unearmarkBy;
		this.unearmarkComment = unearmarkComment;
	}

	public EarmarkSalesforceIdentifier addEarmarkSalesforceIdentifier(
			EarmarkSalesforceIdentifier earmarkSalesforceIdentifier) {
		getEarmarkSalesforceIdentifiers().add(earmarkSalesforceIdentifier);
		earmarkSalesforceIdentifier.setEarmark(this);

		return earmarkSalesforceIdentifier;
	}

	public EarmarkSalesforceIdentifier removeEarmarkSalesforceIdentifier(
			EarmarkSalesforceIdentifier earmarkSalesforceIdentifier) {
		getEarmarkSalesforceIdentifiers().remove(earmarkSalesforceIdentifier);
		earmarkSalesforceIdentifier.setEarmark(null);

		return earmarkSalesforceIdentifier;
	}

}