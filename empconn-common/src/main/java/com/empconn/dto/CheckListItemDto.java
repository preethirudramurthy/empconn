package com.empconn.dto;

import com.empconn.vo.UnitValue;

public class CheckListItemDto {
	
	private String checkListItemId;
	private UnitValue checkListItem;
	private boolean checked;
	private String comment;
	public String getCheckListItemId() {
		return checkListItemId;
	}
	public void setCheckListItemId(String checkListItemId) {
		this.checkListItemId = checkListItemId;
	}
	public UnitValue getCheckListItem() {
		return checkListItem;
	}
	public void setCheckListItem(UnitValue checkListItem) {
		this.checkListItem = checkListItem;
	}
	public boolean isChecked() {
		return checked;
	}
	public void setChecked(boolean checked) {
		this.checked = checked;
	}
	public String getComment() {
		return comment;
	}
	public void setComment(String comment) {
		this.comment = comment;
	}
	
	
	

}
