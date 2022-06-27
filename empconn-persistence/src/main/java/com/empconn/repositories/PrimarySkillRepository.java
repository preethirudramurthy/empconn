package com.empconn.repositories;

import java.util.List;
import java.util.Optional;

import javax.persistence.QueryHint;

import org.springframework.data.jpa.repository.QueryHints;
import org.springframework.data.repository.CrudRepository;

import com.empconn.persistence.entities.PrimarySkill;

public interface PrimarySkillRepository extends CrudRepository<PrimarySkill, Integer> {

	@QueryHints({ @QueryHint(name = "org.hibernate.cacheable", value = "true") })
	List<PrimarySkill> findByIsActiveTrueAndNameContainingIgnoreCaseOrderByName(String partialName);

	@QueryHints({ @QueryHint(name = "org.hibernate.cacheable", value = "true") })
	Optional<PrimarySkill> findByNameIgnoreCaseAndIsActiveTrue(String primarySkill);

	List<PrimarySkill> findByIsActiveTrue();
}