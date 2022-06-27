package com.empconn.dto;

import javax.validation.constraints.NotEmpty;

public class ProjectSubcategoryInactivateDto {

	@NotEmpty
	private String projectSubcategory;

	public String getProjectSubcategory() {
		return projectSubcategory;
	}

	public void setProjectSubcategory(String projectSubCategory) {
		this.projectSubcategory = projectSubCategory;
	}

}
