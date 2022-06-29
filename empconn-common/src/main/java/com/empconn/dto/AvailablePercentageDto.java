package com.empconn.dto;

import java.io.Serializable;

public class AvailablePercentageDto implements Serializable{

	private String low;
	private String high;

	public String getLow() {
		return low;
	}
	public void setLow(String low) {
		this.low = low;
	}
	public String getHigh() {
		return high;
	}
	public void setHigh(String high) {
		this.high = high;
	}

}
