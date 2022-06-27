package com.empconn.dto.manager;

public class GetGdmsResponseDto {

	public GetGdmsResponseDto() {

	}

	public GetGdmsResponseDto(String vertical, String accountName, String projectName, Long devGdmId, String devGdm,
			Long qaGdmId, String qaGdm) {
		super();
		this.vertical = vertical;
		this.accountName = accountName;
		this.projectName = projectName;
		this.devGdmId = devGdmId;
		this.devGdm = devGdm;
		this.qaGdmId = qaGdmId;
		this.qaGdm = qaGdm;
	}
	private String vertical;
	private String accountName;
	private String projectName;
	private Long devGdmId;
	private String devGdm;
	private Long qaGdmId;
	private String qaGdm;
	public String getVertical() {
		return vertical;
	}
	public void setVertical(String vertical) {
		this.vertical = vertical;
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
	public Long getDevGdmId() {
		return devGdmId;
	}
	public void setDevGdmId(Long devGdmId) {
		this.devGdmId = devGdmId;
	}
	public String getDevGdm() {
		return devGdm;
	}
	public void setDevGdm(String devGdm) {
		this.devGdm = devGdm;
	}
	public Long getQaGdmId() {
		return qaGdmId;
	}
	public void setQaGdmId(Long qaGdmId) {
		this.qaGdmId = qaGdmId;
	}
	public String getQaGdm() {
		return qaGdm;
	}
	public void setQaGdm(String qaGdm) {
		this.qaGdm = qaGdm;
	}


}
