
package com.empconn.service;

import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import java.util.function.Function;
import java.util.function.Predicate;
import java.util.stream.Collectors;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.empconn.constants.ApplicationConstants;
import com.empconn.dto.AllocationDto;
import com.empconn.dto.CancelNDRequestDto;
import com.empconn.dto.CancelRequestNDDto;
import com.empconn.dto.GetMyRequestListResponseDto;
import com.empconn.dto.NDResourcesDto;
import com.empconn.dto.RequestDto;
import com.empconn.dto.ResourceDto;
import com.empconn.dto.ResourceRequestDto;
import com.empconn.email.EmailService;
import com.empconn.exception.EmpConnException;
import com.empconn.exception.PreConditionFailedException;
import com.empconn.mapper.EmployeeToNDResourcesDtoMapper;
import com.empconn.mapper.NDRequestToGetMyRequestListResponseDtoMapper;
import com.empconn.mapper.ResourceRequestDtoToNdRequestMapper;
import com.empconn.persistence.entities.Employee;
import com.empconn.persistence.entities.NdRequest;
import com.empconn.persistence.entities.Project;
import com.empconn.persistence.entities.ProjectLocation;
import com.empconn.repositories.EmployeeRepository;
import com.empconn.repositories.NDRequestRepository;
import com.empconn.repositories.ProjectRepository;
import com.empconn.repositories.specification.NDResourceSpecification;
import com.empconn.security.SecurityUtil;
import com.empconn.util.RolesUtil;
import com.empconn.utilities.DateUtils;

@Service
public class NonDeliveryService {
	private static final Logger logger = LoggerFactory.getLogger(NonDeliveryService.class);

	@Autowired
	private EmployeeRepository employeeRepository;

	@Autowired
	private EmployeeToNDResourcesDtoMapper empToNDResourcesDtoMapper;

	@Autowired
	private ProjectRepository projectRepository;

	@Autowired
	private EmployeeService employeeService;

	@Autowired
	private SecurityUtil securityUtil;

	@Autowired
	private EmailService emailService;


	@Autowired
	NDRequestRepository ndRequestRepository;

	@Autowired
	ResourceRequestDtoToNdRequestMapper resourceRequestDtoToNdRequestMapper;

	@Autowired
	private SecurityUtil jwtEmployeeUtil;

	@Autowired
	private NDRequestToGetMyRequestListResponseDtoMapper ndRequestToGetMyRequestListResponseDtoMapper;

	@Autowired
	private SalesforceIdentifierService identifierService;

	@Autowired
	private AllocationUtilityService allocationUtilityService;

	@Autowired
	private AllocateNDService allocateNDService;

	public List<NDResourcesDto> getNonDeliveryResourceList(ResourceDto filter) {
		List<Employee> empList = employeeRepository.findAll(new NDResourceSpecification(filter));

		empList = empList.stream().filter(emp -> !employeeService.isDelivery(emp)).collect(Collectors.toList());

		final List<NDResourcesDto> ndResourcesDtos = empToNDResourcesDtoMapper.employeeToNDResourcesDto(empList);
		int sumAllocation;
		final int totalAllocation = 100;
		final List<NDResourcesDto> ndResourcesDtos2 = new ArrayList<>();
		for (final NDResourcesDto allocationDto : ndResourcesDtos) {
			sumAllocation = 0;
			final List<AllocationDto> allocationDtos = allocationDto.getAllocationList();
			if (allocationDtos != null && !allocationDtos.isEmpty()) {
				for (final AllocationDto allocationDto2 : allocationDtos) {
					if (!allocationDto2.getProjectName().equals("NDBench"))
						sumAllocation = sumAllocation + allocationDto2.getAllocationPercentage();
				}

			}
			allocationDto.setAvailablePercentage(totalAllocation - sumAllocation);
			ndResourcesDtos2.add(allocationDto);
		}
		return ndResourcesDtos2;
	}

	@Transactional
	public void createNDRequest(ResourceRequestDto resourceRequestDto) {
		final String METHOD_NAME = "createNDRequest";
		logger.debug("createNDRequest starts execution successfully.");
		isValidCreateNDRequest(resourceRequestDto);
		final List<NdRequest> ndRequestsList = new ArrayList<>();
		for (final RequestDto requestDto : resourceRequestDto.getRequestList()) {
			// Validating Extra SalesForce ID list. It should not be used by other
			// Project/Opportunity.
			final List<String> extraSalesForceIdList = requestDto.getExtraSalesforceIdList();
			logger.debug("{} extraSalesForceIdList : {}", METHOD_NAME, extraSalesForceIdList);

			if (extraSalesForceIdList != null && !extraSalesForceIdList.isEmpty()) {
				for (final String extraSalesforceId : extraSalesForceIdList) {
					logger.debug("{} extraSalesforceId : {}", METHOD_NAME, extraSalesforceId);
					final boolean isValidSalesForceId = identifierService
							.isValidSalesforceIdForProject(extraSalesforceId, Long.valueOf(requestDto.getProjectId()));

					if (!isValidSalesForceId) {
						logger.debug("SalesForce ID is already being used for some other Project or Opportunity.");
						throw new EmpConnException("SalesforceIdMustNotAlreadyExistProj");
					}
				}
			}
			final NdRequest ndRequest = resourceRequestDtoToNdRequestMapper.resourceRequestDtoToNdRequest(requestDto);
			ndRequest.setIsActive(Boolean.TRUE);
			ndRequest.setCreatedBy(securityUtil.getLoggedInEmployee().getEmployeeId());
			ndRequestsList.add(ndRequest);

		}
		final List<NdRequest> ndRequest = ndRequestRepository.saveAll(ndRequestsList);
		ndRequest.forEach(nd ->
			allocateNDService.mailNdResourceAllocation(nd)
		);
		logger.info("{} exits successfully.",METHOD_NAME );
	}

