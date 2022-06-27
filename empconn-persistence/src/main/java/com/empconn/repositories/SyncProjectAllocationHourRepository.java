package com.empconn.repositories;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.empconn.persistence.entities.SyncProjectAllocationHour;

public interface SyncProjectAllocationHourRepository extends JpaRepository<SyncProjectAllocationHour, Long> {

	List<SyncProjectAllocationHour> findAllByStatus(String status);

}
