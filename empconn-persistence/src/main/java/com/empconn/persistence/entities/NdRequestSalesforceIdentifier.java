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

/**
 * The persistent class for the nd_request_salesforce_identifier database table.
 *
 */
@Entity
@Table(name = "nd_request_salesforce_identifier")
@NamedQuery(name = "NdRequestSalesforceIdentifier.findAll", query = "SELECT e FROM NdRequestSalesforceIdentifier e")
public class NdRequestSalesforceIdentifier extends Auditable<Long> implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	@SequenceGenerator(name = "NDREQUEST_SALESFORCE_IDENTIFIERID_GENERATOR", sequenceName = "ND_REQUEST_SALESFORCE_IDENTIFIER_ID_SEQUENCE", allocationSize = 1)
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "NDREQUEST_SALESFORCE_IDENTIFIERID_GENERATOR")
	@Column(name = "nd_request_salesforce_identifier_id")
	private Long ndRequestSalesforceIdentifierId;

	private String value;

	// bi-directional many-to-one association to NdRequest
	@ManyToOne
	@JoinColumn(name = "nd_request_id")
	private NdRequest ndRequest;

	public Long getNdRequestSalesforceIdentifierId() {
		return this.ndRequestSalesforceIdentifierId;
	}

	public void setNdRequestSalesforceIdentifierId(Long ndRequestSalesforceIdentifierId) {
		this.ndRequestSalesforceIdentifierId = ndRequestSalesforceIdentifierId;
	}

	public String getValue() {
		return this.value;
	}

	public void setValue(String value) {
		this.value = value;
	}

	public NdRequest getNdRequest() {
		return this.ndRequest;
	}

	public void setNdRequest(NdRequest ndRequest) {
		this.ndRequest = ndRequest;
	}

}