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
 * The persistent class for the sync_account database table.
 *
 */
@Entity
@Table(name="sync_account")
@NamedQuery(name="SyncAccount.findAll", query="SELECT s FROM SyncAccount s")
public class SyncAccount extends Auditable<Long> implements Serializable {
	private static final long serialVersionUID = 1L;

	public SyncAccount(Long createdBy, String status, Account account) {
		super();
		this.setCreatedBy(createdBy);
		this.setCreatedOn(TimeUtils.getCreatedOn());
		this.setIsActive(true);
		this.status = status;
		this.account = account;
	}

	@Id
	@SequenceGenerator(name="SYNC_ACCOUNT_SYNCACCOUNTID_GENERATOR", sequenceName="SYNC_ACCOUNT_ID_SEQUENCE", allocationSize = 1)
	@GeneratedValue(strategy=GenerationType.SEQUENCE, generator="SYNC_ACCOUNT_SYNCACCOUNTID_GENERATOR")
	@Column(name="sync_account_id")
	private Long syncAccountId;

	private String status;

	//bi-directional many-to-one association to Account
	@ManyToOne
	@JoinColumn(name="account_id")
	private Account account;

	public SyncAccount() {
	}

	public Long getSyncAccountId() {
		return this.syncAccountId;
	}

	public void setSyncAccountId(Long syncAccountId) {
		this.syncAccountId = syncAccountId;
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

}