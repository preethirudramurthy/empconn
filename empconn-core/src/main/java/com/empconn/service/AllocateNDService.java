package com.empconn.service;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.ZoneId;
import java.time.temporal.ChronoField;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;

import com.empconn.constants.ApplicationConstants;
import com.empconn.dto.allocation.AllocationRequestDto;
import com.empconn.dto.allocation.AllocationSummaryDto;
import com.empconn.dto.allocation.ExistingAllocationDto;
import com.empconn.dto.allocation.NDBenchAllocationRequestDto;
import com.empconn.dto.earmark.NdRequestListForAllocationDto;
import com.empconn.dto.earmark.NdRequestListForAllocationResponseDto;
import com.empconn.dto.ndallocation.IsValidNDRequestDto;
import com.empconn.dto.ndallocation.IsValidNDResponseDto;
import com.empconn.dto.ndallocation.NDAllocateRequest;
import com.empconn.dto.ndallocation.NDAllocateRequestDto;
import com.empconn.dto.ndallocation.NDAllocateResponseDto;
import com.empconn.dto.ndallocation.NdRequestDetailsForAllocationResponseDto;
import com.empconn.email.EmailService;
import com.empconn.exception.EmpConnException;
import com.empconn.mapper.CommonQualifiedMapper;
import com.empconn.mapper.NdRequestToNdRequestListForAllocationDtoMapper;
import com.empconn.persistence.entities.Allocation;
import com.empconn.persistence.entities.AllocationDetail;
import com.empconn.persistence.entities.Employee;
import com.empconn.persistence.entities.NdRequest;
import com.empconn.persistence.entities.NdRequestSalesforceIdentifier;
import com.empconn.persistence.entities.Project;
import com.empconn.persistence.entities.ProjectLocation;
import com.empconn.persistence.entities.SalesforceIdentifier;
import com.empconn.persistence.entities.WorkGroup;
import com.empconn.repositories.AllocationRepository;
import com.empconn.repositories.EmployeeRepository;
import com.empconn.repositories.NDRequestRepository;
import com.empconn.repositories.ProjectLocationRespository;
import com.empconn.repositories.ProjectRepository;
import com.empconn.repositories.SalesforceIdentifierRepository;
import com.empconn.repositories.WorkGroupRepository;
import com.empconn.repositories.specification.NDAllocateSpecification;
import com.empconn.vo.UnitValue;
import com.google.common.base.Predicates;
import com.google.common.collect.Iterables;

@Service
@Transactional
public class AllocateNDService {

	private static final Logger logger = LoggerFactory.getLogger(AllocateNDService.class);

	@Autowired
	private NDRequestRepository ndRequestRepository;

	@Autowired
	private AllocationRepository allocationRepository;

	@Autowired
	private EmailService emailService;

	@Autowired
	private AllocationUtilityService allocationUtilityService;

	@Autowired
	private ProjectRepository projectRepository;

	@Autowired
	private EmployeeRepository employeeRepository;

	@Autowired
	private WorkGroupRepository workGroupRepository;

	@Autowired
	private ProjectLocationRespository projectLocationRespository;

	@Autowired
	private SalesforceIdentifierRepository salesforceIdentifierRepository;

	@Autowired
	private SalesforceIdentifierService identifierService;

	@Autowired
	private MasterService masterService;

	@Autowired
	private NdRequestToNdRequestListForAllocationDtoMapper ndRequestToNdRequestListForAllocationDtoMapper;

	@Autowired
	private AllocationService allocationService;


	public List<NdRequestListForAllocationResponseDto> getNdRequestListForAllocation(
			final NdRequestListForAllocationDto filterCriteria) {
		final List<NdRequest> data = ndRequestRepository.findAll(new NDAllocateSpecification(filterCriteria));
		return ndRequestToNdRequestListForAllocationDtoMapper.mapToDtos(data);
	}

