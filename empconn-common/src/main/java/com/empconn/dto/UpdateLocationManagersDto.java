package com.empconn.dto;

public class UpdateLocationManagersDto {
	
	private String projectLocationId;
	private String locationName;
	
	
	public UpdateLocationManagersDto() {
		super();
	}
	public UpdateLocationManagersDto(String projectLocationId, String locationName, ProjectLocationManagerInfoDto devManager, ProjectLocationManagerInfoDto qaManager,
			ProjectLocationManagerInfoDto uiManager, ProjectLocationManagerInfoDto manager1, ProjectLocationManagerInfoDto manager2) {
		super();
		this.projectLocationId = projectLocationId;
		this.locationName = locationName;
		this.devManager = devManager;
		this.qaManager = qaManager;
		this.uiManager = uiManager;
		this.manager1 = manager1;
		this.manager2 = manager2;
	}
	public String getProjectLocationId() {
		return projectLocationId;
	}
	public void setProjectLocationId(String projectLocationId) {
		this.projectLocationId = projectLocationId;
	}
	public String getLocationName() {
		return locationName;
	}
	public void setLocationName(String locationName) {
		this.locationName = locationName;
	}
	public ProjectLocationManagerInfoDto getDevManager() {
		return devManager;
	}
	public void setDevManager(ProjectLocationManagerInfoDto devManager) {
		this.devManager = devManager;
	}
	public ProjectLocationManagerInfoDto getQaManager() {
		return qaManager;
	}
	public void setQaManager(ProjectLocationManagerInfoDto qaManager) {
		this.qaManager = qaManager;
	}
	public ProjectLocationManagerInfoDto getUiManager() {
		return uiManager;
	}
	public void setUiManager(ProjectLocationManagerInfoDto uiManager) {
		this.uiManager = uiManager;
	}
	public ProjectLocationManagerInfoDto getManager1() {
		return manager1;
	}
	public void setManager1(ProjectLocationManagerInfoDto manager1) {
		this.manager1 = manager1;
	}
	public ProjectLocationManagerInfoDto getManager2() {
		return manager2;
	}
	public void setManager2(ProjectLocationManagerInfoDto manager2) {
		this.manager2 = manager2;
	}
	private ProjectLocationManagerInfoDto devManager;
	private ProjectLocationManagerInfoDto qaManager;
	private ProjectLocationManagerInfoDto uiManager;
	private ProjectLocationManagerInfoDto manager1;
	private ProjectLocationManagerInfoDto manager2;
	
	

}
