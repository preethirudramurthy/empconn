package com.empconn.controller;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.empconn.dto.manager.ChangeGdmRequestDto;
import com.empconn.dto.manager.ChangeManagerWrapperDto;
import com.empconn.dto.manager.ChangeProjectManagerWrapperDto;
import com.empconn.dto.manager.ChangeReportingManagerWrapperDto;
import com.empconn.dto.manager.GetGdmsResponseDto;
import com.empconn.dto.manager.GetManagersResponseDto;
import com.empconn.dto.manager.GetPossibleRepMgrChangeResponseDto;
import com.empconn.dto.manager.GetReporteeListResponseDto;
import com.empconn.dto.manager.GetReportingManagerResponseDto;
import com.empconn.service.ChangeManagerService;
import com.empconn.service.EarmarkService;

@RestController
@RequestMapping("manager")
@CrossOrigin(origins = { "${app.domain}" })
public class ChangeManagerController {
	private static final Logger logger = LoggerFactory.getLogger(ChangeManagerController.class);

	@Autowired
	private ChangeManagerService changeManagerService;

	@Autowired
	EarmarkService earmarkService;

	@GetMapping("get-gdms")
	public GetGdmsResponseDto getGdms(@RequestParam String projectId) {
		logger.info("ChangeManagerController - getGdms - starts");
		return changeManagerService.getGdms(projectId);
	}

	@GetMapping("get-possible-reporting-manger-change")
	public List<GetPossibleRepMgrChangeResponseDto>  getPossibleReportingManagerChange(@RequestParam Long projectId, @RequestParam(required = false) Long devGdmId, @RequestParam(required = false) Long qaGdmId) {
		logger.info("ChangeManagerController - getGdms - starts ");
		return changeManagerService.getPossibleReportingManagerChange(projectId, devGdmId, qaGdmId);
	}

	@PostMapping("change-gdms")
	public void changeGdms(@RequestBody ChangeGdmRequestDto request) {
		logger.info("ChangeManagerController - changeGdms - starts ");
		changeManagerService.changeGdms(request);
	}

	@GetMapping("get-reporting-manager-list")
	public List<GetReportingManagerResponseDto> getReportingManagerList(@RequestParam String resourceId) {
		logger.info("ChangeManagerController - getReportingManagerList - starts ");
		return changeManagerService.getReportingManagerList(resourceId);
	}

	@GetMapping("get-all-managers")
	public List<GetManagersResponseDto> getAllManagers(@RequestParam String projectId) {
		logger.info("ChangeManagerController - getAllManagers - starts ");
		return changeManagerService.getAllManagers(projectId);
	}

	@PostMapping("change-project-mangers")
	public void changeProjectManagers(@RequestBody ChangeProjectManagerWrapperDto request) {
		logger.info("ChangeManagerController - changeProjectMangers - starts ");
		changeManagerService.changeManager(request);
	}

	@PostMapping("change-reporting-and-primary-mangers")
	public void changeReportingAndPrimaryMangers(@RequestBody ChangeManagerWrapperDto request) {
		logger.info("ChangeManagerController - changeReportingAndPrimaryMangers - starts ");
		changeManagerService.changeReportingAndPrimaryMangers(request);
	}

	@PostMapping("change-reporting-mangers")
	public void changeReportingMangers(@RequestBody ChangeReportingManagerWrapperDto request) {
		logger.info("ChangeManagerController - changeReportingAndPrimaryMangers - starts ");
		changeManagerService.changeReportingManagers(request);
	}

	@GetMapping("get-reportee-list")
	public List<GetReporteeListResponseDto> getReporteeList(@RequestParam String reportingManagerResourceId) {
		return changeManagerService.getReporteeList(reportingManagerResourceId);
	}
}
