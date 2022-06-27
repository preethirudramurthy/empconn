package com.empconn.dto.manager;

public class ChangeProjectManagerRequestDto {

	private String projectLocationId;
	private String qaManagerId;
	private String devManagerId;
	private String uiManagerId;
	private String manager1Id;
	private String manager2Id;

	public String getProjectLocationId() {
		return projectLocationId;
	}
	public void setProjectLocationId(String projectLocationId) {
		this.projectLocationId = projectLocationId;
	}
	public String getQaManagerId() {
		return qaManagerId;
	}
	public void setQaManagerId(String qaManagerId) {
		this.qaManagerId = qaManagerId;
	}
	public String getDevManagerId() {
		return devManagerId;
	}
	public void setDevManagerId(String devManagerId) {
		this.devManagerId = devManagerId;
	}
	public String getUiManagerId() {
		return uiManagerId;
	}
	public void setUiManagerId(String uiManagerId) {
		this.uiManagerId = uiManagerId;
	}
	public String getManager1Id() {
		return manager1Id;
	}
	public void setManager1Id(String manager1Id) {
		this.manager1Id = manager1Id;
	}
	public String getManager2Id() {
		return manager2Id;
	}
	public void setManager2Id(String manager2Id) {
		this.manager2Id = manager2Id;
	}
}
