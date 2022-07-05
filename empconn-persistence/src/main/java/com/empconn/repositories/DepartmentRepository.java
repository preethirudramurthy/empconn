package com.empconn.repositories;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.empconn.persistence.entities.Department;

public interface DepartmentRepository extends JpaRepository<Department, Integer> {
	@Query("select d from Department d where d.name not in ('Dev','QE','UI','Operations') order by name ASC")
	List<Department> findNDDepartment();

	Department findByNameIgnoringCase(String name);

}
