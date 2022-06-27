package com.empconn.dto;

import com.empconn.vo.UnitValue;

public class LocationManagersDto {

	private String projectLocationId;
	private UnitValue location;
	private UnitValue devManager;
	private UnitValue qaManager;
	private UnitValue uiManager;
	private UnitValue manager1;
	private UnitValue manager2;

	public String getProjectLocationId() {
		return projectLocationId;
	}

	public void setProjectLocationId(String projectLocationId) {
		this.projectLocationId = projectLocationId;
	}

	public UnitValue getLocation() {
		return location;
	}

	public void setLocation(UnitValue location) {
		this.location = location;
	}

	public UnitValue getDevManager() {
		return devManager;
	}

	public void setDevManager(UnitValue devManager) {
		this.devManager = devManager;
	}

	public UnitValue getQaManager() {
		return qaManager;
	}

	public void setQaManager(UnitValue qaManager) {
		this.qaManager = qaManager;
	}

	public UnitValue getUiManager() {
		return uiManager;
	}

	public void setUiManager(UnitValue uiManager) {
		this.uiManager = uiManager;
	}

	public UnitValue getManager1() {
		return manager1;
	}

	public void setManager1(UnitValue manager1) {
		this.manager1 = manager1;
	}

	public UnitValue getManager2() {
		return manager2;
	}

	public void setManager2(UnitValue manager2) {
		this.manager2 = manager2;
	}

}
