package com.empconn.repositories;

import java.util.List;
import java.util.Set;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.transaction.annotation.Transactional;

import com.empconn.persistence.entities.ProjectLocation;
import com.empconn.vo.UnitValue;

public interface ProjectLocationRespository extends JpaRepository<ProjectLocation, Long> {

	@Query("SELECT new com.empconn.vo.UnitValue(pl.projectLocationId, pl.location.name) FROM ProjectLocation pl WHERE pl.project.projectId = ?1 AND pl.isActive = 'TRUE'")
	Set<UnitValue> findLocationsForProjectId(Long projectId);

	@Query("SELECT pl.projectLocationId FROM ProjectLocation pl WHERE pl.project.projectId = ?1 AND pl.isActive = 'TRUE'")
	Set<Long> findLocationIdsForProjectId(Long projectId);

	@Transactional
	@Modifying
	@Query("UPDATE ProjectLocation pl SET pl.isActive = 'FALSE' WHERE pl.project.projectId = :projectId")
	void softDeleteByProjectId(long projectId);

	@Transactional
	@Modifying
	@Query("UPDATE ProjectLocation pl SET pl.isActive = 'FALSE' WHERE pl.projectLocationId NOT IN(?1) AND pl.project.projectId = ?2")
	void softDeleteProjectLocationsForProject(Set<Long> savedIds, Long projectId);

	@Transactional
	@Modifying
	@Query("UPDATE ProjectLocation pl SET pl.isActive = 'FALSE' WHERE  pl.project.projectId = :projectId")
	void softDeleteAllProjectLocationsForProject(Long projectId);

	@Query("SELECT DISTINCT p.project.projectId FROM ProjectLocation p WHERE p.isActive = 'TRUE' AND (p.employee1.employeeId = :employeeId OR p.employee2.employeeId = :employeeId OR p.employee3.employeeId = :employeeId OR p.employee4.employeeId = :employeeId OR p.employee5.employeeId = :employeeId)")
	Set<Long> findProjectsWhereOneOfManagerIs(Long employeeId);

	@Query("SELECT pl.projectLocationId FROM ProjectLocation pl WHERE pl.project.projectId = :projectId AND pl.isActive = 'FALSE'")
	Set<Long> findSoftDeletedLocationIdsForProject(Long projectId);

	List<ProjectLocation> findByProjectProjectIdAndLocationLocationId(Long projectId, Integer locationId);

	ProjectLocation findOneByProjectProjectId(Long projectId);
	
	ProjectLocation findFirstByProjectProjectId(Long projectId);

	@Query("SELECT pl FROM ProjectLocation pl WHERE pl.project.name = 'Central Bench' AND pl.location.name = 'Global' and pl.isActive = 'TRUE'")
	ProjectLocation findGlobalCentralBenchProjectLocation();

	@Query("SELECT pl FROM ProjectLocation pl WHERE pl.project.projectId IN(?1) AND pl.isActive = 'true'")
	List<ProjectLocation> findProjectByProjectId(List<Long> projectIds);

}
