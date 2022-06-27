package com.empconn.service;

import java.util.List;
import java.util.Set;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.empconn.dto.FMSEmployeeDetailsDto;
import com.empconn.mapper.EmployeeToFMSEMployeeDetailsDtoMapper;
import com.empconn.persistence.entities.Employee;
import com.empconn.repositories.EmployeeRepository;

@Service
public class FMSEmployeeService {

	private static final Logger logger = LoggerFactory.getLogger(FMSEmployeeService.class);

	@Autowired
	private EmployeeRepository employeeRepository;

	@Autowired
	private EmployeeToFMSEMployeeDetailsDtoMapper mapper;

	public List<FMSEmployeeDetailsDto> getAllEmployees() {

		logger.debug("Get all employees to FMS");
		final Set<Employee> employees = employeeRepository.findAllByIsActiveTrue();
		return mapper.employeesToFmsEmployeesDetailsDto(employees);

	}



}
