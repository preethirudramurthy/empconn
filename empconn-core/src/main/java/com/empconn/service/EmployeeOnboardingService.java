package com.empconn.service;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;

import com.empconn.constants.ApplicationConstants;
import com.empconn.dto.CancelNDRequestDto;
import com.empconn.dto.SFEmployeeDto;
import com.empconn.email.EmailService;
import com.empconn.mapper.EmployeeToSFEmployeeDtoMapper;
import com.empconn.persistence.entities.Allocation;
import com.empconn.persistence.entities.AllocationDetail;
import com.empconn.persistence.entities.AllocationStatus;
import com.empconn.persistence.entities.Earmark;
import com.empconn.persistence.entities.Employee;
import com.empconn.persistence.entities.EmployeeRole;
import com.empconn.persistence.entities.Location;
import com.empconn.persistence.entities.NdRequest;
import com.empconn.persistence.entities.Project;
import com.empconn.persistence.entities.ProjectLocation;
import com.empconn.persistence.entities.Role;
import com.empconn.persistence.entities.WorkGroup;
import com.empconn.repositories.AllocationRepository;
import com.empconn.repositories.AllocationStatusRepository;
import com.empconn.repositories.EarmarkRepository;
import com.empconn.repositories.EmployeeRepository;
import com.empconn.repositories.EmployeeRoleRepository;
import com.empconn.repositories.LocationRepository;
import com.empconn.repositories.NDRequestRepository;
import com.empconn.repositories.ProjectLocationRespository;
import com.empconn.repositories.RoleRepository;
import com.empconn.repositories.WorkGroupRepository;
import com.empconn.security.SecurityUtil;
import com.empconn.utilities.TimeUtils;

@Service
@Transactional
public class EmployeeOnboardingService {

	private static final Logger logger = LoggerFactory.getLogger(EmployeeOnboardingService.class);

	@Autowired
	private EmployeeRepository employeeRepository;

	@Autowired
	private RoleRepository roleRepository;

	@Autowired
	private EmployeeRoleRepository employeeRoleRepository;

	@Autowired
	private AllocationRepository allocationRepository;

	@Autowired
	private EmployeeToSFEmployeeDtoMapper employeeToSFEmployeeDtoMapper;

	@Autowired
	private MasterAdditionService masterAdditionService;

	@Autowired
	private AllocationService allocationService;

	@Autowired
	private CommonBenchService commonBenchService;

	@Autowired
	private CentralBenchService centralBenchService;

	@Autowired
	private DeliveryBenchService deliveryBenchService;

	@Autowired
	private DeactivationService deactivationService;

	@Autowired
	private NDBenchService ndBenchService;

	@Autowired
	private EmployeeService employeeService;

	@Autowired
	private SecurityUtil securityUtil;

	@Autowired
	private AllocationStatusRepository allocationStatusRepository;

	@Autowired
	private LocationRepository locationRepository;

	@Autowired
	private ProjectLocationRespository projectLocationRespository;

	@Autowired
	private WorkGroupRepository workGroupRepository;

	@Autowired
	private ProjectService projectService;

	@Autowired
	private NonDeliveryService nonDeliveryService;

	@Autowired
	private NDRequestRepository ndRequestRepository;

	@Autowired
	private EarmarkRepository earmarkRepository;

	@Autowired
	private EarmarkService earmarkService;

	@Autowired
	private SyncToTimesheetService syncToTimesheetService;

	@Autowired
	private EmailService emailService;

	@Value("${spring.profiles.active:Local}")
	private String activeProfile;

	public void onboardOrUpdateEmployee(Set<SFEmployeeDto> sfEmployees) {
		logger.debug("Onboard or update employee");

		if (CollectionUtils.isEmpty(sfEmployees)) {
			logger.debug("No employees are sent in the request.");
			return;
		}

		sfEmployees.forEach(this::onboardOrEmployee);

	}

	@Transactional
	public void onboardOrEmployee(SFEmployeeDto sfEmployee) {

		try {
			final Employee employee = employeeRepository.findByEmpCodeAndIsActiveTrue(sfEmployee.getEmpCode());
			final Employee gdm = employeeRepository.findByEmpCodeAndIsActiveTrue(sfEmployee.getGdmId());
			final Employee reportingMamnager = employeeRepository
					.findByEmpCodeAndIsActiveTrue(sfEmployee.getReportingManagerId());

			if (gdm != null && reportingMamnager != null) {
				if (null == employee)
					onboardNewEmployee(sfEmployee);
				else
					updateEmployee(sfEmployee, employee);
			} else {
				logger.error(
						"Exception[{}] in onboarding employee with id [{}]. Skipping this record and proceeding further with other available records.",
						"Either gdmId or reportingManagerId is not present in system", sfEmployee.getEmpCode());
				final Map<String, Object> templateModel = new HashMap<>();
				templateModel.put("environmentName", activeProfile);
				templateModel.put("employee", sfEmployee);
				emailService.send("employee-onboarding-failure", templateModel);
			}

		} catch (final Exception e) {
			logger.error(
					"Exception[{}] in onboarding employee with id [{}]. Skipping this record and proceeding further with other available records.",
					e.getMessage(), sfEmployee.getEmpCode());

			final Map<String, Object> templateModel = new HashMap<>();
			templateModel.put("environmentName", activeProfile);
			templateModel.put("employee", sfEmployee);
			emailService.send("employee-onboarding-failure", templateModel);

		}

	}

