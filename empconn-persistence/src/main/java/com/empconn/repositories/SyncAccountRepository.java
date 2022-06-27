package com.empconn.repositories;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.empconn.persistence.entities.SyncAccount;

public interface SyncAccountRepository extends JpaRepository<SyncAccount, Long> {

	List<SyncAccount> findAllByStatus(String status);

}
