package com.empconn.repositories;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.empconn.persistence.entities.SyncProjectManager;

public interface SyncProjectManagerRepository extends JpaRepository<SyncProjectManager, Long> {

	List<SyncProjectManager> findAllByStatus(String status);

}
