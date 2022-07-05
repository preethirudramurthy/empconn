package com.empconn.service;

import static java.time.DayOfWeek.SATURDAY;
import static java.time.DayOfWeek.SUNDAY;

import java.text.SimpleDateFormat;
import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.temporal.ChronoField;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;

import com.empconn.constants.ApplicationConstants;
import com.empconn.dto.allocation.AllocationForEarmarkDto;
import com.empconn.dto.allocation.AllocationRequestDto;
import com.empconn.dto.allocation.AllocationSummaryDto;
import com.empconn.dto.allocation.EarmarkAllocationRequestDto;
import com.empconn.dto.allocation.EarmarkForAllocationDto;
import com.empconn.dto.allocation.EarmarkSearchDto;
import com.empconn.dto.allocation.ExistingAllocationDto;
import com.empconn.dto.allocation.IsValidEarmarkAllocationDto;
import com.empconn.dto.allocation.ValidateEarmarkAllocateDto;
import com.empconn.email.EmailService;
import com.empconn.exception.EmpConnException;
import com.empconn.mapper.AllocationMapper;
import com.empconn.mapper.CommonQualifiedMapper;
import com.empconn.mapper.EarmarkToAllocationForEarmarkDtoMapper;
import com.empconn.persistence.entities.Allocation;
import com.empconn.persistence.entities.AllocationDetail;
import com.empconn.persistence.entities.AllocationStatus;
import com.empconn.persistence.entities.Earmark;
import com.empconn.persistence.entities.Employee;
import com.empconn.persistence.entities.Opportunity;
import com.empconn.persistence.entities.Project;
import com.empconn.persistence.entities.ProjectLocation;
import com.empconn.persistence.entities.WorkGroup;
import com.empconn.repositories.AllocationRepository;
import com.empconn.repositories.AllocationStatusRepository;
import com.empconn.repositories.EarmarkRepository;
import com.empconn.repositories.EarmarkSalesforceIdentifierRepository;
import com.empconn.repositories.EmployeeRepository;
import com.empconn.repositories.ProjectRepository;
import com.empconn.repositories.SalesforceIdentifierRepository;
import com.empconn.repositories.specification.EarmarkSpecification;
import com.empconn.security.SecurityUtil;
import com.empconn.successfactor.service.SFIntegrationService;
import com.empconn.utilities.TimeUtils;

@Service
public class AllocateEarmarkedService {
	
	private static final String PROJECT_NAME = "projectName";

	public static final String NOT_AVAILABLE_PRIOR_ALLOC = "NotAvailablePrioAllocatePercentage";

	@Autowired
	private AllocationRepository allocationRepository;

	@Autowired
	private AllocationMapper allocationMapper;

	@Autowired
	private EarmarkRepository earMarkRepository;

	@Autowired
	private ProjectRepository projectRepository;

	@Autowired
	private SalesforceIdentifierRepository salesforceIdentifierRepository;

	@Autowired
	private EmailService emailService;

	@Autowired
	private SecurityUtil securityUtil;

	@Autowired
	private EarmarkToAllocationForEarmarkDtoMapper earmarkToAllocationForEarmarkDtoMapper;

	@Autowired
	private MasterService masterService;

	@Autowired
	AllocationService allocationService;

	@Autowired
	private AllocationStatusRepository allocationStatusRepository;

	@Autowired
	SalesforceIdentifierService salesforceIdentifierService;

	@Autowired
	EmployeeRepository employeeRepository;

	@Autowired
	SFIntegrationService sfIntegrationService;

	@Autowired
	EarmarkSalesforceIdentifierRepository earmarkSalesforceIdentifierRepository;

	public List<AllocationForEarmarkDto> getEarmarkListForAllocation(EarmarkSearchDto earmarkSearchDto) {
		final EarmarkSpecification earmarkSpecification = new EarmarkSpecification(earmarkSearchDto);
		final List<Earmark> earmarks = earMarkRepository.findAll(earmarkSpecification);
		earmarks.sort(Comparator.comparing(e -> e.getAllocation().getEmployee().getFullName()));
		return earmarkToAllocationForEarmarkDtoMapper.earmarkToAllocationForEarmarkDtoList(earmarks);
	}

