package com.empconn.dto;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;

import com.fasterxml.jackson.annotation.JsonProperty;

public class CheckItemDto {

	@NotEmpty(message = "checkListItemId required")
	private String checkListItemId;
	@JsonProperty
	@NotNull(message = "checked required")
	private boolean checked;
	private String comment;

	public CheckItemDto() {
		super();
	}

	public CheckItemDto(String checkListItemId, boolean checked, String comment) {
		super();
		this.checkListItemId = checkListItemId;
		this.checked = checked;
		this.comment = comment;
	}

	public String getCheckListItemId() {
		return checkListItemId;
	}

	public void setCheckListItemId(String checkListItemId) {
		this.checkListItemId = checkListItemId;
	}

	public boolean isChecked() {
		return checked;
	}

	public void setChecked(boolean checked) {
		this.checked = checked;
	}

	public String getComment() {
		return comment;
	}

	public void setComment(String comment) {
		this.comment = comment;
	}

	@Override
	public String toString() {
		return "CheckItemDto [checkListItemId=" + checkListItemId + ", checked=" + checked + ", comment=" + comment
				+ "]";
	}

}
