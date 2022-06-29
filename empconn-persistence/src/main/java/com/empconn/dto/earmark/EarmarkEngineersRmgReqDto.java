package com.empconn.dto.earmark;

import java.util.List;

public class EarmarkEngineersRmgReqDto {

	private List<String> verticalIdList;
	private List<String> accountIdList;
	private List<String> projectIdList;
	private List<String> salesforceIdList;
	private List<String> primarySkillIdList;
	private List<String> secondarySkillIdList;
	private boolean isOpp;
	private Boolean billable;
	private List<String> gdmIdList;
	private List<String> managerIdList;

	public List<String> getVerticalIdList() {
		return verticalIdList;
	}

	public void setVerticalIdList(List<String> verticalIdList) {
		this.verticalIdList = verticalIdList;
	}

	public List<String> getAccountIdList() {
		return accountIdList;
	}

	public void setAccountIdList(List<String> accountIdList) {
		this.accountIdList = accountIdList;
	}

	public List<String> getProjectIdList() {
		return projectIdList;
	}

	public void setProjectIdList(List<String> projectIdList) {
		this.projectIdList = projectIdList;
	}

	public List<String> getSalesforceIdList() {
		return salesforceIdList;
	}

	public void setSalesforceIdList(List<String> salesforceIdList) {
		this.salesforceIdList = salesforceIdList;
	}

	public List<String> getPrimarySkillIdList() {
		return primarySkillIdList;
	}

	public void setPrimarySkillIdList(List<String> primarySkillIdList) {
		this.primarySkillIdList = primarySkillIdList;
	}

	public List<String> getSecondarySkillIdList() {
		return secondarySkillIdList;
	}

	public void setSecondarySkillIdList(List<String> secondarySkillIdList) {
		this.secondarySkillIdList = secondarySkillIdList;
	}

	public boolean getIsOpp() {
		return isOpp;
	}

	public void setIsOpp(boolean isOpp) {
		this.isOpp = isOpp;
	}

	public Boolean getBillable() {
		return billable;
	}

	public void setBillable(Boolean billable) {
		this.billable = billable;
	}

	public List<String> getGdmIdList() {
		return gdmIdList;
	}

	public void setGdmIdList(List<String> gdmIdList) {
		this.gdmIdList = gdmIdList;
	}

	public List<String> getManagerIdList() {
		return managerIdList;
	}

	public void setManagerIdList(List<String> managerIdList) {
		this.managerIdList = managerIdList;
	}

}
