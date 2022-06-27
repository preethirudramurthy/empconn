package com.empconn.dto;

import java.time.LocalDate;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.datatype.jsr310.deser.LocalDateDeserializer;
import com.fasterxml.jackson.datatype.jsr310.ser.LocalDateSerializer;

public class ProjectEndDateChangedDto {
	
	@JsonDeserialize(using=LocalDateDeserializer.class)
	@JsonSerialize(using=LocalDateSerializer.class)
	@JsonFormat(pattern="yyyy-MM-dd")
	private LocalDate newEndDate;

	public LocalDate getNewEndDate() {
		return newEndDate;
	}

	public void setNewEndDate(LocalDate newEndDate) {
		this.newEndDate = newEndDate;
	}
	
	

}
