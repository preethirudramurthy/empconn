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
	public Set<SalesforceIdentifier> findBySalesForceIdNotInProject(String salesforceId, Long projectId);

	@Transactional
	@Modifying
	@Query("UPDATE SalesforceIdentifier s SET s.isActive = 'FALSE' WHERE s.project.projectId = :projectId")
	public void softDeleteByProjectId(long projectId);

	@Transactional
	@Modifying
	@Query("UPDATE SalesforceIdentifier s SET s.isActive = 'FALSE' WHERE s.salesforceIdentifierId NOT IN(?1) AND s.project.projectId = ?2")
	public void softDeleteSalesforcesForProject(Set<Long> savedIds, Long projectId);

	@Transactional
	@Modifying
	@Query("UPDATE SalesforceIdentifier s SET s.isActive = 'FALSE' WHERE s.project.projectId = :projectId")
	public void softDeleteAllSalesforcesForProject(Long projectId);

	@Query("select s.value from SalesforceIdentifier s where s.project.projectId = :projectId ")
	public List<String> findByProjectId(Long projectId);

	@Query("select s.value from SalesforceIdentifier s where s.project.projectId = :projectId AND s.isActive = 'true'")
	public Set<String> getsalesforceIdentifierValueByProjectId(Long projectId);

	@Query("select s.value from SalesforceIdentifier s where s.project.projectId = :projectId AND s.isActive = 'true'")
	public Set<String> findSalesforceIdsForProject(Long projectId);

	public Optional<SalesforceIdentifier> findByValue(String value);

	@Query("Select  s from SalesforceIdentifier s where s.value = :value and s.isActive= :flag")
	public Optional<List<SalesforceIdentifier>> findSalesforceIdentifierByValue(String value, Boolean flag);

	@Query("Select  s from SalesforceIdentifier s where s.value = :value and s.isActive= :flag and project.projectId != :projectId")
	public Optional<List<SalesforceIdentifier>> sameSalesForceIdsForOtherProjects(String value, Boolean flag,
			Long projectId);

	@Query("Select  s from SalesforceIdentifier s where s.value IN :salesforceIdList")
	public Optional<List<SalesforceIdentifier>> findByValues(List<String> salesforceIdList);

	public List<SalesforceIdentifier> findByProjectProjectIdAndIsActiveIsTrue(Long projectId);

	public Long countByValueAndProjectProjectIdNotAndProjectCurrentStatusNotInAndIsActiveIsTrue(String value,
			Long projectId, List<String> projectStatus);

	public Long countByValueAndProjectCurrentStatusNotInAndIsActiveIsTrue(String sfId, List<String> projectStatus);
}