	public NdRequestDetailsForAllocationResponseDto getNdRequestDetailsForAllocation(final Long ndRequestId) {
		final String METHOD_NAME = "getNdRequestDetailsForAllocation";
		logger.info("{} starts execution with input ndRequestId : {}", METHOD_NAME, ndRequestId);

		final NdRequestDetailsForAllocationResponseDto dataDto = new NdRequestDetailsForAllocationResponseDto();
		Optional<NdRequest> nd = ndRequestRepository.findById(ndRequestId);
		NdRequest ndRequest = null;
		if (nd.isPresent())
			ndRequest = nd.get();

		dataDto.setNdRequestId(ndRequestId);
		if (ndRequest != null) {
			dataDto.setResourceId(ndRequest.getEmployee1().getEmployeeId());
			dataDto.setEmpCode(ndRequest.getEmployee1().getEmpCode());
			dataDto.setEmpName(ndRequest.getEmployee1().getFullName());
			dataDto.setTitle(ndRequest.getEmployee1().getTitle().getName());
			dataDto.setProjectId(ndRequest.getProject().getProjectId());
			dataDto.setProjectName(ndRequest.getProject().getName());
			dataDto.setRequestedPercentage(ndRequest.getPercentage());
			dataDto.setBillable(ndRequest.getIsBillable());
			dataDto.setAvailablePercentage(
					allocationUtilityService.getAvailablePercentageForND(ndRequest.getEmployee1().getEmployeeId()));

			final List<String> salesForceIdList = salesforceIdentifierRepository
					.findByProjectId(ndRequest.getProject().getProjectId());
			logger.debug("{} salesForceIdList : {} for project : {}", METHOD_NAME, salesForceIdList,
					ndRequest.getProject().getProjectId());
			dataDto.setProjectSalesforceIdList(salesForceIdList);

			final DateFormat df = new SimpleDateFormat(ApplicationConstants.DATE_FORMAT_YYYYMMDD);
			final String projectEndDate = df.format(ndRequest.getProject().getEndDate());
			logger.debug("{} projectEndDate : {}", METHOD_NAME, projectEndDate);
			dataDto.setProjectEndDate(projectEndDate);

			logger.debug("{} salesForceIdList : {}", METHOD_NAME, salesForceIdList);
			dataDto.setNdRequestSalesforceIdList(ndRequestToNdRequestSalesforceIdList(ndRequest));
			dataDto.getProjectSalesforceIdList().addAll(ndRequestToNdRequestSalesforceIdList(ndRequest));

			final String movementDate = CommonQualifiedMapper.getDateOfMovement().toString();
			logger.debug("{} movementDate : {}", METHOD_NAME, movementDate);
			dataDto.setMovementDate(movementDate);

			final Employee reportingManager = employeeRepository.findByEmployeeId(ndRequest.getEmployee2().getEmployeeId());
			final UnitValue rptMgr = new UnitValue(reportingManager.getEmployeeId(), reportingManager.getFullName());
			logger.debug("{} reporting manager : {}", METHOD_NAME, rptMgr.getValue());
			dataDto.setReportingManager(rptMgr);

			final List<Map<String, String>> managerList = allocationUtilityService
					.projectToManagerList(ndRequest.getProject());
			logger.debug("{} managerlist : {}", METHOD_NAME, managerList);
			dataDto.setManagerList(managerList);

			final List<Allocation> existingAllocationsInThisProject = allocationRepository
					.findByEmployeeEmployeeIdAndProjectProjectIdAndIsActiveIsTrue(ndRequest.getEmployee1().getEmployeeId(),
							ndRequest.getProject().getProjectId());

			// Getting all the existing allocations of Employee
			final List<ExistingAllocationDto> existingAllocations = new ArrayList<>();
			if (existingAllocationsInThisProject != null) {
				for (final Allocation allocation : existingAllocationsInThisProject) {
					final String reportingManagerName = allocation.getReportingManagerId().getFullName();

					boolean isPrimary = false;
					if (allocation.getAllocationId()
							.equals(ndRequest.getEmployee1().getPrimaryAllocation().getAllocationId())) {
						isPrimary = true;
					}

					final ExistingAllocationDto existingAllocationDto = new ExistingAllocationDto(
							Long.toString(allocation.getProjectLocation().getProjectLocationId()),
							allocation.getProjectLocation().getLocation().getName(), allocation.getWorkGroup().getName(),
							Long.toString(allocation.getReportingManagerId().getEmployeeId()), reportingManagerName,
							isPrimary, CommonQualifiedMapper.dateToLocalDate(allocation.getReleaseDate()));
					existingAllocations.add(existingAllocationDto);
				}
				logger.debug("{} existingAllocations : {}", METHOD_NAME, existingAllocations);
				dataDto.setExistingAllocationsInThisProject(existingAllocations);
			}

			// Getting Allocation Summary here:
			final List<AllocationSummaryDto> allocationSummary = allocationRepository
					.getAllocationSummary(ndRequest.getEmployee1().getEmployeeId());
			final Long primaryAllocationId = ndRequest.getEmployee1().getPrimaryAllocation().getAllocationId();

			final Set<Allocation> empAllocations = allocationRepository
					.findByEmployeeEmployeeIdAndIsActive(ndRequest.getEmployee1().getEmployeeId(), true);

			final Map<Long, List<Allocation>> employeeAllocationMap = empAllocations.stream()
					.collect(Collectors.groupingBy(a -> a.getProject().getProjectId()));

			allocationSummary.forEach(a -> {
				a.setReportingMangerName(a.getReportingManager().getFullName());
				a.setIsPrimary(employeeAllocationMap.get(a.getProjectId()).stream()
						.filter(i -> i.getAllocationId().equals(primaryAllocationId)).findAny().isPresent());
			});

			logger.debug("{} allocationSummary : {}", METHOD_NAME, allocationSummary);
			dataDto.setAllocationSummary(allocationSummary);
	
		}
		
		return dataDto;
	}

