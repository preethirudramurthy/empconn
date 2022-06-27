package com.empconn.dto.map;

import java.util.Set;

import com.fasterxml.jackson.annotation.JsonFilter;

@JsonFilter("map-account-filter")
public class MapAccountDto {

	private String _id;
	private Boolean isClientActive;
	private String projectSince;
	private String description;
	private String practice;
	private Set<String> field;
	private Set<MapAccountLocationDto> locations;
	private String clientLink;
	private String clientName;

	public String get_id() {
		return _id;
	}

	public void set_id(String _id) {
		this._id = _id;
	}

	public Boolean getIsClientActive() {
		return isClientActive;
	}

	public void setIsClientActive(Boolean isClientActive) {
		this.isClientActive = isClientActive;
	}

	public String getProjectSince() {
		return projectSince;
	}

	public void setProjectSince(String projectSince) {
		this.projectSince = projectSince;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public String getPractice() {
		return practice;
	}

	public void setPractice(String practice) {
		this.practice = practice;
	}

	public Set<String> getField() {
		return field;
	}

	public void setField(Set<String> field) {
		this.field = field;
	}

	public Set<MapAccountLocationDto> getLocations() {
		return locations;
	}

	public void setLocations(Set<MapAccountLocationDto> locations) {
		this.locations = locations;
	}

	public String getClientLink() {
		return clientLink;
	}

	public void setClientLink(String clientLink) {
		this.clientLink = clientLink;
	}

	public String getClientName() {
		return clientName;
	}

	public void setClientName(String clientName) {
		this.clientName = clientName;
	}

}
