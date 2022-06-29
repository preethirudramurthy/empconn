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


/**
 * The persistent class for the contact database table.
 *
 */
@Entity
@NamedQuery(name="Contact.findAll", query="SELECT c FROM Contact c")
public class Contact extends Auditable<Long> implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	@SequenceGenerator(name="CONTACT_CONTACTID_GENERATOR", sequenceName="CONTACT_ID_SEQUENCE", allocationSize = 1)
	@GeneratedValue(strategy=GenerationType.SEQUENCE, generator="CONTACT_CONTACTID_GENERATOR")
	@Column(name="contact_id")
	private Long contactId;

	private String email;

	private String name;

	@Column(name="phone_number")
	private String phoneNumber;

	//bi-directional many-to-one association to ClientLocation
	@ManyToOne
	@JoinColumn(name="client_location_id")
	private ClientLocation clientLocation;
	
	public Long getContactId() {
		return this.contactId;
	}

	public void setContactId(Long contactId) {
		this.contactId = contactId;
	}

	public String getEmail() {
		return this.email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getName() {
		return this.name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getPhoneNumber() {
		return this.phoneNumber;
	}

	public void setPhoneNumber(String phoneNumber) {
		this.phoneNumber = phoneNumber;
	}

	public ClientLocation getClientLocation() {
		return this.clientLocation;
	}

	public void setClientLocation(ClientLocation clientLocation) {
		this.clientLocation = clientLocation;
	}

}