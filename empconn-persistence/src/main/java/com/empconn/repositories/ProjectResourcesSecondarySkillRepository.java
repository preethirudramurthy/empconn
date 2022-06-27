package com.empconn.repositories;

import java.util.Set;

import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.transaction.annotation.Transactional;

import com.empconn.persistence.entities.ProjectResourcesSecondarySkill;

public interface ProjectResourcesSecondarySkillRepository extends CrudRepository<ProjectResourcesSecondarySkill, Long> {

	@Transactional
	@Modifying
	@Query("UPDATE ProjectResourcesSecondarySkill prs SET prs.isActive = 'FALSE' WHERE  prs.projectResource.projectResourcesId IN (:resourceIds)")
	public Integer softDeleteAllSecondariesForProjectResourceIds(Set<Long> resourceIds);

	@Transactional
	@Modifying
	@Query("UPDATE ProjectResourcesSecondarySkill prs SET prs.isActive = 'FALSE' WHERE prs.projectResourcesSecondarySkillId NOT IN (:secondaryIds) AND prs.projectResource.projectResourcesId IN (:resourceIds)")
	public Integer softDeleteSecondariesForProjectResourceIds(Set<Long> secondaryIds, Set<Long> resourceIds);

}
