package com.empconn.controller;

import java.util.List;
import java.util.Set;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

import com.empconn.dto.EmployeeDataResponseDto;
import com.empconn.dto.EmployeeInfoResponseDto;
import com.empconn.service.PulseService;

@RestController
@CrossOrigin(origins = { "${app.domain}" })
public class PulseController {

	private static final Logger logger = LoggerFactory.getLogger(PulseController.class);

	@Autowired
	private PulseService pulseService;

	@GetMapping("/employee/{loginId}")
	public EmployeeInfoResponseDto getEmployeeDetails(@PathVariable String loginId) {
		logger.info("Inside PulseController for getEmployeeDetails");
		return pulseService.getEmployeeDetail(loginId);
	}

	@GetMapping("/suggest/{empCode}")
	public Set<EmployeeDataResponseDto> getAllEmployeesBasedOnProject(@PathVariable String empCode) {
		logger.info("Inside PulseController for getAllEmployeesBasedOnProject");
		return pulseService.getAllEmployeesBasedOnProject(empCode);
	}

	@GetMapping("/search/{empName}")
	public List<EmployeeDataResponseDto> getAllEmployeesPartialSearch(@PathVariable String empName) {
		logger.info("Inside PulseController for getAllEmployeesBasedOnProject");
		return pulseService.getAllEmployeesPartialSearch(empName);
	}
}
