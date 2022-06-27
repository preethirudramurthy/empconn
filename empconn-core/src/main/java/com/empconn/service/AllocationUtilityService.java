package com.empconn.service;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Set;
import java.util.function.Function;
import java.util.stream.Collectors;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.empconn.constants.ApplicationConstants;
import com.empconn.dto.allocation.AutoRMSearchDto;
import com.empconn.dto.manager.GetResourcesResponseDto;
import com.empconn.persistence.entities.Allocation;
import com.empconn.persistence.entities.AllocationDetail;
import com.empconn.persistence.entities.Earmark;
import com.empconn.persistence.entities.Employee;
import com.empconn.persistence.entities.Project;
import com.empconn.persistence.entities.ProjectLocation;
import com.empconn.repositories.AllocationRepository;
import com.empconn.repositories.EmployeeRepository;
import com.empconn.repositories.ProjectLocationRespository;
import com.empconn.repositories.ProjectRepository;
import com.empconn.security.SecurityUtil;
import com.empconn.utilities.DateUtils;
import com.empconn.utilities.TimeUtils;

@Service
public class AllocationUtilityService {
	private static final Logger logger = LoggerFactory.getLogger(AllocationUtilityService.class);

	@Autowired
	private AllocationRepository allocationRepository;

	@Autowired
	private ProjectRepository projectRepository;

	@Autowired
	private EmployeeRepository employeeRepository;

	@Autowired
	private SecurityUtil securityUtil;

	@Autowired
	private ProjectLocationRespository projectLocationRepository;

	@Autowired
	private EarmarkService earmarkService;

	public Allocation getPrimaryManager(Allocation currentAllocation, Long employeeId, Boolean isPrimary,
			Boolean isPartial, Allocation toAllocation, boolean isNew, Integer percentage) {
		final Set<Allocation> existingActiveAllocations = allocationRepository
				.findByEmployeeEmployeeIdAndIsActive(employeeId, true);

		if (isPrimary) {
			return toAllocation;
		}

		if (!isPartial) {
			// if !partial, remove the current allocation from the logic as it will be made
			// inactive to complete switchover
			existingActiveAllocations.removeIf(x -> x.getAllocationId().equals(currentAllocation.getAllocationId()));
		}
		final List<String> benchProjects = Arrays.asList("Central Bench", "NDBench");
		existingActiveAllocations.removeIf(x -> benchProjects.contains(x.getProject().getName()));
		final Optional<Employee> employee = employeeRepository.findById(employeeId);

		if (!existingActiveAllocations.isEmpty()) {
			if (!isBenchManager(employee.get().getPrimaryAllocation().getReportingManagerId())
					&& !employee.get().getPrimaryAllocation().getReportingManagerId().getEmployeeId()
					.equals(currentAllocation.getReportingManagerId().getEmployeeId())) {
				return employee.get().getPrimaryAllocation();
			}
		} else {
			existingActiveAllocations.add(toAllocation);
		}

		if (isNew && !benchProjects.contains(toAllocation.getProject().getName())) {
			existingActiveAllocations.add(toAllocation);
		}

		return getPrimaryAllocation(existingActiveAllocations);

	}

	public Allocation getPrimaryAllocation(final Set<Allocation> existingActiveAllocations) {
		if (!existingActiveAllocations.isEmpty()) {
			Comparator<Allocation> sortByPercentageAndStartDate = ((Allocation a,
					Allocation b) -> getMergedAllocatedPercentage(a).compareTo(getMergedAllocatedPercentage(b)));
			sortByPercentageAndStartDate = sortByPercentageAndStartDate.reversed()
					.thenComparing((a, b) -> Collections
							.min(a.getAllocationDetails().stream().map(AllocationDetail::getStartDate)
									.collect(Collectors.toList()))
							.compareTo(Collections.min(b.getAllocationDetails().stream()
									.map(AllocationDetail::getStartDate).collect(Collectors.toList()))));

			final List<Allocation> sortedAllocations = existingActiveAllocations.stream()
					.sorted(sortByPercentageAndStartDate).collect(Collectors.toList());

			return sortedAllocations.stream().findFirst().get();

		}
		return null;
	}

