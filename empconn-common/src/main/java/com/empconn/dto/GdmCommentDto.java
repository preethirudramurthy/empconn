package com.empconn.dto;

import java.time.LocalDateTime;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.datatype.jsr310.deser.LocalDateTimeDeserializer;
import com.fasterxml.jackson.datatype.jsr310.ser.LocalDateTimeSerializer;

public class GdmCommentDto {

	private String gdmId;
	private String gdmFullName;
	private String comment;

	@JsonFormat(pattern = "yyyy-MM-dd'T'HH:mm:ss'Z'")
	@JsonDeserialize(using = LocalDateTimeDeserializer.class)
	@JsonSerialize(using = LocalDateTimeSerializer.class)
	private LocalDateTime at;

	public GdmCommentDto(String gdmId, String gdmFullName, String comment, LocalDateTime at) {
		super();
		this.gdmId = gdmId;
		this.gdmFullName = gdmFullName;
		this.comment = comment;
		this.at = at;
	}

	public String getGdmId() {
		return gdmId;
	}

	public void setGdmId(String gdmId) {
		this.gdmId = gdmId;
	}

	public String getGdmFullName() {
		return gdmFullName;
	}

	public void setGdmFullName(String gdmFullName) {
		this.gdmFullName = gdmFullName;
	}

	public String getComment() {
		return comment;
	}

	public void setComment(String comment) {
		this.comment = comment;
	}

	public LocalDateTime getAt() {
		return at;
	}

	public void setAt(LocalDateTime at) {
		this.at = at;
	}

}
