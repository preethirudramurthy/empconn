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
 * The persistent class for the salesforce_identifier database table.
 *
 */
@Entity
@Table(name = "salesforce_identifier")
@NamedQuery(name = "SalesforceIdentifier.findAll", query = "SELECT s FROM SalesforceIdentifier s")
public class SalesforceIdentifier extends Auditable<Long> implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	@SequenceGenerator(name = "SALESFORCE_IDENTIFIER_SALESFORCEIDENTIFIERID_GENERATOR", sequenceName = "SALESFORCE_IDENTIFIER_ID_SEQUENCE", allocationSize = 1)
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "SALESFORCE_IDENTIFIER_SALESFORCEIDENTIFIERID_GENERATOR")
	@Column(name = "salesforce_identifier_id")
	private Long salesforceIdentifierId;

	private String value;

	// bi-directional many-to-one association to Project
	@ManyToOne
	@JoinColumn(name = "project_id")
	private Project project;

	public SalesforceIdentifier() {
	}

	public SalesforceIdentifier(String value, Project project, Long createdBy) {
		super();
		this.value = value;
		this.project = project;
		setCreatedBy(createdBy);
		setCreatedOn(TimeUtils.getCreatedOn());
		setIsActive(true);
	}
	public Long getSalesforceIdentifierId() {
		return this.salesforceIdentifierId;
	}

	public void setSalesforceIdentifierId(Long salesforceIdentifierId) {
		this.salesforceIdentifierId = salesforceIdentifierId;
	}

	public String getValue() {
		return this.value;
	}

	public void setValue(String value) {
		this.value = value;
	}

	public Project getProject() {
		return this.project;
	}

	public void setProject(Project project) {
		this.project = project;
	}

}