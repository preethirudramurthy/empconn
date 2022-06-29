package com.empconn.repositories;

import java.util.Optional;
import java.util.Set;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.empconn.persistence.entities.Opportunity;

public interface OpportunityRepository extends JpaRepository<Opportunity, Long> {

	@Query("SELECT opp FROM Opportunity opp WHERE opp.isActive = true")
	public Set<Opportunity> getMyOpportunityDropdown();

	Optional<Opportunity> findByNameAndIsActiveIsTrue(String opportunityName);

	Opportunity findByNameAndAccountNameAndIsActiveIsTrue(String opportunityName, String accountName);

	@Query("SELECT o.id FROM Opportunity o WHERE (o.employee1.employeeId = :employeeId OR o.employee2.employeeId = :employeeId) AND o.isActive ='TRUE'")
	public Set<Long> findOpportunityIdsForTheGdm(Long employeeId);
}
