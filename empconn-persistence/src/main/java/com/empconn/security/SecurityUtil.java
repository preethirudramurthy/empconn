package com.empconn.security;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import com.empconn.persistence.entities.Employee;
import com.empconn.repositories.EmployeeRepository;

@Service
public class SecurityUtil {

	@Autowired
	private EmployeeRepository employeeRepository;

	public Employee getLoggedInEmployee() {

		final Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
		final String currentUserName = authentication.getName();
		return employeeRepository.findByLoginId(currentUserName);

	}

	public Long getLoggedInEmployeeId() {
		return getLoggedInEmployee().getEmployeeId();
	}
}
