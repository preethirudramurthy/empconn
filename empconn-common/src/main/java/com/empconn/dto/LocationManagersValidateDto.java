package com.empconn.dto;

import java.util.List;

import com.empconn.vo.UnitValue;

import io.swagger.annotations.ApiModelProperty;

public class LocationManagersValidateDto {

	private String projectLocationId;
	private UnitValue location;
	private ManagerInfoDto devManager;
	private ManagerInfoDto qaManager;
	private ManagerInfoDto uiManager;
	private ManagerInfoDto manager1;
	private ManagerInfoDto manager2;

	@ApiModelProperty(hidden = true)
	List<ResourceItemUnitDto> rrList;

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

	public ManagerInfoDto getDevManager() {
		return devManager;
	}

	public void setDevManager(ManagerInfoDto devManager) {
		this.devManager = devManager;
	}

	public ManagerInfoDto getQaManager() {
		return qaManager;
	}

	public void setQaManager(ManagerInfoDto qaManager) {
		this.qaManager = qaManager;
	}

	public ManagerInfoDto getUiManager() {
		return uiManager;
	}

	public void setUiManager(ManagerInfoDto uiManager) {
		this.uiManager = uiManager;
	}

	public ManagerInfoDto getManager1() {
		return manager1;
	}

	public void setManager1(ManagerInfoDto manager1) {
		this.manager1 = manager1;
	}

	public ManagerInfoDto getManager2() {
		return manager2;
	}

	public void setManager2(ManagerInfoDto manager2) {
		this.manager2 = manager2;
	}

	public List<ResourceItemUnitDto> getRrList() {
		return rrList;
	}

	public void setRrList(List<ResourceItemUnitDto> rrList) {
		this.rrList = rrList;
	}

}
