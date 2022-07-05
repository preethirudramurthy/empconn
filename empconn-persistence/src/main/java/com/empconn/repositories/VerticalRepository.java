package com.empconn.repositories;

import java.util.List;
import java.util.Optional;
import java.util.Set;

import javax.persistence.QueryHint;

import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.jpa.repository.QueryHints;
import org.springframework.transaction.annotation.Transactional;

import com.empconn.persistence.entities.Vertical;

public interface VerticalRepository extends JpaRepository<Vertical, Integer> {

	@QueryHints({ @QueryHint(name = "org.hibernate.cacheable", value ="true") })
	List<Vertical> findByIsActiveOrderByName(Boolean flag);

	Optional<Vertical> findByNameIgnoreCaseAndIsActiveTrue(String name);

	Iterable<Vertical> findByIsActiveTrue(Sort sort);

	Set<Vertical> findByIsActiveTrue();

	@Transactional
	@Modifying
	@Query("UPDATE Vertical v SET v.isActive = 'false' WHERE v.name = :name")
	void softDelete(String name);

}
