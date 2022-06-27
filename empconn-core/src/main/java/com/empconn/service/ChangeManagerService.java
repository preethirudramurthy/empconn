
package com.empconn.service;

import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Set;
import java.util.stream.Collectors;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;

import com.empconn.activedirectory.ActiveDirectoryDeltaUpdateService;
import com.empconn.constants.ApplicationConstants;
import com.empconn.constants.Roles;
import com.empconn.dto.manager.ChangeGdmRequestDto;
import com.empconn.dto.manager.ChangeManagerDto;
import com.empconn.dto.manager.ChangeManagerWrapperDto;
import com.empconn.dto.manager.ChangeProjectManagerRequestDto;
import com.empconn.dto.manager.ChangeProjectManagerWrapperDto;
import com.empconn.dto.manager.ChangeReportingManagerDto;
import com.empconn.dto.manager.ChangeReportingManagerWrapperDto;
import com.empconn.dto.manager.GetGdmsResponseDto;
import com.empconn.dto.manager.GetManagersResponseDto;
import com.empconn.dto.manager.GetPossibleRepMgrChangeResponseDto;
import com.empconn.dto.manager.GetReporteeListResponseDto;
import com.empconn.dto.manager.GetReportingManagerResponseDto;
import com.empconn.email.EmailService;
import com.empconn.mapper.CommonQualifiedMapper;
import com.empconn.persistence.entities.Allocation;
import com.empconn.persistence.entities.AllocationDetail;
import com.empconn.persistence.entities.Employee;
import com.empconn.persistence.entities.Project;
import com.empconn.persistence.entities.ProjectLocation;
import com.empconn.persistence.entities.WorkGroup;
import com.empconn.repositories.AllocationRepository;
import com.empconn.repositories.EmployeeRepository;
import com.empconn.repositories.ProjectLocationRespository;
import com.empconn.repositories.ProjectRepository;
import com.empconn.successfactor.service.SFIntegrationService;
import com.empconn.successfactors.dto.GdmChangeDto;
import com.empconn.successfactors.dto.ManagerChangeDto;
import com.empconn.successfactors.dto.ProjectChangeDto;
import com.empconn.utilities.CommonUtil;

@Service
@Transactional
public class ChangeManagerService {
	private static final Logger logger = LoggerFactory.getLogger(ChangeManagerService.class);

	@Autowired
	SFIntegrationService sfIntegrationService;

	@Autowired
	private ProjectRepository projectRepository;

	@Autowired
	private AllocationRepository allocationRepository;

	@Autowired
	private ProjectLocationRespository projectLocationRespository;

	@Autowired
	private EmployeeRepository employeeRepository;

	@Autowired
	private AllocationUtilityService allocationUtilityService;

	@Autowired
	private EmailService emailService;

	@Autowired
	private MasterService masterService;

	@Autowired
	private ActiveDirectoryDeltaUpdateService activeDirectoryDeltaUpdateService;

	@Autowired
	private SyncToTimesheetService syncToTimesheetService;

	@Autowired
	private ProjectService projectService;

	@Autowired
	private ResourceService resourceService;

	public GetGdmsResponseDto getGdms(String projectId) {
		final Project project = projectRepository.findByProjectId(Long.valueOf(projectId));

		return new GetGdmsResponseDto(project.getAccount().getVertical().getName(), project.getAccount().getName(),
				project.getName(), project.getEmployee1() != null ? project.getEmployee1().getEmployeeId() : null,
				project.getEmployee1() != null ? StringUtils.join(project.getEmployee1().getFirstName(), " ",
						project.getEmployee1().getLastName()) : null,
				project.getEmployee2() != null ? project.getEmployee2().getEmployeeId() : null,
				project.getEmployee2() != null
						? StringUtils.join(project.getEmployee2().getFirstName(), " ",
								project.getEmployee2().getLastName())
						: null);
	}

