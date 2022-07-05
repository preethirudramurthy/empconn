package com.empconn.repositories;

import java.util.List;

import javax.transaction.Transactional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

import com.empconn.persistence.entities.NdRequest;

public interface NDRequestRepository extends JpaRepository<NdRequest, Long> , JpaSpecificationExecutor<NdRequest>{
	@Query("select ndr from NdRequest ndr where ndr.isActive = true")
	List<NdRequest> getAllActiveNDRequests();

	@Transactional
	@Modifying
	@Query(value = "update NdRequest ndr set ndr.isActive = :status where ndr.ndRequestId = :ndRequestId")
	void updateNDRequestStatus(Long ndRequestId, Boolean status);

	@Query("select ndr from NdRequest ndr where ndr.isActive = true and ndr.project.projectId= :projectId and ndr.employee1.employeeId = :employeeId" )
	List<NdRequest> findExistingNdRequest(Long projectId, Long employeeId);

	@Query("select ndr from NdRequest ndr where ndr.isActive = true order by ndr.employee1.firstName asc")
	List<NdRequest> getNDRequestsAsManager(Long employeeId);

	@Query("select ndr from NdRequest ndr, Project p where ndr.project.projectId = p.projectId and ndr.isActive = true and (p.employee1.employeeId = :employeeId or p.employee2.employeeId = :employeeId or p.employee3.employeeId = :employeeId) order by ndr.employee1.firstName asc")
	List<NdRequest> getNDRequestsAsGDM(Long employeeId);

	@Query("select ndr from NdRequest ndr where ndr.isActive = true and ndr.employee1.employeeId= :employeeId")
	List<NdRequest> findByEmployeeId(Long employeeId);
}
