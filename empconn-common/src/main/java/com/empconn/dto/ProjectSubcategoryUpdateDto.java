package com.empconn.dto;

import javax.validation.constraints.NotEmpty;

public class ProjectSubcategoryUpdateDto {

	private String oldProjectSubcategory;
	@NotEmpty
	private String newProjectSubcategory;

	public String getOldProjectSubcategory() {
		return oldProjectSubcategory;
	}

	public void setOldProjectSubcategory(String oldProjectSubcategory) {
		this.oldProjectSubcategory = oldProjectSubcategory;
	}

	public String getNewProjectSubcategory() {
		return newProjectSubcategory;
	}

	public void setNewProjectSubcategory(String newProjectSubcategory) {
		this.newProjectSubcategory = newProjectSubcategory;
	}

}