	public List<GetPossibleRepMgrChangeResponseDto> getPossibleReportingManagerChange(Long projectId, Long devGdmId,
			Long qaGdmId) {

		final Project project = projectRepository.findByProjectId(Long.valueOf(projectId));
		final List<GetPossibleRepMgrChangeResponseDto> responseDtos = new ArrayList<GetPossibleRepMgrChangeResponseDto>();

		if (devGdmId != null) {
			final Employee oldDevGDM = project.getEmployee1();
			final boolean isDevGDMPresent = oldDevGDM != null;
			if (isDevGDMPresent) {

				final Map<Long, Map<String, Employee>> oldGdmAsManager = getOldGDMAsManager(project, oldDevGDM);

				// get all allocations which have old GDM as the Reporting manager
				final Set<Allocation> allocations = allocationRepository
						.findByProjectProjectIdAndReportingManagerIdEmployeeIdAndIsActive(Long.valueOf(projectId),
								oldDevGDM.getEmployeeId(), true);
				for (final Allocation allocation : allocations) {
					// If there are such allocations, check if the location/workgroup is same as the
					// combination for which old gdm is the manager
					// If not then, ask for a confirmation if the Reporting manager has to be
					// updated.
					final ProjectLocation projectLocation = allocation.getProjectLocation();
					final WorkGroup workgroup = allocation.getWorkGroup();
					if (oldGdmAsManager.get(projectLocation.getProjectLocationId()) == null) {
						responseDtos.add(new GetPossibleRepMgrChangeResponseDto(Long.valueOf(devGdmId),
								allocation.getAllocationId(), allocation.getEmployee().getFullName(),
								projectLocation.getLocation().getName(), workgroup.getName(),
								allocation.getReportingManagerId().getFullName()));
					} else if (oldGdmAsManager.get(projectLocation.getProjectLocationId()) != null && oldGdmAsManager
							.get(projectLocation.getProjectLocationId()).get(workgroup.getName()) == null) {
						responseDtos.add(new GetPossibleRepMgrChangeResponseDto(Long.valueOf(devGdmId),
								allocation.getAllocationId(), allocation.getEmployee().getFullName(),
								projectLocation.getLocation().getName(), workgroup.getName(),
								allocation.getReportingManagerId().getFullName()));
					}
				}
			}
		}

		if (qaGdmId != null) {
			final Employee oldQAGDM = project.getEmployee2();
			final boolean isQAGDMPresent = oldQAGDM != null;
			if (isQAGDMPresent) {

				// get all managers of the project
				final Map<Long, Map<String, Employee>> existingAllManagers = project.getProjectLocations().stream()
						.collect(Collectors.toMap(ProjectLocation::getProjectLocationId,
								ProjectLocation::getAllManagers));

				// is the old GDM defined as a manager for any location/workgroup combination
				final Map<Long, Map<String, Employee>> oldGdmAsManager = new HashMap<Long, Map<String, Employee>>();

				existingAllManagers.entrySet().stream().forEach(e -> {
					final Map<String, Employee> m = e.getValue();
					m.entrySet().stream().forEach(k -> {
						if (k.getValue().getEmployeeId().equals(oldQAGDM.getEmployeeId())) {
							oldGdmAsManager.computeIfAbsent(e.getKey(), x -> new HashMap<String, Employee>());
							oldGdmAsManager.get(e.getKey()).put(k.getKey(), k.getValue());
						}
					});

				});

				// get all allocations which have old GDM as the Reporting manager
				final Set<Allocation> allocations = allocationRepository
						.findByProjectProjectIdAndReportingManagerIdEmployeeIdAndIsActive(Long.valueOf(projectId),
								oldQAGDM.getEmployeeId(), true);
				for (final Allocation allocation : allocations) {
					// If there are such allocations, check if the location/workgroup is same as the
					// combination for which old gdm is the manager
					// If not then, ask for a confirmation if the Reporting manager has to be
					// updated.
					final ProjectLocation projectLocation = allocation.getProjectLocation();
					final WorkGroup workgroup = allocation.getWorkGroup();
					if (oldGdmAsManager.get(projectLocation.getProjectLocationId()) == null) {
						responseDtos.add(new GetPossibleRepMgrChangeResponseDto(Long.valueOf(qaGdmId),
								allocation.getAllocationId(), allocation.getEmployee().getFullName(),
								projectLocation.getLocation().getName(), workgroup.getName(),
								allocation.getReportingManagerId().getFullName()));
					} else if (oldGdmAsManager.get(projectLocation.getProjectLocationId()) != null && oldGdmAsManager
							.get(projectLocation.getProjectLocationId()).get(workgroup.getName()) == null) {
						responseDtos.add(new GetPossibleRepMgrChangeResponseDto(Long.valueOf(qaGdmId),
								allocation.getAllocationId(), allocation.getEmployee().getFullName(),
								projectLocation.getLocation().getName(), workgroup.getName(),
								allocation.getReportingManagerId().getFullName()));
					}
				}
			}
		}
		return responseDtos;
	}

