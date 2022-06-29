package com.empconn.service;

import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;

import com.empconn.persistence.entities.Employee;

@Service
public class EmployeeService {

	private static final String DEPARTMENT_PMO = "PMO";
	private static final String DEPARTMENT_SEPG = "SEPG";
	private static final String DIVISION_DELIVERY = "Delivery";
	private static final String BU_DELIVERY = "Delivery";

	public boolean isDelivery(Employee employee) {

		return ((StringUtils.equalsIgnoreCase(BU_DELIVERY, getBusinessUnitName(employee))
				&& StringUtils.equalsIgnoreCase(DIVISION_DELIVERY, getDivisionName(employee)))
				&& !(StringUtils.equalsIgnoreCase(DEPARTMENT_SEPG, getDepartmentName(employee))
						|| StringUtils.equalsIgnoreCase(DEPARTMENT_PMO, getDepartmentName(employee))));

	}

	public Boolean isNotDelivery(Employee employee) {
		return !isDelivery(employee);
	}

	private String getDepartmentName(Employee employee) {
		return employee.getDepartment().getName();
	}

	private String getDivisionName(Employee employee) {
		return employee.getDivision().getName();
	}

	private String getBusinessUnitName(Employee employee) {
		return employee.getBusinessUnit().getName();
	}

}
