package com.empconn.exception;

public class PreConditionFailedException extends EmpConnException {

	private static final long serialVersionUID = 1L;
	private final String code;

	public PreConditionFailedException(String require) {
		super(require);
		this.code = require;
	}

	@Override
	public String getCode() {
		return code;
	}

}
