package com.empconn.service;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.empconn.constants.ApplicationConstants;
import com.empconn.dto.deallocation.DeallocateDto;
import com.empconn.email.DeallocationEmailUtil;
import com.empconn.email.EmailService;
import com.empconn.persistence.entities.Allocation;
import com.empconn.persistence.entities.AllocationDetail;
import com.empconn.persistence.entities.AllocationFeedback;
import com.empconn.persistence.entities.AllocationHour;
import com.empconn.persistence.entities.AllocationStatus;
import com.empconn.persistence.entities.Auditable;
import com.empconn.persistence.entities.Earmark;
import com.empconn.persistence.entities.Employee;
import com.empconn.persistence.entities.Location;
import com.empconn.persistence.entities.ManagerChange;
import com.empconn.persistence.entities.Project;
import com.empconn.persistence.entities.ProjectChange;
import com.empconn.persistence.entities.ProjectLocation;
import com.empconn.persistence.entities.WorkGroup;
import com.empconn.repositories.AllocationRepository;
import com.empconn.repositories.AllocationStatusRepository;
import com.empconn.repositories.LocationRepository;
import com.empconn.repositories.ManagerChangeRepository;
import com.empconn.repositories.ProjectChangeRepository;
import com.empconn.repositories.ProjectLocationRespository;
import com.empconn.repositories.WorkGroupRepository;
import com.empconn.security.SecurityUtil;
import com.empconn.utilities.TimeUtils;

@Service
@Transactional(readOnly = true)
public class DeallocationService {

	private static final String PENDING = "Pending";

	@Autowired
	private AllocationRepository allocationRepository;

	@Autowired
	private WorkGroupRepository workgroupRepository;

	@Autowired
	private SecurityUtil jwtEmployeeUtil;

	@Autowired
	private ProjectLocationRespository projectLocationRespository;

	@Autowired
	private EmployeeService employeeService;

	@Autowired
	private ProjectService projectService;

	@Autowired
	private AllocationService allocationService;

	@Autowired
	private EarmarkService earmarkService;

	@Autowired
	private MasterService masterService;

	@Autowired
	private EmailService emailService;

	@Autowired
	private AllocationUtilityService allocationUtilityService;
	@Value("${spring.profiles.active:Local}")
	private String activeProfile;


	@Autowired
	private DeallocationEmailUtil deallocationEmailUtil;

	@Autowired
	private CommonBenchService benchService;

	@Autowired
	private LocationRepository locationRepository;

	@Autowired
	private AllocationHoursService allocationHoursService;

	@Autowired
	private ManagerChangeRepository managerChangeRepository;

	@Autowired
	private ProjectChangeRepository projectChangeRepository;

	@Autowired
	private AllocationStatusRepository allocationStatusRepository;

	@Autowired
	private SyncToTimesheetService syncToTimesheetService;

