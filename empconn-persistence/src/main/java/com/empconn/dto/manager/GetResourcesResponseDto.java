package com.empconn.dto.manager;

public class GetResourcesResponseDto {

	public GetResourcesResponseDto() {

	}

	public GetResourcesResponseDto(Long id, String value, String empCode, String title) {
		super();
		this.id = id;
		this.value = value;
		this.empCode = empCode;
		this.title = title;
	}
	private Long id;
	private String value;
	private String empCode;
	private String title;
	public Long getId() {
		return id;
	}
	public void setId(Long id) {
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
