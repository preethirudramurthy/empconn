
package com.empconn.repositories;

import java.util.List;
import java.util.Set;

import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;

import com.empconn.persistence.entities.ProjectChange;

public interface ProjectChangeRepository extends CrudRepository<ProjectChange, Long>, JpaSpecificationExecutor<ProjectChange>  {

	@Query("select p from ProjectChange p where p.status != 'Processed'")
	Set<ProjectChange> findPendingRequests();

	@Modifying
	@Query("update ProjectChange p set p.status = :newStatus, p.modifiedOn = now() where p.projectChangeId in (:projectChangeIdList)")
	void updateProcessingStatus(String newStatus, List<Long> projectChangeIdList);

}