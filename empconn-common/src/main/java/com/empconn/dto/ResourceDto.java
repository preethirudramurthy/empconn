package com.empconn.dto;

import java.io.Serializable;
import java.util.List;

public class ResourceDto implements Serializable {
	
	private static final long serialVersionUID = -3241226936999335797L;
	private List<String> resourceIdList;
	private List<String> orgLocationIdList;
	private List<String> titleIdList;
	private List<String> departmentIdList;

	public List<String> getResourceIdList() {
		return resourceIdList;
	}

	public void setResourceIdList(List<String> resourceIdList) {
		this.resourceIdList = resourceIdList;
	}

	public List<String> getOrgLocationIdList() {
		return orgLocationIdList;
	}

	public void setOrgLocationIdList(List<String> orgLocationIdList) {
		this.orgLocationIdList = orgLocationIdList;
	}

	public List<String> getTitleIdList() {
		return titleIdList;
	}

	public void setTitleIdList(List<String> titleIdList) {
		this.titleIdList = titleIdList;
	}

	public List<String> getDepartmentIdList() {
		return departmentIdList;
	}

	public void setDepartmentIdList(List<String> departmentIdList) {
		this.departmentIdList = departmentIdList;
	}

}