	@Transactional
	public IsValidNDResponseDto isValidNDRequestAllocation(IsValidNDRequestDto dto) {
		final String METHOD_NAME = "isValidNDRequestAllocation";
		logger.info("{} starts execution with input validation}", METHOD_NAME);
		final IsValidNDResponseDto responseDto = new IsValidNDResponseDto();
		NdRequest ndRequest = null;
		
		Optional<NdRequest> nd = ndRequestRepository.findById(dto.getNdRequestId());
		
		if (nd.isPresent()) ndRequest = nd.get();
		
		if (ndRequest != null) {
		final Long projectId = ndRequest.getProject().getProjectId();
		final Optional<Project> project = projectRepository.findById(projectId);
		final List<SalesforceIdentifier> salesforceIdentifierIdsList = salesforceIdentifierRepository
				.findByProjectProjectIdAndIsActiveIsTrue(projectId);

		if (!ndRequest.getIsActive())
			throw new EmpConnException("NDInvalidAllocateError");

		final Integer availablePercentage = allocationUtilityService
				.getAvailablePercentageForND(ndRequest.getEmployee1().getEmployeeId());
		logger.debug("{} availablePercentage : {}", METHOD_NAME, availablePercentage);

		final List<Allocation> a = allocationRepository.findByEmployeeEmployeeIdAndProjectProjectIdAndIsActiveIsTrue(
				dto.getResourceId(), projectId);
		if (!a.isEmpty()) {
			for (final Allocation all : a)
					responseDto.setInvalidLocationWorkgroup(!(all.getProjectLocation().getProjectLocationId().equals((Long.valueOf(dto.getProjectLocationId())))
							&& all.getWorkGroup().getName().equals(dto.getWorkgroup()) && all.getReportingManagerId()
							.getEmployeeId().equals(Long.valueOf(dto.getReportingManagerId()))));
		} else {
			responseDto.setInvalidLocationWorkgroup(false);
		}

		Iterables.removeIf(dto.getRequestSalesforceIdList(), Predicates.isNull());
		if (!dto.getRequestSalesforceIdList().isEmpty()) {

			for (final SalesforceIdentifier id : salesforceIdentifierIdsList) {
				dto.getRequestSalesforceIdList().remove(id.getValue());
			}

			for (final String id : dto.getRequestSalesforceIdList()) {
				if (project.isPresent() && !identifierService.isValidSalesforceIdForProject(id, project.get().getProjectId())) {
					responseDto.setInvalidSalesforceIdList(true);
					break;
				} else {
					responseDto.setInvalidSalesforceIdList(false);
				}
			}
		}

		responseDto.setInvalidAllocationPercentage((dto.getPercentage() > availablePercentage || dto.getPercentage() > 100));

		final Date releaseDate = Date.from(dto.getReleaseDate().atZone(ZoneId.systemDefault()).toInstant());
		responseDto.setInvalidReleaseDateAfterProjectDate((project.isPresent() && project.get().getEndDate().compareTo(releaseDate) < 0));
		final DayOfWeek day = DayOfWeek.of(dto.getReleaseDate().get(ChronoField.DAY_OF_WEEK));
		switch (day) {
		case SATURDAY:
			responseDto.setInvalidReleaseDateOnWeekend(true);
			break;
		case SUNDAY:
			responseDto.setInvalidReleaseDateOnWeekend(true);
			break;
		default:
			responseDto.setInvalidReleaseDateOnWeekend(false);
			break;
		}
		}
		return responseDto;

	}

