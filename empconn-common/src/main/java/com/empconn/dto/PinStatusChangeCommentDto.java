package com.empconn.dto;

public class PinStatusChangeCommentDto {
	
	private Long projectId;
	private String comment;
	public Long getProjectId() {
		return projectId;
	}
	public void setProjectId(Long projectId) {
		this.projectId = projectId;
	}
	public String getComment() {
		return comment;
	}
	public void setComment(String comment) {
		this.comment = comment;
	}
	
}
