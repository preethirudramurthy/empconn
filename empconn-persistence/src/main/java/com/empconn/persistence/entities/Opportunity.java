package com.empconn.persistence.entities;

import java.io.Serializable;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

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

/**
 * The persistent class for the opportunity database table.
 *
 */
@Entity
@NamedQuery(name = "Opportunity.findAll", query = "SELECT o FROM Opportunity o")
public class Opportunity extends Auditable<Long> implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	@SequenceGenerator(name = "OPPORTUNITY_OPPORTUNITYID_GENERATOR", sequenceName = "OPPORTUNITY_ID_SEQUENCE", allocationSize = 1)
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "OPPORTUNITY_OPPORTUNITYID_GENERATOR")
	@Column(name = "opportunity_id")
	private Long opportunityId;

	@Column(name = "account_name")
	private String accountName;

	private String name;

	// bi-directional many-to-one association to Earmark
	@OneToMany(mappedBy = "opportunity")
	private List<Earmark> earmarks;

	// bi-directional many-to-one association to Account
	@ManyToOne
	@JoinColumn(name = "account_id")
	private Account account;

	// bi-directional many-to-one association to Employee
	@ManyToOne
	@JoinColumn(name = "dev_gdm")
	private Employee employee1;

	// bi-directional many-to-one association to Employee
	@ManyToOne
	@JoinColumn(name = "qa_gdm")
	private Employee employee2;

	// bi-directional many-to-one association to Vertical
	@ManyToOne
	@JoinColumn(name = "vertical_id")
	private Vertical vertical;

	public Long getOpportunityId() {
		return this.opportunityId;
	}

	public void setOpportunityId(Long opportunityId) {
		this.opportunityId = opportunityId;
	}

	public String getAccountName() {
		return this.accountName;
	}

	public void setAccountName(String accountName) {
		this.accountName = accountName;
	}

	public String getName() {
		return this.name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public List<Earmark> getEarmarks() {
		return this.earmarks;
	}

	public void setEarmarks(List<Earmark> earmarks) {
		this.earmarks = earmarks;
	}

	public Earmark addEarmark(Earmark earmark) {
		getEarmarks().add(earmark);
		earmark.setOpportunity(this);

		return earmark;
	}

	public Earmark removeEarmark(Earmark earmark) {
		getEarmarks().remove(earmark);
		earmark.setOpportunity(null);

		return earmark;
	}

	public Account getAccount() {
		return this.account;
	}

	public void setAccount(Account account) {
		this.account = account;
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

	public Vertical getVertical() {
		return this.vertical;
	}

	public void setVertical(Vertical vertical) {
		this.vertical = vertical;
	}

	public Map<String, Employee> getGdms() {
		final Map<String, Employee> gdms = new LinkedHashMap<>();
		gdms.computeIfAbsent("DEV", val -> getEmployee1());
		gdms.computeIfAbsent("QA", val -> getEmployee2());

		return gdms;
	}
}