	@Transactional
	public NDAllocateResponseDto ndAllocate(final NDAllocateRequest ndAllocateRequest) {
		validateNDAllocate(ndAllocateRequest.getAllocationList());
		final String METHOD_NAME = "ndAllocate";
		logger.info("{} starts execution with input ndAllocateRequests : {}", METHOD_NAME,
				ndAllocateRequest.getAllocationList().size());
		final NDAllocateResponseDto ndAllocateResponseDto = new NDAllocateResponseDto();

		final List<NDAllocateRequestDto> ndAllocateRequests = ndAllocateRequest.getAllocationList();

		// Check if any any request is having requested percentage more than 100 %
		final Map<Long, Long> resourceToPercentageDataMap = getResourceAndTotalRequestedPercentageMap(
				ndAllocateRequests);
		for (final Map.Entry<Long, Long> map : resourceToPercentageDataMap.entrySet()) {
			logger.debug("{} resourceId : {}, requested percentage : {}", METHOD_NAME, map.getKey(), map.getValue());
			if (map.getValue() > 100) {
				throw new EmpConnException("NDAllocatePercentageError");
			}
		}

		final Map<Long, Set<Allocation>> empAllocationsMap = new HashMap<>();

		for (final NDAllocateRequestDto ndAllocateRequestDto : ndAllocateRequests) {
			NdRequest ndRequest = null;
			
			Optional<NdRequest> nd = ndRequestRepository.findById(ndAllocateRequestDto.getRequestId());
			
			if (nd.isPresent()) ndRequest = nd.get();
			final List<String> requestSalesforceIdList = ndAllocateRequestDto.getRequestSalesforceIdList();

			// Get Extra SalesForceIds in the Request:
			if (ndRequest != null) {
				final List<SalesforceIdentifier> salesforceIdentifierIdsList = salesforceIdentifierRepository
						.findByProjectProjectIdAndIsActiveIsTrue(ndRequest.getProject().getProjectId());
				for (final SalesforceIdentifier id : salesforceIdentifierIdsList) {
					requestSalesforceIdList.remove(id.getValue());
				}
				logger.debug("requestSalesforceIdList:{}", requestSalesforceIdList);

				Optional<Project> p = projectRepository.findById(ndRequest.getProject().getProjectId());
				final Project project = p.isPresent()? p.get() : null;
				final WorkGroup workGroup = workGroupRepository.findByName(ndAllocateRequestDto.getWorkgroup());
				final Optional<ProjectLocation> projectLocation = projectLocationRespository
						.findById(Long.valueOf(ndAllocateRequestDto.getProjectLocationId()));
				final Project ndBenchProject = projectRepository.findByName("NDBench");
				final List<Allocation> allocation = allocationRepository
						.findByEmployeeEmployeeIdAndProjectProjectIdAndIsActiveIsTrue(
								ndAllocateRequestDto.getResourceId(), ndBenchProject.getProjectId());
				final Long ndBenchAllocationId = allocation.get(0).getAllocationId();

				final NDBenchAllocationRequestDto ndBenchAllocationRequestDto = new NDBenchAllocationRequestDto(
						ndBenchAllocationId, ndAllocateRequestDto.getResourceId(),
						String.valueOf(
								projectLocation.isPresent() ? projectLocation.get().getProjectLocationId() : null),
						workGroup.getName(), String.valueOf(ndAllocateRequestDto.getReportingManagerId()),
						requestSalesforceIdList, ndAllocateRequestDto.getPercentage(),
						LocalDate.parse(ndAllocateRequestDto.getStartDate()), ndAllocateRequestDto.getReleaseDate(),
						ndAllocateRequestDto.getBillable(), ndAllocateRequestDto.getIsPrimaryManager(),
						String.valueOf((project != null)?project.getProjectId():null));

				final AllocationRequestDto allocationRequestDto = new AllocationRequestDto(ndBenchAllocationRequestDto);
				final Allocation toAllocation = allocationService.allocate(allocationRequestDto);

				// Update the NDRequest status as False against which the User got the
				// Allocation.
				ndRequestRepository.updateNDRequestStatus(ndAllocateRequestDto.getRequestId(), Boolean.FALSE);

				// Auto-Cancel all the ND-Requests where "Requested Percentage" is greater than
				// "Available Percentage".
				autoCancelNDRequests(ndRequest.getEmployee1().getEmployeeId());

				// Send Mail for ND-Allocation.
				mailForAllocateRequestedNd(toAllocation, project, ndAllocateRequestDto);

				ndAllocateResponseDto.setFailedRequestIdList(new ArrayList<>());
				empAllocationsMap.computeIfAbsent(ndAllocateRequestDto.getResourceId(), k -> new HashSet<Allocation>());
				empAllocationsMap.get(ndAllocateRequestDto.getResourceId()).add(toAllocation);

			}

		}

		if (empAllocationsMap.size() > 0) {

			empAllocationsMap.entrySet().forEach(a -> {
				if (a.getValue() != null && a.getValue().size() > 1) {
					final Allocation primaryAllocation = allocationUtilityService.getPrimaryAllocation(a.getValue());
					primaryAllocation.getEmployee().setPrimaryAllocation(primaryAllocation);
					allocationRepository.save(primaryAllocation);

				}
			});
		}

		logger.info("{} exits successfully with return value : {}", METHOD_NAME, ndAllocateResponseDto);
		return ndAllocateResponseDto;
	}

