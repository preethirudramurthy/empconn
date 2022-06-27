package com.empconn.dto.earmark;

import java.util.List;

public class EarmarkListManagerDto {
	
	private List<String> projectNameList;
	private List<String> salesforceIdList;
	private Boolean earmarkedByMe;
	private Boolean earmarkedByGdm;
	private Boolean earmarkedByRmg;
	private String primarySkillId;
	private List<String> secondarySkillIdList;
	
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
	
	

}