	public EarmarkForAllocationDto getEarmarkDetailsForAllocation(Long earmarkId, Long projectId) {
		final Optional<Earmark> selectedEarmark = earMarkRepository.findById(earmarkId);
		Optional<Project> p = projectRepository.findById(projectId);
		Project toProject = null;
		if (p.isPresent())
			toProject = p.get();
		if (selectedEarmark.isPresent()) {
			final Long employeeId = selectedEarmark.get().getAllocation().getEmployee().getEmployeeId();

			final List<AllocationSummaryDto> allocationSummary = allocationRepository.getAllocationSummary(employeeId);
			final Long primaryAllocationId = selectedEarmark.get().getAllocation().getEmployee().getPrimaryAllocation()
					.getAllocationId();

			final Set<Allocation> empAllocations = allocationRepository.findByEmployeeEmployeeIdAndIsActive(
					selectedEarmark.get().getAllocation().getEmployee().getEmployeeId(), true);

			final Map<Long, List<Allocation>> employeeAllocationMap = empAllocations.stream()
					.collect(Collectors.groupingBy(a -> a.getProject().getProjectId()));

			allocationSummary.forEach(a -> {
				a.setReportingMangerName(String.join(" ", a.getReportingManager().getFirstName(),
						a.getReportingManager().getLastName()));
				a.setIsPrimary(employeeAllocationMap.get(a.getProjectId()).stream()
						.anyMatch(i -> i.getAllocationId().equals(primaryAllocationId)));
			});

			ExistingAllocationDto existingAllocationDto = null;
			Allocation existAllocation;
			final List<Allocation> existingAllocationList = allocationRepository
					.findByEmployeeEmployeeIdAndProjectProjectIdAndIsActiveTrue(employeeId, projectId);
			if (!existingAllocationList.isEmpty()) {
				existAllocation = existingAllocationList.get(0);
				final String reportingManagerName = existAllocation.getReportingManagerId().getFirstName() + " "
						+ existAllocation.getReportingManagerId().getLastName();
				boolean isPrimary =  (existAllocation.getReportingManagerId().getEmployeeId().equals(employeeId)) ;
				existingAllocationDto = new ExistingAllocationDto(
						Long.toString(existAllocation.getProjectLocation().getProjectLocationId()),
						existAllocation.getProjectLocation().getLocation().getName(),
						existAllocation.getWorkGroup().getName(),
						Long.toString(existAllocation.getReportingManagerId().getEmployeeId()), reportingManagerName,
						isPrimary, CommonQualifiedMapper.dateToLocalDate(existAllocation.getReleaseDate()));

			}

			return allocationMapper.earmarkToEarmarkForAllocationDto(selectedEarmark.get(), allocationSummary,
					existingAllocationDto, toProject);
		} else
			return null;
	}

