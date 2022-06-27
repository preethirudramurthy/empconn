package com.empconn.repositories;

import java.util.Set;

import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.transaction.annotation.Transactional;

import com.empconn.persistence.entities.ProjectChecklist;

public interface ProjectChecklistRepository extends CrudRepository<ProjectChecklist, Long> {

	@Transactional
	@Modifying
	@Query("UPDATE ProjectChecklist pcl SET pcl.isActive = 'FALSE' WHERE pcl.projectChecklistId NOT IN (?1) AND pcl.project.projectId = ?2")
	public Integer softDeleteProjectChecklistsForProject(Set<Long> savedIds, Long projectId);

	@Transactional
	@Modifying
	@Query("UPDATE ProjectChecklist pcl SET pcl.isActive = 'FALSE' WHERE pcl.project.projectId = :projectId")
	public void softDeleteByProjectId(long projectId);

}
