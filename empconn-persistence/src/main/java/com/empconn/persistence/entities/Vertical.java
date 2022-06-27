package com.empconn.persistence.entities;

import java.io.Serializable;
import java.util.Set;

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

import com.fasterxml.jackson.annotation.JsonBackReference;


/**
 * The persistent class for the vertical database table.
 *
 */
@Entity
@NamedQuery(name="Vertical.findAll", query="SELECT v FROM Vertical v")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
public class Vertical extends Auditable<Long> implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	@SequenceGenerator(name="VERTICAL_VERTICALID_GENERATOR", sequenceName="VERTICAL_ID_SEQUENCE", allocationSize = 1)
	@GeneratedValue(strategy=GenerationType.SEQUENCE, generator="VERTICAL_VERTICALID_GENERATOR")
	@Column(name="vertical_id")
	private Integer verticalId;

	private String name;

	//bi-directional many-to-one association to Account
	@OneToMany(mappedBy="vertical")
	@JsonBackReference
	private Set<Account> accounts;

	public Vertical() {
	}

	public Integer getVerticalId() {
		return this.verticalId;
	}

	public void setVerticalId(Integer verticalId) {
		this.verticalId = verticalId;
	}

	public String getName() {
		return this.name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public Set<Account> getAccounts() {
		return this.accounts;
	}

	public void setAccounts(Set<Account> accounts) {
		this.accounts = accounts;
	}

	public Account addAccount(Account account) {
		getAccounts().add(account);
		account.setVertical(this);

		return account;
	}

	public Account removeAccount(Account account) {
		getAccounts().remove(account);
		account.setVertical(null);

		return account;
	}

}