	private Map<Long, Map<String, Employee>> getOldGDMAsManager(final Project project, final Employee oldDevGDM) {
		// get all managers of the project
		final Map<Long, Map<String, Employee>> existingAllManagers = project.getProjectLocations().stream()
				.collect(Collectors.toMap(ProjectLocation::getProjectLocationId, ProjectLocation::getAllManagers));

		// is the old GDM defined as a manager for any location/workgroup combination
		final Map<Long, Map<String, Employee>> oldGdmAsManager = new HashMap<Long, Map<String, Employee>>();

		existingAllManagers.entrySet().stream().forEach(e -> {
			final Map<String, Employee> m = e.getValue();
			m.entrySet().stream().forEach(k -> {
				if (k.getValue().getEmployeeId().equals(oldDevGDM.getEmployeeId())) {
					oldGdmAsManager.computeIfAbsent(e.getKey(), x -> new HashMap<String, Employee>());
					oldGdmAsManager.get(e.getKey()).put(k.getKey(), k.getValue());
				}
			});

		});
		return oldGdmAsManager;
	}

	public void changeGdms(ChangeGdmRequestDto request) {
		logger.info("ChangeManagerService - changeGdms - starts.");
		final Long projectId = Long.valueOf(request.getProjectId());
		final Project project = projectRepository.findByProjectId(projectId);
		Employee newAssignedDevGdm = null;
		Employee newAssignedQaGdm = null;
		boolean isDevGDMPresent = false;
		boolean isQAGDMPresent = false;
		final Employee oldDevGDM = project.getEmployee1();
		final Employee oldQAGDM = project.getEmployee2();
		if (request.getDevGdmId() != null) {
			isDevGDMPresent = oldDevGDM != null;
			newAssignedDevGdm = employeeRepository.findByEmployeeId(Long.valueOf(request.getDevGdmId()));
			mailForChangeGdmForProject(newAssignedDevGdm, project.getEmployee1(), project);
			project.setEmployee1(newAssignedDevGdm);
		}

		if (request.getQaGdmId() != null) {
			isQAGDMPresent = oldQAGDM != null;
			newAssignedQaGdm = employeeRepository.findByEmployeeId(Long.valueOf(request.getQaGdmId()));
			mailForChangeGdmForProject(newAssignedQaGdm, project.getEmployee2(), project);
			project.setEmployee2(newAssignedQaGdm);
		}

		if (request.getReportingManagerList() != null) {
			for (final ChangeReportingManagerDto changReportingManagerDto : request.getReportingManagerList()) {
				changeReportingManager(changReportingManagerDto);
			}
		} else {
			// skip and save has been selected
			// check if GDM is still one of the managers of the project
			// If yes then, employees can continue to report to the old GDM
			// If no then, employees were reporting to because Old GDM was the GDM, now that
			// GDM has changed, Old GDM will be the reporting manager
			// but allocation manager has to be updated as per the location/workgroup
			// combination

			if (isDevGDMPresent) {
				// get all allocations which have old GDM as the Reporting manager
				updateAutoAllocationManager(project, oldDevGDM);
			}

			if (isQAGDMPresent) {
				// get all allocations which have old GDM as the Reporting manager
				updateAutoAllocationManager(project, oldQAGDM);
			}

		}

		if (!CollectionUtils.isEmpty(request.getGdmAssignResources()))
			resourceService.assignUserRole(request.getGdmAssignResources(), Roles.GDM.name());

		projectRepository.save(project);

		sfCallForGdmChange(newAssignedDevGdm, newAssignedQaGdm, projectId, oldDevGDM, oldQAGDM);

	}

