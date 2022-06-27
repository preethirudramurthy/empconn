package com.empconn.repositories;

import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import com.empconn.persistence.entities.ProjectReview;

@Repository
public interface ProjectReviewRepository extends CrudRepository<ProjectReview, Long> {

	@Transactional
	@Modifying
	@Query("UPDATE ProjectReview pr SET pr.isActive = 'FALSE' WHERE pr.project.projectId = :projectId")
	public void softDeleteByProjectId(long projectId);
}
