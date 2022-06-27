package com.empconn.email;

public class EmailConfig {

	private final String[] to;
	private final String[] cc;
	private final boolean shouldAppendMailIds;

	public EmailConfig(String[] to, String[] cc, boolean shouldAppendMailIds) {
		super();
		this.to = to;
		this.cc = cc;
		this.shouldAppendMailIds = shouldAppendMailIds;
	}

	public String[] getTo() {
		return to;
	}

	public String[] getCc() {
		return cc;
	}

	public boolean isShouldAppendMailIds() {
		return shouldAppendMailIds;
	}

}