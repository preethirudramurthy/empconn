package com.empconn.repositories;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

import com.empconn.persistence.entities.NdRequestSalesforceIdentifier;

public interface NdRequestSalesforceIdentifierRepository extends JpaRepository<NdRequestSalesforceIdentifier, Long> {

	public Long countByValueAndNdRequestProjectProjectIdNotAndIsActiveIsTrue(String value, Long projectId);

	public Long countByValueAndIsActiveIsTrue(String sfId);

	@Modifying
	@Query("UPDATE NdRequestSalesforceIdentifier e SET e.isActive = 'FALSE' WHERE e.ndRequest.ndRequestId IN(:ndRequestIds)")
	public void softDelete(List<Long> ndRequestIds);

}
