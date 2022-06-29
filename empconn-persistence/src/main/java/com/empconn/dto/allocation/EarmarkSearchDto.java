package com.empconn.dto.allocation;

import java.io.Serializable;
import java.util.List;

public class EarmarkSearchDto implements Serializable{
	
	private static final long serialVersionUID = 8475980954301960879L;
	private boolean isOpp;
	private List<String> accountNameList;
	private List<String> projOppNameList;
	private List<String> verticalIdList;
	private List<String> salesforceIdList;
	private List<String> resourceIdList;
	private List<String> secondarySkillIdList;
	private List<String> gdmIdList;
	private List<String> managerIdList;
	private Boolean billable;
	private List<String> primarySkillIdList;

	public boolean getIsOpp() {
		return isOpp;
	}

	public void setOpp(boolean isOpp) {
		this.isOpp = isOpp;
	}

	public List<String> getAccountNameList() {
		return accountNameList;
	}

	public void setAccountNameList(List<String> accountNameList) {
		this.accountNameList = accountNameList;
	}

	public List<String> getProjOppNameList() {
		return projOppNameList;
	}

	public void setProjOppNameList(List<String> projOppNameList) {
		this.projOppNameList = projOppNameList;
	}

	public List<String> getVerticalIdList() {
		return verticalIdList;
	}

	public void setVerticalIdList(List<String> verticalIdList) {
		this.verticalIdList = verticalIdList;
	}

	public List<String> getSalesforceIdList() {
		return salesforceIdList;
	}

	public void setSalesforceIdList(List<String> salesforceIdList) {
		this.salesforceIdList = salesforceIdList;
	}

	public List<String> getResourceIdList() {
		return resourceIdList;
	}

	public void setResourceIdList(List<String> resourceIdList) {
		this.resourceIdList = resourceIdList;
	}

	public List<String> getSecondarySkillIdList() {
		return secondarySkillIdList;
	}

	public void setSecondarySkillIdList(List<String> secondarySkillIdList) {
		this.secondarySkillIdList = secondarySkillIdList;
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

	public Boolean getBillable() {
		return billable;
	}

	public void setBillable(Boolean billable) {
		this.billable = billable;
	}

	public List<String> getPrimarySkillIdList() {
		return primarySkillIdList;
	}

	public void setPrimarySkillIdList(List<String> primarySkillIdList) {
		this.primarySkillIdList = primarySkillIdList;
	}

}
