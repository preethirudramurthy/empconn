package com.empconn.repositories;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.transaction.annotation.Transactional;

import com.empconn.persistence.entities.EarmarkSalesforceIdentifier;

public interface EarmarkSalesforceIdentifierRepository extends JpaRepository<EarmarkSalesforceIdentifier, Long> {

	Long countByValueAndEarmarkProjectProjectIdNotAndIsActiveIsTrue(String value, Long projectId);

	Long countByValueAndEarmarkOpportunityNotNullAndEarmarkIsActiveTrueAndIsActiveIsTrue(String value);

	Long countByValueAndIsActiveIsTrue(String sfId);

	Long countByValueAndEarmarkProjectNotNullAndIsActiveIsTrue(String sfId);

	Long countByValueAndEarmarkOpportunityOpportunityIdNotAndIsActiveIsTrue(String sfId, Long opportunityId);

	@Transactional
	@Modifying
	@Query("UPDATE EarmarkSalesforceIdentifier e SET e.isActive = 'FALSE' WHERE e.earmark.earmarkId IN(:earmarkIds)")
	void softDelete(List<Long> earmarkIds);

}
