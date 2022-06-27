package com.empconn.dto;

import java.time.LocalDate;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.datatype.jsr310.deser.LocalDateDeserializer;
import com.fasterxml.jackson.datatype.jsr310.ser.LocalDateSerializer;
import com.empconn.vo.UnitValue;

public class ResourceItemUnitDto {
	private String rrId;
	private UnitValue title;
	private String noOfResources;

	@JsonDeserialize(using = LocalDateDeserializer.class)
	@JsonSerialize(using = LocalDateSerializer.class)
	@JsonFormat(pattern = "yyyy-MM-dd")
	private LocalDate startDate;
	@JsonDeserialize(using = LocalDateDeserializer.class)
	@JsonSerialize(using = LocalDateSerializer.class)
	@JsonFormat(pattern = "yyyy-MM-dd")
	private LocalDate endDate;

	private Integer percentage;
	private UnitValue primarySkill;
	private List<UnitValue> secondarySkillIdList;
	private Boolean billable;

	public String getRrId() {
		return rrId;
	}

	public void setRrId(String rrId) {
		this.rrId = rrId;
	}

	public UnitValue getTitle() {
		return title;
	}

	public void setTitle(UnitValue title) {
		this.title = title;
	}

	public String getNoOfResources() {
		return noOfResources;
	}

	public void setNoOfResources(String noOfResources) {
		this.noOfResources = noOfResources;
	}

	public LocalDate getStartDate() {
		return startDate;
	}

	public void setStartDate(LocalDate startDate) {
		this.startDate = startDate;
	}

	public LocalDate getEndDate() {
		return endDate;
	}

	public void setEndDate(LocalDate endDate) {
		this.endDate = endDate;
	}

	public Integer getPercentage() {
		return percentage;
	}

	public void setPercentage(Integer percentage) {
		this.percentage = percentage;
	}

	public UnitValue getPrimarySkill() {
		return primarySkill;
	}

	public void setPrimarySkill(UnitValue primarySkill) {
		this.primarySkill = primarySkill;
	}

	public List<UnitValue> getSecondarySkillIdList() {
		return secondarySkillIdList;
	}

	public void setSecondarySkillIdList(List<UnitValue> secondarySkillIdList) {
		this.secondarySkillIdList = secondarySkillIdList;
	}

	public Boolean getBillable() {
		return billable;
	}

	public void setBillable(Boolean billable) {
		this.billable = billable;
	}

}
