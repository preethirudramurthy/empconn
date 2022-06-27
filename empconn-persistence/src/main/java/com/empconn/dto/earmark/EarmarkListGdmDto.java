package com.empconn.dto.earmark;

import java.util.List;

public class EarmarkListGdmDto {
	private List<String> verticalIdList;
	private List<String> accountNameList;
	private List<String> projectNameList;
	private List<String> salesforceIdList;
	private Boolean earmarkedByMe;
	private Boolean earmarkForOthers;
	private Boolean earmarkedByGdm;
	private Boolean earmarkedByRmg;
	private String primarySkillId;
	private List<String> secondarySkillIdList;
	
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
	public List<String> getVerticalIdList() {
		return verticalIdList;
	}
	public void setVerticalIdList(List<String> verticalIdList) {
		this.verticalIdList = verticalIdList;
	}
	public List<String> getAccountNameList() {
		return accountNameList;
	}
	public void setAccountNameList(List<String> accountNameList) {
		this.accountNameList = accountNameList;
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
	public Boolean getEarmarkedByMe() {
		return earmarkedByMe;
	}
	public void setEarmarkedByMe(Boolean earmarkedByMe) {
		this.earmarkedByMe = earmarkedByMe;
	}
	public Boolean getEarmarkForOthers() {
		return earmarkForOthers;
	}
	public void setEarmarkForOthers(Boolean earmarkForOthers) {
		this.earmarkForOthers = earmarkForOthers;
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
