package com.empconn.controller;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.empconn.dto.allocation.AllocationForEarmarkDto;
import com.empconn.dto.allocation.AllocationForSwitchOverDto;
import com.empconn.dto.allocation.AutoRMSearchDto;
import com.empconn.dto.allocation.EarmarkAllocationRequestWrapperDto;
import com.empconn.dto.allocation.EarmarkForAllocationDto;
import com.empconn.dto.allocation.EarmarkSearchDto;
import com.empconn.dto.allocation.IsValidEarmarkAllocationDto;
import com.empconn.dto.allocation.SwitchOverDtlsDto;
import com.empconn.dto.allocation.SwitchOverRequestWrapperDto;
import com.empconn.dto.allocation.SwitchoverSearchDto;
import com.empconn.dto.allocation.ValidateEarmarkAllocateDto;
import com.empconn.dto.allocation.ValidateSwitchOverDto;
import com.empconn.dto.allocation.ValidateSwitchOverResponseDto;
import com.empconn.dto.deallocation.DeallocateWrapperDto;
import com.empconn.dto.earmark.NdRequestListForAllocationDto;
import com.empconn.dto.earmark.NdRequestListForAllocationResponseDto;
import com.empconn.dto.manager.GetResourcesResponseDto;
import com.empconn.dto.ndallocation.IsValidNDRequestDto;
import com.empconn.dto.ndallocation.IsValidNDResponseDto;
import com.empconn.dto.ndallocation.NDAllocateRequest;
import com.empconn.dto.ndallocation.NDAllocateResponseDto;
import com.empconn.dto.ndallocation.NdRequestDetailsForAllocationResponseDto;
import com.empconn.persistence.entities.Project;
import com.empconn.repositories.EmployeeSkillRepository;
import com.empconn.repositories.ProjectRepository;
import com.empconn.service.AllocateEarmarkedService;
import com.empconn.service.AllocateNDService;
import com.empconn.service.AllocateSwitchOverService;
import com.empconn.service.AllocationUtilityService;
import com.empconn.service.DeallocationService;

@RestController
@RequestMapping("allocation")
@CrossOrigin(origins = { "${app.domain}" })
public class AllocationController {

	private static final Logger logger = LoggerFactory.getLogger(AllocationController.class);

	@Autowired
	private DeallocationService deallocationService;

	@Autowired
	AllocateEarmarkedService allocateEarmarkedService;

	@Autowired
	private AllocateNDService allocateNDService;

	@Autowired
	private AllocateSwitchOverService allocateSwitchOverService;

	@Autowired
	private AllocationUtilityService allocationUtilityService;

	@Autowired
	private ProjectRepository projectRepository;

	@Autowired
	private EmployeeSkillRepository employeeSkillRepository;

	@PostMapping("get-n-d-request-list-for-allocation")
	public List<NdRequestListForAllocationResponseDto> getNdRequestListForAllocation(
			@RequestBody NdRequestListForAllocationDto filterCriteriaDto) {
		logger.info("get-n-d-request-list-for-allocation starts execution");
		return allocateNDService.getNdRequestListForAllocation(filterCriteriaDto);
	}

	@PostMapping("get-allocation-list-for-switchover")
	public List<AllocationForSwitchOverDto> getAllocationListForSwitchOver(
			@RequestBody SwitchoverSearchDto allocationSearchDto) {
		return allocateSwitchOverService.getAllocationListForSwitchOver(allocationSearchDto);
	}

	@GetMapping("get-switchover-details-for-allocation")
	public SwitchOverDtlsDto getSwitchOverDetailsForAllocation(@RequestParam Long allocationId,
			@RequestParam Long toProjectId) {
		return allocateSwitchOverService.getSwitchOverDetailsForAllocation(allocationId, toProjectId);
	}

	@PostMapping("is-valid-switchover-allocation")
	public ValidateSwitchOverResponseDto isValidSwitchOverAllocation(@RequestBody ValidateSwitchOverDto dto) {
		return allocateSwitchOverService.isValidSwitchOverAllocation(dto);
	}

