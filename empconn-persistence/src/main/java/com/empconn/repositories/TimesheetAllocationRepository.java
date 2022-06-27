package com.empconn.repositories;

import org.springframework.data.jpa.repository.JpaRepository;

import com.empconn.persistence.entities.TimesheetAllocation;

public interface TimesheetAllocationRepository extends JpaRepository<TimesheetAllocation, Long> {

	TimesheetAllocation findByAllocationId(Long allocationId);

}
