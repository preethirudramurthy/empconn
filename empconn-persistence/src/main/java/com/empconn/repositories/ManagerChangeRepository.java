
package com.empconn.repositories;

import java.util.List;
import java.util.Set;

import javax.transaction.Transactional;

import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;

import com.empconn.persistence.entities.ManagerChange;

public interface ManagerChangeRepository extends CrudRepository<ManagerChange, Long>, JpaSpecificationExecutor<ManagerChange>  {

	@Query("select m from ManagerChange m where m.status != 'Processed' and m.isGdm = :isGDM")
	public Set<ManagerChange> findPendingRequests(Boolean isGDM);

	@Transactional
	@Modifying
	@Query("update ManagerChange m set m.status = :newStatus, m.modifiedOn = now() where m.managerChangeId in (:managerChangeIdList)")
	public void updateProcessingStatus(String newStatus, List<Long> managerChangeIdList);

}