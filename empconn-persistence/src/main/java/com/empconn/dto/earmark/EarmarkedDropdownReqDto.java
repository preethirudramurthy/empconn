package com.empconn.dto.earmark;

import java.util.List;

import javax.validation.constraints.Size;

public class EarmarkedDropdownReqDto {

	private boolean isOpp;
	private boolean earmarkedByMe;
	private boolean earmarkedByGdm;
	private boolean earmarkedByRmg;
	private boolean earmarkedForOthers;
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

	public boolean getEarmarkedForOthers() {
		return earmarkedForOthers;
	}

	public void setEarmarkedForOthers(boolean earmarkedForOthers) {
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
