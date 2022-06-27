package com.empconn.persistence.entities;

import javax.persistence.MappedSuperclass;

@MappedSuperclass
public class NamedAuditable<U> extends Auditable<U> {

	private String name;


	public String getName() {
		return this.name;
	}

	public void setName(String name) {
		this.name = name;
	}

}