	@Transactional(readOnly = false)
	public void deallocate(List<DeallocateDto> deallocateDtos) {
		for (final DeallocateDto request : deallocateDtos) {
			final Employee loggedInEmployee = jwtEmployeeUtil.getLoggedInEmployee();
			Optional<Allocation> allocOpt = allocationRepository.findById(request.getAllocationId());
			final Allocation currentAllocation = allocOpt.orElse(null);
			if (currentAllocation != null) {
			final Employee employee = currentAllocation.getEmployee();
			final boolean isDelivery = employeeService.isDelivery(currentAllocation.getEmployee());
			final Project benchProject = projectService.getBenchProject(isDelivery);
			final Location location = locationRepository.findByName(ApplicationConstants.LOCATION_GLOBAL);
			final ProjectLocation benchProjectLocation = projectLocationRespository
					.findByProjectProjectIdAndLocationLocationId(benchProject.getProjectId(), location.getLocationId())
					.get(0);
			final String workgroupName = (isDelivery) ? ApplicationConstants.WORK_GROUP_DEV
					: ApplicationConstants.WORK_GROUP_SUPPORT_1;
			final WorkGroup benchWorkGroup = workgroupRepository.findByName(workgroupName);
			final Employee reportingManager = (isDelivery) ? benchProjectLocation.getEmployee1()
					: employee.getNdReportingManagerId();
			final AllocationStatus pureBenchStatus = allocationStatusRepository.findByStatus(ApplicationConstants.ALLOCATION_STATUS_PUREBENCH);

			final Optional<Allocation> existingBenchAllocation = allocationRepository
					.findOneByEmployeeEmployeeIdAndProjectProjectIdAndIsActive(employee.getEmployeeId(),
							benchProject.getProjectId(), true);

			final boolean isNew = !existingBenchAllocation.isPresent();

			final Allocation toAllocation = (isNew)
					? allocationService.populateToAllocation(employee, benchProject, benchProjectLocation,
							benchWorkGroup, reportingManager, reportingManager, false, null, pureBenchStatus)
							: existingBenchAllocation.get();

					if (request.getPartial()) {
						completeDeallocate(request, loggedInEmployee, currentAllocation, employee, isNew, toAllocation);
					} else {
						partialDeallocate(request, loggedInEmployee, currentAllocation, toAllocation);
					}

					final List<String> benchProjects = benchService.getBenchProjectNames();
					if (!benchProjects.contains(currentAllocation.getProject().getName())) {
						final Map<String, AllocationHour> currentAllocHrs = currentAllocation.getAllocationHours().stream()
								.collect(Collectors.toMap(a -> String.join("-", String.valueOf(a.getYear()), a.getMonth()),
										a -> a));

						final Map<String, AllocationHour> copyOfCurrAllocHrs = allocationHoursService
								.getBillingHours(currentAllocation, currentAllocation.getAllocationDetails());

						if (copyOfCurrAllocHrs == null || copyOfCurrAllocHrs.isEmpty()) {
							currentAllocation.getAllocationHours().clear();
						} else {
							updateAllocHours(currentAllocation, currentAllocHrs, copyOfCurrAllocHrs);
						}
					}

					if (!benchProjects.contains(toAllocation.getProject().getName())) {
						final Map<String, AllocationHour> monthlyBillableHoursOfToAllocation = toAllocation.getAllocationHours()
								.stream().collect(Collectors
										.toMap(a -> String.join("-", String.valueOf(a.getYear()), a.getMonth()), a -> a));

						final Map<String, AllocationHour> monthlyBillableHoursOfToAllocCopy = allocationHoursService
								.getBillingHours(toAllocation, toAllocation.getAllocationDetails());

						updateAllocHours(toAllocation, monthlyBillableHoursOfToAllocation, monthlyBillableHoursOfToAllocCopy);
						toAllocation.getAllocationHours().addAll(monthlyBillableHoursOfToAllocation.values());

					}

					allocationRepository.save(toAllocation);
					allocationRepository.save(currentAllocation);

					syncToTimesheetService.syncAllocationWithHoursForDeallocation(currentAllocation, toAllocation);

					if (request.getPartial())
						mailForCompleteDeallocation(currentAllocation);
					else
						mailForPartialDeallocation(currentAllocation, request, toAllocation);
		}
		}

	}

	private void partialDeallocate(final DeallocateDto request, final Employee loggedInEmployee,
			final Allocation currentAllocation, final Allocation toAllocation) {
		toAllocation.setIsActive(true);
		final AllocationDetail allocationDetail = new AllocationDetail(request.getPercentage(), LocalDate.now(),
				loggedInEmployee.getEmployeeId());
		allocationDetail.setAllocation(toAllocation);
		toAllocation.addAllocationDetails(allocationDetail);

		final List<AllocationDetail> allocationDetails = currentAllocation.getAllocationDetails().stream()
				.filter(AllocationDetail::getIsActive).collect(Collectors.toList());
		Comparator<AllocationDetail> sortByStartDateAndCreatedOn = (Comparator.comparing(AllocationDetail::getStartDate));
		sortByStartDateAndCreatedOn = sortByStartDateAndCreatedOn
				.thenComparing(Comparator.comparing(Auditable::getCreatedOn));

		allocationDetails.sort(sortByStartDateAndCreatedOn);

		Integer percentageToBeDeallocated = request.getPercentage();
		for (final AllocationDetail a : allocationDetails) {
			while (percentageToBeDeallocated.intValue() > 0 && a.getIsActive()) {
				if (a.getAllocatedPercentage().intValue() <= percentageToBeDeallocated.intValue()) {
					updateForDeallocation(a);
					percentageToBeDeallocated = percentageToBeDeallocated.intValue()
							- a.getAllocatedPercentage().intValue();

				} else {
					final Integer remainingActivePercentage = a.getAllocatedPercentage().intValue()
							- percentageToBeDeallocated.intValue();
					final AllocationDetail copy = new AllocationDetail(remainingActivePercentage, TimeUtils.getToday(),
							currentAllocation, loggedInEmployee.getEmployeeId(), true);

					updateForDeallocation(a);
					percentageToBeDeallocated = 0;

					currentAllocation.getAllocationDetails().add(copy);
				}
			}
		}
	}

