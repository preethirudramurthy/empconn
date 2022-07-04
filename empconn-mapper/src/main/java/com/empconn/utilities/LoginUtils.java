package com.empconn.utilities;

import java.util.HashSet;
import java.util.Set;

import com.empconn.persistence.entities.Employee;
import com.empconn.persistence.entities.EmployeeRole;
import com.empconn.persistence.entities.Role;

public class LoginUtils {

	

	protected static final Set<Employee> dummyUsers = new HashSet<>();
	
	
	public static void createDummyUsers() {

		final Set<Employee> mockUsers = new HashSet<>();

		final Employee man1 = new Employee();
		final Employee man2 = new Employee();
		final Employee man3 = new Employee();
		final Employee man4 = new Employee();
		final Employee man5 = new Employee();
		final Employee gdm1 = new Employee();
		final Employee gdm2 = new Employee();
		final Employee gdm3 = new Employee();
		final Employee gdm4 = new Employee();
		final Employee pmo1 = new Employee();
		final Employee pmo2 = new Employee();
		final Employee rmg1 = new Employee();
		final Employee rmg2 = new Employee();

		final EmployeeRole employeeManager1Role = new EmployeeRole();
		final EmployeeRole employeeManager2Role = new EmployeeRole();
		final EmployeeRole employeeManager3Role = new EmployeeRole();
		final EmployeeRole employeeManager4Role = new EmployeeRole();
		final EmployeeRole employeeManager5Role = new EmployeeRole();
		final EmployeeRole employeeGdm1Role = new EmployeeRole();
		final EmployeeRole employeeGdm2Role = new EmployeeRole();
		final EmployeeRole employeeGdm3Role = new EmployeeRole();
		final EmployeeRole employeeGdm4Role = new EmployeeRole();
		final EmployeeRole employeePmo1Role = new EmployeeRole();
		final EmployeeRole employeePmo2Role = new EmployeeRole();
		final EmployeeRole employeeRmg1Role = new EmployeeRole();
		final EmployeeRole employeeRmg2Role = new EmployeeRole();

		final Role managerRole = new Role();
		final Role gdmRole = new Role();
		final Role pmoRole = new Role();
		final Role rmgRole = new Role();

		managerRole.setRoleId(102);
		managerRole.setName("MANAGER");
		gdmRole.setRoleId(103);
		gdmRole.setName("GDM");
		pmoRole.setRoleId(100);
		pmoRole.setName("PMO");
		rmgRole.setRoleId(104);
		rmgRole.setName("RMG");

		employeeManager1Role.setRole(managerRole);
		employeeManager1Role.setEmployeeRolesId((long) 1);
		employeeManager1Role.setEmployee(man1);
		employeeManager2Role.setRole(managerRole);
		employeeManager2Role.setEmployeeRolesId((long) 10);
		employeeManager2Role.setEmployee(man2);
		employeeManager3Role.setRole(managerRole);
		employeeManager3Role.setEmployeeRolesId((long) 11);
		employeeManager3Role.setEmployee(man3);
		employeeManager4Role.setRole(managerRole);
		employeeManager4Role.setEmployeeRolesId((long) 12);
		employeeManager4Role.setEmployee(man4);
		employeeManager5Role.setRole(managerRole);
		employeeManager5Role.setEmployeeRolesId((long) 13);
		employeeManager5Role.setEmployee(man5);

		employeeGdm1Role.setRole(gdmRole);
		employeeGdm1Role.setEmployeeRolesId((long) 2);
		employeeGdm1Role.setEmployee(gdm1);
		employeeGdm2Role.setRole(gdmRole);
		employeeGdm2Role.setEmployeeRolesId((long) 3);
		employeeGdm2Role.setEmployee(gdm2);
		employeeGdm3Role.setRole(gdmRole);
		employeeGdm3Role.setEmployeeRolesId((long) 4);
		employeeGdm3Role.setEmployee(gdm3);
		employeeGdm4Role.setRole(gdmRole);
		employeeGdm4Role.setEmployeeRolesId((long) 5);
		employeeGdm4Role.setEmployee(gdm4);

		employeePmo1Role.setRole(pmoRole);
		employeePmo1Role.setEmployeeRolesId((long) 6);
		employeePmo1Role.setEmployee(pmo1);
		employeePmo2Role.setRole(pmoRole);
		employeePmo2Role.setEmployeeRolesId((long) 7);
		employeePmo2Role.setEmployee(pmo2);

		employeeRmg1Role.setRole(rmgRole);
		employeeRmg1Role.setEmployeeRolesId((long) 8);
		employeeRmg1Role.setEmployee(rmg1);
		employeeRmg1Role.setRole(rmgRole);
		employeeRmg1Role.setEmployeeRolesId((long) 9);
		employeeRmg1Role.setEmployee(rmg2);

		final Set<EmployeeRole> employeeRolesManager1 = new HashSet<>();
		final Set<EmployeeRole> employeeRolesManager2 = new HashSet<>();
		final Set<EmployeeRole> employeeRolesManager3 = new HashSet<>();
		final Set<EmployeeRole> employeeRolesManager4 = new HashSet<>();
		final Set<EmployeeRole> employeeRolesManager5 = new HashSet<>();
		final Set<EmployeeRole> employeeRolesGdm1 = new HashSet<>();
		final Set<EmployeeRole> employeeRolesGdm2 = new HashSet<>();
		final Set<EmployeeRole> employeeRolesGdm3 = new HashSet<>();
		final Set<EmployeeRole> employeeRolesGdm4 = new HashSet<>();
		final Set<EmployeeRole> employeeRolesPmo1 = new HashSet<>();
		final Set<EmployeeRole> employeeRolesPmo2 = new HashSet<>();
		final Set<EmployeeRole> employeeRolesRmg1 = new HashSet<>();
		final Set<EmployeeRole> employeeRolesRmg2 = new HashSet<>();

		employeeRolesManager1.add(employeeManager1Role);
		employeeRolesManager2.add(employeeManager2Role);
		employeeRolesManager3.add(employeeManager3Role);
		employeeRolesManager4.add(employeeManager4Role);
		employeeRolesManager5.add(employeeManager5Role);
		employeeRolesGdm1.add(employeeGdm1Role);
		employeeRolesGdm2.add(employeeGdm2Role);
		employeeRolesGdm3.add(employeeGdm3Role);
		employeeRolesGdm4.add(employeeGdm4Role);
		employeeRolesPmo1.add(employeePmo1Role);
		employeeRolesPmo2.add(employeePmo2Role);
		employeeRolesRmg1.add(employeeRmg1Role);
		employeeRolesRmg2.add(employeeRmg2Role);

		man1.setEmployeeId((long) 101);
		man1.setFirstName("MAN-1");
		man1.setLastName("MAN-1");
		man1.setEmpCode("man-1");
		man1.setEmail("man1@test.com");
		man1.setLoginId("man-1");
		man1.setEmployeeRoles(employeeRolesManager1);

		man2.setEmployeeId((long) 110);
		man2.setFirstName("MAN-2");
		man2.setLastName("MAN-2");
		man2.setEmpCode("man-2");
		man2.setEmail("man2@test.com");
		man2.setLoginId("man-2");
		man2.setEmployeeRoles(employeeRolesManager2);

		man3.setEmployeeId((long) 111);
		man3.setFirstName("MAN-3");
		man3.setLastName("MAN-3");
		man3.setEmpCode("man-3");
		man3.setEmail("man3@test.com");
		man3.setLoginId("man-3");
		man3.setEmployeeRoles(employeeRolesManager3);

		man4.setEmployeeId((long) 112);
		man4.setFirstName("MAN-4");
		man4.setLastName("MAN-4");
		man4.setEmpCode("man-4");
		man4.setEmail("man4@test.com");
		man4.setLoginId("man-4");
		man4.setEmployeeRoles(employeeRolesManager4);

		man5.setEmployeeId((long) 113);
		man5.setFirstName("MAN-5");
		man5.setLastName("MAN-5");
		man5.setEmpCode("man-5");
		man5.setEmail("man5@test.com");
		man5.setLoginId("man-5");
		man5.setEmployeeRoles(employeeRolesManager5);

		gdm1.setEmployeeId((long) 102);
		gdm1.setFirstName("GDM-1");
		gdm1.setLastName("GDM-1");
		gdm1.setEmpCode("gdm-1");
		gdm1.setEmail("gdm1@test.com");
		gdm1.setLoginId("gdm-1");
		gdm1.setEmployeeRoles(employeeRolesGdm1);

		gdm2.setEmployeeId((long) 103);
		gdm2.setFirstName("GDM-2");
		gdm2.setLastName("GDM-2");
		gdm2.setEmpCode("gdm-2");
		gdm2.setEmail("gdm2@test.com");
		gdm2.setLoginId("gdm-2");
		gdm2.setEmployeeRoles(employeeRolesGdm2);

		gdm3.setEmployeeId((long) 104);
		gdm3.setFirstName("GDM-3");
		gdm3.setLastName("GDM-3");
		gdm3.setEmpCode("gdm-3");
		gdm3.setEmail("gdm3@test.com");
		gdm3.setLoginId("gdm-3");
		gdm3.setEmployeeRoles(employeeRolesGdm3);

		gdm4.setEmployeeId((long) 105);
		gdm4.setFirstName("GDM-4");
		gdm4.setLastName("GDM-4");
		gdm4.setEmpCode("gdm-4");
		gdm4.setEmail("gdm4@test.com");
		gdm4.setLoginId("gdm-4");
		gdm4.setEmployeeRoles(employeeRolesGdm4);

		pmo1.setEmployeeId((long) 106);
		pmo1.setFirstName("PMO-1");
		pmo1.setLastName("PMO-1");
		pmo1.setEmpCode("pmo-1");
		pmo1.setEmail("pmo1@test.com");
		pmo1.setLoginId("pmo-1");
		pmo1.setEmployeeRoles(employeeRolesPmo1);

		pmo2.setEmployeeId((long) 107);
		pmo2.setFirstName("PMO-2");
		pmo2.setLastName("PMO-2");
		pmo2.setEmpCode("pmo-2");
		pmo2.setEmail("pmo2@test.com");
		pmo2.setLoginId("pmo-2");
		pmo2.setEmployeeRoles(employeeRolesPmo2);

		rmg1.setEmployeeId((long) 108);
		rmg1.setFirstName("RMG-1");
		rmg1.setLastName("RMG-1");
		rmg1.setEmpCode("rmg-1");
		rmg1.setEmail("rmg1@test.com");
		rmg1.setLoginId("rmg-1");
		rmg1.setEmployeeRoles(employeeRolesRmg1);

		rmg2.setEmployeeId((long) 109);
		rmg2.setFirstName("RMG-2");
		rmg2.setLastName("RMG-2");
		rmg2.setEmpCode("rmg-2");
		rmg2.setEmail("rmg2@test.com");
		rmg2.setLoginId("rmg-2");
		rmg2.setEmployeeRoles(employeeRolesRmg2);

		mockUsers.add(man1);
		mockUsers.add(man2);
		mockUsers.add(man3);
		mockUsers.add(man4);
		mockUsers.add(man5);
		mockUsers.add(gdm1);
		mockUsers.add(gdm2);
		mockUsers.add(gdm3);
		mockUsers.add(gdm4);
		mockUsers.add(pmo1);
		mockUsers.add(pmo2);
		mockUsers.add(rmg1);
		mockUsers.add(rmg2);

		dummyUsers.clear();
		dummyUsers.addAll(mockUsers);
	}

}
