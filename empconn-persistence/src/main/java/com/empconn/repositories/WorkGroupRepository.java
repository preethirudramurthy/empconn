package com.empconn.repositories;

import java.util.List;

import javax.persistence.QueryHint;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.QueryHints;
import org.springframework.stereotype.Repository;

import com.empconn.persistence.entities.WorkGroup;

@Repository
public interface WorkGroupRepository extends JpaRepository<WorkGroup, Integer>{

	@QueryHints({ @QueryHint(name = "org.hibernate.cacheable", value ="true") })
	List<WorkGroup> findByIsActiveOrderByHierarchyAsc(Boolean flag);

	@QueryHints({ @QueryHint(name = "org.hibernate.cacheable", value ="true") })
	WorkGroup findByName(String name);

}