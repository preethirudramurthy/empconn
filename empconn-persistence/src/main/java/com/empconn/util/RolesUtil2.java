package com.empconn.util;

import java.util.Optional;

import com.empconn.constants.Roles;
import com.empconn.persistence.entities.Employee;
import com.empconn.persistence.entities.EmployeeRole;

public class RolesUtil2 {

	public static Boolean isAManager(Employee employee) {
		final Optional<EmployeeRole> employeeRole = employee.getEmployeeRoles().stream()
				.filter(e -> e.getRole().getName().equalsIgnoreCase(Roles.MANAGER.name())).findAny();

		return employeeRole.isPresent();
	}

	public static Boolean isOnlyGDM(Employee employee) {
		final Optional<EmployeeRole> employeeRole = employee.getEmployeeRoles().stream()
				.filter(e -> e.getRole().getName().equalsIgnoreCase(Roles.GDM.name())
						&& !e.getRole().getName().equalsIgnoreCase(Roles.MANAGER.name()))
				.findAny();

		return employeeRole.isPresent();
	}

	public static Boolean isGDMAndManager(Employee employee) {
		final Optional<EmployeeRole> employeeRole = employee.getEmployeeRoles().stream()
				.filter(e -> e.getRole().getName().equalsIgnoreCase(Roles.GDM.name())
						|| e.getRole().getName().equalsIgnoreCase(Roles.MANAGER.name()))
				.findAny();

		return employeeRole.isPresent();
	}

	public static Boolean isGDM(Employee employee) {
		final Optional<EmployeeRole> employeeRole = employee.getEmployeeRoles().stream()
				.filter(e -> e.getRole().getName().equalsIgnoreCase(Roles.GDM.name()))
				.findAny();

		return employeeRole.isPresent();
	}

	public static Boolean isRMG(Employee employee) {
		final Optional<EmployeeRole> employeeRole = employee.getEmployeeRoles().stream()
				.filter(e -> e.getRole().getName().equalsIgnoreCase(Roles.RMG.name()))
				.findAny();

		return employeeRole.isPresent();
	}
}
