package com.empconn.dto;

public class MasterResourceDto {

	private String id;
	private String value;
	private String empCode;
	private String title;

	public MasterResourceDto(String id, String value, String empCode, String title) {
		super();
		this.id = id;
		this.value = value;
		this.empCode = empCode;
		this.title = title;
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getValue() {
		return value;
	}

	public void setValue(String value) {
		this.value = value;
	}

	public String getEmpCode() {
		return empCode;
	}

	public void setEmpCode(String empCode) {
		this.empCode = empCode;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

}
