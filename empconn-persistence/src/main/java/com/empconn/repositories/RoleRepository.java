package com.empconn.repositories;

import java.util.List;

import javax.persistence.QueryHint;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.QueryHints;
import org.springframework.stereotype.Repository;

import com.empconn.persistence.entities.Role;

@Repository
public interface RoleRepository extends JpaRepository<Role, Integer> {

	@QueryHints({ @QueryHint(name = "org.hibernate.cacheable", value = "true") })
	Role findByName(String name);

	List<Role> findByNameIn(List<String> roleNames);

	Role findByNameIgnoreCase(String role);

}
