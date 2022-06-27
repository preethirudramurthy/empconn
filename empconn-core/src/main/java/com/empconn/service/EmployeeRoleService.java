package com.empconn.service;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.Set;
import java.util.stream.Collectors;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.thymeleaf.util.StringUtils;

import com.empconn.persistence.entities.Employee;
import com.empconn.persistence.entities.EmployeeRole;
import com.empconn.persistence.entities.Role;
import com.empconn.repositories.EmployeeRepository;
import com.empconn.repositories.EmployeeRoleRepository;
import com.empconn.repositories.RoleRepository;
import com.empconn.security.SecurityUtil;

@Service
public class EmployeeRoleService {

	private static final Logger logger = LoggerFactory.getLogger(EmployeeRoleService.class);

	@Autowired
	private EmployeeRepository employeeRepository;

	@Autowired
	private EmployeeRoleRepository employeeRoleRepository;

	@Autowired
	private RoleRepository roleRepository;

	@Autowired
	private SecurityUtil securityUtil;

	public void assignRoleIfNotSet(final Employee employee, String role) {
		final Set<EmployeeRole> employeeRoles = employeeRoleRepository.findByEmployeeId(employee.getEmployeeId());
		if (roleIsNotSet(employeeRoles, role)) {
			addEmployeeRole(employee, role);
		}
	}

	@Transactional
	private void addEmployeeRole(final Employee employee, final String roleName) {
		final EmployeeRole employeeRole = new EmployeeRole();
		employeeRole.setEmployee(employee);
		final Role role = roleRepository
				.findByName(StringUtils.toUpperCase(StringUtils.trim(roleName), Locale.ENGLISH));
		if (null == role) {
			logger.error(
					"{} is not a valid role. Can't assign this to employee {}. Proceeding further with other available requests",
					roleName, employee.getEmpCode());
			return;
		}
		employeeRole.setRole(role);
		employeeRole.setCreatedBy(securityUtil.getLoggedInEmployeeId());
		employeeRole.setIsActive(true);

		employeeRoleRepository.save(employeeRole);
	}

	private boolean roleIsNotSet(Set<EmployeeRole> employeeRoles, String role) {

		for (final EmployeeRole employeeRole : employeeRoles)
			if (StringUtils.equalsIgnoreCase(role, employeeRole.getRole().getName())) {
				logger.debug("Role {} is already set for employee {}", role, employeeRole.getEmployee().getEmpCode());
				return false;
			}

		return true;

	}

	public void removeCurrentRolesNotPartOf(Employee employee, Set<String> roleNames) {
		// GENERAL role should not be removed even if it is not sent in the input. So
		// adding to the role name set to prevent this
		roleNames.add("GENERAL");
		final Set<EmployeeRole> employeeRoles = employee.getEmployeeRoles().stream()
				.filter(er -> er.getIsActive() == true).collect(Collectors.toSet());
		final List<EmployeeRole> currentRolesNotInNewRoles = employeeRoles.stream()
				.filter(er -> !roleNames.contains(er.getRole().getName())).collect(Collectors.toList());

		currentRolesNotInNewRoles.forEach(er -> employeeRoleRepository.deleteEmpRoles(er.getEmployeeRolesId()));

	}

	public List<String> getRoles(String empCode) {
		final Employee employee = employeeRepository.findByEmpCodeAndIsActiveTrue(empCode);

		if (null == employee) {
			logger.error("Employee with empCode {} doesn't exist. Skipping this employee.", empCode);
			return new ArrayList<>();
		}

		final Set<EmployeeRole> employeeRoles = employee.getEmployeeRoles();
		return employeeRoles.stream().map(er -> er.getRole().getName()).collect(Collectors.toList());
	}

}