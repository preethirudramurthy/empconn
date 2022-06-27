package com.empconn.dto.allocation;

public class AutoRMSearchDto {
	private	Long projectId;

	private	Long projectLocationId;
	private	String workgroup;

	public Long getProjectId() {
		return projectId;
	}
	public void setProjectId(Long projectId) {
		this.projectId = projectId;
	}
	public Long getProjectLocationId() {
		return projectLocationId;
	}
	public void setProjectLocationId(Long projectLocationId) {
		this.projectLocationId = projectLocationId;
	}
	public String getWorkgroup() {
		return workgroup;
	}
	public void setWorkgroup(String workgroup) {
		this.workgroup = workgroup;
	}

}
