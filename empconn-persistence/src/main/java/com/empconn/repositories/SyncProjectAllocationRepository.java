package com.empconn.repositories;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.empconn.persistence.entities.SyncProjectAllocation;

public interface SyncProjectAllocationRepository extends JpaRepository<SyncProjectAllocation, Long> {

	List<SyncProjectAllocation> findAllByStatus(String status);

}
