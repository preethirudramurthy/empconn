package com.empconn.persistence.entities;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.NamedQuery;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;


/**
 * The persistent class for the email_configuration database table.
 *
 */
@Entity
@Table(name="email_configuration")
@NamedQuery(name="EmailConfiguration.findAll", query="SELECT e FROM EmailConfiguration e")
public class EmailConfiguration extends Auditable<Long> implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	@SequenceGenerator(name="EMAIL_CONFIGURATION_EMAILCONFIGURATIONID_GENERATOR", sequenceName="EMAIL_CONFIGURATION_ID_SEQUENCE", allocationSize = 1)
	@GeneratedValue(strategy=GenerationType.SEQUENCE, generator="EMAIL_CONFIGURATION_EMAILCONFIGURATIONID_GENERATOR")
	@Column(name="email_configuration_id")
	private Integer emailConfigurationId;

	@Column(name="append_mail_ids")
	private Boolean appendMailIds;

	@Column(name="cc_mail_ids")
	private String ccMailIds;

	private String name;

	@Column(name="to_mail_ids")
	private String toMailIds;

	public EmailConfiguration() {
	}

	public Integer getEmailConfigurationId() {
		return this.emailConfigurationId;
	}

	public void setEmailConfigurationId(Integer emailConfigurationId) {
		this.emailConfigurationId = emailConfigurationId;
	}

	public Boolean getAppendMailIds() {
		return this.appendMailIds;
	}

	public void setAppendMailIds(Boolean appendMailIds) {
		this.appendMailIds = appendMailIds;
	}

	public String getCcMailIds() {
		return this.ccMailIds;
	}

	public void setCcMailIds(String ccMailIds) {
		this.ccMailIds = ccMailIds;
	}

	public String getName() {
		return this.name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getToMailIds() {
		return this.toMailIds;
	}

	public void setToMailIds(String toMailIds) {
		this.toMailIds = toMailIds;
	}

}