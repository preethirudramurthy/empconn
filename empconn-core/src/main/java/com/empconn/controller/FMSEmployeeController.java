package com.empconn.controller;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.empconn.dto.FMSEmployeeDetailsDto;
import com.empconn.service.FMSEmployeeService;

@RestController
@RequestMapping("employees")
@CrossOrigin(origins = {"${fms.domain}"})
public class FMSEmployeeController {

	private static final Logger logger = LoggerFactory.getLogger(FMSEmployeeController.class);

	@Autowired
	private FMSEmployeeService fmsEmployeeService;

	@GetMapping
	public List<FMSEmployeeDetailsDto> getAllEmployees() {
		logger.debug("Get all employees");
		return fmsEmployeeService.getAllEmployees();
	}

}
