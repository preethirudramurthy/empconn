package com.empconn.exception;

public class EmpConnException extends RuntimeException{

	private static final long serialVersionUID = 1L;
	private final String code;

	public EmpConnException(String require) {
		super(require);
		this.code = require;
	}

	public String getCode() {
		return code;
	}

}
