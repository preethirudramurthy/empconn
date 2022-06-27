package com.empconn.repositories;

import java.util.List;

import javax.persistence.QueryHint;

import org.springframework.data.jpa.repository.QueryHints;
import org.springframework.data.repository.CrudRepository;

import com.empconn.persistence.entities.Title;

public interface TitleRepository extends CrudRepository<Title, Integer> {

	@QueryHints({ @QueryHint(name = "org.hibernate.cacheable", value ="true") })
	List<Title> findByNameContainingIgnoreCaseOrderByName(String partialName);

	@QueryHints({ @QueryHint(name = "org.hibernate.cacheable", value ="true") })
	Title findByNameIgnoringCase(String title);

}
