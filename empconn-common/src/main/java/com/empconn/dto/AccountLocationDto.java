package com.empconn.dto;

import java.util.List;

public class AccountLocationDto {
	private String accountLocationId;
	private String locationName;
	private Double latitude;
	private Double longitude;
	private List<AccountLocationContactDto> accountLocationContactList;
	
	public AccountLocationDto() {
		super();
	}
	
	public AccountLocationDto(String accountLocationId, String locationName, Double latitude, Double longitude,
			List<AccountLocationContactDto> accountLocationContactList) {
		super();
		this.accountLocationId = accountLocationId;
		this.locationName = locationName;
		this.latitude = latitude;
		this.longitude = longitude;
		this.accountLocationContactList = accountLocationContactList;
	}
	public String getAccountLocationId() {
		return accountLocationId;
	}
	public void setAccountLocationId(String accountLocationId) {
		this.accountLocationId = accountLocationId;
	}
	public String getLocationName() {
		return locationName;
	}
	public void setLocationName(String locationName) {
		this.locationName = locationName;
	}
	public Double getLatitude() {
		return latitude;
	}
	public void setLatitude(Double latitude) {
		this.latitude = latitude;
	}
	public Double getLongitude() {
		return longitude;
	}
	public void setLongitude(Double longitude) {
		this.longitude = longitude;
	}
	public List<AccountLocationContactDto> getAccountLocationContactList() {
		return accountLocationContactList;
	}
	public void setAccountLocationContactList(List<AccountLocationContactDto> accountLocationContactList) {
		this.accountLocationContactList = accountLocationContactList;
	}

	@Override
	public String toString() {
		return "AccountLocationDto [accountLocationId=" + accountLocationId + ", locationName=" + locationName
				+ ", latitude=" + latitude + ", longitude=" + longitude + ", accountLocationContactList="
				+ accountLocationContactList + "]";
	}
	
	
}
