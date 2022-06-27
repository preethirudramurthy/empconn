package com.empconn.repositories;

import java.util.Set;

import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.transaction.annotation.Transactional;

import com.empconn.persistence.entities.ClientLocation;

public interface ClientLocationRepository extends CrudRepository<ClientLocation, Long> {

	@Transactional
	@Modifying
	@Query("UPDATE ClientLocation cl SET cl.isActive = 'FALSE' WHERE cl.clientLocationId NOT IN(:savedIds) AND cl.account.accountId = :accountId")
	public Integer softDeleteClientLocationsForAccount(Set<Long> savedIds, Integer accountId);

	@Transactional
	@Modifying
	@Query("UPDATE ClientLocation cl SET cl.isActive = 'FALSE' WHERE cl.account.accountId = :accountId")
	public Integer softDeleteAllClientLocationsForAccount(Integer accountId);

	@Transactional
	@Modifying
	@Query("SELECT cl.clientLocationId FROM ClientLocation cl WHERE cl.account.accountId = :accountId AND cl.isActive = 'FALSE'")
	public Set<Long> findSoftDeletedIdsForAccount(Integer accountId);

}
