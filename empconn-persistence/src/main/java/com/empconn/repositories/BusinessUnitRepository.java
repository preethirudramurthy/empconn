package com.empconn.repositories;

import org.springframework.data.jpa.repository.JpaRepository;

import com.empconn.persistence.entities.BusinessUnit;

public interface BusinessUnitRepository extends JpaRepository<BusinessUnit, Integer> {

	BusinessUnit findByNameIgnoringCase(String name);

}
