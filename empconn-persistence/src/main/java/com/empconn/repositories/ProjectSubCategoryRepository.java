package com.empconn.repositories;

import java.util.List;
import java.util.Optional;

import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.JpaRepository;

import com.empconn.persistence.entities.ProjectSubCategory;

public interface ProjectSubCategoryRepository extends JpaRepository<ProjectSubCategory, Integer> {

	List<ProjectSubCategory> findByIsActiveTrue(Sort sort);

	Optional<ProjectSubCategory> findByNameIgnoreCaseEqualsAndIsActiveTrue(String oldProjectSubcategory);

}
