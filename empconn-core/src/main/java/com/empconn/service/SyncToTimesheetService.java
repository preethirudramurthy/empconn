package com.empconn.service;

import java.sql.Timestamp;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import com.empconn.constants.ApplicationConstants;
import com.empconn.mapper.CommonQualifiedMapper;
import com.empconn.persistence.entities.Account;
import com.empconn.persistence.entities.Allocation;
import com.empconn.persistence.entities.AllocationDetail;
import com.empconn.persistence.entities.Employee;
import com.empconn.persistence.entities.Project;
import com.empconn.persistence.entities.ProjectLocation;
import com.empconn.persistence.entities.SyncAccount;
import com.empconn.persistence.entities.SyncEmployee;
import com.empconn.persistence.entities.SyncProject;
import com.empconn.persistence.entities.SyncProjectAllocation;
import com.empconn.persistence.entities.SyncProjectAllocationHour;
import com.empconn.persistence.entities.SyncProjectManager;
import com.empconn.repositories.AllocationRepository;
import com.empconn.repositories.SyncAccountRepository;
import com.empconn.repositories.SyncEmployeeRepository;
import com.empconn.repositories.SyncProjectAllocationHourRepository;
import com.empconn.repositories.SyncProjectAllocationRepository;
import com.empconn.repositories.SyncProjectManagerRepository;
import com.empconn.repositories.SyncProjectRepository;
import com.empconn.repositories.WorkGroupRepository;
import com.empconn.security.SecurityUtil;

@Service
public class SyncToTimesheetService {

	@Autowired
	private SyncProjectAllocationRepository syncProjectAllocationRepository;

	@Autowired
	private SyncProjectAllocationHourRepository syncProjectAllocationHourRepository;

	@Autowired
	private SecurityUtil securityUtil;

	@Autowired
	private WorkGroupRepository workGroupRepository;

	@Autowired
	private SyncProjectManagerRepository syncProjectManagerRepository;

	@Autowired
	private SyncProjectRepository syncProjectRepository;

	@Autowired
	private SyncEmployeeRepository syncEmployeeRepository;

	@Autowired
	private SyncAccountRepository syncAccountRepository;

	@Autowired
	private AllocationRepository allocationRepository;

	@Value("${syncToTimeSheet:false}")
	private boolean syncToTimeSheet;

	@Autowired
	private CommonQualifiedMapper commonQualifiedMapper;


	public void syncProjectAllocationsForReleaseDate(Project prj, Set<Allocation> allocations) {

		if (syncToTimeSheet) {
			syncProjectRepository.save(new SyncProject(securityUtil.getLoggedInEmployee().getEmployeeId(), ApplicationConstants.STATUS_PENDING, prj));
			for (final Allocation allocation : allocations) {
				syncProjectAllocationRepository.save(new SyncProjectAllocation(
						allocation.getCreatedBy(), securityUtil.getLoggedInEmployeeId(), commonQualifiedMapper.getAllocationStartDate(allocation), allocation));
				syncProjectAllocationHourRepository.save(new SyncProjectAllocationHour(allocation.getCreatedBy(), allocation.getModifiedBy(), allocation));
			}
		}

	}

