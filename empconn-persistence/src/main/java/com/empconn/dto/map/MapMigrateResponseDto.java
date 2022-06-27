package com.empconn.dto.map;

import java.util.Set;

public class MapMigrateResponseDto {

	private String account;
	private String mapAccountId;
	private Set<MapMigrateProjectDto> projects;

	public MapMigrateResponseDto() {
		super();
	}

	public MapMigrateResponseDto(String account, String mapAccountId, Set<MapMigrateProjectDto> projects) {
		super();
		this.account = account;
		this.mapAccountId = mapAccountId;
		this.projects = projects;
	}

	public String getAccount() {
		return account;
	}

	public void setAccount(String account) {
		this.account = account;
	}

	public String getMapAccountId() {
		return mapAccountId;
	}

	public void setMapAccountId(String mapAccountId) {
		this.mapAccountId = mapAccountId;
	}

	public Set<MapMigrateProjectDto> getProjects() {
		return projects;
	}

	public void setProjects(Set<MapMigrateProjectDto> projects) {
		this.projects = projects;
	}

}
