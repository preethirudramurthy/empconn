package com.empconn.dto.earmark;

import java.util.List;

public class EarmarkListRmgDto {
	private List<String> accountNameList;
	private Boolean billable;
	private String gdmId;
	private Boolean isOpp;
	private String managerId;
	private String primarySkillId;
	private List<String> secondarySkillIdList;
	private List<String> projectNameList;
	private List<String> salesforceIdList;
	private List<String> verticalIdList;
	public List<String> getAccountNameList() {
		return accountNameList;
	}
	public void setAccountNameList(List<String> accountNameList) {
		this.accountNameList = accountNameList;
	}
	public Boolean getBillable() {
		return billable;
	}
	public void setBillable(Boolean billable) {
		this.billable = billable;
	}
	public Boolean getIsOpp() {
		return isOpp;
	}
	public void setIsOpp(Boolean isOpp) {
		this.isOpp = isOpp;
	}
	public String getPrimarySkillId() {
		return primarySkillId;
	}
	public void setPrimarySkillId(String primarySkillId) {
		this.primarySkillId = primarySkillId;
	}
	public List<String> getSecondarySkillIdList() {
		return secondarySkillIdList;
	}
	public void setSecondarySkillIdList(List<String> secondarySkillIdList) {
		this.secondarySkillIdList = secondarySkillIdList;
	}
	public List<String> getProjectNameList() {
		return projectNameList;
	}
	public void setProjectNameList(List<String> projectNameList) {
		this.projectNameList = projectNameList;
	}
	public List<String> getSalesforceIdList() {
		return salesforceIdList;
	}
	public void setSalesforceIdList(List<String> salesforceIdList) {
		this.salesforceIdList = salesforceIdList;
	}
	public List<String> getVerticalIdList() {
		return verticalIdList;
	}
	public void setVerticalIdList(List<String> verticalIdList) {
		this.verticalIdList = verticalIdList;
	}
	public String getGdmId() {
		return gdmId;
	}
	public void setGdmId(String gdmId) {
		this.gdmId = gdmId;
	}
	public String getManagerId() {
		return managerId;
	}
	public void setManagerId(String managerId) {
		this.managerId = managerId;
	}
}