	private void completeDeallocate(final DeallocateDto request, final Employee loggedInEmployee,
			final Allocation currentAllocation, final Employee employee, final boolean isNew,
			final Allocation toAllocation) {
		// Move earmark if eligible
		final Set<Earmark> earmarks = currentAllocation.getEarmarks();

		final Integer allocatedPercentage = currentAllocation.getAllocationDetails().stream()
				.filter(AllocationDetail::getIsActive).map(AllocationDetail::getAllocatedPercentage)
				.reduce(0, Integer::sum);
		final AllocationDetail allocationDetail = new AllocationDetail(allocatedPercentage, LocalDate.now(),
				loggedInEmployee.getEmployeeId());
		allocationDetail.setAllocation(toAllocation);
		toAllocation.getAllocationDetails().add(allocationDetail);

		if (toAllocation.getAllocationFeedbacks() == null) {
			toAllocation.setAllocationFeedbacks(new ArrayList<>());
		}

		toAllocation.getAllocationFeedbacks()
		.add(new AllocationFeedback(request.getSoftSkillFeedback(), request.getSoftSkillRating(),
				request.getTechFeedback(), request.getTechRating(), toAllocation,
				loggedInEmployee.getEmployeeId(), true));

		currentAllocation.getAllocationDetails().stream().forEach(ad -> {
			ad.setDeallocatedOn(TimeUtils.getToday());
			ad.setDeallocatedBy(loggedInEmployee);
			ad.setIsActive(false);

		});

		currentAllocation.setIsActive(false);

		moveEarmarks(loggedInEmployee, toAllocation, earmarks);

		Employee primaryManager = null;
		// Update primary manager only if the current allocation's RM was the Primary
		// Manager
		if (employee.getPrimaryAllocation() != null
				&& employee.getPrimaryAllocation().getAllocationId().equals(currentAllocation.getAllocationId())) {
			final Allocation primaryAllocation = allocationUtilityService.getPrimaryManager(currentAllocation,
					employee.getEmployeeId(), false, false, toAllocation, isNew);

			employee.setPrimaryAllocation(primaryAllocation);
			primaryManager = primaryAllocation.getReportingManagerId();
			final Map<String, Employee> gdms = primaryAllocation.getProject().getGdms();
			final boolean isDelivery = employeeService.isDelivery(currentAllocation.getEmployee());
			final Employee gdm = (isDelivery)?(((gdms.get(primaryAllocation.getWorkGroup().getName())) == null)
					? gdms.values().stream().findFirst().get()
							: gdms.get(primaryAllocation.getWorkGroup().getName())):employee.getEmployee2();
					initializeSFIntegration(employee, primaryManager, gdm, loggedInEmployee, primaryAllocation.getProject());

		}


	}

	private void initializeSFIntegration(Employee employee, Employee primaryManager, Employee gdm,
			Employee loggedInEmployee, Project project) {
		// Integration call with SF starts here
		final Date startDate = TimeUtils.getToday();

		final ManagerChange managerChange = new ManagerChange(startDate, false, PENDING, employee, primaryManager,
				loggedInEmployee.getEmployeeId());
		managerChangeRepository.save(managerChange);
		final ManagerChange gdmChange = new ManagerChange(startDate, true, PENDING, employee, gdm,
				loggedInEmployee.getEmployeeId());
		managerChangeRepository.save(gdmChange);
		final ProjectChange projectChange = new ProjectChange(startDate, PENDING, project.getAccount(), employee,
				project, loggedInEmployee.getEmployeeId());
		projectChangeRepository.save(projectChange);

		// Integration call with SF ends here

	}

	private void moveEarmarks(final Employee loggedInEmployee, final Allocation toAllocation,
			final Set<Earmark> earmarks) {
		final List<Earmark> earmarksLst = earmarks.stream().filter(Earmark::getIsActive).collect(Collectors.toList());
		for (final Earmark earmark : earmarksLst) {

			final Integer availableBenchPercentage = toAllocation.getAllocationDetails().stream()
					.filter(AllocationDetail::getIsActive).map(AllocationDetail::getAllocatedPercentage)
					.reduce(0, Integer::sum);
			final Integer requestedEarmakrPercentage = earmark.getPercentage();

			if (requestedEarmakrPercentage.intValue() <= availableBenchPercentage.intValue()) {
				earmark.setIsActive(false);
				final Earmark clonedEarmark = new Earmark(earmark);
				clonedEarmark.setCreatedBy(loggedInEmployee.getEmployeeId());
				clonedEarmark.setAllocation(toAllocation);
				toAllocation.getEarmarks().add(clonedEarmark);
			} else {
				earmarkService.unearmarkBySystem(earmark,
						ApplicationConstants.UNEARMARK_DEALLOCATE_NOT_AVAILABLE_PERCENTAGE_COMMENT);
			}

		}
	}