	public void syncAllocationWithHoursForAllocation(final Allocation currentAllocation,
			final Allocation toAllocation) {
		if (syncToTimeSheet) {
			final Date currentAllocationStartDate = Collections.min(currentAllocation.getAllocationDetails().stream()
					.map(AllocationDetail::getStartDate).collect(Collectors.toList()));
			final Date toAllocationStartDate = Collections.min(toAllocation.getAllocationDetails().stream()
					.map(AllocationDetail::getStartDate).collect(Collectors.toList()));
			if (!currentAllocation.getProject().getName().equals(ApplicationConstants.NON_DELIVERY_BENCH_PROJECT_NAME)) {
				syncProjectAllocationRepository.save(new SyncProjectAllocation(currentAllocation.getCreatedBy(), currentAllocation.getModifiedBy(), currentAllocationStartDate, currentAllocation));
			}


			final Optional<Allocation> timesheetAllocation = allocationRepository
					.findByAllocationIdNotAndEmployeeEmployeeIdAndProjectProjectIdAndProjectLocationProjectLocationIdAndWorkGroupWorkGroupIdAndTimesheetAllocationAllocationIdIsNullAndIsActiveTrue(
							toAllocation.getAllocationId(), toAllocation.getEmployee().getEmployeeId(), toAllocation.getProject().getProjectId(),
							toAllocation.getProjectLocation().getProjectLocationId(), toAllocation.getWorkGroup().getWorkGroupId());

			if (timesheetAllocation.isPresent()) {
				//if present then thats the one
				toAllocation.setTimesheetAllocation(timesheetAllocation.get());
				allocationRepository.save(toAllocation);
			} else {

				final List<Allocation> allocationsWithTimesheetAllocation = allocationRepository
						.findByEmployeeEmployeeIdAndProjectProjectIdAndProjectLocationProjectLocationIdAndWorkGroupWorkGroupIdAndTimesheetAllocationAllocationId(
								toAllocation.getEmployee().getEmployeeId(), toAllocation.getProject().getProjectId(),
								toAllocation.getProjectLocation().getProjectLocationId(), toAllocation.getWorkGroup().getWorkGroupId(), toAllocation.getAllocationId());

				if (allocationsWithTimesheetAllocation == null || allocationsWithTimesheetAllocation.isEmpty()) {
					final List<Allocation> existingAllocation = allocationRepository
							.findByAllocationIdNotAndEmployeeEmployeeIdAndProjectProjectIdAndProjectLocationProjectLocationIdAndWorkGroupWorkGroupIdAndIsActiveTrue(
									toAllocation.getAllocationId(), toAllocation.getEmployee().getEmployeeId(), toAllocation.getProject().getProjectId(),
									toAllocation.getProjectLocation().getProjectLocationId(), toAllocation.getWorkGroup().getWorkGroupId());

					if (existingAllocation != null && !existingAllocation.isEmpty()) {

						final List<Allocation> timesheetAllocations = allocationRepository
								.findByAllocationIdNotAndEmployeeEmployeeIdAndProjectProjectIdAndProjectLocationProjectLocationIdAndWorkGroupWorkGroupIdAndTimesheetAllocationAllocationIdIsNull(
										toAllocation.getAllocationId(), toAllocation.getEmployee().getEmployeeId(), toAllocation.getProject().getProjectId(),
										toAllocation.getProjectLocation().getProjectLocationId(), toAllocation.getWorkGroup().getWorkGroupId());

						final Comparator<Allocation> allocationCreatedOnComparator = Comparator
								.comparing(Allocation::getCreatedOn, Timestamp::compareTo).reversed();
						timesheetAllocations.sort(allocationCreatedOnComparator);

						toAllocation.setTimesheetAllocation((!timesheetAllocations.isEmpty() &&
								!timesheetAllocations.get(0).getAllocationId().equals(toAllocation.getAllocationId()))?timesheetAllocations.get(0):null);
						allocationRepository.save(toAllocation);

					}
				}
			}

			syncProjectAllocationRepository.save(new SyncProjectAllocation(toAllocation.getCreatedBy(), toAllocation.getModifiedBy(), toAllocationStartDate,toAllocation));
			if (currentAllocation.getAllocationHours() != null && !currentAllocation.getAllocationHours().isEmpty())
				syncProjectAllocationHourRepository.save(new SyncProjectAllocationHour(currentAllocation.getCreatedBy(), currentAllocation.getModifiedBy(), currentAllocation));
			if (toAllocation.getAllocationHours() != null && !toAllocation.getAllocationHours().isEmpty())
				syncProjectAllocationHourRepository.save(new SyncProjectAllocationHour(toAllocation.getCreatedBy(), toAllocation.getModifiedBy(), toAllocation));
		}
	}

