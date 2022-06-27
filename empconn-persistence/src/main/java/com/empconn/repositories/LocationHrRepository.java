package com.empconn.repositories;

import java.util.Set;

import javax.persistence.QueryHint;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.QueryHints;

import com.empconn.persistence.entities.LocationHr;

public interface LocationHrRepository extends JpaRepository<LocationHr, Integer> {

	@QueryHints({ @QueryHint(name = "org.hibernate.cacheable", value ="true") })
	public Set<LocationHr> findByLocationLocationId(Integer locationId);

}
