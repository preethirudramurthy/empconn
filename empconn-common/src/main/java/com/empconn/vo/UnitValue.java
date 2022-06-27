package com.empconn.vo;

import java.util.Objects;

public class UnitValue {

	private String id;
	private String value;

	public UnitValue() {
		super();
	}

	public UnitValue(String id, String value) {
		super();
		this.id = id;
		this.value = value;
	}

	public UnitValue(Long id, String value) {
		super();
		this.id = Long.toString(id);
		this.value = value;
	}

	public UnitValue(Integer verticalId, String value) {
		super();
		this.id = Integer.toString(verticalId);
		this.value = value;
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

	@Override
	public int hashCode() {
		return Objects.hash(id, value);
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		final UnitValue other = (UnitValue) obj;
		return Objects.equals(id, other.id) && Objects.equals(value, other.value);
	}

}
