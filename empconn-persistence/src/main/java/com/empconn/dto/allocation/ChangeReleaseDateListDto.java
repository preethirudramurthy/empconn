package com.empconn.dto.allocation;

import java.time.LocalDate;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.datatype.jsr310.deser.LocalDateDeserializer;
import com.fasterxml.jackson.datatype.jsr310.ser.LocalDateSerializer;

public class ChangeReleaseDateListDto {

	public ChangeReleaseDateListDto(String allocationId, LocalDate releaseDate,
			List<EditReleaseDateAllocationHour> allocationHours) {
		super();
		this.allocationId = allocationId;
		this.releaseDate = releaseDate;
		this.allocationHours = allocationHours;
	}

	public ChangeReleaseDateListDto() {
		super();
	}


	private String allocationId;
	@JsonDeserialize(using=LocalDateDeserializer.class)
	@JsonSerialize(using=LocalDateSerializer.class)
	@JsonFormat(shape = JsonFormat.Shape.STRING, pattern="yyyy-MM-dd'T'HH:mm:ss.SSSXXX") //yyyy-MM-dd'T'HH:mm:ss.SSSXXX
	private LocalDate releaseDate;

	private List<EditReleaseDateAllocationHour> allocationHours;
	public String getAllocationId() {
		return allocationId;
	}
	public void setAllocationId(String allocationId) {
		this.allocationId = allocationId;
	}
	public LocalDate getReleaseDate() {
		return releaseDate;
	}
	public void setReleaseDate(LocalDate releaseDate) {
		this.releaseDate = releaseDate;
	}
	public List<EditReleaseDateAllocationHour> getAllocationHours() {
		return allocationHours;
	}
	public void setAllocationHours(List<EditReleaseDateAllocationHour> allocationHours) {
		this.allocationHours = allocationHours;
	}

}