	private void updateAutoAllocationManager(final Project project, final Employee oldGDM) {
		final Set<Allocation> allocations = allocationRepository
				.findByProjectProjectIdAndReportingManagerIdEmployeeIdAndIsActive(Long.valueOf(project.getProjectId()),
						oldGDM.getEmployeeId(), true);

		if (allocations != null && !allocations.isEmpty()) {
			final Map<Long, Map<String, Employee>> oldGdmAsManager = getOldGDMAsManager(project, oldGDM);
			for (final Allocation allocation : allocations) {
				final ProjectLocation projectLocation = allocation.getProjectLocation();
				final WorkGroup workgroup = allocation.getWorkGroup();
				if (oldGdmAsManager.get(projectLocation.getProjectLocationId()) == null
						|| (oldGdmAsManager.get(projectLocation.getProjectLocationId()) != null && oldGdmAsManager
								.get(projectLocation.getProjectLocationId()).get(workgroup.getName()) == null)) {
					final Employee allocationManager = allocationUtilityService.getAutoReportingManagerId(project,
							projectLocation, workgroup.getName());
					allocation.setAllocationManagerId(allocationManager);
					allocationRepository.save(allocation);
				}

			}

		}
	}

	private void sfCallForGdmChange(Employee newDevGdm, Employee newQaGdm, Long projectId, Employee oldDevGDM,
			Employee oldQAGDM) {
		final List<GdmChangeDto> gdmChangeDtos = new ArrayList<GdmChangeDto>();
		final Set<Allocation> allocationSet = allocationRepository.findByProjectId(projectId);

		for (final Allocation a : allocationSet) {
			final Allocation primaryAllocation = a.getEmployee().getPrimaryAllocation();
			if (primaryAllocation.getAllocationId().equals(a.getAllocationId())) {
				GdmChangeDto gdmChangeDto = null;
				if (primaryAllocation.getWorkGroup().getName().equalsIgnoreCase(ApplicationConstants.QA_WORK_GROUP)) {
					if (newQaGdm != null) {
						gdmChangeDto = new GdmChangeDto(String.valueOf(a.getEmployee().getEmployeeId()),
								Roles.GDM.name(), String.valueOf(newQaGdm.getEmployeeId()), new Date());
					} else if (oldQAGDM == null && newDevGdm != null) {
						gdmChangeDto = new GdmChangeDto(String.valueOf(a.getEmployee().getEmployeeId()),
								Roles.GDM.name(), String.valueOf(newDevGdm.getEmployeeId()), new Date());
					}
				} else {
					if (newDevGdm != null) {
						gdmChangeDto = new GdmChangeDto(String.valueOf(a.getEmployee().getEmployeeId()),
								Roles.GDM.name(), String.valueOf(newDevGdm.getEmployeeId()), new Date());
					} else if (oldDevGDM == null && newQaGdm != null) {
						gdmChangeDto = new GdmChangeDto(String.valueOf(a.getEmployee().getEmployeeId()),
								Roles.GDM.name(), String.valueOf(newQaGdm.getEmployeeId()), new Date());
					}

				}

				gdmChangeDtos.add(gdmChangeDto);
			}
		}
		sfIntegrationService
				.changeGDM(gdmChangeDtos.parallelStream().filter(Objects::nonNull).collect(Collectors.toList()));
	}

	public void changeReportingManagers(ChangeReportingManagerWrapperDto changeReportingManagerWrapperDto) {
		for (final ChangeReportingManagerDto request : changeReportingManagerWrapperDto.getChangeManagerList()) {
			changeReportingManager(request);
		}
	}

	private void changeReportingManager(final ChangeReportingManagerDto changReportingManagerDto) {
		final Allocation allocation = allocationRepository
				.findByAllocationIdAndIsActive(changReportingManagerDto.getAllocationId(), true).get();
		final Employee oldReportingManager = allocation.getReportingManagerId();
		final Employee newReportingManager = employeeRepository
				.findByEmployeeId(Long.valueOf(changReportingManagerDto.getNewReportingMangerId()));
		allocation.setReportingManagerId(newReportingManager);

		final Employee allocationManager = allocationUtilityService.getAllocationManagerId(
				allocation.getProjectLocation().getProject(), allocation.getProjectLocation(), newReportingManager,
				allocation.getWorkGroup().getName());
		if (!allocation.getAllocationManagerId().getEmployeeId().equals(allocationManager.getEmployeeId())) {
			allocation.setAllocationManagerId(allocationManager);
		}

		allocationRepository.save(allocation);

		final Allocation primaryAllocation = allocation.getEmployee().getPrimaryAllocation();

		mailForChangeReportingManagerForResource(newReportingManager, oldReportingManager, allocation, false);
		if (primaryAllocation.getAllocationId().equals(allocation.getAllocationId())) {
			mailForChangeReportingManagerForResource(newReportingManager, oldReportingManager, allocation, true);
			sfCallPrimaryManagerChange(newReportingManager, allocation);
		}

		syncToTimesheetService.syncProjectAllocation(allocation);

	}

