package com.empconn.repositories;

import java.util.List;

import javax.persistence.QueryHint;

import org.springframework.data.jpa.repository.QueryHints;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import com.empconn.persistence.entities.AllocationStatus;

@Repository
public interface AllocationStatusRepository extends CrudRepository<AllocationStatus, Long>{

	@QueryHints({ @QueryHint(name = "org.hibernate.cacheable", value ="true") })
	List<AllocationStatus> findByIsActive(Boolean flag);

	@QueryHints({ @QueryHint(name = "org.hibernate.cacheable", value ="true") })
	AllocationStatus findByStatus(String status);

}