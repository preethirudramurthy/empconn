package com.empconn.dto.manager;

public class GetManagersResponseDto {

	public GetManagersResponseDto() {

	}

	public GetManagersResponseDto(Long projectLocationId, String projectLocation, String devManager, String qaManager,
			String uiManager, String manager1, String manager2) {
		super();
		this.projectLocationId = projectLocationId;
		this.projectLocation = projectLocation;
		this.devManager = devManager;
		this.qaManager = qaManager;
		this.uiManager = uiManager;
		this.manager1 = manager1;
		this.manager2 = manager2;
	}
	private Long projectLocationId;
	private String projectLocation;
	private String devManager;
	private String qaManager;
	private String uiManager;
	private String manager1;
	private String manager2;
	public Long getProjectLocationId() {
		return projectLocationId;
	}
	public void setProjectLocationId(Long projectLocationId) {
		this.projectLocationId = projectLocationId;
	}
	public String getProjectLocation() {
		return projectLocation;
	}
	public void setProjectLocation(String projectLocation) {
		this.projectLocation = projectLocation;
	}
	public String getDevManager() {
		return devManager;
	}
	public void setDevManager(String devManager) {
		this.devManager = devManager;
	}
	public String getQaManager() {
		return qaManager;
	}
	public void setQaManager(String qaManager) {
		this.qaManager = qaManager;
	}
	public String getUiManager() {
		return uiManager;
	}
	public void setUiManager(String uiManager) {
		this.uiManager = uiManager;
	}
	public String getManager1() {
		return manager1;
	}
	public void setManager1(String manager1) {
		this.manager1 = manager1;
	}
	public String getManager2() {
		return manager2;
	}
	public void setManager2(String manager2) {
		this.manager2 = manager2;
	}


}
