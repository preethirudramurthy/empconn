package com.empconn.controller;

import java.util.List;
import java.util.Set;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.empconn.dto.SFEmployeeDto;
import com.empconn.service.EmployeeOnboardingService;
import com.empconn.service.EmployeeRoleService;

@RestController
@RequestMapping("employee")
@CrossOrigin(origins = { "${employee.onboarding.domain}" })
public class EmployeeController {

	private static final Logger logger = LoggerFactory.getLogger(EmployeeController.class);

	@Autowired
	private EmployeeOnboardingService employeeOnboardingService;

	@Autowired
	private EmployeeRoleService employeeRoleService;

	@PostMapping
	public void onboardOrUpdateEmployee(@RequestBody Set<SFEmployeeDto> employees) {
		logger.debug("Onboard or update employee");
		employeeOnboardingService.onboardOrUpdateEmployee(employees);
	}

	@GetMapping("/{empCode}/roles")
	public List<String> getRoles(@PathVariable String empCode) {
		logger.debug("Get roles of employee with empCode {}", empCode);
		return employeeRoleService.getRoles(empCode);
	}

}