	public void sfCallPrimaryManagerChange(Employee newReportingManager, Allocation allocation) {
		final ManagerChangeDto managerChangeDto = new ManagerChangeDto(
				String.valueOf(allocation.getEmployee().getEmployeeId()),
				String.valueOf(newReportingManager.getEmployeeId()), new Date());
		sfIntegrationService.changeManager(managerChangeDto);
		final ProjectChangeDto projectChangeDto = new ProjectChangeDto(
				String.valueOf(allocation.getEmployee().getEmployeeId()), new Date(),
				String.valueOf(allocation.getProject().getProjectId()),
				String.valueOf(allocation.getProject().getAccount().getAccountId()));
		sfIntegrationService.changeProjectOrAccount(projectChangeDto);
	}

	public List<GetReportingManagerResponseDto> getReportingManagerList(String resourceId) {

		final Set<Allocation> allocations = allocationRepository
				.findByEmployeeEmployeeIdAndIsActive(Long.valueOf(resourceId), true);

		return allocations.stream()
				.filter(a -> null != a && null != a.getProject()
						&& !StringUtils.equalsIgnoreCase(ApplicationConstants.NON_DELIVERY_BENCH_PROJECT_NAME,
								a.getProject().getName()))
				.map(a -> new GetReportingManagerResponseDto(a.getAllocationId(), a.getProject().getAccount().getName(),
						a.getProject().getName(),
						a.getAllocationDetails().stream().filter(AllocationDetail::getIsActive)
								.map(AllocationDetail::getAllocatedPercentage).reduce(0, Integer::sum),
						a.getReportingManagerId().getFullName(),
						a.getEmployee().getPrimaryAllocation().getAllocationId().equals(a.getAllocationId())))
				.collect(Collectors.toList());

	}

	public List<GetManagersResponseDto> getAllManagers(String projectId) {
		final Project project = projectRepository.findByProjectId(Long.valueOf(projectId));

		return project.getProjectLocations().stream().map(pl -> new GetManagersResponseDto(pl.getProjectLocationId(),
				pl.getLocation().getName(),
				CommonQualifiedMapper.employeeToFullName(pl.getAllManagers().get(ApplicationConstants.WORK_GROUP_DEV)),
				CommonQualifiedMapper.employeeToFullName(pl.getAllManagers().get(ApplicationConstants.QA_WORK_GROUP)),
				CommonQualifiedMapper.employeeToFullName(pl.getAllManagers().get(ApplicationConstants.WORK_GROUP_UI)),
				CommonQualifiedMapper
						.employeeToFullName(pl.getAllManagers().get(ApplicationConstants.WORK_GROUP_SUPPORT_1)),
				CommonQualifiedMapper
						.employeeToFullName(pl.getAllManagers().get(ApplicationConstants.WORK_GROUP_SUPPORT_2))))
				.collect(Collectors.toList());
	}

	public void changeManager(ChangeProjectManagerWrapperDto changeProjectManagerWrapperDto) {
		if (!CollectionUtils.isEmpty(changeProjectManagerWrapperDto.getManagerAssignResources()))
			resourceService.assignUserRole(changeProjectManagerWrapperDto.getManagerAssignResources(),
					Roles.MANAGER.name());

		for (final ChangeProjectManagerRequestDto request : changeProjectManagerWrapperDto
				.getChangeProjectMangerList()) {
			final ProjectLocation projectLocation = projectLocationRespository
					.findById(Long.valueOf(request.getProjectLocationId())).get();
			final Map<String, Employee> existingManagers = projectLocation.getAllManagers();

			if (request.getDevManagerId() != null) {
				updateWorkgroupMgr(request.getDevManagerId(), projectLocation, existingManagers,
						ApplicationConstants.WORK_GROUP_DEV);
			}

			if (request.getQaManagerId() != null) {
				updateWorkgroupMgr(request.getQaManagerId(), projectLocation, existingManagers,
						ApplicationConstants.QA_WORK_GROUP);
			}

			if (request.getUiManagerId() != null) {
				updateWorkgroupMgr(request.getUiManagerId(), projectLocation, existingManagers,
						ApplicationConstants.WORK_GROUP_UI);
			}

			if (request.getManager1Id() != null) {
				updateWorkgroupMgr(request.getManager1Id(), projectLocation, existingManagers,
						ApplicationConstants.WORK_GROUP_SUPPORT_1);
			}

			if (request.getManager2Id() != null) {
				updateWorkgroupMgr(request.getManager2Id(), projectLocation, existingManagers,
						ApplicationConstants.WORK_GROUP_SUPPORT_2);
			}
			projectLocationRespository.save(projectLocation);
		}
	}

