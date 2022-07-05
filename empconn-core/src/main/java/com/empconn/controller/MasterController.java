package com.empconn.controller;

import java.util.List;
import java.util.Set;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.empconn.dto.AccountDropdownDto;
import com.empconn.dto.IsValidStatusDto;
import com.empconn.dto.MasterResourceDto;
import com.empconn.dto.ProjectDropdownDto;
import com.empconn.dto.manager.GetResourcesResponseDto;
import com.empconn.service.AccountService;
import com.empconn.service.CommonBenchService;
import com.empconn.service.MasterService;
import com.empconn.service.ProjectService;
import com.empconn.vo.UnitValue;

@RestController
@RequestMapping("master")
@Validated
@CrossOrigin(origins = { "${app.domain}" })
public class MasterController {
	private static final Logger logger = LoggerFactory.getLogger(MasterController.class);

	private MasterService masterService;

	private AccountService accountService;

	private ProjectService projectService;

	private CommonBenchService commonBenchService;

	@Autowired
	public void setCommonBenchService(CommonBenchService commonBenchService) {
		this.commonBenchService = commonBenchService;
	}

	@Autowired
	public void setMasterService(MasterService masterService) {
		this.masterService = masterService;
	}

	@Autowired
	public void setAccountService(AccountService accountService) {
		this.accountService = accountService;
	}

	@Autowired
	public void setProjectService(ProjectService projectService) {
		this.projectService = projectService;
	}

	@GetMapping("get-workgroup-dropdown")
	public List<UnitValue> getWorkgroupDropdown() {
		return masterService.getWorkgroupDropdown();
	}

	@GetMapping("get-categories")
	public List<UnitValue> getCategories() {
		return masterService.getProjectCategories();
	}

	@GetMapping("get-sub-categories")
	public List<UnitValue> getSubCategories() {
		return masterService.getSubCategories();
	}

	@GetMapping("get-my-sub-categories")
	public List<UnitValue> getMySubCategories() {
		logger.info("get-my-sub-categories starts execution");
		return masterService.getMySubCategories();
	}

	// @Secured({"ROLE_MANAGER", "ROLE_PMO"})
	@GetMapping("get-verticals")
	public List<UnitValue> getVerticals() {
		return masterService.getVerticals();
	}

	// @Secured("ROLE_PMO")
	@GetMapping("get-horizontals")
	public List<UnitValue> getHorizontals() {
		return masterService.getHorizontals();
	}

	@GetMapping("get-n-d-departments")
	public List<UnitValue> getNDDepartments() {
		return masterService.getNDDepartments();
	}

	@GetMapping("get-my-horizontals")
	public List<UnitValue> getMyHorizontals() {
		logger.info("get-my-horizontals starts execution");
		return masterService.getMyHorizontals();
	}

	@GetMapping("get-check-lists")
	public List<UnitValue> getChecklists() {
		return masterService.getChecklists();
	}

	@GetMapping("get-titles")
	public List<UnitValue> getTitles(@RequestParam String partial) {
		return masterService.getTitles(partial);
	}

	@GetMapping("get-primary-skills")
	public List<UnitValue> getPrimarySkills(@RequestParam String partial) {
		return masterService.getPrimarySkills(partial);
	}

	@GetMapping("get-secondary-skills")
	public List<UnitValue> getSecondarySkills(@RequestParam String primarySkillId) {
		return masterService.getSecondarySkills(primarySkillId);
	}

	@PostMapping("get-secondary-skills")
	public List<UnitValue> getSecondarySkills(@RequestBody List<String> primarySkillIds) {
		return masterService.getSecondarySkills(primarySkillIds);
	}

	@GetMapping("get-resources")
	public List<GetResourcesResponseDto> getResources(@RequestParam String partial) {
		return masterService.getAllResources(partial);
	}

	@GetMapping("get-n-d-resources")
	public Set<MasterResourceDto> getNDResources(@RequestParam String partial) {
		return masterService.getNDResources(partial);
	}

	@GetMapping("get-delivery-resources")
	public List<MasterResourceDto> getDeliveryResources(@RequestParam String partial) {
		return masterService.getDeliveryResources(partial);
	}

	@GetMapping("get-org-locations")
	public List<UnitValue> getOrgLocations() {
		return masterService.getOrgLocations();
	}

	@GetMapping("is-valid-new-account")
	public IsValidStatusDto isValidNewAccount(@RequestParam String accountName) {
		return accountService.isValidNewAccount(accountName);
	}

	@GetMapping("get-active-accounts")
	public List<UnitValue> getActiveAccounts(@RequestParam(required = false) Boolean withBench) {
		return accountService.getActiveAccounts(withBench);
	}

	@GetMapping("get-client-accounts")
	public List<UnitValue> getClientAccounts(@RequestParam(required = false, defaultValue = "") String partial) {
		return accountService.getClientAccounts(partial);
	}

	@GetMapping("get-internal-accounts")
	public List<UnitValue> getInternalAccounts(@RequestParam(required = false, defaultValue = "") String partial) {
		return accountService.getInternalAccounts(partial);
	}

	@GetMapping("get-projects")
	public List<UnitValue> getProjects(@RequestParam(required = false) String accountId,
			@RequestParam(required = false) Boolean isActive,
			@RequestParam(required = false) Boolean onlyFutureReleaseDate,
			@RequestParam(required = false) Boolean withBench, @RequestParam(required = false) String partial) {
		return projectService.getProjects(accountId, isActive, onlyFutureReleaseDate, withBench, partial);
	}

	@PostMapping("get-projects")
	public List<UnitValue> getProjects(@RequestBody ProjectDropdownDto requestparams) {
		return projectService.getProjects(requestparams);
	}

	@GetMapping("get-bench-projects-drop-down")
	public List<UnitValue> getBenchProjectsDropdown(@RequestParam(required = false) Boolean isActive,
			@RequestParam(required = false) Boolean onlyFutureReleaseDate, @RequestParam String partial) {
		// Note: isActive and onlyFutureReleaseDate are taken from swagger of EmpConn 1.
		// Not using them here since it is not called out in FM.
		// Need to remove these attributes in Front end and here as part of clean up
		return commonBenchService.findBenchProjects(partial);
	}

	@GetMapping("get-project-locations")
	public Set<UnitValue> getProjectLocations(@RequestParam String projectId) {
		return projectService.getProjectLocations(projectId);
	}

	@GetMapping("get-my-vertical-drop-down")
	public List<UnitValue> getMyVerticalDropdown(@RequestParam boolean ignoreRole) {
		return masterService.getMyVerticalDropdown(ignoreRole);
	}

	@PostMapping("get-my-account-drop-down")
	public List<UnitValue> getMyAccountDropdown(@RequestBody AccountDropdownDto requestparams) {
		logger.info("Get My-Account Drop Down starts execution");
		return masterService.getMyAccountDropdown(requestparams);
	}

	@PostMapping("get-my-project-drop-down")
	public List<UnitValue> getMyProjectDropdown(@RequestBody ProjectDropdownDto requestparams) {
		logger.info("Get My-Project Drop Down starts execution");
		return masterService.getMyProjectDropdown(requestparams);
	}

}
