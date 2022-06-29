package com.empconn.dto;

import java.io.Serializable;

public class BenchAgeDto implements Serializable{
	
	private static final long serialVersionUID = 132887244488721411L;
	private String from;
	private String to;

	public String getFrom() {
		return from;
	}
	public void setFrom(String from) {
		this.from = from;
	}
	public String getTo() {
		return to;
	}
	public void setTo(String to) {
		this.to = to;
	}

}
