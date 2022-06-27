package com.empconn.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.empconn.service.AllocationService;

@RestController
@RequestMapping("allocation")
//@Profile(value={"dev", "qa"})
@CrossOrigin(origins = { "${app.domain}" })
public class DataController {

	@Autowired
	private AllocationService allocationService;

	@GetMapping("createAllocation")
	public void createAllocation() {
		allocationService.createAllocationForAll();
	}

	@GetMapping("createTimesheetAllocation")
	public void createTimesheetAllocation() {
		allocationService.createTimesheetAllocationForAll();
	}
}
