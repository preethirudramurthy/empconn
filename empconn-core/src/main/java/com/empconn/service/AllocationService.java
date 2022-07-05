
package com.empconn.service;

import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.*;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.empconn.constants.ApplicationConstants;
import com.empconn.dto.allocation.AllocationRequestDto;
import com.empconn.persistence.entities.Allocation;
import com.empconn.persistence.entities.AllocationDetail;
import com.empconn.persistence.entities.AllocationStatus;
import com.empconn.persistence.entities.Employee;
import com.empconn.persistence.entities.Project;
import com.empconn.persistence.entities.ProjectLocation;
import com.empconn.persistence.entities.SalesforceIdentifier;
import com.empconn.persistence.entities.TimesheetAllocation;
import com.empconn.persistence.entities.WorkGroup;
import com.empconn.repositories.AllocationRepository;
import com.empconn.repositories.AllocationStatusRepository;
import com.empconn.repositories.EmployeeRepository;
import com.empconn.repositories.ProjectLocationRespository;
import com.empconn.repositories.ProjectRepository;
import com.empconn.repositories.TimesheetAllocationRepository;
import com.empconn.repositories.WorkGroupRepository;
import com.empconn.security.SecurityUtil;
import com.empconn.successfactor.service.SFIntegrationService;
import com.empconn.successfactors.dto.GdmChangeDto;
import com.empconn.successfactors.dto.ManagerChangeDto;
import com.empconn.successfactors.dto.ProjectChangeDto;
import com.empconn.util.AllocationUtil;
import com.empconn.utilities.DateUtils;
import com.empconn.utilities.TimeUtils;
import com.google.common.base.Predicates;
import com.google.common.collect.Iterables;

@Service
@Transactional(readOnly = true)
public class AllocationService {

	@Autowired
	private AllocationRepository allocationRepository;

	@Autowired
	private ProjectRepository projectRepository;

	@Autowired
	private EmployeeRepository employeeRepository;

	@Autowired
	private AllocationStatusRepository allocationStatusRepository;

	@Autowired
	private WorkGroupRepository workGroupRepository;

	@Autowired
	private ProjectLocationRespository projectLocationRespository;

	@Autowired
	private SecurityUtil securityUtil;

	@Autowired
	private ProjectService projectService;

	@Autowired
	private EmployeeService employeeService;

	@Autowired
	private CommonBenchService benchService;

	@Autowired
	private AllocationUtilityService allocationUtilityService;

	@Autowired
	private SFIntegrationService sfIntegrationService;

	@Autowired
	private AllocationHoursService allocationHoursService;

	@Autowired
	private DeallocationService deallocationService;

	@Autowired
	private SyncToTimesheetService syncToTimesheetService;


	@Autowired
	private TimesheetAllocationRepository timesheetAllocationRepository;

	@Transactional
	public void createAllocationForAll() {

		List<Employee> allEmployees = employeeRepository.findAll();
		allEmployees = allEmployees.stream().filter(e -> !e.getEmpCode().equals("1A003")).collect(Collectors.toList());

		final AllocationStatus pureBenchStatus = allocationStatusRepository
				.findByStatus(ApplicationConstants.ALLOCATION_STATUS_PB);

		final Map<Boolean, Project> benchProjectMap = new HashMap<>();
		benchProjectMap.put(Boolean.TRUE, projectService.getBenchProject(true));
		benchProjectMap.put(Boolean.FALSE, projectService.getBenchProject(false));

		final Map<Boolean, ProjectLocation> benchProjectLocationMap = new HashMap<>();
		benchProjectLocationMap.put(Boolean.TRUE, benchService.getGlobalBenchProjectLocation(benchProjectMap.get(true)));
		benchProjectLocationMap.put(Boolean.FALSE, benchService.getGlobalBenchProjectLocation(benchProjectMap.get(false)));


		final Map<Boolean, WorkGroup> workgroupMap = new HashMap<>();
		workgroupMap.put(Boolean.TRUE, workGroupRepository.findByName(ApplicationConstants.WORK_GROUP_DEV));
		workgroupMap.put(Boolean.FALSE, workGroupRepository.findByName(ApplicationConstants.WORK_GROUP_SUPPORT_1));

		for (final Employee employee : allEmployees) {
			final Set<Allocation> a = allocationRepository.findByEmployeeEmployeeId(employee.getEmployeeId());
			allocationRepository.deleteAll(a);
			final long createdBy = 1L;
			final boolean allocationIsPrimary = true;
			Employee manager = null;
			final boolean isDelivery = employeeService.isDelivery(employee);
			final Project bench = benchProjectMap.get(isDelivery);
			final ProjectLocation globalBenchProjectLocation = benchProjectLocationMap.get(isDelivery);
			final WorkGroup workGroup = workgroupMap.get(isDelivery);
			final int allocationPercentage=100;

			if (isDelivery)
				manager = globalBenchProjectLocation.getEmployee1();
			else
				manager = employee.getNdReportingManagerId();

			createAllocation(employee, createdBy, allocationIsPrimary, pureBenchStatus, bench, globalBenchProjectLocation,
					workGroup, manager, allocationPercentage);
		}

	}

