package com.empconn.repositories;

import java.util.Set;

import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.transaction.annotation.Transactional;

import com.empconn.persistence.entities.ProjectResource;

public interface ProjectResourceRepository extends CrudRepository<ProjectResource, Long> {

	@Transactional
	@Modifying
	@Query("UPDATE ProjectResource pr SET pr.isActive = 'FALSE' WHERE pr.projectResourcesId NOT IN (?1) AND pr.projectLocation.projectLocationId IN (?2)")
	public Integer softDeleteProjectResourcesForLocations(Set<Long> resourceIds, Set<Long> locationIds);

	@Transactional
	@Modifying
	@Query("UPDATE ProjectResource pr SET pr.isActive = 'FALSE' WHERE  pr.projectLocation.projectLocationId IN (:locationIds)")
	public Integer softDeleteAllProjectResourcesForLocations(Set<Long> locationIds);

	@Query("SELECT pr.projectResourcesId FROM ProjectResource pr WHERE pr.projectLocation.projectLocationId IN(:projectLocationIds) AND pr.isActive = 'FALSE'")
	public Set<Long> findSoftDeletedProjectResourcesForProjectLocations(Set<Long> projectLocationIds);

}
