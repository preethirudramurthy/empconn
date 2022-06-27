package com.empconn.dto;

import java.time.LocalDate;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.datatype.jsr310.deser.LocalDateDeserializer;
import com.fasterxml.jackson.datatype.jsr310.ser.LocalDateSerializer;

public class ProjectSummarySearchDto {

	@JsonDeserialize(using = LocalDateDeserializer.class)
	@JsonSerialize(using = LocalDateSerializer.class)
	@JsonFormat(pattern = "yyyy-MM-dd")
	private LocalDate fromStartDate;
	@JsonDeserialize(using = LocalDateDeserializer.class)
	@JsonSerialize(using = LocalDateSerializer.class)
	@JsonFormat(pattern = "yyyy-MM-dd")
	private LocalDate toStartDate;
	private String verticalId;
	private List<String> horizontalId;
	private List<String> subCategoryId;
	private String accountId;
	private Boolean includeInactive;

	public ProjectSummarySearchDto(LocalDate fromStartDate, LocalDate toStartDate, String verticalId,
			List<String> horizontalId, List<String> subCategoryId, String accountId, Boolean includeInactive) {
		super();
		this.fromStartDate = fromStartDate;
		this.toStartDate = toStartDate;
		this.verticalId = verticalId;
		this.horizontalId = horizontalId;
		this.subCategoryId = subCategoryId;
		this.accountId = accountId;
		this.includeInactive = includeInactive;
	}

	public ProjectSummarySearchDto() {
		super();
	}

	public LocalDate getFromStartDate() {
		return fromStartDate;
	}

	public void setFromStartDate(LocalDate fromStartDate) {
		this.fromStartDate = fromStartDate;
	}

	public LocalDate getToStartDate() {
		return toStartDate;
	}

	public void setToStartDate(LocalDate toStartDate) {
		this.toStartDate = toStartDate;
	}

	public String getVerticalId() {
		return verticalId;
	}

	public void setVerticalId(String verticalId) {
		this.verticalId = verticalId;
	}

	public List<String> getHorizontalId() {
		return horizontalId;
	}

	public void setHorizontalId(List<String> horizontalId) {
		this.horizontalId = horizontalId;
	}

	public List<String> getSubCategoryId() {
		return subCategoryId;
	}

	public void setSubCategoryId(List<String> subCategoryId) {
		this.subCategoryId = subCategoryId;
	}

	public String getAccountId() {
		return accountId;
	}

	public void setAccountId(String accountId) {
		this.accountId = accountId;
	}

	public Boolean getIncludeInactive() {
		return includeInactive;
	}

	public void setIncludeInactive(Boolean includeInactive) {
		this.includeInactive = includeInactive;
	}

}
