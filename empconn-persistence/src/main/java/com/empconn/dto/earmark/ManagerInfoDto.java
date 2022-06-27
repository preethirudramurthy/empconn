package com.empconn.dto.earmark;

import java.util.Objects;

public class ManagerInfoDto {

	private String id;
	private String value;
	private String empCode;
	private String title;

	public ManagerInfoDto() {
		super();
	}

	public ManagerInfoDto(String id, String value, String empCode, String title) {
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

	@Override
	public int hashCode() {
		return Objects.hash(empCode, id, title, value);
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		final ManagerInfoDto other = (ManagerInfoDto) obj;
		return Objects.equals(empCode, other.empCode) && Objects.equals(id, other.id)
				&& Objects.equals(title, other.title) && Objects.equals(value, other.value);
	}

}