	private void updateEmployee(SFEmployeeDto sfEmployee, Employee existingEmployee) {

		final boolean existingDeliveryTypeIsDelivery = employeeService.isDelivery(existingEmployee);
		final int existingDeliveryBenchAllocationPercentage = deliveryBenchService
				.getAvailableBenchAllocation(existingEmployee);
		final int existingNDBenchAllocationPercentage = ndBenchService.getAvailableBenchAllocation(existingEmployee);

		final Employee employee = employeeToSFEmployeeDtoMapper.sfEmployeeDtoToEmployee(sfEmployee, existingEmployee);

		// Add missing master records
		masterAdditionService.addMissingMasterRecords(sfEmployee, employee);

		// Insert the employee
		Employee savedEmployee = employeeRepository.save(employee);

		syncToTimesheetService.syncEmployee(savedEmployee);

		// Set Primary manager and Gdm of the employee
		final Employee reportingManagerFromSF = employeeRepository
				.findByEmpCodeAndIsActiveTrue(sfEmployee.getReportingManagerId());
		if (employeeService.isNotDelivery(savedEmployee)) {
			// if
			// (commonBenchService.onlyActiveAllocationIsNdOrDeliveryBench(savedEmployee))
			savedEmployee.setNdReportingManagerId(reportingManagerFromSF);
			savedEmployee.setEmployee2(employeeRepository.findByEmpCodeAndIsActiveTrue(sfEmployee.getGdmId()));
			savedEmployee = employeeRepository.save(savedEmployee);
		}

		// If Delivery Resource
		// - Set the end date to current date for any active ND Bench allocations and
		// - deactivate the record(This is to cover ND to Delivery use case)
		// - If there is an active Central Bench allocation
		// -- No change needed
		// - Else
		// -- Create an active Central Bench allocation with the details received from
		// SF,
		// - current date as start date and remaining-percentage-available
		// If Non-Delivery Resource
		// - Set the end date to current date for any active Central Bench allocations
		// and
		// - deactivate the record(This is to cover Delivery to ND use case)
		// - If there is an active ND Bench allocation
		// -- Update the reporting and allocation manager received from SF
		// - Else
		// -- Create NDBench record with the details received from SF, current date as
		// -- start date and remaining-percentage-available
		// Note: Remaining percentage available = (100 - sum of other allocation
		// percentage)

		final boolean allocationIsPrimary = commonBenchService.onlyActiveAllocationIsNdOrDeliveryBench(savedEmployee);
		if (employeeService.isDelivery(savedEmployee)) {
			cancelNdRequest(savedEmployee);
			ndBenchService.deactivateBenchAllocation(savedEmployee);
			if (!existingDeliveryTypeIsDelivery && existingNDBenchAllocationPercentage > 0) {
				final Allocation deliveryBenchAllocation = allocationService.createAllocationForEmployee(savedEmployee,
						existingNDBenchAllocationPercentage, getLoggedInEmployee(), allocationIsPrimary, null);
				syncToTimesheetService.syncProjectAllocation(deliveryBenchAllocation);
			}
		} else {
			unEarmark(savedEmployee);

			deliveryBenchService.deactivateDeliveryBenchAllocation(savedEmployee);
			if (ndBenchService.activeBenchAllocationIsAvailable(savedEmployee)) {
				final Allocation benchAllocation = ndBenchService.getBenchAllocation(savedEmployee);
				benchAllocation.setReportingManagerId(reportingManagerFromSF);
				benchAllocation.setAllocationManagerId(reportingManagerFromSF);
				allocationRepository.save(benchAllocation);
			} else {
				if (existingDeliveryTypeIsDelivery && existingDeliveryBenchAllocationPercentage > 0)
					allocationService.createAllocationForEmployee(savedEmployee,
							existingDeliveryBenchAllocationPercentage, getLoggedInEmployee(), allocationIsPrimary,
							reportingManagerFromSF);
			}
		}

		if (!sfEmployee.isActive())
			deactivationService.deactivate(savedEmployee);

	}

	private Long getLoggedInEmployee() {
		return securityUtil.getLoggedInEmployeeId();
	}

	private void unEarmark(final Employee savedEmployee) {
		final List<Earmark> earmarks = earmarkRepository.findByAllocationEmployee(savedEmployee);
		earmarks.forEach(e -> 
			earmarkService.unearmarkBySystem(e, ApplicationConstants.UNEARMARK_ON_EMPLOYEE_UPDATE_COMMENT)
		);
	}