	private void updateWorkgroupMgr(final String managerId, final ProjectLocation projectLocation,
			final Map<String, Employee> existingManagers, String workgroup) {
		final Employee assignManager = employeeRepository.findByEmployeeId(Long.valueOf(managerId));
		syncToTimesheetService.syncProjectManager(projectLocation, assignManager, workgroup);
		switch (workgroup) {

		case ApplicationConstants.WORK_GROUP_DEV:
			projectLocation.setEmployee1(assignManager);
			break;
		case ApplicationConstants.QA_WORK_GROUP:
			projectLocation.setEmployee2(assignManager);
			break;
		case ApplicationConstants.WORK_GROUP_UI:
			projectLocation.setEmployee3(assignManager);
			break;
		case ApplicationConstants.WORK_GROUP_SUPPORT_1:
			projectLocation.setEmployee4(assignManager);
			break;
		case ApplicationConstants.WORK_GROUP_SUPPORT_2:
			projectLocation.setEmployee5(assignManager);
			break;

		default:
			break;
		}

		mailForChangeManagerForProject(assignManager, existingManagers.get(workgroup), projectLocation);
		addOrUpdateManager(managerId, projectLocation, existingManagers, workgroup);
	}

	private void addOrUpdateManager(String managerId, final ProjectLocation projectLocation,
			final Map<String, Employee> existingManagers, String workgroup) {
		final boolean isManagerPresent = existingManagers.get(workgroup) != null;
		if (isManagerPresent) {
			// Workgroup manager is updated
			final Employee oldManager = existingManagers.get(workgroup);
			final Employee newManager = employeeRepository.findByEmployeeId(Long.valueOf(managerId));
			final List<Long> processedAllocationIds = new ArrayList<Long>();
			final Set<Allocation> allocationsForRM = allocationRepository
					.findByProjectLocationProjectLocationIdAndReportingManagerIdEmployeeIdAndIsActive(
							projectLocation.getProjectLocationId(), oldManager.getEmployeeId(), true);

			for (final Allocation allocation : allocationsForRM) {
				final WorkGroup allWorkgroup = allocation.getWorkGroup();

				// if the current work group's manager is changed or current work group does not
				// have a manager and is reporting to the old manager
				// being changed or although current work group has a manager but the selected
				// reporting manager is the manager being changed
				if (allWorkgroup.getName().equals(workgroup) || existingManagers.get(allWorkgroup.getName()) == null
						|| (existingManagers.get(allWorkgroup.getName()) != null
								&& !allocation.getReportingManagerId().getEmployeeId()
										.equals(existingManagers.get(allWorkgroup.getName()).getEmployeeId()))) {

					allocation.setReportingManagerId(newManager);

					final Employee allocationManager = allocationUtilityService.getAllocationManagerId(
							projectLocation.getProject(), allocation.getProjectLocation(), newManager,
							allocation.getWorkGroup().getName());
					if (!allocation.getAllocationManagerId().getEmployeeId()
							.equals(allocationManager.getEmployeeId())) {
						allocation.setAllocationManagerId(allocationManager);
					}

					mailForChangeReportingManagerForResource(newManager, oldManager, allocation, false);
					final Allocation primaryAllocation = allocation.getEmployee().getPrimaryAllocation();
					if (primaryAllocation.getAllocationId().equals(allocation.getAllocationId())) {
						mailForChangeReportingManagerForResource(newManager, oldManager, allocation, true);
						sfCall(newManager, allocation);
					}
					syncToTimesheetService.syncProjectAllocation(allocation);
				}
				processedAllocationIds.add(allocation.getAllocationId());
			}

			Set<Allocation> allocationsForAM = allocationRepository
					.findByProjectLocationProjectLocationIdAndAllocationManagerIdEmployeeIdAndIsActive(
							projectLocation.getProjectLocationId(), oldManager.getEmployeeId(), true);

			allocationsForAM = allocationsForAM.stream()
					.filter(a -> !processedAllocationIds.contains(a.getAllocationId())).collect(Collectors.toSet());

			for (final Allocation allocation : allocationsForAM) {
				final Employee allocationManager = allocationUtilityService.getAllocationManagerId(
						projectLocation.getProject(), allocation.getProjectLocation(), newManager,
						allocation.getWorkGroup().getName());
				if (!allocation.getAllocationManagerId().getEmployeeId().equals(allocationManager.getEmployeeId())) {
					allocation.setAllocationManagerId(allocationManager);
					syncToTimesheetService.syncProjectAllocation(allocation);
				}
			}
		}
	}