	@PreAuthorize("hasAnyRole('RMG', 'RMG_ADMIN')")
	@PostMapping("switchover")
	public void switchOver(@RequestBody SwitchOverRequestWrapperDto requestWrapperDto) {
		allocateSwitchOverService.switchOver(requestWrapperDto.getAllocationList());
	}

	@PostMapping("is-valid-earmark-allocation")
	public IsValidEarmarkAllocationDto isValidEarmarkAllocation(@RequestBody ValidateEarmarkAllocateDto validateDto) {
		return allocateEarmarkedService.isValidEarmarkAllocation(validateDto);
	}

	@GetMapping("get-n-d-request-details-for-allocation")
	public NdRequestDetailsForAllocationResponseDto getNdRequestDetailsForAllocation(@RequestParam Long ndRequestId) {
		logger.info("get-n-d-request-details-for-allocation starts execution");
		return allocateNDService.getNdRequestDetailsForAllocation(ndRequestId);
	}

	@PostMapping("is-valid-nd-allocation")
	public IsValidNDResponseDto isValidNdAllocation(@RequestBody IsValidNDRequestDto isValidNDRequestDto) {
		logger.info("is-valid-nd-allocation starts execution");
		return allocateNDService.isValidNDRequestAllocation(isValidNDRequestDto);
	}

	@PreAuthorize("hasAnyRole('RMG', 'RMG_ADMIN')")
	@PostMapping("nd-allocate")
	public NDAllocateResponseDto ndAllocate(@RequestBody NDAllocateRequest ndAllocateRequest) {
		logger.info("nd-allocate starts execution");
		return allocateNDService.ndAllocate(ndAllocateRequest);
	}

	@GetMapping("get-earmark-details-for-allocation")
	public EarmarkForAllocationDto getEarmarkDetailsForAllocation(@RequestParam Long earmarkId,
			@RequestParam Long projectId) {
		return allocateEarmarkedService.getEarmarkDetailsForAllocation(earmarkId, projectId);
	}

	@PostMapping("get-earmark-list-for-allocation")
	public List<AllocationForEarmarkDto> getEarmarkListForAllocation(@RequestBody EarmarkSearchDto earmarkSearchDto) {
		return allocateEarmarkedService.getEarmarkListForAllocation(earmarkSearchDto);
	}

	@PreAuthorize("hasAnyRole('RMG', 'RMG_ADMIN')")
	@PostMapping("earmark-allocate")
	public void earmarkAllocate(@RequestBody EarmarkAllocationRequestWrapperDto earmarkAllocationRequestWrapperDto) {
		allocateEarmarkedService.earmarkAllocate(earmarkAllocationRequestWrapperDto.getAllocationList());
	}

	@PostMapping("deallocate")
	public void deallocate(@RequestBody DeallocateWrapperDto requestWrapperDto) {
		deallocationService.deallocate(requestWrapperDto.getDeallocationList());
	}

	@PostMapping("get-auto-reporting-manager")
	public GetResourcesResponseDto getAutoReportingManagerId(@RequestBody AutoRMSearchDto autoRMSearchDto) {
		return allocationUtilityService.getAutoReportingManagerId(autoRMSearchDto);
	}

	@GetMapping("test")
	public String test(@RequestParam String name) {
		/*		final List<EmployeeSkill> empSkills = employeeSkillRepository.findByEmployeeEmployeeId(1265L);

		empSkills.stream().forEach(e -> System.out.println(e.getSecondarySkill().getName()));*/


		/*final Employee emp = employeeRepository.findByEmpCodeAndIsActiveTrue("K0152");
		System.out.println(emp.getFullName());*/
		projectRepository.findAll();
		/*final Optional<Project> bench = projectRepository.findById(1L);
		System.out.println(bench.get().getName());*/

		System.out.println("Is this cached");
		final Project bench2 = projectRepository.findByName(name);
		if (bench2 != null) {
			return bench2.getName();
		} else {
			return "Project does not exist";
		}



	}
}
