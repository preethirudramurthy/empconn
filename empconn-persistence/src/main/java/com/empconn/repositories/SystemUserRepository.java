package com.empconn.repositories;

import javax.persistence.QueryHint;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.QueryHints;
import org.springframework.stereotype.Repository;

import com.empconn.persistence.entities.SystemUser;

@Repository
public interface SystemUserRepository extends JpaRepository<SystemUser, Integer> {

	@QueryHints({ @QueryHint(name = "org.hibernate.cacheable", value ="true") })
	SystemUser findByEmployeeEmployeeId(Long employeeId);

}