	private void sfCall(final Employee newManager, final Allocation allocation) {
		final ManagerChangeDto managerChangeDto = new ManagerChangeDto(
				String.valueOf(allocation.getEmployee().getEmployeeId()), String.valueOf(newManager.getEmployeeId()),
				new Date());
		sfIntegrationService.changeManager(managerChangeDto);
		final ProjectChangeDto projectChangeDto = new ProjectChangeDto(
				String.valueOf(allocation.getEmployee().getEmployeeId()), new Date(),
				String.valueOf(allocation.getProject().getProjectId()),
				String.valueOf(allocation.getProject().getAccount().getAccountId()));
		sfIntegrationService.changeProjectOrAccount(projectChangeDto);
	}

	public void changeReportingAndPrimaryMangers(ChangeManagerWrapperDto request) {
		logger.info("ChangeManagerService - changeReportingAndPrimaryMangers - starts.");
		for (final ChangeManagerDto changeManagerDto : request.getChangeManagerList()) {
			final Allocation allocation = allocationRepository.findById(changeManagerDto.getAllocationId()).get();
			final Employee oldReportingManager = allocation.getReportingManagerId();
			Employee newManager = null;
			if (changeManagerDto.getNewReportingMangerId() != null) {
				newManager = employeeRepository.findByEmployeeId(changeManagerDto.getNewReportingMangerId());

				allocation.setReportingManagerId(newManager);

				final Employee allocationManager = allocationUtilityService.getAllocationManagerId(
						allocation.getProjectLocation().getProject(), allocation.getProjectLocation(), newManager,
						allocation.getWorkGroup().getName());
				if (!allocation.getAllocationManagerId().getEmployeeId().equals(allocationManager.getEmployeeId())) {
					allocation.setAllocationManagerId(allocationManager);
					syncToTimesheetService.syncProjectAllocation(allocation);
				}
				mailForChangeReportingManagerForResource(newManager, oldReportingManager, allocation, false);
			}
			if (changeManagerDto.getIsPrimary()) {
				allocation.getEmployee().setPrimaryAllocation(allocation);
				final ManagerChangeDto managerChangeDto = new ManagerChangeDto(
						allocation.getEmployee().getEmployeeId().toString(),
						allocation.getReportingManagerId().getEmployeeId().toString(), new Date());
				sfIntegrationService.changeManager(managerChangeDto);
				final ProjectChangeDto projectChangeDto = new ProjectChangeDto(
						String.valueOf(allocation.getEmployee().getEmployeeId()), new Date(),
						String.valueOf(allocation.getProject().getProjectId()),
						String.valueOf(allocation.getProject().getAccount().getAccountId()));
				sfIntegrationService.changeProjectOrAccount(projectChangeDto);

				final Long gdmId = sfIntegrationService.getGdmIdForSFIntegration(allocation.getProject().getProjectId(),
						allocation.getWorkGroup().getName());

				final GdmChangeDto gdmChangeDto = new GdmChangeDto(
						String.valueOf(allocation.getEmployee().getEmployeeId()), "GDM", String.valueOf(gdmId),
						new Date());
				sfIntegrationService.changeGDM(gdmChangeDto);

				activeDirectoryDeltaUpdateService.update(
						CommonUtil.loginIdToMailId(allocation.getEmployee().getLoginId()),
						CommonUtil.loginIdToMailId(allocation.getReportingManagerId().getLoginId()),
						allocation.getProject().getName());
			}

			allocationRepository.save(allocation);

			final Allocation primaryAllocation = allocation.getEmployee().getPrimaryAllocation();
			if (primaryAllocation.getAllocationId().equals(allocation.getAllocationId())) {
				mailForChangeReportingManagerForResource(primaryAllocation.getReportingManagerId(), oldReportingManager,
						allocation, true);
			}
		}

	}

