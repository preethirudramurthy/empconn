package com.empconn.service;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Optional;
import java.util.Set;
import java.util.function.Predicate;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.google.common.base.Predicates;
import com.google.common.collect.Iterables;
import com.empconn.dto.allocation.AllocationForSwitchOverDto;
import com.empconn.dto.allocation.AllocationRequestDto;
import com.empconn.dto.allocation.AllocationSummaryDto;
import com.empconn.dto.allocation.ExistingAllocationDto;
import com.empconn.dto.allocation.SwitchOverDtlsDto;
import com.empconn.dto.allocation.SwitchOverRequestDto;
import com.empconn.dto.allocation.SwitchoverSearchDto;
import com.empconn.dto.allocation.ValidateSwitchOverDto;
import com.empconn.dto.allocation.ValidateSwitchOverResponseDto;
import com.empconn.exception.PreConditionFailedException;
import com.empconn.mapper.AllocationMapper;
import com.empconn.mapper.CommonQualifiedMapper;
import com.empconn.persistence.entities.Allocation;
import com.empconn.persistence.entities.AllocationDetail;
import com.empconn.persistence.entities.Project;
import com.empconn.persistence.entities.SalesforceIdentifier;
import com.empconn.persistence.entities.SecondarySkill;
import com.empconn.repositories.AllocationRepository;
import com.empconn.repositories.ProjectRepository;
import com.empconn.repositories.SalesforceIdentifierRepository;
import com.empconn.repositories.SecondarySkillRepository;
import com.empconn.repositories.specification.AllocationSpecification;

@Service
public class AllocateSwitchOverService {

	@Autowired
	private AllocationMapper allocationMapper;

	@Autowired
	private SecondarySkillRepository secondarySkillRepository;

	@Autowired
	private AllocationRepository allocationRepository;

	@Autowired
	private SwithOverEmailService swithOverEmailService;

	@Autowired
	private AllocationService allocationService;

	@Autowired
	private AllocationUtilityService allocationUtilityService;

	@Autowired
	private SalesforceIdentifierRepository salesforceIdentifierRepository;

	@Autowired
	private ProjectRepository projectRepository;

	@Autowired
	private SalesforceIdentifierService identifierService;

	@Transactional(readOnly = false)
	public void switchOver(List<SwitchOverRequestDto> requestDtos) {

		final Map<Long, Set<String>> validateAllocationMap = requestDtos.stream()
				.collect(Collectors.groupingBy(SwitchOverRequestDto::getResourceId, Collectors.mapping(
						a -> String.join(",", a.getProjectLocationId(), a.getWorkgroup(), a.getReportingManagerId()),
						Collectors.toSet())));

		for (final Entry<Long, Set<String>> employee : validateAllocationMap.entrySet()) {

			if (validateAllocationMap.get(employee.getKey()).size() > 1) {
				throw new PreConditionFailedException("ResourceWrongAllocation");
			}
		}
		for (final SwitchOverRequestDto request : requestDtos) {
			Optional<Allocation> a = allocationRepository.findById(request.getAllocationId());
			final Allocation currentAllocation = a.isPresent()?a.get():null;
			final Integer availablePercentage = allocationUtilityService
					.getMergedAllocatedPercentage(currentAllocation);
			final boolean isPartial = (request.getPercentage().intValue() < availablePercentage.intValue());

			final Allocation toAllocation = allocationService.allocate(new AllocationRequestDto(request));

			if (isPartial) {
				swithOverEmailService.mailForPartialSwitchOver(currentAllocation, request.getStartDate(), toAllocation);
				swithOverEmailService.mailForSwitchOverTo(toAllocation, request.getStartDate());
			} else {
				swithOverEmailService.mailForSwitchOverTo(toAllocation, request.getStartDate());
				swithOverEmailService.mailForSwitchOverFrom(currentAllocation, request.getStartDate());
			}

		}

	}

	public List<AllocationForSwitchOverDto> getAllocationListForSwitchOver(SwitchoverSearchDto allocationSearchDto) {

		if (allocationSearchDto.getPrimarySkillId() != null && !allocationSearchDto.getPrimarySkillId().isEmpty()) {
			final List<SecondarySkill> secondarySkills = secondarySkillRepository
					.findByPrimarySkillPrimarySkillIdAndIsActiveTrue(Integer.valueOf(allocationSearchDto.getPrimarySkillId()));
			allocationSearchDto.getSecondarySkillIdList().addAll(
					secondarySkills.stream().map(s -> s.getSecondarySkillId().toString()).collect(Collectors.toList()));
		}

		final List<Allocation> allocations = allocationRepository
				.findAll(new AllocationSpecification(allocationSearchDto));

		return allocationMapper.allocationToAvailableResourceDtoList(allocations);
	}