	public IsValidEarmarkAllocationDto isValidEarmarkAllocation(ValidateEarmarkAllocateDto dto) {
		final IsValidEarmarkAllocationDto isValidDto = new IsValidEarmarkAllocationDto();
		final Project project = projectRepository.findByProjectId(Long.valueOf(dto.getProjectId()));
		final Set<String> projectSalesforceIds = salesforceIdentifierRepository
				.findSalesforceIdsForProject(Long.valueOf(dto.getProjectId()));
		final List<Allocation> allocations = allocationRepository
				.findByEmployeeEmployeeIdAndProjectProjectIdAndIsActiveIsTrue(Long.valueOf(dto.getResourceId()),
						Long.valueOf(dto.getProjectId()));
		Earmark earmark = null;
		Optional<Earmark> e = earMarkRepository.findById(Long.valueOf(dto.getEarmarkId()));
			if (e.isPresent())
					earmark = e.get();
		if (earmark != null && !earmark.getIsActive())
			throw new EmpConnException("NotAvailableAllocatePercentage");
		final Allocation currentAllocation = Objects.requireNonNull(earmark).getAllocation();

		if (!allocations.isEmpty()) {
			for (final Allocation allocation : allocations)
					isValidDto.setInvalidLocationWorkgroup(!(allocation.getProjectLocation().getProjectLocationId()
							.equals((Long.valueOf(dto.getProjectLocationId())))
							&& allocation.getWorkGroup().getName().equals(dto.getWorkgroup())
							&& allocation.getReportingManagerId().getEmployeeId()
									.equals(Long.valueOf(dto.getReportingManagerId()))));
		}

		if (currentAllocation != null
				&& currentAllocation.getProject().getAccount().getName().equalsIgnoreCase("Bench")) {
				isValidDto.setInvalidAllocationPercentage((dto.getPercentage() > currentAllocation.getAllocationDetails().stream()
						.filter(AllocationDetail::getIsActive).map(AllocationDetail::getAllocatedPercentage)
						.reduce(0, Integer::sum)));
		} else
			isValidDto.setInvalidAllocationPercentage(true);

		for (final String id : dto.getRequestSalesforceIdList()) {
			if (!projectSalesforceIds.contains(id)) {
				boolean isValidSf;
				if (earmark.getOpportunity() != null)
					isValidSf = salesforceIdentifierService.isValidSalesforceIdForOppurtunity(id,
							earmark.getOpportunity().getOpportunityId());
				else
					isValidSf = salesforceIdentifierService.isValidSalesforceIdForProject(id,
							Long.valueOf(dto.getProjectId()));
				if (!isValidSf) {
					isValidDto.setInvalidSalesforceIdList(true);
					break;
				} else
					isValidDto.setInvalidSalesforceIdList(false);
			}
		}

		final Date releaseDate = CommonQualifiedMapper.localDateTimeToDate(dto.getReleaseDate());
		final DayOfWeek releaseDay = DayOfWeek.of(dto.getReleaseDate().get(ChronoField.DAY_OF_WEEK));
		isValidDto.setInvalidReleaseDateAfterProjectDate(((project.getEndDate().compareTo(releaseDate) < 0) || (releaseDay.equals(SATURDAY) || releaseDay.equals(SUNDAY))));

		return isValidDto;
	}

	@Transactional
	public void earmarkAllocate(List<EarmarkAllocationRequestDto> allocationList) {
		validateEarmarkAllocate(allocationList);
		for (final EarmarkAllocationRequestDto request : allocationList) {
		Earmark earmark = null;
		Optional<Earmark> e = earMarkRepository.findById(Long.valueOf(request.getEarmarkId()));
			if (e.isPresent())
					earmark = e.get();
			final Allocation toAllocation = allocationService.allocate(new AllocationRequestDto(request));
			if (earmark != null) {
				request.setAllocationId(earmark.getAllocation().getAllocationId());

				if ((earmark.getProject() != null
						&& earmark.getProject().getProjectId().equals(toAllocation.getProject().getProjectId()))
						|| earmark.getOpportunity() != null) {
					earmark.setUnearmarkInfo(ApplicationConstants.SYSTEM,
							ApplicationConstants.UNEARMARK_ALLOCATE_EARMARK_COMMENT);
					earMarkRepository.save(earmark);
				}
				unearmarkOnEarmarkAllocate(earmark.getAllocation());
			}
			sendMailOnAllocateEarmarkedResource(toAllocation);
		}
	}