	public Employee getAllocationManagerId(Project project, ProjectLocation projectLocation,
			Employee reportingManagerId, String workgroup) {

		final List<Employee> allManagers = project.getProjectLocations().stream().map(p -> p.getAllManagers().values())
				.flatMap(Collection::stream).collect(Collectors.toList());

		// final Collection<Employee> gdms = project.getGdms().values();

		final Optional<Employee> managerInsideProject = allManagers.stream()
				.filter(e -> e.getEmployeeId().equals(reportingManagerId.getEmployeeId())).findAny();

		if (managerInsideProject.isPresent()) {
			return reportingManagerId;
		}

		/*
		 * final Optional<Employee> gdmIsManager = gdms.stream() .filter(e ->
		 * e.getEmployeeId().equals(reportingManagerId.getEmployeeId())).findAny();
		 *
		 * if (gdmIsManager.isPresent()) { return reportingManagerId; }
		 */

		if (projectLocation.getAllManagers().get(workgroup) != null) {
			return (projectLocation.getAllManagers().get(workgroup));
		}

		final List<ProjectLocation> otherLocationsWorkgroupManagers = project.getProjectLocations().stream()
				.filter(p -> (!p.getProjectLocationId().equals(projectLocation.getProjectLocationId())
						&& p.getAllManagers().get(workgroup) != null))
				.collect(Collectors.toList());
		if (otherLocationsWorkgroupManagers.size() > 0) {
			final Comparator<ProjectLocation> sortByLocationHierarchy = ((ProjectLocation a, ProjectLocation b) -> a
					.getLocation().getHierarchy().compareTo(b.getLocation().getHierarchy()));

			otherLocationsWorkgroupManagers.sort(sortByLocationHierarchy);

			return (otherLocationsWorkgroupManagers.get(0).getAllManagers().get(workgroup));
		}

		return projectLocation.getAllManagers().values().stream().findFirst().get();

	}

	public Boolean isBenchManager(Employee employee) {
		final Project bench = projectRepository.findByAccountCategoryAndName("Internal", "Central Bench");
		final List<Employee> allManagers = bench.getProjectLocations().stream().map(p -> p.getAllManagers().values())
				.flatMap(Collection::stream).collect(Collectors.toList());
		return allManagers.contains(employee);
	}

	public Integer getMergedAllocatedPercentage(final Allocation currentAllocation) {
		return currentAllocation.getAllocationDetails().stream().filter(AllocationDetail::getIsActive)
				.map(AllocationDetail::getAllocatedPercentage).reduce(0, Integer::sum);
	}

	Function<ProjectLocation, List<Map<String, String>>> getProjectManagers = projectLocation -> {
		final List<Map<String, String>> locationManagerList = new ArrayList<Map<String, String>>();
		final Map<String, Employee> allManagers = projectLocation.getAllManagers();
		allManagers.entrySet().stream().forEach(y -> {
			if (y.getValue() != null) {
				final Map<String, String> locationManagerMap = new HashMap<String, String>();
				locationManagerMap.put("projectLocationName", projectLocation.getLocation().getName());
				locationManagerMap.put("workgroup", y.getKey());
				locationManagerMap.put("mangerName", y.getValue().getFirstName());
				locationManagerList.add(locationManagerMap);
			}

		});

		return locationManagerList;
	};

	List<Map<String, String>> projectToManagerList(Project project) {
		return project.getProjectLocations().stream().map(x -> getProjectManagers.apply(x)).flatMap(List::stream)
				.collect(Collectors.toList());
	}

