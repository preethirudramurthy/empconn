package com.empconn.dto.map;

import com.fasterxml.jackson.annotation.JsonFilter;

/**
 * @author varun.mb
 *
 */
@JsonFilter("map-project-filter")
public class MapProjectDto {

	private String _id;
	private String projectDescription;
	private String clientId;
	private String account;
	private boolean isProjectActive;
	private String technology;
	private String db;
	private String os;
	private String projectName;
	private String horizontal;

	public String get_id() {
		return _id;
	}

	public void set_id(String _id) {
		this._id = _id;
	}

	public String getProjectDescription() {
		return projectDescription;
	}

	public void setProjectDescription(String projectDescription) {
		this.projectDescription = projectDescription;
	}

	public String getClientId() {
		return clientId;
	}

	public void setClientId(String clientId) {
		this.clientId = clientId;
	}

	public String getAccount() {
		return account;
	}

	public void setAccount(String account) {
		this.account = account;
	}

	public boolean getIsProjectActive() {
		return isProjectActive;
	}

	public void setIsProjectActive(boolean isProjectActive) {
		this.isProjectActive = isProjectActive;
	}

	public String getTechnology() {
		return technology;
	}

	public void setTechnology(String technology) {
		this.technology = technology;
	}

	public String getDb() {
		return db;
	}

	public void setDb(String db) {
		this.db = db;
	}

	public String getOs() {
		return os;
	}

	public void setOs(String os) {
		this.os = os;
	}

	public String getProjectName() {
		return projectName;
	}

	public void setProjectName(String projectName) {
		this.projectName = projectName;
	}

	public String getHorizontal() {
		return horizontal;
	}

	public void setHorizontal(String horizontal) {
		this.horizontal = horizontal;
	}

}