	private void validateEarmarkAllocate(List<EarmarkAllocationRequestDto> allocationList) {
		class Tuple {
			final String employeeId;
			final String projectId;

			public Tuple(String employeeId, String projectId) {
				super();
				this.employeeId = employeeId;
				this.projectId = projectId;
			}

			@Override
			public int hashCode() {
				return Objects.hash(employeeId, projectId);
			}

			@Override
			public boolean equals(Object obj) {
				if (this == obj)
					return true;
				if (obj == null)
					return false;
				if (getClass() != obj.getClass())
					return false;
				final Tuple other = (Tuple) obj;
				return Objects.equals(employeeId, other.employeeId) && Objects.equals(projectId, other.projectId);
			}

		}

		class TupleGroup {
			private final String projectLocationId;
			private final String workgroup;
			private final String reportingManagerId;

			public TupleGroup(String projectLocationId, String workgroup, String reportingManagerId) {
				super();
				this.projectLocationId = projectLocationId;
				this.workgroup = workgroup;
				this.reportingManagerId = reportingManagerId;
			}

			@Override
			public int hashCode() {
				return Objects.hash(projectLocationId, reportingManagerId, workgroup);
			}

			@Override
			public boolean equals(Object obj) {
				if (this == obj)
					return true;
				if (obj == null)
					return false;
				if (getClass() != obj.getClass())
					return false;
				final TupleGroup other = (TupleGroup) obj;
				return Objects.equals(projectLocationId, other.projectLocationId)
						&& Objects.equals(reportingManagerId, other.reportingManagerId)
						&& Objects.equals(workgroup, other.workgroup);
			}
		}
		final Map<Tuple, List<EarmarkAllocationRequestDto>> employeeAllocationMap = allocationList.stream()
				.collect(Collectors.groupingBy(a -> new Tuple(a.getResourceId(), a.getProjectId())));

		employeeAllocationMap.forEach((e, a) -> {
			Optional<Earmark> earmarkOpt = earMarkRepository.findById(Long.valueOf(a.get(0).getEarmarkId()));
			if (earmarkOpt.isPresent()) {
				final Earmark earmark = earMarkRepository.findById(Long.valueOf(a.get(0).getEarmarkId())).get();
				final Allocation currentAllocation = earmark.getAllocation();

				if (!earmark.getIsActive())
					throw new EmpConnException("NotAvailableAllocatePercentage");

				final List<Allocation> existingAllocations = allocationRepository
						.findByEmployeeEmployeeIdAndProjectProjectIdAndIsActiveIsTrue(Long.valueOf(e.employeeId),
								Long.valueOf(e.projectId));
				final Integer sumOfAllocationPercent = a.stream().map(EarmarkAllocationRequestDto::getPercentage)
						.reduce(0, Integer::sum);

				if (currentAllocation == null)
					throw new EmpConnException(NOT_AVAILABLE_PRIOR_ALLOC);

				if (sumOfAllocationPercent > currentAllocation.getAllocationDetails().stream()
						.filter(AllocationDetail::getIsActive).map(AllocationDetail::getAllocatedPercentage)
						.reduce(0, Integer::sum)) {
					throw new EmpConnException(NOT_AVAILABLE_PRIOR_ALLOC);
				}
				if (!existingAllocations.isEmpty()) {
					for (final Allocation allocation : existingAllocations)
						for (final EarmarkAllocationRequestDto dto : a) {
							if (!allocation.getProjectLocation().getProjectLocationId()
									.equals((Long.valueOf(dto.getProjectLocationId())))
									|| !allocation.getWorkGroup().getName().equals(dto.getWorkgroup())
									|| !allocation.getReportingManagerId().getEmployeeId()
											.equals(Long.valueOf(dto.getReportingManagerId()))) {
								throw new EmpConnException("LocationWorkGroupNotSameForSameProject");
							}
						}
				}
				if (a.size() > 1) {
					final Map<TupleGroup, List<EarmarkAllocationRequestDto>> allocationResourceMap = a.stream()
							.collect(Collectors.groupingBy(al -> new TupleGroup(al.getProjectLocationId(),
									al.getWorkgroup(), al.getReportingManagerId())));
					if (allocationResourceMap.size() > 1)
						throw new EmpConnException("LocationWorkGroupNotSameForSameProject");
				}
			}
		});

	}