	public void mailForPartialDeallocation(Allocation a, DeallocateDto request, Allocation oldAllocation) {
		final Map<String, Object> templateModel = deallocationEmailUtil.mailForPartialDeallocation(a, request, oldAllocation);
		final String[] emailToList = new String[] { a.getEmployee() == null ? "" : a.getEmployee().getEmail(),
				a.getReportingManagerId() == null ? "" : a.getReportingManagerId().getEmail(),
						(a.getAllocationManagerId() == null || a.getAllocationManagerId().equals(a.getReportingManagerId()))
						? ""
								: a.getAllocationManagerId().getEmail() };
		final String[] emailCCList = new String[] {
				a.getProject().getEmployee1() == null ? "" : a.getProject().getEmployee1().getEmail(),
						a.getProject().getEmployee2() == null ? "" : a.getProject().getEmployee2().getEmail(),
								a.getEmployee().getNdReportingManagerId() == null ? ""
										: a.getEmployee().getNdReportingManagerId().getEmail() };
		emailService.send("partial-deallocation", templateModel, emailToList, emailCCList);
	}

	public void mailForCompleteDeallocation(Allocation a) {
		final Set<String> locationHr = masterService
				.getLocationHr(a.getEmployee().getLocation().getLocationId());
		final Map<String, Object> templateModel = deallocationEmailUtil.mailForCompleteDeallocation(a);
		final Set<ProjectLocation> pl = a.getProject().getProjectLocations();
		final StringBuilder sb = new StringBuilder();
		for (final ProjectLocation prjLoc : pl) {
			final Map<String, Employee> allManagers = prjLoc.getAllManagers();
			for (final Map.Entry<String, Employee> entry : allManagers.entrySet()) {
				sb.append(entry.getValue().getEmail()).append(",");
			}
		}
		final String[] emailToList = new String[] { a.getEmployee() == null ? "" : a.getEmployee().getEmail(),sb.toString() };
		final String[] emailCCList = new String[] {
				a.getProject().getEmployee1() == null ? "" : a.getProject().getEmployee1().getEmail(),
						a.getProject().getEmployee2() == null ? "" : a.getProject().getEmployee2().getEmail(),
								a.getEmployee().getNdReportingManagerId() == null ? ""
										: a.getEmployee().getNdReportingManagerId().getEmail(),
										String.join(",", locationHr) };
		emailService.send("complete-deallocation", templateModel, emailToList, emailCCList);
	}

	private void updateAllocHours(final Allocation currentAllocation, final Map<String, AllocationHour> currentAllocHrs,
			final Map<String, AllocationHour> copyOfCurrAllocHrs) {
		copyOfCurrAllocHrs.entrySet().stream().forEach(e -> {
			final Integer year = Integer.valueOf(e.getKey().split("-")[0]);
			final String monthName = e.getKey().split("-")[1];
			final BigDecimal maxHours = e.getValue().getBillingHours().divide(new BigDecimal(8))
					.multiply(new BigDecimal(24));
			currentAllocHrs.computeIfAbsent(e.getKey(),
					k -> new AllocationHour(new BigDecimal(0), 1L, monthName, year));
			currentAllocHrs.get(e.getKey()).setBillingHours(e.getValue().getBillingHours());
			currentAllocHrs.get(e.getKey())
			.setBillingHoursRounded(e.getValue().getBillingHours().setScale(0, RoundingMode.UP).intValue());
			currentAllocHrs.get(e.getKey()).setMaxHours(maxHours.setScale(0, RoundingMode.UP).intValue());
			currentAllocHrs.get(e.getKey()).setAllocation(currentAllocation);
		});
	}

	public void updateForDeallocation(final AllocationDetail a) {
		a.setDeallocatedOn(TimeUtils.getToday());
		a.setDeallocatedBy(jwtEmployeeUtil.getLoggedInEmployee());
		a.setIsActive(false);
		a.setModifiedOn(TimeUtils.getToday());
		a.setModifiedBy(jwtEmployeeUtil.getLoggedInEmployee().getEmployeeId());
	}

}
