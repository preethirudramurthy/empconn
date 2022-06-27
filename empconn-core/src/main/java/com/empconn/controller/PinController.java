
package com.empconn.controller;

import java.util.Date;
import java.util.List;
import java.util.Set;

import javax.validation.Valid;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.empconn.dto.AccountDetailsDto;
import com.empconn.dto.AccountStatusChangeDto;
import com.empconn.dto.AccountSummaryDto;
import com.empconn.dto.ChecklistDto;
import com.empconn.dto.ChecklistPmoDto;
import com.empconn.dto.IsValidDto;
import com.empconn.dto.IsValidSalesforceDto;
import com.empconn.dto.MyPinDto;
import com.empconn.dto.PinCountDto;
import com.empconn.dto.PinDetailsDto;
import com.empconn.dto.PinStatusChangeCommentDto;
import com.empconn.dto.PinStatusChangeDto;
import com.empconn.dto.PinStatusChangedDto;
import com.empconn.dto.ProjectDetailsDto;
import com.empconn.dto.ProjectEndDateChangeDto;
import com.empconn.dto.ProjectEndDateChangedDto;
import com.empconn.dto.ProjectStatusChangeCommentDto;
import com.empconn.dto.ProjectSummaryDto;
import com.empconn.dto.ProjectSummarySearchDto;
import com.empconn.dto.ResourceRequirementDto;
import com.empconn.dto.SaveAccountDto;
import com.empconn.dto.SavePinDto;
import com.empconn.dto.SavedAccountDto;
import com.empconn.dto.SavedPinDto;
import com.empconn.dto.UpdateProjectDetailsDto;
import com.empconn.dto.ValidatedPinDto;
import com.empconn.dto.map.MapMigrateResponseDto;
import com.empconn.map.MapService;
import com.empconn.service.AccountService;
import com.empconn.service.PinService;
import com.empconn.service.ProjectService;
import com.empconn.vo.UnitValue;

import io.swagger.annotations.ApiOperation;

@RestController
@CrossOrigin(origins = { "${app.domain}" })
public class PinController {

	private static final Logger logger = LoggerFactory.getLogger(PinController.class);

	/*
	 * @Value("${app.domain}") private String domain;
	 */

	@Autowired
	private PinService pinService;

	@Autowired
	private AccountService accountService;

	@Autowired
	private ProjectService projectService;

	@Autowired
	MapService mapService;

	@GetMapping("/pin/get-pin-count")
	public PinCountDto getPinCount() {
		return pinService.getPinCount();
	}

	@GetMapping("/pin/get-my-pin-list")
	public List<MyPinDto> getMyPinList() {
		return pinService.getMyPinList();
	}

	@GetMapping("/pin/get-pins-for-review-list")
	public List<MyPinDto> getPinsForReviewList() {
		return pinService.getPinsForReviewList();
	}

	@GetMapping("/pin/get-pins-for-approval-list")
	public List<MyPinDto> getPinsForApprovalList() {
		return pinService.getPinsForApproval();
	}

	@GetMapping("/pin/get-pin-details")
	public PinDetailsDto getPinDetails(@RequestParam String projectId) {
		return pinService.getPinDetails(projectId);
		// return PinResponse.getpinDetails();
	}

	@GetMapping("/pin/is-valid-new-project")
	public IsValidDto isValidNewProject(@RequestParam String accountId, @RequestParam String projectName) {
		return projectService.isValidNewProjectInAccount(projectName, accountId);
	}

