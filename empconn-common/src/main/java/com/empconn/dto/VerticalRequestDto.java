package com.empconn.dto;

import javax.validation.constraints.NotEmpty;

public class VerticalRequestDto {

	private String oldVertical;

	@NotEmpty
	private String newVertical;

	public String getOldVertical() {
		return oldVertical;
	}
	public void setOldVertical(String oldVertical) {
		this.oldVertical = oldVertical;
	}
	public String getNewVertical() {
		return newVertical;
	}
	public void setNewVertical(String newVertical) {
		this.newVertical = newVertical;
	}
}
