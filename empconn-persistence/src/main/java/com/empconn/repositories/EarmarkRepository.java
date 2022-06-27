package com.empconn.repositories;

import java.util.Date;
import java.util.List;
import java.util.Optional;

import javax.transaction.Transactional;

import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;

import com.empconn.persistence.entities.Allocation;
import com.empconn.persistence.entities.Earmark;
import com.empconn.persistence.entities.Employee;
import com.empconn.persistence.entities.Project;

public interface EarmarkRepository extends CrudRepository<Earmark, Long>, JpaSpecificationExecutor<Earmark> {

	@Query("select e from Earmark e where e.allocation.employee.employeeId = :employeeId and e.isActive = 'true'")
	public List<Earmark> findAllocationEmployeeId(Long employeeId);

	@Query("select e from Earmark e where e.earmarkId in(:earmarkIds) and e.isActive = 'true'")
	public List<Earmark> findByIds(List<Long> earmarkIds);

	public List<Earmark> findByEarmarkIdIn(List<Long> earmarkIds);

	public List<Earmark> findByAllocationEmployee(Employee emp);

	@Modifying
	@Transactional
	@Query(value = "update Earmark e set e.percentage = :allocationPercentage, e.billable = :isBillable, e.isClientInterviewNeeded = :isClientInterview where e.earmarkId = :earmarkId")
	public void updateEarmark(Long earmarkId, Integer allocationPercentage, boolean isBillable,
			boolean isClientInterview);

	public Optional<List<Earmark>> findByAllocationAndProject(Employee employee, Project project);

	Optional<List<Earmark>> findByAllocationAllocationIdAndProjectProjectIdAndStartDateAndEndDateAndBillable(
			Long allocationId, Long projectId, Date startDate, Date endDate, Boolean billable);

	Optional<List<Earmark>> findByAllocationAllocationIdAndProjectProjectIdAndIsActiveIsTrue(Long allocationId,
			Long projectId);

	@Query("SELECT o FROM Opportunity o, Earmark e where e.opportunity=o.opportunityId AND o.isActive=true "
			+ "AND e.isActive=true AND e.allocation.allocationId= :allocationId AND o.name= :opportunityName")
	public Optional<List<Earmark>> findByAllocationAndOppurtunity(Long allocationId, String opportunityName);

	@Transactional
	@Modifying
	@Query("UPDATE Earmark p SET p.isActive = 'false' WHERE p.earmarkId in(:earmarkids)")
	public Integer unEarmark(List<Long> earmarkids);

	@Transactional
	@Modifying
	@Query("UPDATE Earmark e SET e.isActive = 'false' WHERE e.earmarkId = :earmarkId")
	public Integer unEarmark(Long earmarkId);

	public Optional<List<Earmark>> findByAllocation(Allocation allocationId);

	public Optional<List<Earmark>> findByAllocationAllocationIdAndIsActiveIsTrue(Long allocationId);

	List<Earmark> findByCreatedByAndEmployee2EmployeeIdAndProjectIsNotNullAndOpportunityIsNullAndIsActiveIsTrue(
			Long createdBy, Long employeeId);

	List<Earmark> findByCreatedByAndEmployee2EmployeeIdNotAndProjectIsNotNullAndOpportunityIsNullAndIsActiveIsTrue(
			Long createdBy, Long employeeId);

	List<Earmark> findByCreatedByNotAndEmployee2EmployeeIdAndProjectIsNotNullAndOpportunityIsNullAndIsActiveIsTrue(
			Long createdBy, Long employeeId);

	public List<Earmark> findByProjectIsNotNullAndOpportunityIsNullAndIsActiveIsTrue();

	List<Earmark> findByCreatedByAndEmployee2EmployeeIdAndProjectIsNullAndOpportunityIsNotNullAndIsActiveIsTrue(
			Long createdBy, Long employeeId);

	List<Earmark> findByCreatedByAndEmployee2EmployeeIdNotAndProjectIsNullAndOpportunityIsNotNullAndIsActiveIsTrue(
			Long createdBy, Long employeeId);

	List<Earmark> findByCreatedByNotAndEmployee2EmployeeIdAndProjectIsNullAndOpportunityIsNotNullAndIsActiveIsTrue(
			Long createdBy, Long employeeId);

	public List<Earmark> findByProjectIsNullAndOpportunityIsNotNullAndIsActiveIsTrue();

}
