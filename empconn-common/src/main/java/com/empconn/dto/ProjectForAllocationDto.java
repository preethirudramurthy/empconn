package com.empconn.dto;

import com.fasterxml.jackson.annotation.JsonProperty;

public class ProjectForAllocationDto {

	public ProjectForAllocationDto() {

	}

	public ProjectForAllocationDto(String accountId, Boolean isActive, Boolean onlyFutureReleaseDate, Boolean withBench,
			String partial) {
		super();
		this.accountId = accountId;
		this.isActive = isActive;
		this.onlyFutureReleaseDate = onlyFutureReleaseDate;
		this.withBench = withBench;
		this.partial = partial;
	}
	private String accountId;
	@JsonProperty
	private Boolean isActive;
	@JsonProperty
	private Boolean onlyFutureReleaseDate;
	@JsonProperty
	private Boolean withBench;
	private String partial;
	public String getAccountId() {
		return accountId;
	}
	public void setAccountId(String accountId) {
		this.accountId = accountId;
	}
	public Boolean getIsActive() {
		return isActive;
	}
	public void setIsActive(Boolean isActive) {
		this.isActive = isActive;
	}
	public Boolean getOnlyFutureReleaseDate() {
		return onlyFutureReleaseDate;
	}
	public void setOnlyFutureReleaseDate(Boolean onlyFutureReleaseDate) {
		this.onlyFutureReleaseDate = onlyFutureReleaseDate;
	}
	public Boolean getWithBench() {
		return withBench;
	}
	public void setWithBench(Boolean withBench) {
		this.withBench = withBench;
	}
	public String getPartial() {
		return partial;
	}
	public void setPartial(String partial) {
		this.partial = partial;
	}

}
