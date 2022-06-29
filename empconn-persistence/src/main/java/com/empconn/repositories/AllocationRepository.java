package com.empconn.repositories;

import java.util.Date;
import java.util.List;
import java.util.Optional;
import java.util.Set;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import com.empconn.dto.allocation.AllocationSummaryDto;
import com.empconn.persistence.entities.Allocation;
import com.empconn.persistence.entities.AllocationStatus;

@Repository
public interface AllocationRepository extends JpaRepository<Allocation, Long>, JpaSpecificationExecutor<Allocation> {

	List<Allocation> findByIsActive(Boolean flag);

	@Transactional
	@Modifying
	@Query("UPDATE Allocation a SET a.releaseDate = :newEndDate WHERE (a.releaseDate = :prevEndDate or a.releaseDate > :newEndDate) AND a.project.projectId = :projectId AND a.isActive = 'true'")
	public Integer changeReleaseDateForResources(Long projectId, Date newEndDate, Date prevEndDate);

	@Query("select a from Allocation a where a.project.projectId = :projectId AND a.isActive='true' and (a.releaseDate = :prevEndDate or a.releaseDate > :newEndDate)")
	public Set<Allocation> findByProjectId(Long projectId, Date newEndDate, Date prevEndDate);

	@Query("select a from Allocation a where a.project.projectId = ?1 AND a.isActive='true'")
	public Set<Allocation> findByProjectId(Long projectId);

	public Set<Allocation> findByProjectProjectIdInAndIsActiveTrue(Set<Long> projectId);

	Optional<Allocation> findByAllocationIdAndIsActive(Long allocationId, Boolean status);

	List<Allocation> findByAllocationIdNotAndEmployeeEmployeeIdAndProjectProjectIdAndProjectLocationProjectLocationIdAndWorkGroupWorkGroupId(
			Long allocationId, Long employeeId, Long projectId, Long projectLocationId, Integer workGroupId);

	List<Allocation> findByTimesheetAllocationAllocationId(Long allocationId);

	List<Allocation> findByAllocationIdNotAndEmployeeEmployeeIdAndProjectProjectIdAndProjectLocationProjectLocationIdAndWorkGroupWorkGroupIdAndIsActiveTrue(
			Long allocationId, Long employeeId, Long projectId, Long projectLocationId, Integer workGroupId);

	Optional<Allocation> findByEmployeeEmployeeIdAndProjectProjectIdAndProjectLocationProjectLocationIdAndWorkGroupWorkGroupIdAndIsBillableAndReleaseDateAndIsActive(
			Long employeeId, Long projectId, Long projectLocationId, Integer workGroupId, Boolean isBillable,
			Date releaseDate, Boolean status);

	List<Allocation> findByAllocationIdNotAndEmployeeEmployeeIdAndProjectProjectIdAndProjectLocationProjectLocationIdAndWorkGroupWorkGroupIdAndTimesheetAllocationAllocationIdIsNull(
			Long allocationId, Long employeeId, Long projectId, Long projectLocationId, Integer workGroupId);

	Optional<Allocation> findByAllocationIdNotAndEmployeeEmployeeIdAndProjectProjectIdAndProjectLocationProjectLocationIdAndWorkGroupWorkGroupIdAndTimesheetAllocationAllocationIdIsNullAndIsActiveTrue(
			Long allocationId, Long employeeId, Long projectId, Long projectLocationId, Integer workGroupId);

	List<Allocation> findByEmployeeEmployeeIdAndProjectProjectIdAndProjectLocationProjectLocationIdAndWorkGroupWorkGroupIdAndTimesheetAllocationAllocationId(
			Long employeeId, Long projectId, Long projectLocationId, Integer workGroupId, Long allocationId);

	Set<Allocation> findByEmployeeEmployeeIdAndIsActive(Long employeeId, Boolean isActive);

	@Query("select a from Allocation a where a.employee.employeeId = ?1 and a.isActive = ?2 and a.project.account.name != 'Bench'")
	Set<Allocation> findByEmployeeIdAndIsActive(Long employeeId, Boolean isActive);

	@Query("select a from Allocation a where a.employee.employeeId = ?1 and a.isActive = ?2 and a.project.account.name = 'Bench'")
	Set<Allocation> findByEmployeeIdAndBenchProject(Long employeeId, Boolean isActive);

	@Query("select a from Allocation a where a.employee.employeeId = ?1 and a.isActive = ?2 and a.project.account.name = 'Bench' and lower(a.project.name) != 'ndbench'")
	Set<Allocation> findByEmployeeIdAndDeliveryBenchProject(Long employeeId, Boolean isActive);

