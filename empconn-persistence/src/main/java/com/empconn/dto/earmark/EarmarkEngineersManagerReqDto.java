package com.empconn.dto.earmark;

import java.util.List;

public class EarmarkEngineersManagerReqDto {

	private List<String> projectIdList;
	private List<String> salesforceIdList;
	private boolean isOpp;
	private Boolean earmarkedByMe;
	private Boolean earmarkedByGdm;
	private Boolean earmarkedByRmg;

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

	public Boolean getIsOpp() {
		return isOpp;
	}

	public void setIsOpp(Boolean isOpp) {
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

	public Boolean getEarmarkedByRmg() {
		return earmarkedByRmg;
	}

	public void setEarmarkedByRmg(Boolean earmarkedByRmg) {
		this.earmarkedByRmg = earmarkedByRmg;
	}

}
