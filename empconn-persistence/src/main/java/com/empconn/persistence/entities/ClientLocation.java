package com.empconn.persistence.entities;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Set;

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

import org.hibernate.annotations.Where;

/**
 * The persistent class for the client_location database table.
 *
 */
@Entity
@Table(name = "client_location")
@NamedQuery(name = "ClientLocation.findAll", query = "SELECT c FROM ClientLocation c")
public class ClientLocation extends Auditable<Long> implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	@SequenceGenerator(name = "CLIENT_LOCATION_CLIENTLOCATIONID_GENERATOR", sequenceName = "CLIENT_LOCATION_ID_SEQUENCE", allocationSize = 1)
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "CLIENT_LOCATION_CLIENTLOCATIONID_GENERATOR")
	@Column(name = "client_location_id")
	private Long clientLocationId;

	private BigDecimal latitude;

	private String location;

	private BigDecimal longitude;

	// bi-directional many-to-one association to Account
	@ManyToOne
	@JoinColumn(name = "account_id")
	private Account account;

	// bi-directional many-to-one association to Contact
	@OneToMany(mappedBy = "clientLocation", cascade = CascadeType.ALL)
	@Where(clause = "is_active = true")
	private Set<Contact> contacts;

	public Long getClientLocationId() {
		return this.clientLocationId;
	}

	public void setClientLocationId(Long clientLocationId) {
		this.clientLocationId = clientLocationId;
	}

	public BigDecimal getLatitude() {
		return this.latitude;
	}

	public void setLatitude(BigDecimal latitude) {
		this.latitude = latitude;
	}

	public String getLocation() {
		return this.location;
	}

	public void setLocation(String location) {
		this.location = location;
	}

	public BigDecimal getLongitude() {
		return this.longitude;
	}

	public void setLongitude(BigDecimal longitude) {
		this.longitude = longitude;
	}

	public Account getAccount() {
		return this.account;
	}

	public void setAccount(Account account) {
		this.account = account;
	}

	public Set<Contact> getContacts() {
		return this.contacts;
	}

	public void setContacts(Set<Contact> contacts) {
		this.contacts = contacts;
	}

	public Contact addContact(Contact contact) {
		getContacts().add(contact);
		contact.setClientLocation(this);

		return contact;
	}

	public Contact removeContact(Contact contact) {
		getContacts().remove(contact);
		contact.setClientLocation(null);

		return contact;
	}

}