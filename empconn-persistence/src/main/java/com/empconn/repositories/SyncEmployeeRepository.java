package com.empconn.repositories;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.empconn.persistence.entities.SyncEmployee;

public interface SyncEmployeeRepository extends JpaRepository<SyncEmployee, Long> {

	List<SyncEmployee> findAllByStatus(String status);

}