	private void isValidCreateNDRequest(ResourceRequestDto resourceRequestDto) {

		final String METHOD_NAME = "isValidCreateNDRequest";
		for (final RequestDto requestDto : resourceRequestDto.getRequestList()) {
			if (getExistingNdRequest(requestDto) > 0) {
				throw new PreConditionFailedException("NotAlreadyRequestedForSameProject");
			}

			// Scenario : Allocation Start Date should not be less than the Today's Date
			final Date todayDate = DateUtils
					.convertToDateViaInstant(LocalDate.now());
			final Date reqStartDate = DateUtils
					.convertToDateViaInstant(DateUtils.convertToLocalDateViaMilisecond(requestDto.getStartDate()));
			if (reqStartDate.compareTo(todayDate) < 0) {
				throw new EmpConnException("AllocationStartDateShouldNotBeLessThanTodaysDate");
			}


			// Scenario - Restrict request of a record for tentative release date greater
			// than project end date
			final Long projectId = Long.parseLong(requestDto.getProjectId());
			final Optional<Project> project = projectRepository.findById(projectId);
			final Date releaseDate = DateUtils
					.convertToDateViaInstant(DateUtils.convertToLocalDateViaMilisecond(requestDto.getReleaseDate()));
			if (project.isPresent() && project.get().getEndDate().compareTo(releaseDate) < 0) {
				throw new EmpConnException("ReleaseDateCanNotBeGreaterThanProjectEndDate");
			}

			// Scenario: When requested percentage is greater than available percentage
			final Integer requetedPercentage = requestDto.getPercentage();
			final Integer availablePercentage = allocationUtilityService
					.getAvailablePercentageForND(Long.parseLong(requestDto.getResourceId()));
			logger.debug("{} availablePercentage : {}", METHOD_NAME, availablePercentage);
			if (requetedPercentage.intValue() > availablePercentage.intValue()) {
				throw new EmpConnException("NotAvailableAllocatePercentage");
			}

		}

	}

	private int getExistingNdRequest(RequestDto requestDto) {

		final List<NdRequest> ndRequests = ndRequestRepository.findExistingNdRequest(
				Long.valueOf(requestDto.getProjectId()), Long.valueOf(requestDto.getResourceId()));
		if (ndRequests != null && !ndRequests.isEmpty()) {
			return ndRequests.size();
		}
		return 0;
	}

	public List<GetMyRequestListResponseDto> getMyRequestList() {
		final String METHOD_NAME = "getMyRequestList";
		logger.info("{} starts execution successfully.", METHOD_NAME);
		List<GetMyRequestListResponseDto> responseList = new ArrayList<>();
		List<NdRequest> ndRequests;
		final Employee loginUser = jwtEmployeeUtil.getLoggedInEmployee();
		logger.debug("{} loginUser : {}", METHOD_NAME, loginUser);

		if (RolesUtil.isGDMAndManager(loginUser)) {
			logger.debug("Getting ndrequests as GDM");
			ndRequests = ndRequestRepository.getNDRequestsAsGDM(loginUser.getEmployeeId());
			responseList.addAll(
					ndRequestToGetMyRequestListResponseDtoMapper.ndRequestsToGetMyRequestListResponses(ndRequests));
			responseList.addAll(getNdRequestsAsManager(loginUser));
			responseList = responseList.stream().filter(distinctByKey(GetMyRequestListResponseDto::getRequestId))
					.collect(Collectors.toList());

		} else if (RolesUtil.isOnlyGDM(loginUser)) {
			logger.debug("Getting ndrequests as GDM");
			ndRequests = ndRequestRepository.getNDRequestsAsGDM(loginUser.getEmployeeId());
			responseList = ndRequestToGetMyRequestListResponseDtoMapper
					.ndRequestsToGetMyRequestListResponses(ndRequests);
		} else {
			responseList = getNdRequestsAsManager(loginUser);
		}

		return responseList;
	}

