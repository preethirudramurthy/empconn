package com.empconn.repositories;

import java.util.List;
import java.util.Optional;
import java.util.Set;

import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import com.empconn.persistence.entities.Checklist;

@Repository
public interface ChecklistRespository extends CrudRepository<Checklist, Integer>, JpaSpecificationExecutor<Checklist> {

	List<Checklist> findByIsActiveTrue(Sort by);

	Optional<Checklist> findByNameIgnoreCaseEqualsAndIsActiveTrue(String oldChecklist);

	Set<Checklist> findByIsActive(boolean b);
}