	private void cancelNdRequest(final Employee savedEmployee) {
		final List<NdRequest> ndRequests = ndRequestRepository.findByEmployeeId(savedEmployee.getEmployeeId());
		final List<Long> ndRequestId = ndRequests.stream().map(NdRequest::getNdRequestId).collect(Collectors.toList());

		final CancelNDRequestDto cancelNDRequestDto = new CancelNDRequestDto();
		cancelNDRequestDto.setRequestIdList(ndRequestId);
		nonDeliveryService.cancelRequests(cancelNDRequestDto);
	}

	private void onboardNewEmployee(SFEmployeeDto sfEmployee) {

		final Employee employee = employeeToSFEmployeeDtoMapper.sfEmployeeDtoToEmployee(sfEmployee);
		employee.setCreatedOn(TimeUtils.getCreatedOn());

		// Add missing master records
		masterAdditionService.addMissingMasterRecords(sfEmployee, employee);

		// Insert the employee
		Employee savedEmployee = employeeRepository.save(employee);

		syncToTimesheetService.syncEmployee(savedEmployee);

		// Set Primary manager and Gdm of the employee
		if (employeeService.isDelivery(savedEmployee)) {
			savedEmployee.setEmployee2(centralBenchService.getDevGdm());
			savedEmployee = employeeRepository.save(savedEmployee);
		} else {
			savedEmployee.setNdReportingManagerId(
					employeeRepository.findByEmpCodeAndIsActiveTrue(sfEmployee.getReportingManagerId()));
			savedEmployee.setEmployee2(employeeRepository.findByEmpCodeAndIsActiveTrue(sfEmployee.getGdmId()));
			savedEmployee = employeeRepository.save(savedEmployee);
		}

		// Assign GENERAL role for all employees and additionally Manager role for
		// managers
		employeeRoleRepository.save(getEmployeeRole(savedEmployee, ApplicationConstants.ROLE_GENERAL));
		if (sfEmployee.isManager())
			employeeRoleRepository.save(getEmployeeRole(savedEmployee, ApplicationConstants.ROLE_MANAGER));

		// If delivery resource
		// Create central-bench record including the allocation/reporting manager of the
		// employee
		// If non-delivery resource
		// Create nd-bench record including the allocation/reporting manager of the
		// employee
		onboardEmployee(savedEmployee, sfEmployee.getReportingManagerId());

	}

	private EmployeeRole getEmployeeRole(final Employee employee, String roleName) {
		final EmployeeRole generalEmployeeRole = new EmployeeRole();
		final Role generalRole = roleRepository.findByName(roleName);
		generalEmployeeRole.setEmployee(employee);
		generalEmployeeRole.setRole(generalRole);
		generalEmployeeRole.setIsActive(true);
		generalEmployeeRole.setCreatedBy(getLoggedInEmployee());
		return generalEmployeeRole;
	}

	// @Transactional
	public void onboardEmployee(Employee employee, String reportingManagerEmpCode) {

		final Allocation allocation = new Allocation();

		final AllocationStatus pureBenchStatus = allocationStatusRepository
				.findByStatus(ApplicationConstants.ALLOCATION_STATUS_PB);
		final boolean isDelivery = employeeService.isDelivery(employee);
		final Project bench = projectService.getBenchProject(isDelivery);
		final Location location = locationRepository.findByName(ApplicationConstants.LOCATION_GLOBAL);
		final ProjectLocation globalBenchProjectLocation = projectLocationRespository
				.findByProjectProjectIdAndLocationLocationId(bench.getProjectId(), location.getLocationId()).get(0);
		final String workgroupName = (isDelivery) ? ApplicationConstants.WORK_GROUP_DEV
				: ApplicationConstants.WORK_GROUP_SUPPORT_1;
		final WorkGroup workGroup = workGroupRepository.findByName(workgroupName);
		final Employee manager = (isDelivery) ? globalBenchProjectLocation.getEmployee1()
				: employeeRepository.findByEmpCodeAndIsActiveTrue(reportingManagerEmpCode);

		allocation.setAllocationManagerId(manager);
		allocation.setReportingManagerId(manager);
		allocation.setAllocationStatus(pureBenchStatus);
		allocation.setCreatedBy(getLoggedInEmployee());
		allocation.setEmployee(employee);
		allocation.setIsActive(Boolean.TRUE);
		allocation.setProject(bench);
		allocation.setProjectLocation(globalBenchProjectLocation);
		allocation.setWorkGroup(workGroup);
		allocation.setCreatedOn(TimeUtils.getCreatedOn());
		allocation.setIsBillable(false);
		final AllocationDetail allocationDetail = new AllocationDetail();
		allocationDetail.setAllocatedPercentage(100);
		allocationDetail.setCreatedBy(getLoggedInEmployee());
		allocationDetail.setIsActive(Boolean.TRUE);
		allocationDetail.setStartDate(TimeUtils.getToday());
		allocationDetail.setCreatedOn(TimeUtils.getCreatedOn());
		allocationDetail.setAllocation(allocation);
		allocation.setAllocationDetails(Arrays.asList(allocationDetail));
		employee.setPrimaryAllocation(allocation);
		allocationRepository.save(allocation);
		if (isDelivery) {
			syncToTimesheetService.syncProjectAllocation(allocation);
		}
	}

}
