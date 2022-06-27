package com.empconn.dto;

public class TokenDto {

	private final String token;
	private final Integer validityDurationInSeconds;

	public TokenDto(String token, Integer validityDurationInSeconds) {
		super();
		this.token = token;
		this.validityDurationInSeconds = validityDurationInSeconds;
	}

	public String getToken() {
		return token;
	}

	public Integer getValidityDurationInSeconds() {
		return validityDurationInSeconds;
	}

}
