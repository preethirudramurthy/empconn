package com.empconn.dto.map;

public class MapMigrateProjectDto {

	String project;
	String mapProjectId;

	public MapMigrateProjectDto() {
		super();
	}

	public MapMigrateProjectDto(String project, String mapProjectId) {
		super();
		this.project = project;
		this.mapProjectId = mapProjectId;
	}

	public String getProject() {
		return project;
	}

	public void setProject(String project) {
		this.project = project;
	}

	public String getMapProjectId() {
		return mapProjectId;
	}

	public void setMapProjectId(String mapProjectId) {
		this.mapProjectId = mapProjectId;
	}

}
