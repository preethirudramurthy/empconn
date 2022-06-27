package com.empconn.dto.manager;

import java.util.List;

public class ChangeGdmRequestDto {

	private String projectId;
	private String devGdmId;
	private String qaGdmId;
	private List<ChangeReportingManagerDto> reportingManagerList;
	private List<String> gdmAssignResources;

	public String getProjectId() {
		return projectId;
	}

	public void setProjectId(String projectId) {
		this.projectId = projectId;
	}

	public String getDevGdmId() {
		return devGdmId;
	}

	public void setDevGdmId(String devGdmId) {
		this.devGdmId = devGdmId;
	}

	public String getQaGdmId() {
		return qaGdmId;
	}

	public void setQaGdmId(String qaGdmId) {
		this.qaGdmId = qaGdmId;
	}

	public List<ChangeReportingManagerDto> getReportingManagerList() {
		return reportingManagerList;
	}

	public void setReportingManagerList(List<ChangeReportingManagerDto> reportingManagerList) {
		this.reportingManagerList = reportingManagerList;
	}

	public List<String> getGdmAssignResources() {
		return gdmAssignResources;
	}

	public void setGdmAssignResources(List<String> gdmAssignResources) {
		this.gdmAssignResources = gdmAssignResources;
	}

}