	private void validateNDAllocate(List<NDAllocateRequestDto> allocationList) {
		final Map<Long, List<NDAllocateRequestDto>> employeeAllocationMap = allocationList.stream()
				.collect(Collectors.groupingBy(NDAllocateRequestDto::getResourceId));

		employeeAllocationMap.forEach((e, a) -> {
			final Integer availablePercentage = allocationUtilityService.getAvailablePercentageForND(e);
			final Integer sumOfAllocationPercent = a.stream().map(NDAllocateRequestDto::getPercentage).reduce(0,
					Integer::sum);

			if (sumOfAllocationPercent > availablePercentage)
				throw new EmpConnException("NDAllocatePercentageError");

			for (final NDAllocateRequestDto dto : a) {
				final NdRequest ndRequest = ndRequestRepository.findById(dto.getRequestId()).get();

				if (!ndRequest.getIsActive())
					throw new EmpConnException("NDInvalidAllocateError");

				final Long projectId = ndRequest.getProject().getProjectId();
				final List<Allocation> existingAllocations = allocationRepository
						.findByEmployeeEmployeeIdAndProjectProjectIdAndIsActiveIsTrue(dto.getResourceId(),
								projectId);

				if (!existingAllocations.isEmpty()) {
					for (final Allocation allocation : existingAllocations)
						if (!allocation.getProjectLocation().getProjectLocationId()
								.equals((Long.valueOf(dto.getProjectLocationId())))
								|| !allocation.getWorkGroup().getName().equals(dto.getWorkgroup())
								|| !allocation.getReportingManagerId().getEmployeeId()
										.equals(Long.valueOf(dto.getReportingManagerId())))
							throw new EmpConnException("LocationWorkGroupNotSameForSameProject");
				}
			}

		});

	}

