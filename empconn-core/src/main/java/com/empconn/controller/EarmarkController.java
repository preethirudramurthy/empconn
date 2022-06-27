package com.empconn.controller;

import java.util.List;
import java.util.Set;

import javax.validation.Valid;

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

import com.empconn.dto.EarmarkDto;
import com.empconn.dto.earmark.AvailableResourceDto;
import com.empconn.dto.earmark.AvailableResourceReqDto;
import com.empconn.dto.earmark.DropdownGDMDto;
import com.empconn.dto.earmark.DropdownManagerDto;
import com.empconn.dto.earmark.EarmarkAvailabilityDto;
import com.empconn.dto.earmark.EarmarkDetailsDto;
import com.empconn.dto.earmark.EarmarkEngineersGdmReqDto;
import com.empconn.dto.earmark.EarmarkEngineersManagerReqDto;
import com.empconn.dto.earmark.EarmarkEngineersRmgReqDto;
import com.empconn.dto.earmark.EarmarkItemDto;
import com.empconn.dto.earmark.EarmarkOppurtunityDto;
import com.empconn.dto.earmark.EarmarkProjectDto;
import com.empconn.dto.earmark.EarmarkedDropdownReqDto;
import com.empconn.dto.earmark.GdmManagerDropdownReqDto;
import com.empconn.dto.earmark.ManagerInfoDto;
import com.empconn.dto.earmark.UpdateEarmarkDto;
import com.empconn.service.AllocationService;
import com.empconn.service.EarmarkService;
import com.empconn.service.EarmarkedEngineerProjectService;
import com.empconn.vo.UnitValue;

@RestController
@RequestMapping("earmark")
@CrossOrigin(origins = { "${app.domain}" })
public class EarmarkController {
	private static final Logger logger = LoggerFactory.getLogger(EarmarkController.class);
	@Autowired
	AllocationService allocationService;

	@Autowired
	EarmarkService earmarkService;

	@Autowired
	EarmarkedEngineerProjectService earmarkedEngineerService;

	@GetMapping("earmark-availability")
	public EarmarkAvailabilityDto getEarmarkAvailability(@RequestParam String allocationId) {
		return earmarkService.getEarmarkAvailability(allocationId);
	}

	@GetMapping("get-earmark-details")
	public EarmarkDetailsDto getEarmarkDetails(@RequestParam List<String> earmarkId) {
		return earmarkService.getMoreDetails(earmarkId);
	}

	@PostMapping("earmark-for-project")
	public void earmarkForProject(@RequestBody EarmarkProjectDto earmarkProjectDto) {
		earmarkService.saveEarmarkProject(earmarkProjectDto);
	}

	@PostMapping("earmark-for-opportunity")
	public void earmarkForOppurtunity(@RequestBody EarmarkOppurtunityDto earmarkOppurtunityDto) {
		earmarkService.saveEarmarkForOppurtunity(earmarkOppurtunityDto);
	}

	@PostMapping("unearmark")
	public void unEarmark(@RequestBody EarmarkDto earmarkDto) {
		earmarkService.unEarmark(earmarkDto.getEarmarkIdList());
	}

	@PostMapping("update-earmark")
	public void updateEarmark(@RequestBody UpdateEarmarkDto updateEarmarkDto) {
		logger.info("Update Earmark Starts Execution");
		earmarkService.updateEarmark(updateEarmarkDto);
	}

	@PostMapping("get-available-resource-list")
	public List<AvailableResourceDto> getAvailableResourceList(
			@RequestBody AvailableResourceReqDto availableResourceDto) {
		return earmarkService.getResourceList(availableResourceDto);
	}

	@PostMapping("get-gdm-drop-down")
	public List<ManagerInfoDto> getGdmDropdown(@RequestBody DropdownGDMDto gdmDropdownDto) {
		return earmarkService.getAllGdm(gdmDropdownDto);
	}

	@PostMapping("get-manager-drop-down")
	public Set<ManagerInfoDto> getManagerDropdown(@RequestBody DropdownManagerDto managerDropdownDto) {
		return earmarkService.getManagerDropdown(managerDropdownDto);
	}

	@PostMapping("get-earmark-list-as-manager")
	public List<EarmarkItemDto> getEarmarkListAsManager(@RequestBody EarmarkEngineersManagerReqDto dto) {
		return earmarkService.getEarmarkListAsManager(dto);
	}

	@PostMapping("get-earmark-list-as-gdm-manager")
	public List<EarmarkItemDto> getEarmarkListAsGdmManager(@RequestBody EarmarkEngineersGdmReqDto dto) {
		logger.info("Get Earmark List As GdmManager starts Execution");
		return earmarkService.getEarmarkListAsGdmManager(dto);
	}

	@PostMapping("get-earmark-list-as-rmg")
	public List<EarmarkItemDto> getEarmarkListAsRmg(@RequestBody EarmarkEngineersRmgReqDto dto) {
		logger.info("Get Earmark List As RMG starts Execution");
		return earmarkService.getEarmarkListAsRmg(dto);
	}

	@PostMapping("get-earmarked-vertical-dropdown")
	public List<UnitValue> getEarmarkedVerticalDropdown(@RequestBody EarmarkedDropdownReqDto dto) {
		return earmarkService.getEarmarkedVerticalDropdown(dto);
	}

	@PostMapping("get-earmarked-account-dropdown")
	public List<UnitValue> getEarmarkedAccountDropdown(@RequestBody EarmarkedDropdownReqDto dto) {
		return earmarkService.getEarmarkedAccountDropdown(dto);
	}

	@PostMapping("get-earmarked-project-dropdown")
	public List<UnitValue> getEarmarkedProjectDropdown(@RequestBody EarmarkedDropdownReqDto dto) {
		return earmarkService.getEarmarkedProjectDropdown(dto);
	}

	@PostMapping("get-earmarked-salesforce-search")
	public List<String> getEarmarkedSalesforceSearch(@RequestBody @Valid EarmarkedDropdownReqDto dto) {
		return earmarkService.getEarmarkedSalesforceSearch(dto);
	}

	@PostMapping("get-earmarked-gdm-dropdown")
	public List<ManagerInfoDto> getEarmarkedGdmDropdown(@RequestBody GdmManagerDropdownReqDto dto) {
		return earmarkService.getEarmarkedGdmDropdown(dto);
	}

	@PostMapping("get-earmarked-manager-dropdown")
	public List<ManagerInfoDto> getEarmarkedManagerDropdown(@RequestBody GdmManagerDropdownReqDto dto) {
		return earmarkService.getEarmarkedManagerDropdown(dto);
	}

	@GetMapping("get-selected-manager-drop-down")
	public Set<ManagerInfoDto> getManagerDropdown(@RequestParam String projectId) {
		return earmarkService.getSelectedManagerDropdown(projectId);
	}

}