	@Query("SELECT new com.empconn.dto.allocation.AllocationSummaryDto(a.project.account.name as accountName,a.project.projectId as projectId, a.project.name as projectName, "
			+ "sum(allocationDetail.allocatedPercentage) as allocatedPercentage, a.reportingManagerId as reportingManger) "
			+ "FROM Allocation AS a join a.allocationDetails allocationDetail "
			+ "where a.employee.employeeId = :employeeId and allocationDetail.isActive = true and a.isActive = true GROUP BY a.project.account.name,a.project.projectId,a.project.name, a.reportingManagerId ")
	List<AllocationSummaryDto> getAllocationSummary(Long employeeId);

	List<Allocation> findByEmployeeEmployeeIdAndProjectProjectIdAndIsActive(Long employeeId, Long projectId,
			Boolean status);

	Allocation findFirstByEmployeeEmployeeIdAndProjectProjectIdAndIsActive(Long employeeId, Long projectId,
			Boolean status);

	Optional<Allocation> findOneByEmployeeEmployeeIdAndProjectProjectIdAndIsActive(Long employeeId, Long projectId,
			Boolean status);

	Allocation findTop1ByEmployeeEmployeeIdAndProjectProjectIdOrderByCreatedOnAsc(Long employeeId, Long projectId);

	List<Allocation> findByEmployeeEmployeeIdAndProjectProjectIdAndIsActiveTrue(Long employeeId, Long projectId);

	@Query("SELECT COUNT( DISTINCT a.employee.employeeId) FROM Allocation a WHERE a.project.projectId = :projectId and a.isActive = true")
	Integer findNumberOfResourcesForProject(Long projectId);

	@Query("select a.project.projectId from Allocation a where a.allocationId = ?1")
	public Long getProjectIde(Long allocationId);

	@Query("select case when count(a) < 1 then true else false end from Allocation a where a.isActive = true and a.employee.employeeId = :employeeId and a.projectLocation.project.name not in ('Central Bench', 'NDBench')")
	boolean onlyActiveAllocationIsNdOrCentralBench(Long employeeId);

	@Query("select case when count(a) < 1 then true else false end from Allocation a where a.isActive = true and a.employee.employeeId = :employeeId and lower(a.project.account.name) != 'bench'")
	boolean onlyActiveAllocationIsNdOrDeliveryBench(Long employeeId);

	@Query("select a from Allocation a where a.isActive = true and a.employee.employeeId = :employeeId and a.projectLocation.project.name = :projectName")
	public Allocation getAllocation(Long employeeId, String projectName);

	List<Allocation> findByEmployeeEmployeeIdAndProjectProjectIdAndIsActiveIsTrue(Long employeeId, Long projectId);

	@Query("select a from Allocation a where a.employee.employeeId = ?1 and isActive = true")
	public List<Allocation> getAllActiveAllocationsOfEmployee(Long employeeId);

	@Transactional
	@Modifying
	@Query("UPDATE Allocation a SET a.allocationStatus = :allocationStatus WHERE a.allocationId = :allocationId")
	public Integer updateAllocatedStatus(AllocationStatus allocationStatus, Long allocationId);

	@Query("select a from Allocation a where a.isActive = true and a.reportingManagerId.employeeId = :employeeId and lower(a.projectLocation.project.name) != 'ndbench'")
	Set<Allocation> getReporteesExcludingNdBench(Long employeeId);

	Set<Allocation> findByProjectLocationProjectLocationIdAndAllocationManagerIdEmployeeIdAndIsActive(Long projectId,
			Long employeeId, Boolean isActive);

	Set<Allocation> findByProjectProjectIdAndReportingManagerIdEmployeeIdAndIsActive(Long projectId, Long employeeId,
			Boolean isActive);

	Set<Allocation> findByEmployeeEmployeeId(Long employeeId);

	Set<Allocation> findByProjectLocationProjectLocationIdAndReportingManagerIdEmployeeIdAndIsActive(
			Long projectLocationId, Long employeeId, Boolean isActive);

	@Query("select a FROM Allocation a where a.releaseDate = ?1 and a.isActive = true")
	List<Allocation> findByReleaseDateAndIsActiveIsTrue(Date releaseDate);

	@Query("SELECT a FROM Allocation a WHERE a.releaseDate < :currentDate and a.isActive = true")
	List<Allocation> findReleaseDatePast(Date currentDate);

	
}