	@Transactional
	public Allocation createAllocationForEmployee(Employee e, final int allocationPercentage, final long createdBy,
			final boolean allocationIsPrimary, final Employee reportingManager) {
		final AllocationStatus pureBenchStatus = allocationStatusRepository
				.findByStatus(ApplicationConstants.ALLOCATION_STATUS_PB);
		final boolean isDelivery = employeeService.isDelivery(e);
		final Project bench = projectService.getBenchProject(isDelivery);
		final ProjectLocation globalBenchProjectLocation = benchService.getGlobalBenchProjectLocation(bench);
		final WorkGroup workGroup = workGroupRepository.findByName(
				isDelivery ? ApplicationConstants.WORK_GROUP_DEV : ApplicationConstants.WORK_GROUP_SUPPORT_1);
		Employee manager = null;

		if (isDelivery)
			manager = globalBenchProjectLocation.getEmployee1();
		else
			manager = e.getNdReportingManagerId();

		return createAllocation(e, createdBy, allocationIsPrimary, pureBenchStatus, bench, globalBenchProjectLocation,
				workGroup, manager, allocationPercentage);
	}

	private Allocation createAllocation(Employee e, final long createdBy, final boolean allocationIsPrimary,
			final AllocationStatus pureBenchStatus, final Project bench,
			final ProjectLocation globalBenchProjectLocation, final WorkGroup workGroup, Employee manager, final int allocationPercentage) {
		final Allocation allocation = new Allocation(e, bench, globalBenchProjectLocation,workGroup, manager, manager, false,null,createdBy,pureBenchStatus);

		final AllocationDetail allocationDetail = new AllocationDetail(allocationPercentage, TimeUtils.getToday(), allocation, createdBy, true);

		allocation.setAllocationDetails(Collections.singletonList(allocationDetail));
		if (allocationIsPrimary)
			e.setPrimaryAllocation(allocation);

		return allocationRepository.save(allocation);
	}



	public Allocation populateToAllocation(Employee employee, Project project, ProjectLocation projectLocation,
			WorkGroup workgroup, Employee reportingManagerId, Employee allocationManagerId, Boolean billable,
			LocalDateTime releaseDate, AllocationStatus allocatedStatus) {

		final Date releaseDateForAlloc = releaseDate != null ? Timestamp.valueOf(releaseDate) : null;
		return new Allocation(employee, project, projectLocation, workgroup,
				reportingManagerId, allocationManagerId, billable, releaseDateForAlloc, securityUtil.getLoggedInEmployee().getEmployeeId(), allocatedStatus);
	}

