package com.empconn.repositories;

import java.util.List;
import java.util.Optional;
import java.util.Set;

import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.transaction.annotation.Transactional;

import com.empconn.persistence.entities.SalesforceIdentifier;

public interface SalesforceIdentifierRepository extends CrudRepository<SalesforceIdentifier, Long> {

	@Query("select s from SalesforceIdentifier s where trim(upper(s.value)) = trim(upper(:salesforceId)) and s.project.projectId <> :projectId ")
	Set<SalesforceIdentifier> findBySalesForceIdNotInProject(String salesforceId, Long projectId);

	@Transactional
	@Modifying
	@Query("UPDATE SalesforceIdentifier s SET s.isActive = 'FALSE' WHERE s.project.projectId = :projectId")
	void softDeleteByProjectId(long projectId);

	@Transactional
	@Modifying
	@Query("UPDATE SalesforceIdentifier s SET s.isActive = 'FALSE' WHERE s.salesforceIdentifierId NOT IN(?1) AND s.project.projectId = ?2")
	void softDeleteSalesforcesForProject(Set<Long> savedIds, Long projectId);

	@Transactional
	@Modifying
	@Query("UPDATE SalesforceIdentifier s SET s.isActive = 'FALSE' WHERE s.project.projectId = :projectId")
	void softDeleteAllSalesforcesForProject(Long projectId);

	@Query("select s.value from SalesforceIdentifier s where s.project.projectId = :projectId ")
	List<String> findByProjectId(Long projectId);

	@Query("select s.value from SalesforceIdentifier s where s.project.projectId = :projectId AND s.isActive = 'true'")
	Set<String> getsalesforceIdentifierValueByProjectId(Long projectId);

	@Query("select s.value from SalesforceIdentifier s where s.project.projectId = :projectId AND s.isActive = 'true'")
	Set<String> findSalesforceIdsForProject(Long projectId);

	Optional<SalesforceIdentifier> findByValue(String value);

	@Query("Select  s from SalesforceIdentifier s where s.value = :value and s.isActive= :flag")
	Optional<List<SalesforceIdentifier>> findSalesforceIdentifierByValue(String value, Boolean flag);

	@Query("Select  s from SalesforceIdentifier s where s.value = :value and s.isActive= :flag and project.projectId != :projectId")
	Optional<List<SalesforceIdentifier>> sameSalesForceIdsForOtherProjects(String value, Boolean flag,
																		   Long projectId);

	@Query("Select  s from SalesforceIdentifier s where s.value IN :salesforceIdList")
	Optional<List<SalesforceIdentifier>> findByValues(List<String> salesforceIdList);

	List<SalesforceIdentifier> findByProjectProjectIdAndIsActiveIsTrue(Long projectId);

	Long countByValueAndProjectProjectIdNotAndProjectCurrentStatusNotInAndIsActiveIsTrue(String value,
			Long projectId, List<String> projectStatus);

	Long countByValueAndProjectCurrentStatusNotInAndIsActiveIsTrue(String sfId, List<String> projectStatus);
}