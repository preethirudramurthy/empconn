package com.empconn.persistence.entities;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.NamedQuery;
import javax.persistence.Table;

@Entity
@Table(name = "login_token")
@NamedQuery(name = "LoginToken.findAll", query = "SELECT l FROM LoginToken l")
public class LoginToken implements Serializable {

	private static final long serialVersionUID = 1L;

	@Id
	@Column(name = "login_token_id")
	private String loginTokenId;

	public LoginToken() {
		super();
	}

	public LoginToken(String loginTokenId) {
		super();
		this.loginTokenId = loginTokenId;
	}

	public String getLoginTokenId() {
		return loginTokenId;
	}

	public void setLoginTokenId(String loginTokenId) {
		this.loginTokenId = loginTokenId;
	}

}
