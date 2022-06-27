package com.empconn.dto;

public class ProjectStatusChangeCommentDto {

	private String newState;
	private String comment;

	public String getNewState() {
		return newState;
	}

	public void setNewState(String newState) {
		this.newState = newState;
	}

	public String getComment() {
		return comment;
	}

	public void setComment(String comment) {
		this.comment = comment;
	}

}