	private void autoCancelNDRequests(final Long employeeId) {
		final String METHOD_NAME = "autoCancelNDRequests";
		logger.info("{} starts execution successfully with employeeId : {}", METHOD_NAME, employeeId);

		final List<NdRequest> allNDRequestsForEmployee = ndRequestRepository.findByEmployeeId(employeeId);
		final Integer availablePercentage = allocationUtilityService.getAvailablePercentageForND(employeeId);
		logger.debug("{} availablePercentage : {}", METHOD_NAME, availablePercentage);

		for (final NdRequest request : allNDRequestsForEmployee) {
			final Long ndRequestId = request.getNdRequestId();
			final Integer requestedPecentage = request.getPercentage();
			logger.debug("{} ndRequestId : {}, requestedPecentage : {}, availablePercentage : {}", METHOD_NAME,
					ndRequestId, requestedPecentage, availablePercentage);
			if (requestedPecentage > availablePercentage) {
				logger.debug("inside if block for cancelling the request : {}", ndRequestId);
				ndRequestRepository.updateNDRequestStatus(ndRequestId, Boolean.FALSE);

				try {
					mailForCancelAllocateRequest(ndRequestId);// Send email for auto canceling the Allocation Request
				} catch (final Exception exception) {
					exception.printStackTrace();
					logger.error("{} exception raised while sendin gthe email : {}", METHOD_NAME, exception);
				}
			}
		}
		logger.info("{} exits successfully", METHOD_NAME);
	}

	private Map<Long, Long> getResourceAndTotalRequestedPercentageMap(List<NDAllocateRequestDto> ndAllocateRequests) {
		final String METHOD_NAME = "getResourceAndTotalRequestedPercentageMap";
		logger.info("{} starts execution successfully.", METHOD_NAME);
		final Map<Long, Long> dataMap = new HashMap<>();
		for (final NDAllocateRequestDto ndAllocateRequestDto : ndAllocateRequests) {
			final Long resourceId = ndAllocateRequestDto.getResourceId();
			Long percentage = dataMap.get(resourceId);
			logger.debug("{} existing percentage : {}", METHOD_NAME, percentage);
			if (percentage != null && percentage != 0) {
				percentage = percentage + ndAllocateRequestDto.getPercentage();
			} else {
				percentage = Long.valueOf(ndAllocateRequestDto.getPercentage());
			}
			logger.debug("{} resourceId : {}, percentage: {}", METHOD_NAME, resourceId, percentage);
			dataMap.put(resourceId, percentage);
		}
		logger.info("{} exits successfully with return dataMap : {}", METHOD_NAME, dataMap);
		return dataMap;
	}