	public void mergeAllocation(final Allocation currentAllocation, final Allocation toAllocation) {
		final Employee loggedInEmployee = securityUtil.getLoggedInEmployee();
		toAllocation.getAllocationDetails().stream().filter(AllocationDetail::getIsActive).forEach(ad -> {
			ad.setDeallocatedOn(TimeUtils.getToday());
			ad.setDeallocatedBy(loggedInEmployee);
			ad.setIsActive(false);

			final AllocationDetail allocationDetail = new AllocationDetail(ad.getAllocatedPercentage(),
					DateUtils.convertToLocalDateViaMilisecond(ad.getStartDate()), loggedInEmployee.getEmployeeId());

			allocationDetail.setAllocation(currentAllocation);
			currentAllocation.getAllocationDetails().add(allocationDetail);
		});

		final List<Earmark> earmarks = currentAllocation.getEarmarks().stream().filter(Earmark::getIsActive)
				.collect(Collectors.toList());
		earmarks.addAll(toAllocation.getEarmarks().stream().filter(Earmark::getIsActive)
				.collect(Collectors.toList()));

		for (final Earmark e : earmarks) {

			if (DateUtils.convertToLocalDateViaMilisecond(e.getStartDate())
					.isAfter(DateUtils.convertToLocalDateViaMilisecond(currentAllocation.getReleaseDate()))) {
				e.setAllocation(currentAllocation);
				currentAllocation.getEarmarks().add(e);
			} else {
				earmarkService.unearmarkBySystem(e, ApplicationConstants.UNEARMARK_EDIT_RELEASE_DATE_RESOURCE_COMMENT);
			}
		}

		final Integer remainingPercentage = getMergedAllocatedPercentage(toAllocation);
		if (remainingPercentage.intValue() == 0) {
			toAllocation.setIsActive(false);
			toAllocation.setReleaseDate(TimeUtils.getToday());
			toAllocation.getAllocationHours().clear();
		}
	}

	public Integer getAvailablePercentageForND(final Long employeeId) {
		final String METHOD_NAME = "getAvailablePercentageForND";
		logger.info("{} starts execution successfully.", METHOD_NAME);

		final Allocation allocation = allocationRepository.getAllocation(employeeId,
				ApplicationConstants.NON_DELIVERY_BENCH_PROJECT_NAME);
		if (null == allocation)
			return 0;
		return allocation.getAllocationDetails().stream().filter(AllocationDetail::getIsActive)
				.map(AllocationDetail::getAllocatedPercentage).reduce(0, Integer::sum);
	}

	public GetResourcesResponseDto getAutoReportingManagerId(AutoRMSearchDto request) {

		final Project project = projectRepository.findByProjectId(request.getProjectId());
		final ProjectLocation projectLocation = projectLocationRepository.findById(request.getProjectLocationId())
				.get();

		final Employee autoReportingManager = getAutoReportingManagerId(project, projectLocation,
				request.getWorkgroup());

		return new GetResourcesResponseDto(autoReportingManager.getEmployeeId(), autoReportingManager.getFullName(),
				autoReportingManager.getEmpCode(), autoReportingManager.getTitle().getName());

	}

	public Employee getAutoReportingManagerId(Project project, ProjectLocation projectLocation, String workgroup) {

		if (projectLocation.getAllManagers().get(workgroup) != null) {
			return (projectLocation.getAllManagers().get(workgroup));
		}

		final List<ProjectLocation> otherLocationsWorkgroupManagers = project.getProjectLocations().stream()
				.filter(p -> (!p.getProjectLocationId().equals(projectLocation.getProjectLocationId())
						&& p.getAllManagers().get(workgroup) != null))
				.collect(Collectors.toList());
		if (otherLocationsWorkgroupManagers.size() > 0) {
			final Comparator<ProjectLocation> sortByLocationHierarchy = ((ProjectLocation a, ProjectLocation b) -> a
					.getLocation().getHierarchy().compareTo(b.getLocation().getHierarchy()));

			otherLocationsWorkgroupManagers.sort(sortByLocationHierarchy);

			return (otherLocationsWorkgroupManagers.get(0).getAllManagers().get(workgroup));
		}

		return projectLocation.getAllManagers().values().stream().findFirst().get();

	}
}