	private List<GetMyRequestListResponseDto> getNdRequestsAsManager(final Employee loginUser) {
		logger.debug("Getting ndrequests as manager");
		final List<NdRequest> ndRequests = ndRequestRepository.getNDRequestsAsManager(loginUser.getEmployeeId());
		final List<NdRequest> filteredData = new ArrayList<>();

		// Filter only those records where logged in User is the Manager of any of the
		// type DEV/QA.
		for (final NdRequest ndRequest : ndRequests) {
			final List<Map<String, String>> managersList = ndRequest.getProject().getProjectLocations().stream()
					.map(x -> getProjectManagers.apply(x)).flatMap(List::stream).collect(Collectors.toList());
			for (final Map<String, String> manager : managersList) {
				final String mangerEmpCode = manager.get("mangerEmpCode");

				if (mangerEmpCode.equalsIgnoreCase(loginUser.getEmpCode())
						&& ndRequest.getEmployee2().getEmpCode().equalsIgnoreCase(loginUser.getEmpCode())) {
					filteredData.add(ndRequest);
					break;
				}
			}

		}
		return ndRequestToGetMyRequestListResponseDtoMapper.ndRequestsToGetMyRequestListResponses(filteredData);
	}

	Function<ProjectLocation, List<Map<String, String>>> getProjectManagers = projectLocation -> {
		final List<Map<String, String>> locationManagerList = new ArrayList<>();
		final Map<String, Employee> allManagers = projectLocation.getAllManagers();
		allManagers.entrySet().forEach(y -> {
			if (y.getValue() != null) {
				final Map<String, String> locationManagerMap = new HashMap<>();
				locationManagerMap.put("projectLocationName", projectLocation.getLocation().getName());
				locationManagerMap.put("workgroup", y.getKey());
				locationManagerMap.put("mangerName", y.getValue().getFirstName());
				locationManagerMap.put("mangerEmpCode", y.getValue().getEmpCode());
				locationManagerList.add(locationManagerMap);
			}

		});

		return locationManagerList;
	};

	public void cancelRequests(final CancelNDRequestDto cancelNDRequestDto) {
		final String METHOD_NAME = "cancelRequests";
		logger.info("{} starts execution successfully.", METHOD_NAME);
		try {
			final List<Long> requestIdList = cancelNDRequestDto.getRequestIdList();
			for (final Long requestId : requestIdList) {
				logger.debug("{} cancelling request with id : {}", METHOD_NAME, requestId);
				ndRequestRepository.updateNDRequestStatus(requestId, Boolean.FALSE);
				// Manual cancellation for sending Email
				mailForCancelRequestNd(requestId);
			}
		} catch (final Exception exception) {
			exception.printStackTrace();
			logger.error("{} exception raised as : {}", METHOD_NAME,exception.getMessage());
		}
	}

	public void mailForCancelRequestNd(Long requestId) {
		final String METHOD_NAME = "mailForCancelRequestNd";
		logger.info("{} starts execution successfully", METHOD_NAME);
		final Optional<NdRequest> ndRequestById = ndRequestRepository.findById(requestId);
		final Set<CancelRequestNDDto> cancelRequestNDDtos = new HashSet<>();
		try {

			if (ndRequestById.isPresent()) {
				final NdRequest ndRequest = ndRequestById.get();
				final Map<String, Object> templateModel = new HashMap<>();
				final CancelRequestNDDto dto = new CancelRequestNDDto();
				dto.setEmpId(ndRequest.getEmployee1().getEmpCode());
				dto.setResourceName(ndRequest.getEmployee1().getFullName());
				dto.setRequestedAccount(ndRequest.getProject().getAccount().getName());
				dto.setRequestedProject(ndRequest.getProject().getName());
				dto.setAllocation(ndRequest.getPercentage());
				dto.setAllocStartDate(new SimpleDateFormat(ApplicationConstants.DATE_FORMAT_DD_MMM_YYYY).format(ndRequest.getStartDate()));
				dto.setTentativeReleaseEndDate(new SimpleDateFormat(ApplicationConstants.DATE_FORMAT_DD_MMM_YYYY).format(ndRequest.getReleaseDate()));
				cancelRequestNDDtos.add(dto);
				templateModel.put("cancelNDRequest", dto);

				String departmentManagerEmail = null;
				if (ndRequest.getEmployee1().getNdReportingManagerId() != null)
					departmentManagerEmail = ndRequest.getEmployee1().getNdReportingManagerId().getEmail();

				String devGDMEmail = "";
				if (ndRequest.getProject().getEmployee1() != null)
					devGDMEmail = ndRequest.getProject().getEmployee1().getEmail();

				String qaGDMEmail = "";
				if (ndRequest.getProject().getEmployee2() != null)
					qaGDMEmail = ndRequest.getProject().getEmployee2().getEmail();

				emailService.send("request-for-allocation-cancel-request", templateModel, new String[] {},
						new String[] { departmentManagerEmail, qaGDMEmail, devGDMEmail });
			}

		} catch (final Exception exception) {
			exception.printStackTrace();
			logger.error("{} exception raised as : {}", METHOD_NAME, exception);
		}

	}

	public static <T> Predicate<T> distinctByKey(Function<? super T, Object> keyExtractor) {
		final Map<Object, Boolean> seen = new ConcurrentHashMap<>();
		return t -> seen.putIfAbsent(keyExtractor.apply(t), Boolean.TRUE) == null;
	}

}
