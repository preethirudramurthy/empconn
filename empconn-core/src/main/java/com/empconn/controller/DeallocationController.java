package com.empconn.controller;

import java.util.List;

import javax.validation.Valid;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.empconn.dto.allocation.CalculateAllocationHoursDto;
import com.empconn.dto.allocation.CalculateAllocationHoursResponseDto;
import com.empconn.dto.allocation.ChangeReleaseDateDto;
import com.empconn.dto.allocation.DeallocationResourceListRequestDto;
import com.empconn.dto.allocation.DeallocationResourceListResponseDto;
import com.empconn.dto.allocation.EditReleaseDateResourceListRequestDto;
import com.empconn.dto.allocation.EditReleaseDateResourceListResponseDto;
import com.empconn.service.AllocationHoursService;
import com.empconn.service.EditReleaseDateService;

@RestController
@CrossOrigin(origins = { "${app.domain}" })
public class DeallocationController {

	private static final Logger logger = LoggerFactory.getLogger(DeallocationController.class);

	@Autowired
	private AllocationHoursService allocationHoursService;

	@Autowired
	private EditReleaseDateService editReleaseDateService;


	@PostMapping("/deallocate/get-my-allocatable-resource-list")
	public List<DeallocationResourceListResponseDto> getAllocationResourceList(
			@RequestBody DeallocationResourceListRequestDto filterCriteriaDto) {
		logger.info("get-my-allocatable-resource-list starts execution");
		return editReleaseDateService.getAllocationResourceList(filterCriteriaDto);
	}

	@PostMapping("/releasedate/get-my-allocatable-resource-list")
	public List<EditReleaseDateResourceListResponseDto> getReleaseDateAllocationResourceList(
			@RequestBody EditReleaseDateResourceListRequestDto filterCriteriaDto) {
		logger.info("get-my-allocatable-resource-list starts execution");
		return editReleaseDateService.getReleaseDateAllocationResourceList(filterCriteriaDto);
	}

	@PostMapping("/allocation/change-release-date")
	public void changeReleaseDate(@RequestBody ChangeReleaseDateDto dto) {
		logger.info("changeReleaseDate starts execution");
		editReleaseDateService.changeReleaseDate(dto);
	}

	@GetMapping("/releasedate/calculate-allocation-hours")
	public CalculateAllocationHoursResponseDto changeReleaseDate(@Valid CalculateAllocationHoursDto dto) {
		logger.info("changeReleaseDate starts execution");
		return allocationHoursService.getCalculatedAllocationHours(dto);
	}
}
