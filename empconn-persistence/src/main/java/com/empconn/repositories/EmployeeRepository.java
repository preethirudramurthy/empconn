
package com.empconn.repositories;

import java.util.List;
import java.util.Optional;
import java.util.Set;

import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.empconn.persistence.entities.Employee;

public interface EmployeeRepository extends JpaRepository<Employee, Long>, JpaSpecificationExecutor<Employee> {

	/*
	 * @Query("select e from Employee e where e.businessUnit not in(100) and ( upper(e.firstName) like concat('%', upper(?1), '%') or upper(e.lastName) like concat('%', upper(?1), '%'))"
	 * ) public Set<Employee> findByMatchingName(@Param("partialName") String
	 * partialName);
	 */

	@Query("select e from Employee e where (e.businessUnit.name != 'Delivery' or e.division.name != 'Delivery' or e.department.name in ('PMO','SEPG')) and (upper(e.firstName) like concat('%', upper(?1), '%') or upper(e.lastName) like concat('%', upper(?1), '%') or upper(e.empCode) like concat('%', upper(?1), '%')) and e.isActive = true")
	public Set<Employee> findByMatchingNameForND(@Param("partialName") String partialName);

	@Query("select e from Employee e where  e.employeeId in (:resourceIdList) or e.location.locationId in (:orgLocationIdList) or e.title.titleId in (:titleIdList) or e.department.departmentId in(:departmentIdList) ")
	public List<Employee> findByNDResources(List<Long> resourceIdList, List<Integer> orgLocationIdList,
			List<Integer> titleIdList, List<Long> departmentIdList);

	public Employee findByEmployeeId(Long employeeId);

	public Set<Employee> findByIsManager(Boolean isManager);

	public Employee findByLoginId(String loginId);

	Employee findByEmpCodeAndIsActiveTrue(String empCode);

	Employee findByEmpCodeIgnoreCaseAndIsActiveTrue(String empCode);

	public Set<Employee> findAllByIsActiveTrue();

	@Query("select e from Employee e where e.isActive = true")
	public List<Employee> findAllActiveEmployee();

	@EntityGraph(attributePaths = { "employeeRoles", "employeeRoles.role" })
	public List<Employee> findAllByFirstNameIgnoreCaseNotLikeAndIsActiveTrueOrderByFirstName(String firstName);

	@Query("select e.loginId from Employee e where e.isActive = true and e.primaryAllocation.project.projectId = :projectId  and e.primaryAllocation.project.name <> :name  and e.primaryAllocation.project.isActive = true")
	public List<String> getEmployeeLoginIdsOfPrimaryAllocationProjects(Long projectId, String name);

	@Query("select e from Employee e where ( upper(concat(e.firstName, ' ', e.lastName)) like concat('%', upper(:fullName), '%') or upper(e.empCode) like concat('%', upper(:empCode), '%')) and e.isActive = true")
	List<Employee> findEmployeesMatchingNameOrEmpCode(String fullName, String empCode);

	@Query(value = "select t.name as titleName, e.email, e.emp_code, e.first_Name, e.last_Name, e.middle_Name, e.login_id, p.name as projectName, l.name as locationName "
			+ "from cranium.employee e, cranium.title t, cranium.location l, cranium.allocation a, cranium.project p "
			+ "where ((e.first_name ILIKE :empName1%) or (e.last_name ILIKE :empName2%) or (e.first_name ILIKE %:empName3%) or (e.last_name ILIKE %:empName4%)) "
			+ "and e.is_active = true and e.title_id = t.title_id and e.location_id = l.location_id and e.primary_allocation_id = a.allocation_id "
			+ "and a.project_id = p.project_id ORDER BY e.first_name ILIKE :empName5% OR NULL, "
			+ "e.last_name ILIKE :empName6% OR NULL, e.first_name ILIKE %:empName7% OR NULL, e.last_name ILIKE %:empName8% OR NULL LIMIT 10", nativeQuery = true)
	Set<Object[]> findEmployeesMatchingName(String empName1, String empName2, String empName3, String empName4,
			String empName5, String empName6, String empName7, String empName8);

	public List<Employee> findByEmployeeIdIn(List<Long> list);

	@Query("select e from Employee e where (e.businessUnit.name = 'Delivery' and e.division.name = 'Delivery' and e.department.name not in ('PMO','SEPG')) and ( upper(concat(e.firstName, ' ', e.lastName)) like concat('%', upper(:fullName), '%') or upper(e.empCode) like concat('%', upper(:empCode), '%')) and e.isActive = true")
	List<Employee> findMatchingDeliveryResources(String fullName, String empCode);

	public Optional<Employee> findByLoginIdIgnoreCaseAndIsActiveTrue(String loginId);

	public Employee findByEmailAndIsActiveTrue(String username);

	public Employee findByLoginIdAndIsActiveTrue(String username);

}