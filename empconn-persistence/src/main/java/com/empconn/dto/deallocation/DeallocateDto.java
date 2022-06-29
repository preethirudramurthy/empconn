package com.empconn.dto.deallocation;

public class DeallocateDto {

	private Long allocationId;
	private Integer percentage;
	private Integer techRating;
	private String techFeedback;
	private Integer softSkillRating;
	private String softSkillFeedback;
	private boolean partial;
	public Long getAllocationId() {
		return allocationId;
	}
	public void setAllocationId(Long allocationId) {
		this.allocationId = allocationId;
	}
	public Integer getPercentage() {
		return percentage;
	}
	public void setPercentage(Integer percentage) {
		this.percentage = percentage;
	}
	public Integer getTechRating() {
		return techRating;
	}
	public void setTechRating(Integer techRating) {
		this.techRating = techRating;
	}
	public String getTechFeedback() {
		return techFeedback;
	}
	public void setTechFeedback(String techFeedback) {
		this.techFeedback = techFeedback;
	}
	public Integer getSoftSkillRating() {
		return softSkillRating;
	}
	public void setSoftSkillRating(Integer softSkillRating) {
		this.softSkillRating = softSkillRating;
	}
	public String getSoftSkillFeedback() {
		return softSkillFeedback;
	}
	public void setSoftSkillFeedback(String softSkillFeedback) {
		this.softSkillFeedback = softSkillFeedback;
	}
	public boolean getPartial() {
		return partial;
	}
	public void setPartial(boolean partial) {
		this.partial = partial;
	}

}
