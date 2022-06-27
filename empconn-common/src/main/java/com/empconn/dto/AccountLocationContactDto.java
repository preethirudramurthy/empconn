package com.empconn.dto;

import com.fasterxml.jackson.annotation.JsonProperty;

public class AccountLocationContactDto {
	private String accountLocationContactId;
	private String contactName;
	private String email;
	private String phone;
	@JsonProperty
	private Boolean isReadOnly;
	
	
	public AccountLocationContactDto() {
		super();
	}

	public AccountLocationContactDto(String accountLocationContactId, String contactName, String email, String phone) {
		super();
		this.accountLocationContactId = accountLocationContactId;
		this.contactName = contactName;
		this.email = email;
		this.phone = phone;
	}

	public String getAccountLocationContactId() {
		return accountLocationContactId;
	}

	public void setAccountLocationContactId(String accountLocationContactId) {
		this.accountLocationContactId = accountLocationContactId;
	}

	public String getContactName() {
		return contactName;
	}

	public void setContactName(String contactName) {
		this.contactName = contactName;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getPhone() {
		return phone;
	}

	public void setPhone(String phone) {
		this.phone = phone;
	}

	public Boolean getIsReadOnly() {
		return isReadOnly;
	}

	public void setIsReadOnly(Boolean isReadOnly) {
		this.isReadOnly = isReadOnly;
	}

	@Override
	public String toString() {
		return "AccountLocationContactDto [accountLocationContactId=" + accountLocationContactId + ", contactName="
				+ contactName + ", email=" + email + ", phone=" + phone + ", isReadOnly=" + isReadOnly + "]";
	}
	
	
	
}