	public Allocation allocate(AllocationRequestDto request) {
		Optional<Allocation> a = allocationRepository.findById(request.getAllocationId());
		final Allocation currentAllocation = a.orElse(null);
		Optional<Employee> e = employeeRepository.findById(request.getResourceId());
		final Employee employee = e.orElse(null);
		final AllocationStatus allocatedStatus = allocationStatusRepository.findByStatus("Allocated");
		final Integer availablePercentage = allocationUtilityService.getMergedAllocatedPercentage(Objects.requireNonNull(currentAllocation));
		final boolean isPartial = (request.getPercentage().intValue() < availablePercentage.intValue());
		final int remainingPercentage = (availablePercentage.intValue() - request.getPercentage().intValue());
		final Optional<Project> project = projectRepository.findById(Long.valueOf(request.getProjectId()));
		Iterables.removeIf(request.getExtraSalesforceIdList(), Predicates.isNull());
		if (!request.getExtraSalesforceIdList().isEmpty() && project.isPresent()) {
			final Set<SalesforceIdentifier> set =  project.get().getSalesforceIdentifiers();
			final List<String> values = project.get().getSalesforceIdentifiers().stream()
					.map(SalesforceIdentifier::getValue).collect(Collectors.toList());
			request.getExtraSalesforceIdList().forEach(s -> {
				if (!values.contains(s)) {
					set.add(new SalesforceIdentifier(s, project.get(), securityUtil.getLoggedInEmployee().getEmployeeId()));
				}
			});
			project.get().setSalesforceIdentifiers(set);
		}
		final Optional<ProjectLocation> projectLocation = projectLocationRespository.findById(Long.valueOf(request.getProjectLocationId()));
		final WorkGroup workgroup = workGroupRepository.findByName(request.getWorkgroup());
		Optional<Employee> rep = employeeRepository.findById(Long.valueOf(request.getReportingManagerId()));
		final Employee reportingManagerId = rep.orElse(null);

		Allocation existingAllocation = null;
		Allocation toAllocation = null;
		if (employee != null) {
			Optional<Allocation> existingAlloc = allocationRepository
					.findByEmployeeEmployeeIdAndProjectProjectIdAndProjectLocationProjectLocationIdAndWorkGroupWorkGroupIdAndIsBillableAndReleaseDateAndIsActive(
							employee.getEmployeeId(), Long.valueOf(request.getProjectId()),
							Long.valueOf(request.getProjectLocationId()), workgroup.getWorkGroupId(), request.getBillable(),
							Timestamp.valueOf(request.getReleaseDate()), true);
			existingAllocation = existingAlloc.orElse(null);
			final boolean isNew = !existingAlloc.isPresent();

			if (project.isPresent() && projectLocation.isPresent()) {
				final Employee allocationManagerId = allocationUtilityService.getAllocationManagerId(project.get(),
						projectLocation.get(), reportingManagerId, workgroup.getName());
				toAllocation = (isNew)
						? populateToAllocation(employee, project.get(), projectLocation.get(), workgroup,
								reportingManagerId, allocationManagerId, request.getBillable(), request.getReleaseDate(),
								allocatedStatus)
						: existingAllocation;

				if (toAllocation != null) {
				// Logic for partial / complete is different
				if (!isPartial) {
					completeAllocation(request, currentAllocation, toAllocation);
				} else {
					partialAllocation(request, currentAllocation, toAllocation);

				}
				}
				
				final Allocation primaryAllocation = allocationUtilityService.getPrimaryManager(currentAllocation, employee.getEmployeeId(),
						request.getIsPrimaryManager(), isPartial, toAllocation, isNew);

				if (!isPartial && (int) remainingPercentage == 0) {
						currentAllocation.setIsActive(false);
						currentAllocation.setReleaseDate(TimeUtils.getToday());
				}

				employee.setPrimaryAllocation(primaryAllocation);

				//separate for current and to allocation as current allocation requires clearing of allocation details
				allocationHoursService.updateCurrLocationBillableHrs(currentAllocation);
				if (toAllocation != null) {
					allocationHoursService.updateToAllocationBillableHrs(toAllocation);

					allocationRepository.save(toAllocation);
				}
				allocationRepository.save(currentAllocation);

				//Integration call with SF starts here
				if(AllocationUtil.allocationIsActiveAndPrimary(toAllocation)) {
					sfCall(request, employee, toAllocation);
				}

				syncToTimesheetService.syncAllocationWithHoursForAllocation(currentAllocation, toAllocation);

			}

			
			
		}

		return toAllocation;
	}

	private void sfCall(AllocationRequestDto request, final Employee employee, final Allocation toAllocation) {
		final Date startDate = Date.from(request.getStartDate().atStartOfDay(ZoneId.systemDefault()).toInstant());
		final Long gdmId = sfIntegrationService.getGdmIdForSFIntegration(Long.parseLong(request.getProjectId()), toAllocation.getWorkGroup().getName());

		final ManagerChangeDto managerChangeDto = new ManagerChangeDto(String.valueOf(employee.getEmployeeId()),
				String.valueOf(employee.getPrimaryAllocation().getReportingManagerId().getEmployeeId()), startDate);
		sfIntegrationService.changeManager(managerChangeDto);

		final GdmChangeDto gdmChangeDto = new GdmChangeDto( String.valueOf(employee.getEmployeeId()), "GDM", String.valueOf(gdmId), startDate);
		sfIntegrationService.changeGDM(gdmChangeDto);

		final ProjectChangeDto projectChangeDto = new ProjectChangeDto(String.valueOf(employee.getEmployeeId()), startDate,
				String.valueOf(toAllocation.getProject().getProjectId()), String.valueOf(toAllocation.getProject().getAccount().getAccountId()));
		sfIntegrationService.changeProjectOrAccount(projectChangeDto);

		//Integration call with SF ends here
	}

