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
 * The persistent class for the earmark_salesforce_identifier database table.
 *
 */
@Entity
@Table(name = "earmark_salesforce_identifier")
@NamedQuery(name = "EarmarkSalesforceIdentifier.findAll", query = "SELECT e FROM EarmarkSalesforceIdentifier e")
public class EarmarkSalesforceIdentifier extends Auditable<Long> implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	@SequenceGenerator(name = "EARMARK_SALESFORCE_IDENTIFIERID_GENERATOR", sequenceName = "EARMARK_SALESFORCE_IDENTIFIER_ID_SEQUENCE", allocationSize = 1)
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "EARMARK_SALESFORCE_IDENTIFIERID_GENERATOR")
	@Column(name = "earmark_salesforce_identifier_id")
	private Long earmarkSalesforceIdentifierId;

	private String value;

	// bi-directional many-to-one association to Earmark
	@ManyToOne
	@JoinColumn(name = "earmark_id")
	private Earmark earmark;

	public EarmarkSalesforceIdentifier() {
	}

	public Long getEarmarkSalesforceIdentifierId() {
		return this.earmarkSalesforceIdentifierId;
	}

	public void setEarmarkSalesforceIdentifierId(Long earmarkSalesforceIdentifierId) {
		this.earmarkSalesforceIdentifierId = earmarkSalesforceIdentifierId;
	}

	public String getValue() {
		return this.value;
	}

	public void setValue(String value) {
		this.value = value;
	}

	public Earmark getEarmark() {
		return this.earmark;
	}

	public void setEarmark(Earmark earmark) {
		this.earmark = earmark;
	}

}