	public void mailForAllocateRequestedNd(Allocation a, Project p, NDAllocateRequestDto ndAllocateRequestDto) {
		final String METHOD_NAME = "mailForAllocateRequestedNd";
		logger.info("{} starts execution successfully", METHOD_NAME);
		final Set<ProjectLocation> pl = p.getProjectLocations();
		final StringBuilder sb = new StringBuilder();
		for (final ProjectLocation prjLoc : pl) {
			final Map<String, Employee> allManagers = prjLoc.getAllManagers();
			for (final Map.Entry<String, Employee> entry : allManagers.entrySet()) {
				logger.debug("{} projectName : {}", METHOD_NAME, entry);
				sb.append(entry.getValue().getEmail() + ",");
			}
		}
		final Map<String, Object> templateModel = new HashMap<>();
		final Set<String> locationHr = masterService.getLocationHr(a.getEmployee().getLocation().getLocationId());
		templateModel.put("empName", a.getEmployee().getFullName());
		templateModel.put("empId", a.getEmployee().getEmpCode());
		templateModel.put("empTitle", a.getEmployee().getTitle().getName());
		templateModel.put("allocationPercentage", a.getAllocationDetails().stream()
				.map(AllocationDetail::getAllocatedPercentage).reduce(0, Integer::sum));
		templateModel.put("newProjectName", a.getProject().getName());
		logger.debug("{} projectName : {}", METHOD_NAME, a.getProject().getName());
		final String dateOfMovement = ndAllocateRequestDto.getStartDate();
		final SimpleDateFormat sdf = new SimpleDateFormat(ApplicationConstants.DATE_FORMAT_YYYYMMDD);
		final SimpleDateFormat sdf2 = new SimpleDateFormat(ApplicationConstants.DATE_FORMAT_DD_MMM_YYYY);
		try {
			templateModel.put("dateOfMovement", sdf2.format(sdf.parse(dateOfMovement)));
		} catch (final ParseException e) {
			throw new EmpConnException("DefaultError");
		}

		templateModel.put("managerName",
				a.getReportingManagerId().getFirstName() + " " + a.getReportingManagerId().getLastName());

		logger.debug("{} managerName : {}", METHOD_NAME,
				a.getReportingManagerId().getFirstName() + " " + a.getReportingManagerId().getLastName());

		templateModel.put("managerId", a.getReportingManagerId().getEmpCode());

		final String[] emailToList = new String[] { a.getEmployee() == null ? "" : a.getEmployee().getEmail(),
				a.getReportingManagerId() == null ? "" : a.getReportingManagerId().getEmail(),
				(a.getAllocationManagerId() == null || a.getAllocationManagerId().equals(a.getReportingManagerId()))
						? ""
						: a.getAllocationManagerId().getEmail() };
		final String[] emailCCList = new String[] {
				a.getProject().getEmployee1() == null ? "" : a.getProject().getEmployee1().getEmail(),
				a.getProject().getEmployee2() == null ? "" : a.getProject().getEmployee2().getEmail(),
				a.getEmployee().getNdReportingManagerId() == null ? ""
						: a.getEmployee().getNdReportingManagerId().getEmail(),
				String.join(",", locationHr) };

		templateModel.put("newProjectName", a.getProject().getName());
		logger.debug("{} mail templete : {}", METHOD_NAME, templateModel);
		emailService.send("allocate-requested-nd-resource", templateModel, emailToList, emailCCList);
	}