	private void partialAllocation(AllocationRequestDto request, final Allocation currentAllocation,
			final Allocation toAllocation) {
		toAllocation.setIsActive(true);
		final AllocationDetail allocationDetail = new AllocationDetail(request.getPercentage(), request.getStartDate(),
				securityUtil.getLoggedInEmployee().getEmployeeId());

		allocationDetail.setAllocation(toAllocation);
		toAllocation.addAllocationDetails(allocationDetail);

		final List<AllocationDetail> allocationDetails = currentAllocation.getAllocationDetails().stream()
				.filter(AllocationDetail::getIsActive).sorted(Comparator.comparing(AllocationDetail::getStartDate)).collect(Collectors.toList());

		Integer percentageToBeDeallocated = request.getPercentage();
		for (final AllocationDetail a : allocationDetails) {
			while (percentageToBeDeallocated.intValue() > 0 && a.getIsActive()) {
				if (a.getAllocatedPercentage().intValue() <= percentageToBeDeallocated.intValue()) {
					deallocationService.updateForDeallocation(a);
					percentageToBeDeallocated = percentageToBeDeallocated.intValue()
							- a.getAllocatedPercentage().intValue();

				} else {
					final Integer remainingActivePercentage = a.getAllocatedPercentage().intValue()
							- percentageToBeDeallocated.intValue();
					final AllocationDetail copy = new AllocationDetail(remainingActivePercentage, TimeUtils.getToday(),
							currentAllocation, securityUtil.getLoggedInEmployee().getEmployeeId(), true);

					deallocationService.updateForDeallocation(a);
					percentageToBeDeallocated = 0;

					currentAllocation.getAllocationDetails().add(copy);
				}
			}
		}
	}

	private void completeAllocation(AllocationRequestDto request, final Allocation currentAllocation,
			final Allocation toAllocation) {
		currentAllocation.getAllocationDetails().stream().forEach(ad -> {
			ad.setDeallocatedOn(DateUtils.convertToDateViaInstant(request.getStartDate()));
			ad.setDeallocatedBy(securityUtil.getLoggedInEmployee());
			ad.setIsActive(false);
		});
		final AllocationDetail allocationDetail = new AllocationDetail(request.getPercentage(), request.getStartDate(),
				securityUtil.getLoggedInEmployee().getEmployeeId());

		allocationDetail.setAllocation(toAllocation);
		toAllocation.getAllocationDetails().add(allocationDetail);
	}

	public void createAllocationHoursForAll() {
		final List<Allocation> activeAllocations = allocationRepository.findByIsActive(true);

		activeAllocations.forEach(allocation -> allocationHoursService.updateCurrLocationBillableHrs(allocation));

		allocationRepository.saveAll(activeAllocations);

	}

	@Transactional
	public void createTimesheetAllocationForAll() {
		final List<Allocation> activeAllocations = allocationRepository.findByIsActive(true);

		final List<TimesheetAllocation> timesheetAllocations = new ArrayList<>();

		activeAllocations.forEach(allocation -> {
			final TimesheetAllocation timesheetAllocation = new TimesheetAllocation();
			timesheetAllocation.setAllocationId(allocation.getAllocationId());
			timesheetAllocation.setAllocatedPercentage(allocation.getAllocationDetails().stream().filter(AllocationDetail::getIsActive)
					.map(AllocationDetail::getAllocatedPercentage).reduce(0, Integer::sum));
			timesheetAllocation.setIsActive(true);
			timesheetAllocation.setCreatedBy(allocation.getCreatedBy());
			timesheetAllocations.add(timesheetAllocation);

		});

		timesheetAllocationRepository.saveAll(timesheetAllocations);

	}

}