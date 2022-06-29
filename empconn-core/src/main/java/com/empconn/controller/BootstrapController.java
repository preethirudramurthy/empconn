package com.empconn.controller;

import java.util.List;
import java.util.Set;

import javax.validation.Valid;
import javax.validation.constraints.NotEmpty;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.empconn.dto.ChecklistInactivateDto;
import com.empconn.dto.ChecklistUpdateDto;
import com.empconn.dto.EmployeeRoleAssignmentDto;
import com.empconn.dto.EmployeeSkillRequest;
import com.empconn.dto.HorizontalDto;
import com.empconn.dto.HorizontalRequestDto;
import com.empconn.dto.MasterSkillsResponseDto;
import com.empconn.dto.MasterSkillsUpdateRequestDto;
import com.empconn.dto.MasterSkillsUpdateResponseDto;
import com.empconn.dto.ProjectSubcategoryInactivateDto;
import com.empconn.dto.ProjectSubcategoryUpdateDto;
import com.empconn.dto.VerticalDto;
import com.empconn.dto.VerticalRequestDto;
import com.empconn.service.BootstrapService;

@RestController
@RequestMapping("bootstrap")
@Validated
@CrossOrigin(origins = { "${app.domain}" })
public class BootstrapController {

	private static final Logger logger = LoggerFactory.getLogger(BootstrapController.class);

	@Autowired
	private BootstrapService bootstrapService;

	@GetMapping("/assign-default-role")
	public void assignDefaultRole() {
		logger.debug("Assign default role to all Employees");
		bootstrapService.assignDefaultRole();
	}

	@PostMapping("/assign-roles")
	public void assignRoles(@RequestBody Set<EmployeeRoleAssignmentDto> employeeRoleAssignments) {
		logger.debug("Assign role to all Employees");
		bootstrapService.assignEmployeeRoles(employeeRoleAssignments);
	}

	@PostMapping("/employee-skills")
	public void assignEmployeeSkills(@RequestBody @NotEmpty List<@Valid EmployeeSkillRequest> dto) {
		logger.debug("Assign skills to all Employees");
		bootstrapService.assignEmployeeSkills(dto);
	}

	@PostMapping("/update/vertical")
	public void vertical(@RequestBody @Valid VerticalRequestDto dto) {
		logger.debug("Add or update vertical");
		bootstrapService.vertical(dto);
	}

	@PostMapping("/update/horizontal")
	public void horizontal(@RequestBody @Valid HorizontalRequestDto dto) {
		logger.debug("Add or update horizontal");
		bootstrapService.horizontal(dto);
	}

	@GetMapping("/get-vertical")
	public Set<String> getVertical() {
		logger.debug("Get all active vertical");
		return bootstrapService.getVertical();
	}

	@GetMapping("/get-horizontal")
	public Set<String> getHorizontal() {
		logger.debug("Get all active horizontal");
		return bootstrapService.getHorizontal();
	}

	@PostMapping("/delete/vertical")
	public void softDeleteVertical(@RequestBody @Valid VerticalDto dto) {
		logger.debug("Soft delete vertical");
		bootstrapService.softDeleteVertical(dto);
	}

	@PostMapping("/delete/horizontal")
	public void softDeleteHorizontal(@RequestBody @Valid HorizontalDto dto) {
		logger.debug("Soft delete horizontal");
		bootstrapService.softDeleteHorizontal(dto);
	}

	@GetMapping("get-project-subcategory")
	public List<String> getProjectSubCategoryNames() {
		return bootstrapService.getProjectSubCategoryNames();
	}

	@PostMapping("update-project-subcategory")
	public void addOrUpdateProjectSubCategory(@RequestBody @Valid ProjectSubcategoryUpdateDto dto) {
		bootstrapService.addOrUpdateProjectSubCategory(dto);
	}

	@PostMapping("delete-project-subcategory")
	public void inactivateProjectSubCategory(@RequestBody @Valid ProjectSubcategoryInactivateDto dto) {
		bootstrapService.inactivateProjectSubCategory(dto);
	}

	@GetMapping("get-checklist")
	public List<String> getChecklistNames() {
		return bootstrapService.getChecklistNames();
	}

	@PostMapping("update-checklist")
	public void addOrUpdateChecklist(@RequestBody @Valid ChecklistUpdateDto dto) {
		bootstrapService.addOrUpdateChecklist(dto);
	}

	@PostMapping("delete-checklist")
	public void inactivateChecklist(@RequestBody @Valid ChecklistInactivateDto dto) {
		bootstrapService.inactivateChecklist(dto);
	}

	@PostMapping("/skills")
	public List<MasterSkillsUpdateResponseDto> createOrUpdateMasterSkills(
			@RequestBody @NotEmpty List<@Valid MasterSkillsUpdateRequestDto> masterSkillDtos) {
		return bootstrapService.createOrUpdateMasterSkills(masterSkillDtos);
	}

	@GetMapping("skills")
	public List<MasterSkillsResponseDto> getSkills() {
		return bootstrapService.getSkills();
	}

	@PostMapping("delete-skills")
	public void softDeleteSkills(@RequestBody @Valid MasterSkillsUpdateRequestDto dto) {
		bootstrapService.softDeleteSkills(dto);
	}

}
