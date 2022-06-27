package com.empconn.repositories;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.empconn.persistence.entities.SyncProject;

public interface SyncProjectRepository extends JpaRepository<SyncProject, Long> {

	List<SyncProject> findAllByStatus(String status);

}