	@GetMapping("/account/get-account-summary-list")
	public List<AccountSummaryDto> getAccountSummaryList(
			@RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) Date fromStartDate,
			@RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) Date toStartDate) {
		logger.info("Get Account Summary List starts execution");
		return accountService.getAccountSummaryList(fromStartDate, toStartDate);
	}

	@GetMapping("/account/get-account-details")
	public AccountDetailsDto getAccountDetails(@RequestParam String accountId) {
		return accountService.getAccountDetails(accountId);
	}

	@PostMapping("/project/get-project-summary-list")
	public List<ProjectSummaryDto> getProjectSummaryList(@RequestBody(required = false) ProjectSummarySearchDto dto) {
		return projectService.getProjectSummaryList(dto);
	}

	@GetMapping("/project/get-project-details")
	public ProjectDetailsDto getProjectDetails(@RequestParam String projectId) {
		return projectService.getProjectDetails(projectId);
	}

	@PostMapping("/account/save-account")
	public SavedAccountDto saveAccount(@RequestBody SaveAccountDto accountDto) {
		logger.debug("Save account");
		return accountService.saveAccount(accountDto);

	}

	@PostMapping("/pin/save-draft-pin")
	public SavedPinDto saveDraftPin(@RequestBody SavePinDto savePinDto) {
		return pinService.saveDraftPin(savePinDto);
	}

	@PostMapping("/pin/validate-pin")
	public ValidatedPinDto validatePin(@RequestBody @Valid SavePinDto savePinDto) {
		return pinService.validatePin(savePinDto);
	}

	@PostMapping("/pin/save-resource-requirement")
	public void saveResourceRequirement(@RequestBody ResourceRequirementDto resourceRequirementDto) {
		pinService.saveResourceRequirement(resourceRequirementDto);
	}

	@PostMapping("/pin/save-checklist")
	public void saveChecklist(@RequestBody ChecklistDto checklistDto) {
		pinService.saveChecklist(checklistDto);
	}

	@PostMapping("/pin/save-checklist-pmo")
	public void saveChecklistPmo(@RequestBody ChecklistPmoDto checklistPmoDto) {
		pinService.saveChecklistPmo(checklistPmoDto);
	}

	@PostMapping("/pin/submit-pin")
	public void submitPin(@RequestBody PinStatusChangeDto submitDto) {
		pinService.submitPin(submitDto);
	}

	@PostMapping("/pin/reject-pin-on-review")
	public void rejectPinOnReview(@RequestBody PinStatusChangeCommentDto rejectDto) {
		pinService.rejectPinOnReview(rejectDto);
	}

	@PostMapping("/pin/send-back-pin-on-review")
	public void sendBackPinOnReview(@RequestBody PinStatusChangeCommentDto sendbackDto) {
		pinService.sendBackPinOnReview(sendbackDto);
	}

	@PostMapping("/pin/submit-pin-for-approval")
	public void submitPinForApproval(@RequestBody PinStatusChangeDto submitApproveDto) {
		pinService.submitPinForApproval(submitApproveDto);
	}

	@PostMapping("/pin/approve-pin")
	public void approvePin(@RequestBody PinStatusChangeDto approveDto) {
		pinService.approvePin(approveDto);
	}

	@PostMapping("/pin/reject-pin")
	public void rejectPin(@RequestBody PinStatusChangeCommentDto rejectDto) {
		pinService.rejectPin(rejectDto);
	}

	@PostMapping("/pin/cancel-pin")
	public void cancelPin(@RequestBody PinStatusChangeDto cancelDto) {
		pinService.cancelPin(cancelDto);
		return;
	}

	@PostMapping("/pin/is-valid-salesforce-ids-for-the-project")
	public IsValidDto isValidSalesforceIdsForTheProject(@RequestBody @Valid IsValidSalesforceDto isValidSalesforceDto) {
		return pinService.isValidSalesForceIdsForTheProject(isValidSalesforceDto);
	}

	@PostMapping("/project/update-project-details")
	public void updateProjectDetails(@RequestBody UpdateProjectDetailsDto updateProjectDto) {
		projectService.updateProjectDetails(updateProjectDto);
	}

	@PostMapping("/project/change-project-end-date")
	public ProjectEndDateChangedDto changeProjectEndDate(@RequestBody ProjectEndDateChangeDto endChangeDto) {
		return projectService.changeProjectEndDate(endChangeDto);
	}

	@PostMapping("/project/put-on-hold")
	public ProjectStatusChangeCommentDto putOnHold(@RequestBody PinStatusChangeCommentDto putHoldDto) {
		return projectService.projectOnHold(putHoldDto);
	}

	@PostMapping("/project/activate")
	public PinStatusChangedDto activate(@RequestBody PinStatusChangeDto activateDto) {
		return projectService.activateProject(activateDto);
	}

	@PostMapping("/project/deactivate")
	public PinStatusChangedDto deactivate(@RequestBody PinStatusChangeDto deactivateDto) {
		return projectService.deactivateProject(deactivateDto);
	}

	@GetMapping("/resource/get-all-roles")
	public Set<UnitValue> getAllRoles() {
		return pinService.getAllRoles();
	}

	@PostMapping("/map/migrate-accounts-projects")
	public Set<MapMigrateResponseDto> migrateAllAccountsAndProjectsToMap() {
		return mapService.migrateAllAccountsAndProjectsToMap();
	}

	@ApiOperation(value = "activateAccount", notes = "Use any one of accountId or accountName to update the accountStatus")
	@PostMapping("/account/activate")
	public void activateAccount(@RequestBody AccountStatusChangeDto dto) {
		accountService.activateAccount(dto);
	}

}