	public void unearmarkOnEarmarkAllocate(Allocation earmarkedAllocation) {
		final Optional<List<Earmark>> earmarksForAllocation = earMarkRepository
				.findByAllocationAllocationIdAndIsActiveIsTrue(earmarkedAllocation.getAllocationId());

		if (earmarksForAllocation.isPresent()) {
			final List<Earmark> earmarks = earmarksForAllocation.get();
			final List<Earmark> unearmarks = new ArrayList<>();
			if (earmarkedAllocation.getIsActive()) {
				for (final Earmark e : earmarks) {
					if (e.getPercentage() > earmarkedAllocation.getAllocationDetails().stream()
							.filter(AllocationDetail::getIsActive).map(AllocationDetail::getAllocatedPercentage)
							.reduce(0, Integer::sum)) {
						unearmarks.add(e);
					}
				}
			} else {
				unearmarks.addAll(earmarks);
			}
			if (!CollectionUtils.isEmpty(unearmarks)) {
				unearmarks.forEach(e -> 
					e.setUnearmarkInfo(ApplicationConstants.SYSTEM,
							ApplicationConstants.UNEARMARK_ALLOCATE_NOT_AVAILABLE_PERCENTAGE_COMMENT)
				);
				final List<Long> unearmarkIds = unearmarks.stream().map(Earmark::getEarmarkId)
						.collect(Collectors.toList());
				earMarkRepository.saveAll(unearmarks);
				earmarkSalesforceIdentifierRepository.softDelete(unearmarkIds);
				for (final Earmark e : unearmarks)
					sendMailOnAllocateEarmarkedResourceUnearmarked(e);
			}
		}
	}

	public void sendMailOnAllocateEarmarkedResource(Allocation allocation) {
		final List<String> emailToList = new ArrayList<>();
		final List<String> emailCCList = new ArrayList<>();
		final Map<String, Object> templateModel = new HashMap<>();

		final Employee emp = allocation.getEmployee();
		final Project project = allocation.getProject();

		final Optional<AllocationDetail> allocationDetail = allocation.getAllocationDetails().stream()
				.filter(AllocationDetail::getIsActive).max(Comparator.comparing(AllocationDetail::getCreatedOn));

		if (allocation.getReportingManagerId().getEmployeeId()
				.equals(allocation.getAllocationManagerId().getEmployeeId()))
			emailToList.add(allocation.getReportingManagerId().getEmail());
		else {
			emailToList.add(allocation.getReportingManagerId().getEmail());
			emailToList.add(allocation.getAllocationManagerId().getEmail());
		}

		emailToList.add(allocation.getEmployee().getEmail());

		if (project.getEmployee1() != null) {
			emailCCList.add(project.getEmployee1().getEmail());
		}
		if (project.getEmployee2() != null) {
			emailCCList.add(project.getEmployee2().getEmail());
		}

		final Set<String> locationHr = masterService
				.getLocationHr(allocation.getEmployee().getLocation().getLocationId());
		emailCCList.addAll(locationHr);
		templateModel.put("employeeName", CommonQualifiedMapper.employeeToFullName(emp));
		templateModel.put("employeeId", emp.getEmpCode());
		templateModel.put("employeeTitle", emp.getTitle().getName());
		templateModel.put("allocationPercent",
				StringUtils.join(allocation.getAllocationDetails().stream().filter(AllocationDetail::getIsActive)
						.map(AllocationDetail::getAllocatedPercentage).reduce(0, Integer::sum), "%"));
		templateModel.put(PROJECT_NAME, allocation.getProject().getName());

		allocationDetail.ifPresent(detail -> templateModel.put("dateOfMovement",
				new SimpleDateFormat(ApplicationConstants.DATE_FORMAT_DD_MMM_YYYY).format(detail.getStartDate())));
		templateModel.put("reportingManagerName",
				CommonQualifiedMapper.employeeToFullName(allocation.getReportingManagerId()));
		templateModel.put("reportingManagerId", allocation.getReportingManagerId().getEmpCode());
		emailService.send("allocate-earmarked-resource", templateModel,
				emailToList.toArray(new String[0]),
				emailCCList.toArray(new String[0]));
	}

