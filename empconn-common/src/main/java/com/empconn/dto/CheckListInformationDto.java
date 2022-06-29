package com.empconn.dto;

public class CheckListInformationDto {
	private String description;
	private String comments;

	public CheckListInformationDto() {
		super();
	}

	public CheckListInformationDto(String description, String comments) {
		super();
		this.description = description;
		this.comments = comments;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public String getComments() {
		return comments;
	}

	public void setComments(String comments) {
		this.comments = comments;
	}

}
