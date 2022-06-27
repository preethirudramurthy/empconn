package com.empconn.repositories;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.empconn.persistence.entities.AllocationHour;

public interface AllocationHourRepository extends JpaRepository<AllocationHour, Long> {

	@Query("select ah from AllocationHour ah where ah.allocation.allocationId= ?1")
	List<AllocationHour> findByAllocation(Long allocationId);

}
