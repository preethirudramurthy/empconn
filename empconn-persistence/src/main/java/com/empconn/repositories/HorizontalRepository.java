package com.empconn.repositories;

import java.util.Optional;
import java.util.Set;

import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.transaction.annotation.Transactional;

import com.empconn.persistence.entities.Horizontal;


public interface HorizontalRepository extends JpaRepository<Horizontal, Integer> {
	Iterable<Horizontal> findByIsActiveTrue(Sort sort);

	Optional<Horizontal> findByNameIgnoreCaseAndIsActiveTrue(String name);

	Set<Horizontal> findByIsActiveTrue();

	@Transactional
	@Modifying
	@Query("UPDATE Horizontal h SET h.isActive = 'false' WHERE h.name = :name")
	public void softDelete(String name);
}
