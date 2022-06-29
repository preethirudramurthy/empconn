package com.empconn.dto.earmark;

import java.util.List;

public class EarmarkEngineersGdmReqDto {

	private List<String> verticalIdList;
	private List<String> accountIdList;
	private List<String> projectIdList;
	private List<String> salesforceIdList;
	private boolean isOpp;
	private Boolean earmarkedByMe;
	private Boolean earmarkedByGdm;
	private Boolean earmarkedForOthers;
	private Boolean earmarkedByRmg;

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

	public boolean getIsOpp() {
		return isOpp;
	}

	public void setIsOpp(boolean isOpp) {
		this.isOpp = isOpp;
	}

	public Boolean getEarmarkedByMe() {
		return earmarkedByMe;
	}

	public void setEarmarkedByMe(Boolean earmarkedByMe) {
		this.earmarkedByMe = earmarkedByMe;
	}

	public Boolean getEarmarkedByGdm() {
		return earmarkedByGdm;
	}

	public void setEarmarkedByGdm(Boolean earmarkedByGdm) {
		this.earmarkedByGdm = earmarkedByGdm;
	}

	public Boolean getEarmarkedForOthers() {
		return earmarkedForOthers;
	}

	public void setEarmarkedForOthers(Boolean earmarkedForOthers) {
		this.earmarkedForOthers = earmarkedForOthers;
	}

	public Boolean getEarmarkedByRmg() {
		return earmarkedByRmg;
	}

	public void setEarmarkedByRmg(Boolean earmarkedByRmg) {
		this.earmarkedByRmg = earmarkedByRmg;
	}

}
