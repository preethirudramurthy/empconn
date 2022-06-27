package com.empconn.repositories;

import java.util.List;

import javax.persistence.QueryHint;

import org.springframework.data.jpa.repository.QueryHints;
import org.springframework.data.repository.CrudRepository;

import com.empconn.persistence.entities.Employee;
import com.empconn.persistence.entities.EmployeeSkill;

public interface EmployeeSkillRepository extends CrudRepository<EmployeeSkill, Long> {

	public List<EmployeeSkill> findByEmployeeAndSecondarySkillPrimarySkillNameIgnoreCase(Employee employee, String name);

	@QueryHints({ @QueryHint(name = "org.hibernate.cacheable", value ="true") })
	List<EmployeeSkill> findByEmployeeEmployeeId(Long employeeId);
}