	public void mailForCancelAllocateRequest(Long ndRequestId) {
		final String METHOD_NAME = "mailForCancelAllocateRequest";
		logger.info("{} starts execution successfully", METHOD_NAME);
		final Optional<NdRequest> ndRequestById = ndRequestRepository.findById(ndRequestId);
		if (ndRequestById.isPresent()) {
			final NdRequest ndRequest = ndRequestById.get();
			final Map<String, Object> templateModel = new HashMap<>();
			templateModel.put("resourceName",
					ndRequest.getEmployee1().getFirstName() + " " + ndRequest.getEmployee1().getLastName());
			templateModel.put("projectName", ndRequest.getProject().getName());
			templateModel.put("allocation", ndRequest.getPercentage());
			logger.debug("{} percentage : {} ", METHOD_NAME, ndRequest.getPercentage());
			final SimpleDateFormat sdf1 = new SimpleDateFormat(ApplicationConstants.DATE_FORMAT_DD_MMM_YYYY);
			final Date startDate = ndRequest.getStartDate();
			final Date tentativeReleaseEndDate = ndRequest.getReleaseDate();
			templateModel.put("allocStartDate", sdf1.format(startDate));
			templateModel.put("TentativeReleaseEndDate", sdf1.format(tentativeReleaseEndDate));

			String rptPMEmail = "";
			if (ndRequest.getEmployee2() != null)
				rptPMEmail = ndRequest.getEmployee2().getEmail();

			String departmentManagerEmail = "";
			if (ndRequest.getEmployee1().getNdReportingManagerId() != null)
				departmentManagerEmail = ndRequest.getEmployee1().getNdReportingManagerId().getEmail();

			String devGDMEmail = "";
			if (ndRequest.getProject().getEmployee1() != null)
				devGDMEmail = ndRequest.getProject().getEmployee1().getEmail();

			String qaGDMEmail = "";
			if (ndRequest.getProject().getEmployee2() != null)
				qaGDMEmail = ndRequest.getProject().getEmployee2().getEmail();

			emailService.send("auto-cancel-allocation-for-request", templateModel, new String[] { rptPMEmail },
					new String[] { qaGDMEmail, devGDMEmail, departmentManagerEmail });
		}

	}

	public void mailNdResourceAllocation(NdRequest ndRequest) {
		if (ndRequest != null) {
			final Map<String, Object> templateModel = new HashMap<>();
			templateModel.put("empId", ndRequest.getEmployee1().getEmpCode());
			templateModel.put("resourceName", ndRequest.getEmployee1().getFullName());
			templateModel.put("requestedAccount", ndRequest.getProject().getAccount().getName());
			templateModel.put("requestedProject", ndRequest.getProject().getName());
			templateModel.put("allocation", ndRequest.getPercentage());
			templateModel.put("startDate", new SimpleDateFormat(ApplicationConstants.DATE_FORMAT_DD_MMM_YYYY).format(ndRequest.getStartDate()));
			templateModel.put("endDate", new SimpleDateFormat(ApplicationConstants.DATE_FORMAT_DD_MMM_YYYY).format(ndRequest.getReleaseDate()));
			String devGDMEmail = "";
			if (ndRequest.getProject().getEmployee1() != null)
				devGDMEmail = ndRequest.getProject().getEmployee1().getEmail();

			String qaGDMEmail = "";
			if (ndRequest.getProject().getEmployee2() != null)
				qaGDMEmail = ndRequest.getProject().getEmployee2().getEmail();

			String departmentManagerEmail = "";
			if (ndRequest.getEmployee1().getNdReportingManagerId() != null)
				departmentManagerEmail = ndRequest.getEmployee1().getNdReportingManagerId().getEmail();

			emailService.send("nd-request-allocation", templateModel, new String[] {},
					new String[] { departmentManagerEmail, qaGDMEmail, devGDMEmail });
		}
	}

	List<String> ndRequestToNdRequestSalesforceIdList(NdRequest ndRequest) {
		if (CollectionUtils.isEmpty(ndRequest.getNdRequestSalesforceIdentifiers())) {
			return new ArrayList<>();
		}
		final List<String> ndRequestSfList = ndRequest.getNdRequestSalesforceIdentifiers().stream()
				.map(NdRequestSalesforceIdentifier::getValue).collect(Collectors.toList());
		if (!CollectionUtils.isEmpty(ndRequest.getProject().getSalesforceIdentifiers())) {
			final List<String> projectSfList = ndRequest.getProject().getSalesforceIdentifiers().stream()
					.map(SalesforceIdentifier::getValue).collect(Collectors.toList());
			ndRequestSfList.removeAll(projectSfList);
		}
		return ndRequestSfList;
	}

}
