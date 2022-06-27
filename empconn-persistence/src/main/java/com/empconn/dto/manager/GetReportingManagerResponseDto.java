package com.empconn.dto.manager;

public class GetReportingManagerResponseDto {


	public GetReportingManagerResponseDto() {

	}
	public GetReportingManagerResponseDto(Long allocationId, String accountName, String projectName, Integer percentage,
			String reportingManger, Boolean isPrimary) {
		super();
		this.allocationId = allocationId;
		this.accountName = accountName;
		this.projectName = projectName;
		this.percentage = percentage;
		this.reportingManger = reportingManger;
		this.isPrimary = isPrimary;
	}
	private Long allocationId;
	private String accountName;
	private String projectName;
	private Integer percentage;
	private String reportingManger;
	private Boolean isPrimary;
	public Long getAllocationId() {
		return allocationId;
	}
	public void setAllocationId(Long allocationId) {
		this.allocationId = allocationId;
	}
	public String getAccountName() {
		return accountName;
	}
	public void setAccountName(String accountName) {
		this.accountName = accountName;
	}
	public String getProjectName() {
		return projectName;
	}
	public void setProjectName(String projectName) {
		this.projectName = projectName;
	}
	public Integer getPercentage() {
		return percentage;
	}
	public void setPercentage(Integer percentage) {
		this.percentage = percentage;
	}
	public String getReportingManger() {
		return reportingManger;
	}
	public void setReportingManger(String reportingManger) {
		this.reportingManger = reportingManger;
	}
	public Boolean getIsPrimary() {
		return isPrimary;
	}
	public void setIsPrimary(Boolean isPrimary) {
		this.isPrimary = isPrimary;
	}
}
