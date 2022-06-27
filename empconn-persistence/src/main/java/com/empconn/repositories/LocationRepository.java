package com.empconn.repositories;

import java.util.List;

import javax.persistence.QueryHint;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.jpa.repository.QueryHints;

import com.empconn.persistence.entities.Location;

public interface LocationRepository extends JpaRepository<Location, Integer> {

	@QueryHints({ @QueryHint(name = "org.hibernate.cacheable", value ="true") })
	Location findByName(String name);

	@QueryHints({ @QueryHint(name = "org.hibernate.cacheable", value ="true") })
	Location findByNameIgnoringCase(String name);

	@Query("SELECT max(hierarchy) FROM Location l WHERE l.isActive = true")
	@QueryHints({ @QueryHint(name = "org.hibernate.cacheable", value ="true") })
	Integer findMaxHierarchy();

	@Query("SELECT l FROM Location l WHERE l.isActive = true and l.name <> 'Global' order by name")
	@QueryHints({ @QueryHint(name = "org.hibernate.cacheable", value ="true") })
	List<Location> findAllExcludingInternalLocations();

}
