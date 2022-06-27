package com.empconn.util;

import java.util.Optional;

import com.empconn.constants.Roles;
import com.empconn.persistence.entities.Employee;
import com.empconn.persistence.entities.EmployeeRole;
import com.empconn.persistence.entities.Role;

public class RolesUtil {

	public static Boolean isAManager(Employee employee) {
		final Optional<EmployeeRole> employeeRole = employee.getEmployeeRoles().stream()
				.filter(EmployeeRole::getIsActive)
				.filter(e -> e.getRole().getName().equalsIgnoreCase(Roles.MANAGER.name())).findAny();

		return employeeRole.isPresent();
	}

	public static Boolean isOnlyGDM(Employee employee) {
		final Optional<EmployeeRole> employeeRoleGDM = employee.getEmployeeRoles().stream()
				.filter(EmployeeRole::getIsActive).filter(e -> e.getRole().getName().equalsIgnoreCase(Roles.GDM.name()))
				.findAny();
		final Optional<EmployeeRole> employeeRoleOthers = employee.getEmployeeRoles().stream()
				.filter(EmployeeRole::getIsActive)
				.filter(e -> e.getRole().getName().equalsIgnoreCase(Roles.MANAGER.name())
						|| e.getRole().getName().equalsIgnoreCase(Roles.RMG.name())
						|| e.getRole().getName().equalsIgnoreCase(Roles.RMG_ADMIN.name())
						|| e.getRole().getName().equalsIgnoreCase(Roles.PMO.name())
						|| e.getRole().getName().equalsIgnoreCase(Roles.PMO_ADMIN.name()))
				.findAny();

		return employeeRoleGDM.isPresent() && !employeeRoleOthers.isPresent();
	}

	public static Boolean isGDMAndManager(Employee employee) {
		final Optional<EmployeeRole> employeeRoleGDM = employee.getEmployeeRoles().stream()
				.filter(EmployeeRole::getIsActive).filter(e -> e.getRole().getName().equalsIgnoreCase(Roles.GDM.name()))
				.findAny();
		final Optional<EmployeeRole> employeeRoleManager = employee.getEmployeeRoles().stream()
				.filter(EmployeeRole::getIsActive)
				.filter(e -> e.getRole().getName().equalsIgnoreCase(Roles.MANAGER.name())).findAny();

		return employeeRoleGDM.isPresent() && employeeRoleManager.isPresent();
	}

	public static Boolean isGDM(Employee employee) {
		final Optional<EmployeeRole> employeeRole = employee.getEmployeeRoles().stream()
				.filter(EmployeeRole::getIsActive).filter(e -> e.getRole().getName().equalsIgnoreCase(Roles.GDM.name()))
				.findAny();

		return employeeRole.isPresent();
	}

	public static Boolean isPMO(Employee employee) {
		final Optional<EmployeeRole> employeeRole = employee.getEmployeeRoles().stream()
				.filter(EmployeeRole::getIsActive).filter(e -> e.getRole().getName().equalsIgnoreCase(Roles.PMO.name())
						|| e.getRole().getName().equalsIgnoreCase(Roles.PMO_ADMIN.name()))
				.findAny();

		return employeeRole.isPresent();
	}

	public static Boolean isRMG(Employee employee) {
		final Optional<EmployeeRole> employeeRole = employee.getEmployeeRoles().stream()
				.filter(EmployeeRole::getIsActive).filter(e -> e.getRole().getName().equalsIgnoreCase(Roles.RMG.name())
						|| e.getRole().getName().equalsIgnoreCase(Roles.RMG_ADMIN.name()))
				.findAny();

		return employeeRole.isPresent();
	}

	public static Boolean isSystemUser(Employee employee) {
		final Optional<EmployeeRole> employeeRole = employee.getEmployeeRoles().stream()
				.filter(EmployeeRole::getIsActive)
				.filter(e -> e.getRole().getName().equalsIgnoreCase(Roles.SYSTEM_USER.name())).findAny();

		return employeeRole.isPresent();
	}

	public static boolean isValidRoleForUser(Employee employee, Role Role) {
		final Optional<EmployeeRole> employeeRole = employee.getEmployeeRoles().stream()
				.filter(EmployeeRole::getIsActive).filter(e -> e.getRole().getName().equalsIgnoreCase(Role.getName()))
				.findAny();

		return employeeRole.isPresent();
	}
}