	public List<GetReporteeListResponseDto> getReporteeList(String reportingManagerResourceId) {

		final Set<Allocation> allocations = allocationRepository
				.getReporteesExcludingNdBench(Long.valueOf(reportingManagerResourceId));

		return allocations.stream()
				.map(a -> new GetReporteeListResponseDto(a.getAllocationId(), a.getEmployee().getEmpCode(),
						a.getEmployee().getFullName(), a.getProject().getAccount().getName(), a.getProject().getName(),
						a.getWorkGroup().getName(), a.getProjectLocation().getLocation().getName()))
				.collect(Collectors.toList());

	}

	public void mailForChangeManagerForProject(Employee newManager, Employee oldManager, ProjectLocation projectLoca) {
		logger.debug("Test Mail for mailForProjectManagerChange");
		final Map<String, Object> templateModel = new HashMap<>();
		templateModel.put("managerName", newManager.getFullName());
		templateModel.put("oldmanagerName", oldManager != null ? oldManager.getFullName() : "");
		templateModel.put("startDate", new SimpleDateFormat("dd-MMM-YYYY").format(new Date()));
		templateModel.put("projectName", projectLoca.getProject().getName());
		templateModel.put("accountName", projectLoca.getProject().getAccount().getName());
		templateModel.put("location", projectLoca.getLocation().getName());
		final String[] emailToList = new String[] { newManager.getEmail(),
				oldManager != null ? oldManager.getEmail() : "" };
		// templateModel.put(ApplicationConstants.EMAIL_CC, new String[]

		emailService.send("change-manager-for-project", templateModel, emailToList, new String[] {});
	}

	public void mailForChangeGdmForProject(Employee newManager, Employee oldManager, Project project) {
		logger.debug("Test Mail for mailForProjectManagerChange");
		final Map<String, Object> templateModel = new HashMap<>();
		templateModel.put("gdmName", newManager.getFullName());
		templateModel.put("oldgdmName", oldManager != null ? oldManager.getFullName() : "");
		templateModel.put("startDate", LocalDate.now().toString());
		templateModel.put("projectName", project.getName());
		templateModel.put("startDate", LocalDate.now().toString());
		templateModel.put("accountName", project.getAccount().getName());
		final Set<ProjectLocation> projectLocations = project.getProjectLocations();
		final String allLocationManagerEmails = projectLocations.stream().map(this::getEmails)
				.collect(Collectors.joining(","));
		final String[] emailToList = new String[] { newManager.getEmail(),
				oldManager != null ? oldManager.getEmail() : "", allLocationManagerEmails };
		emailService.send("change-gdm-for-project", templateModel, emailToList, new String[] {});
	}

	public String getEmails(ProjectLocation proLoc) {
		final Map<String, Employee> allManagers = proLoc.getAllManagers();
		final Collection<Employee> values = allManagers.values();
		final String collect = values.stream().map(Employee::getEmail).collect(Collectors.joining(","));

		return collect;

	}

	public void mailForChangeReportingManagerForResource(Employee reportingManger, Employee oldManager, Allocation a,
			boolean isPrimary) {
		logger.debug("Test Mail for mailForProjectManagerChange");
		String templateName = null;
		if (isPrimary) {
			templateName = "change-primary-manager-of-resource";
		} else {
			templateName = "change-reporting-manager-of-resource";
		}

		final Map<String, Object> templateModel = new HashMap<>();
		final Set<String> locationHr = masterService.getLocationHr(a.getEmployee().getLocation().getLocationId());
		templateModel.put("manager", reportingManger.getFullName());
		templateModel.put("employeeName", a.getEmployee().getFullName());
		templateModel.put("empCode", a.getEmployee().getEmpCode());
		templateModel.put("title", a.getEmployee().getTitle().getName());
		templateModel.put("projectName", a.getProject().getName());

		final String[] emailToList = new String[] { reportingManger.getEmail(),
				oldManager != null ? oldManager.getEmail() : "" };

		final String[] emailCCList = new String[] { String.join(",", locationHr) };

		emailService.send(templateName, templateModel, emailToList, emailCCList);
	}

}