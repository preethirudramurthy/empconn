package com.empconn.dto.map;

import java.math.BigDecimal;
import java.util.Set;

public class MapAccountLocationDto {

	private String location;
	private BigDecimal latitude;
	private BigDecimal longitude;
	private Set<MapAccountLocationContactDto> contacts;

	public String getLocation() {
		return location;
	}

	public void setLocation(String location) {
		this.location = location;
	}

	public BigDecimal getLatitude() {
		return latitude;
	}

	public void setLatitude(BigDecimal latitude) {
		this.latitude = latitude;
	}

	public BigDecimal getLongitude() {
		return longitude;
	}

	public void setLongitude(BigDecimal longitude) {
		this.longitude = longitude;
	}

	public Set<MapAccountLocationContactDto> getContacts() {
		return contacts;
	}

	public void setContacts(Set<MapAccountLocationContactDto> contacts) {
		this.contacts = contacts;
	}

}
