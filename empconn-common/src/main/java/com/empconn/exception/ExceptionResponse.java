package com.empconn.exception;

public class ExceptionResponse {

	private final String require;
	private final String message;

	public ExceptionResponse(String require, String message) {
		super();
		this.require = require;
		this.message = message;
	}

	public String getRequire() {
		return require;
	}

	public String getMessage() {
		return message;
	}

}