	public void syncAllocationWithHoursForDeallocation(final Allocation currentAllocation,
			final Allocation toAllocation) {
		if (syncToTimeSheet) {
			final Date currentAllocationStartDate = Collections.min(currentAllocation.getAllocationDetails().stream()
					.map(AllocationDetail::getStartDate).collect(Collectors.toList()));
			final Date toAllocationStartDate = Collections.min(toAllocation.getAllocationDetails().stream()
					.map(AllocationDetail::getStartDate).collect(Collectors.toList()));
			syncProjectAllocationRepository.save(new SyncProjectAllocation(
					currentAllocation.getCreatedBy(), currentAllocation.getModifiedBy(), currentAllocationStartDate, currentAllocation));

			if (!toAllocation.getProject().getName().equals(ApplicationConstants.NON_DELIVERY_BENCH_PROJECT_NAME)) {
				syncProjectAllocationRepository.save(new SyncProjectAllocation(
						toAllocation.getCreatedBy(), toAllocation.getModifiedBy(), toAllocationStartDate,toAllocation));
			}

			//any deallocation - partial/complete, allocation hours should be synced with timesheet
			syncProjectAllocationHourRepository.save(new SyncProjectAllocationHour(currentAllocation.getCreatedBy(), currentAllocation.getModifiedBy(), currentAllocation));

			if (toAllocation.getAllocationHours() != null && !toAllocation.getAllocationHours().isEmpty())
				syncProjectAllocationHourRepository.save(new SyncProjectAllocationHour(toAllocation.getCreatedBy(), toAllocation.getModifiedBy(), toAllocation));
		}
	}

	public void syncProjectAllocation(final Allocation allocation) {
		if (syncToTimeSheet) {
			syncProjectAllocationRepository.save(new SyncProjectAllocation(
					allocation.getCreatedBy(), allocation.getModifiedBy(), commonQualifiedMapper.getAllocationStartDate(allocation), allocation));
		}
	}

	public void syncProjectManager(final ProjectLocation projectLocation, final Employee assignDevManager, String workgroup) {
		if (syncToTimeSheet) {
			final SyncProjectManager s = new SyncProjectManager(securityUtil.getLoggedInEmployee().getEmployeeId(),
					assignDevManager,projectLocation.getProject(),projectLocation,workGroupRepository.findByName(workgroup), ApplicationConstants.STATUS_PENDING);
			syncProjectManagerRepository.save(s);
		}
	}

	public void syncProjectAllocationHours(final Allocation allocation) {
		if (syncToTimeSheet) {
			syncProjectAllocationRepository.save(new SyncProjectAllocation(
					allocation.getCreatedBy(), allocation.getModifiedBy(), commonQualifiedMapper.getAllocationStartDate(allocation), allocation));
			syncProjectAllocationHourRepository.save(new SyncProjectAllocationHour(allocation.getCreatedBy(), allocation.getModifiedBy(), allocation));
		}
	}

	public void syncAccountAndProject(final Project project, final Account account) {
		if (syncToTimeSheet) {
			syncAccountRepository.save(new SyncAccount(securityUtil.getLoggedInEmployee().getEmployeeId(), ApplicationConstants.STATUS_PENDING, account));
			syncProjectRepository.save(new SyncProject(securityUtil.getLoggedInEmployee().getEmployeeId(), ApplicationConstants.STATUS_PENDING, project));

			project.getProjectLocations().forEach(pl ->
					pl.getAllManagers().forEach((key, value) -> {
						final SyncProjectManager s = new SyncProjectManager(securityUtil.getLoggedInEmployee().getEmployeeId(),
								value, project, pl, workGroupRepository.findByName(key), ApplicationConstants.STATUS_PENDING);
						syncProjectManagerRepository.save(s);
					})
			);

		}
	}

	public void syncProject(final Project project) {
		if (syncToTimeSheet) {
			syncProjectRepository.save(new SyncProject(securityUtil.getLoggedInEmployee().getEmployeeId(), ApplicationConstants.STATUS_PENDING, project, project.getIsActive()));
		}
	}

	public void syncEmployee(Employee employee) {
		if (syncToTimeSheet) {
			syncEmployeeRepository.save(new SyncEmployee(employee.getCreatedBy(), ApplicationConstants.STATUS_PENDING, employee));
		}
	}
}
