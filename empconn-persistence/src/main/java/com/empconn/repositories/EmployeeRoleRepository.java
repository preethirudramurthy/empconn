package com.empconn.repositories;

import java.util.Set;

import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;

import com.empconn.persistence.entities.EmployeeRole;

public interface EmployeeRoleRepository extends CrudRepository<EmployeeRole, Long> {

	@Query("select er from EmployeeRole er where er.employee.employeeId = :employeeId and er.isActive = true")
	public Set<EmployeeRole> findByEmployeeId(@Param("employeeId") Long employeeId);

	public Set<EmployeeRole> findByRoleRoleId(Integer id);

	@Modifying
	@Query("delete from EmployeeRole e where e.employeeRolesId=:id")
	void deleteEmpRoles(Long id);
}