	public SwitchOverDtlsDto getSwitchOverDetailsForAllocation(Long allocationId, Long projectId) {
		final Optional<Allocation> selectedAllocation = allocationRepository.findById(allocationId);
		final Optional<Project> toProject = projectRepository.findById(projectId);
		final List<AllocationSummaryDto> allocationSummary = selectedAllocation.isPresent()? allocationRepository
				.getAllocationSummary(selectedAllocation.get().getEmployee().getEmployeeId()):null;
		final Long primaryAllocationId = selectedAllocation.isPresent()?selectedAllocation.get().getEmployee().getPrimaryAllocation()
				.getAllocationId():null;

		final Set<Allocation> empAllocations = selectedAllocation.isPresent()?allocationRepository
				.findByEmployeeEmployeeIdAndIsActive(selectedAllocation.get().getEmployee().getEmployeeId(), true):null;

		if (empAllocations != null && !empAllocations.isEmpty()) {
			final Map<Long, List<Allocation>> employeeAllocationMap = empAllocations.stream()
					.collect(Collectors.groupingBy(a -> a.getProject().getProjectId()));
			if (allocationSummary != null && !allocationSummary.isEmpty()) {
				allocationSummary.forEach(a -> {
					a.setReportingMangerName(a.getReportingManager().getFullName());
					a.setIsPrimary(employeeAllocationMap.get(a.getProjectId()).stream()
							.anyMatch(i -> i.getAllocationId().equals(primaryAllocationId)));
				});	
			}
		}

		ExistingAllocationDto exitingAllocationDto = null;
		final Allocation existingAllocation = selectedAllocation.isPresent()?allocationRepository
				.findFirstByEmployeeEmployeeIdAndProjectProjectIdAndIsActive(
						selectedAllocation.get().getEmployee().getEmployeeId(), projectId, true):null;
		if (existingAllocation != null) {
			final String reportingManagerName = existingAllocation.getReportingManagerId().getFullName();

			boolean isPrimary = false;
			if (existingAllocation.getAllocationId()
					.equals(selectedAllocation.get().getEmployee().getPrimaryAllocation().getAllocationId())) {
				isPrimary = true;
			}
			exitingAllocationDto = new ExistingAllocationDto(
					Long.toString(existingAllocation.getProjectLocation().getProjectLocationId()),
					existingAllocation.getProjectLocation().getLocation().getName(),
					existingAllocation.getWorkGroup().getName(),
					Long.toString(existingAllocation.getReportingManagerId().getEmployeeId()), reportingManagerName,
					isPrimary, CommonQualifiedMapper.dateToLocalDate(existingAllocation.getReleaseDate()));
		}

		return (selectedAllocation.isPresent() && toProject.isPresent())?allocationMapper.allocationToAllocationDetailsDto(selectedAllocation.get(), toProject.get(),
				allocationSummary, exitingAllocationDto):null;
	}

	public ValidateSwitchOverResponseDto isValidSwitchOverAllocation(ValidateSwitchOverDto dto) {
		final ValidateSwitchOverResponseDto responseDto = new ValidateSwitchOverResponseDto();
		final Optional<Allocation> allocation = allocationRepository.findById(dto.getAllocationId());
		final Optional<Project> project = projectRepository.findById(Long.valueOf(dto.getProjectId()));
		final List<SalesforceIdentifier> salesforceIdentifierIdsList = salesforceIdentifierRepository
				.findByProjectProjectIdAndIsActiveIsTrue(Long.valueOf(dto.getProjectId()));

		final List<Allocation> a = allocationRepository.findByEmployeeEmployeeIdAndProjectProjectIdAndIsActive(
				Long.valueOf(dto.getResourceId()), Long.valueOf(dto.getProjectId()), true);
		if (!a.isEmpty()) {
			for (final Allocation all : a)
					responseDto.setInvalidLocationWorkgroup(!(all.getProjectLocation().getProjectLocationId().equals((Long.valueOf(dto.getProjectLocationId())))
							&& all.getWorkGroup().getName().equals(dto.getWorkgroup()) && all.getReportingManagerId()
							.getEmployeeId().equals(Long.valueOf(dto.getReportingManagerId()))));
		}

		Iterables.removeIf(dto.getExtraSalesforceIdList(), Predicates.isNull());
		if (!dto.getExtraSalesforceIdList().isEmpty()) {
			for (final SalesforceIdentifier id : salesforceIdentifierIdsList) {
				dto.getExtraSalesforceIdList().remove(id.getValue());
			}

			for (final String id : dto.getExtraSalesforceIdList()) {
				
				if (project.isPresent() && !identifierService.isValidSalesforceIdForProject(id, project.get().getProjectId())) {
					responseDto.setInvalidSalesforceIdList(true);
					break;
				} else {
					responseDto.setInvalidSalesforceIdList(false);
				}
			}
		}

		if (allocation.isPresent())
			responseDto.setInvalidAllocationPercentage((dto.getPercentage() > allocation.get().getAllocationDetails().stream()
						.map(AllocationDetail::getAllocatedPercentage).reduce(0, Integer::sum)) );

		final Date releaseDate = Date.from(dto.getReleaseDate().atZone(ZoneId.systemDefault()).toInstant());

		if (project.isPresent())
			responseDto.setInvalidReleaseDateAfterProjectDate((project.get().getEndDate().compareTo(releaseDate) < 0));

		final Predicate<LocalDate> isWeekend = date -> date.getDayOfWeek() == DayOfWeek.SATURDAY
				|| date.getDayOfWeek() == DayOfWeek.SUNDAY;

		responseDto.setInvalidReleaseDateOnWeekend(isWeekend.test(dto.getReleaseDate().toLocalDate()));

		return responseDto;
	}

}
