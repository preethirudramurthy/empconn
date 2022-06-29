package com.empconn.dto.earmark;

import java.io.Serializable;
import java.util.List;

public class EarmarkEngineersManagerReqDto implements Serializable {

	
	private static final long serialVersionUID = 4490322096045522377L;
	private List<String> projectIdList;
	private List<String> salesforceIdList;
	private boolean isOpp;
	private boolean earmarkedByMe;
	private boolean earmarkedByGdm;
	private boolean earmarkedByRmg;

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

	public boolean getEarmarkedByMe() {
		return earmarkedByMe;
	}

	public void setEarmarkedByMe(boolean earmarkedByMe) {
		this.earmarkedByMe = earmarkedByMe;
	}

	public boolean getEarmarkedByGdm() {
		return earmarkedByGdm;
	}

	public void setEarmarkedByGdm(boolean earmarkedByGdm) {
		this.earmarkedByGdm = earmarkedByGdm;
	}

	public boolean getEarmarkedByRmg() {
		return earmarkedByRmg;
	}

	public void setEarmarkedByRmg(boolean earmarkedByRmg) {
		this.earmarkedByRmg = earmarkedByRmg;
	}

}