	public void sendMailOnAllocateEarmarkedResourceUnearmarked(Earmark earmark) {
		final List<String> emailToList = new ArrayList<>();
		final List<String> emailCCList = new ArrayList<>();
		final Map<String, Object> templateModel = new HashMap<>();

		// TO DO : chek earmark is for project or oppurtunity

		final Project project = earmark.getProject();
		final Opportunity oppurtunity = earmark.getOpportunity();
		final Employee emp = earmark.getAllocation().getEmployee();

		if (project != null) {
			if (project.getEmployee2() != null)
				emailCCList.add(project.getEmployee2().getEmail());

			if (project.getEmployee1() != null)
				emailCCList.add(project.getEmployee1().getEmail());

			templateModel.put("accountName", project.getAccount().getName());
			templateModel.put(PROJECT_NAME, project.getName());

		} else if (oppurtunity != null) {
			if (oppurtunity.getEmployee2() != null)
				emailCCList.add(oppurtunity.getEmployee2().getEmail());

			if (oppurtunity.getEmployee1() != null)
				emailCCList.add(oppurtunity.getEmployee1().getEmail());

			templateModel.put("accountName", oppurtunity.getAccountName());
			templateModel.put(PROJECT_NAME, oppurtunity.getName());
		}

		emailToList.add(earmark.getEmployee2().getEmail());

		// TO DO :Mailer to the managers of the earmarked project, emailToList
		templateModel.put("employeeId", emp.getEmpCode());
		templateModel.put("employeeName", CommonQualifiedMapper.employeeToFullName(emp));
		templateModel.put("title", emp.getTitle().getName());

		templateModel.put("startDate", new SimpleDateFormat(ApplicationConstants.DATE_FORMAT_DD_MMM_YYYY).format(earmark.getStartDate()));
		templateModel.put("endDate", new SimpleDateFormat(ApplicationConstants.DATE_FORMAT_DD_MMM_YYYY).format(earmark.getEndDate()));
		templateModel.put("isBillable", earmark.getBillable()? "Yes" : "No");
		emailService.send("allocate-earmarked-resource-unearmarked", templateModel,
				emailToList.toArray(new String[0]),
				emailCCList.toArray(new String[0]));
	}

	public Allocation populateToAllocation(Employee employee, Project project, ProjectLocation projectLocation,
			WorkGroup workgroup, Employee reportingManagerId, Employee allocationManagerId, Boolean billable,
			LocalDate releaseDate) {
		final Allocation toAllocation = new Allocation();
		toAllocation.setEmployee(employee);
		toAllocation.setProject(project);
		toAllocation.setProjectLocation(projectLocation);
		toAllocation.setIsBillable(billable);
		toAllocation.setAllocationManagerId(allocationManagerId);
		toAllocation.setReleaseDate(CommonQualifiedMapper.localDateToDate(releaseDate));
		toAllocation.setReportingManagerId(reportingManagerId);
		toAllocation.setIsActive(Boolean.TRUE);
		toAllocation.setWorkGroup(workgroup);
		toAllocation.setCreatedOn(TimeUtils.getCreatedOn());
		toAllocation.setCreatedBy(securityUtil.getLoggedInEmployee().getEmployeeId());
		toAllocation.setIsActive(Boolean.TRUE);
		toAllocation.setAllocationDetails(new ArrayList<>());
		toAllocation.setEarmarks(new HashSet<>());
		final AllocationStatus allocatedStatus = allocationStatusRepository.findByStatus("Allocated");
		toAllocation.setAllocationStatus(allocatedStatus);
		return toAllocation;
	}

}
