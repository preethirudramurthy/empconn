package com.empconn.dto;

public class PinCountDto {
	
	private Integer myPins;
	private Integer pinsForReview;
	private Integer pinsForApproval;
	
	
	public PinCountDto() {
		super();
	}
	public PinCountDto(Integer myPins, Integer pinsForReview, Integer pinsForApproval) {
		super();
		this.myPins = myPins;
		this.pinsForReview = pinsForReview;
		this.pinsForApproval = pinsForApproval;
	}
	public Integer getMyPins() {
		return myPins;
	}
	public void setMyPins(Integer myPins) {
		this.myPins = myPins;
	}
	public Integer getPinsForReview() {
		return pinsForReview;
	}
	public void setPinsForReview(Integer pinsForReview) {
		this.pinsForReview = pinsForReview;
	}
	public Integer getPinsForApproval() {
		return pinsForApproval;
	}
	public void setPinsForApproval(Integer pinsForApproval) {
		this.pinsForApproval = pinsForApproval;
	}
	
	
	

}
