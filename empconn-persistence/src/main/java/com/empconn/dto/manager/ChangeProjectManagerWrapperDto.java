package com.empconn.dto.manager;

import java.util.List;

public class ChangeProjectManagerWrapperDto {

	private List<ChangeProjectManagerRequestDto> changeProjectMangerList;

	private List<String> managerAssignResources;

	public List<ChangeProjectManagerRequestDto> getChangeProjectMangerList() {
		return changeProjectMangerList;
	}

	public void setChangeProjectMangerList(List<ChangeProjectManagerRequestDto> changeProjectMangerList) {
		this.changeProjectMangerList = changeProjectMangerList;
	}

	public List<String> getManagerAssignResources() {
		return managerAssignResources;
	}

	public void setManagerAssignResources(List<String> managerAssignResources) {
		this.managerAssignResources = managerAssignResources;
	}

}
