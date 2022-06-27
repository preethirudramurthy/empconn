package com.empconn.persistence.entities;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Set;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
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

import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;
import org.hibernate.annotations.Where;

import com.fasterxml.jackson.annotation.JsonManagedReference;

/**
 * The persistent class for the account database table.
 *
 */
@Entity
@NamedQuery(name = "Account.findAll", query = "SELECT a FROM Account a")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
public class Account extends Auditable<Long> implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	@SequenceGenerator(name = "ACCOUNT_ACCOUNTID_GENERATOR", sequenceName = "ACCOUNT_ID_SEQUENCE", allocationSize = 1)
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "ACCOUNT_ACCOUNTID_GENERATOR")
	@Column(name = "account_id")
	private Integer accountId;

	@Column(name = "account_tok_link")
	private String accountTokLink;

	private String category;

	@Column(name = "client_website_link")
	private String clientWebsiteLink;

	private String description;

	@Temporal(TemporalType.DATE)
	@Column(name = "end_date")
	private Date endDate;

	private String name;

	@Temporal(TemporalType.DATE)
	@Column(name = "start_date")
	private Date startDate;

	private String status;

	// bi-directional many-to-one association to Vertical
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "vertical_id")
	@JsonManagedReference
	private Vertical vertical;

	// bi-directional many-to-one association to ClientLocation
	@OneToMany(mappedBy = "account", cascade = CascadeType.ALL)
	@Where(clause = "is_active = true")
	private Set<ClientLocation> clientLocations;

	// bi-directional many-to-one association to Project
	@OneToMany(mappedBy = "account")
	@Where(clause = "is_active = true")
	private Set<Project> projects;

	@Column(name = "map_account_id")
	private String mapAccountId;

	//bi-directional many-to-one association to SyncAccount
	@OneToMany(mappedBy="account", cascade = CascadeType.ALL)
	private List<SyncAccount> syncAccounts;

	public Account() {
	}

	public Integer getAccountId() {
		return this.accountId;
	}

	public void setAccountId(Integer accountId) {
		this.accountId = accountId;
	}

	public String getAccountTokLink() {
		return this.accountTokLink;
	}

	public void setAccountTokLink(String accountTokLink) {
		this.accountTokLink = accountTokLink;
	}

	public String getCategory() {
		return this.category;
	}

	public void setCategory(String category) {
		this.category = category;
	}

	public String getClientWebsiteLink() {
		return this.clientWebsiteLink;
	}

	public void setClientWebsiteLink(String clientWebsiteLink) {
		this.clientWebsiteLink = clientWebsiteLink;
	}

	public String getDescription() {
		return this.description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public Date getEndDate() {
		return this.endDate;
	}

	public void setEndDate(Date endDate) {
		this.endDate = endDate;
	}

	public String getName() {
		return this.name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public Date getStartDate() {
		return this.startDate;
	}

	public void setStartDate(Date startDate) {
		this.startDate = startDate;
	}

	public String getStatus() {
		return this.status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public Vertical getVertical() {
		return this.vertical;
	}

	public void setVertical(Vertical vertical) {
		this.vertical = vertical;
	}

	public Set<ClientLocation> getClientLocations() {
		return this.clientLocations;
	}

	public void setClientLocations(Set<ClientLocation> clientLocations) {
		this.clientLocations = clientLocations;
	}

	public ClientLocation addClientLocation(ClientLocation clientLocation) {
		getClientLocations().add(clientLocation);
		clientLocation.setAccount(this);

		return clientLocation;
	}

	public ClientLocation removeClientLocation(ClientLocation clientLocation) {
		getClientLocations().remove(clientLocation);
		clientLocation.setAccount(null);

		return clientLocation;
	}

	public Set<Project> getProjects() {
		return this.projects;
	}

	public void setProjects(Set<Project> projects) {
		this.projects = projects;
	}

	public Project addProject(Project project) {
		getProjects().add(project);
		project.setAccount(this);

		return project;
	}

	public Project removeProject(Project project) {
		getProjects().remove(project);
		project.setAccount(null);

		return project;
	}

	public String getMapAccountId() {
		return mapAccountId;
	}

	public void setMapAccountId(String mapAccountId) {
		this.mapAccountId = mapAccountId;
	}

	public List<SyncAccount> getSyncAccounts() {
		if (this.syncAccounts == null) {
			return new ArrayList<SyncAccount>();
		}
		return this.syncAccounts;
	}

	public void setSyncAccounts(List<SyncAccount> syncAccounts) {
		this.syncAccounts = syncAccounts;
	}

	public SyncAccount addSyncAccount(SyncAccount syncAccount) {
		getSyncAccounts().add(syncAccount);
		syncAccount.setAccount(this);

		return syncAccount;
	}

	public SyncAccount removeSyncAccount(SyncAccount syncAccount) {
		getSyncAccounts().remove(syncAccount);
		syncAccount.setAccount(null);

		return syncAccount;
	}

}