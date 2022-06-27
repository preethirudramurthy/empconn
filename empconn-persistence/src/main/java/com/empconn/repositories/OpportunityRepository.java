package com.empconn.repositories;

import java.util.Optional;
import java.util.Set;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.empconn.persistence.entities.Opportunity;

public interface OpportunityRepository extends JpaRepository<Opportunity, Long> {

	// @Query("SELECT opp FROM Opportunity opp WHERE (opp.employee1.employeeId =
	// :empId OR opp.employee2.employeeId = :empId) AND opp.isActive = true")
	@Query("SELECT opp FROM Opportunity opp WHERE opp.isActive = true")
	public Set<Opportunity> getMyOpportunityDropdown();

	Optional<Opportunity> findByNameAndIsActiveIsTrue(String opportunityName);
	// @Query("Select o from Opportunity o, Earmark e where o.name= :name AND o. ")
	// void findByOpportunityNameAndEmployee(String name, EarmarkInfoDto
	// earmarkInfoDto);

	Opportunity findByNameAndAccountNameAndIsActiveIsTrue(String opportunityName, String accountName);

	@Query("SELECT o.id FROM Opportunity o WHERE (o.employee1.employeeId = :employeeId OR o.employee2.employeeId = :employeeId) AND o.isActive ='TRUE'")
	public Set<Long> findOpportunityIdsForTheGdm(Long employeeId);
}
