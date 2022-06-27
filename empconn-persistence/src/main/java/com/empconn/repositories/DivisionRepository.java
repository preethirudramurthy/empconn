package com.empconn.repositories;

import org.springframework.data.jpa.repository.JpaRepository;

import com.empconn.persistence.entities.Division;

public interface DivisionRepository extends JpaRepository<Division, Integer> {

	Division findByNameIgnoringCase(String name);

}
