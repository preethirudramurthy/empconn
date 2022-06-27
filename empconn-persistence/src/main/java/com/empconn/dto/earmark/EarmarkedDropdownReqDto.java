package com.empconn.dto.earmark;

import java.util.List;

import javax.validation.constraints.Size;

public class EarmarkedDropdownReqDto {

	private boolean isOpp;
	private Boolean earmarkedByMe;
	private Boolean earmarkedByGdm;
	private Boolean earmarkedByRmg;
	private Boolean earmarkedForOthers;
	private List<String> accountIdList;
	private List<String> verticalIdList;
	@Size(min = 3)
	private String partial;

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

	public Boolean getEarmarkedByRmg() {
		return earmarkedByRmg;
	}

	public void setEarmarkedByRmg(Boolean earmarkedByRmg) {
		this.earmarkedByRmg = earmarkedByRmg;
	}

	public Boolean getEarmarkedForOthers() {
		return earmarkedForOthers;
	}

	public void setEarmarkedForOthers(Boolean earmarkedForOthers) {
		this.earmarkedForOthers = earmarkedForOthers;
	}

	public List<String> getAccountIdList() {
		return accountIdList;
	}

	public void setAccountIdList(List<String> accountIdList) {
		this.accountIdList = accountIdList;
	}

	public List<String> getVerticalIdList() {
		return verticalIdList;
	}

	public void setVerticalIdList(List<String> verticalIdList) {
		this.verticalIdList = verticalIdList;
	}

	public String getPartial() {
		return partial;
	}

	public void setPartial(String partial) {
		this.partial = partial;
	}

}
