package com.empconn.service;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.sql.Timestamp;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.empconn.constants.ApplicationConstants;
import com.empconn.dto.allocation.ChangeReleaseDateDto;
import com.empconn.dto.allocation.ChangeReleaseDateListDto;
import com.empconn.dto.allocation.DeallocationResourceListRequestDto;
import com.empconn.dto.allocation.DeallocationResourceListResponseDto;
import com.empconn.dto.allocation.EditReleaseDateAllocationHour;
import com.empconn.dto.allocation.EditReleaseDateResourceListRequestDto;
import com.empconn.dto.allocation.EditReleaseDateResourceListResponseDto;
import com.empconn.dto.allocation.EditReleaseMonthDto;
import com.empconn.mapper.DeallocationMapper;
import com.empconn.mapper.EditReleaseDateMapper;
import com.empconn.persistence.entities.Allocation;
import com.empconn.persistence.entities.AllocationHour;
import com.empconn.persistence.entities.Earmark;
import com.empconn.persistence.entities.Employee;
import com.empconn.repositories.AllocationHourRepository;
import com.empconn.repositories.AllocationRepository;
import com.empconn.repositories.ProjectRepository;
import com.empconn.repositories.WorkGroupRepository;
import com.empconn.repositories.specification.DeallocationSpecification;
import com.empconn.repositories.specification.EditReleaseDateSpecification;
import com.empconn.security.SecurityUtil;
import com.empconn.utilities.DateUtils;
import com.empconn.utilities.TimeUtils;

@Service
public class EditReleaseDateService {

	private static final Logger logger = LoggerFactory.getLogger(EditReleaseDateService.class);

	@Autowired
	private DeallocationMapper deallocationMapper;

	@Autowired
	private SecurityUtil jwtEmployeeUtil;

	@Autowired
	private ProjectRepository projectRepository;

	@Autowired
	private WorkGroupRepository workgroupRepository;

	@Autowired
	private AllocationRepository allocationRepository;

	@Autowired
	private AllocationUtilityService allocationUtilityService;

	@Autowired
	private EditReleaseDateMapper editReleaseDateMapper;

	@Autowired
	private EarmarkService earmarkService;

	@Autowired
	private AllocationHoursService allocationHoursService;

	@Autowired
	private AllocationHourRepository allocationHourRepository;

	@Autowired
	private SyncToTimesheetService syncToTimesheetService;

	public List<DeallocationResourceListResponseDto> getAllocationResourceList(DeallocationResourceListRequestDto dto) {
		logger.debug("getAllocationResourceList");
		final Employee loggedInEmployee = jwtEmployeeUtil.getLoggedInEmployee();

		dto.setAllProjects(projectRepository.findAll());
		dto.setAllWorkgroups(workgroupRepository.findAll());

		final List<Allocation> allocations = allocationRepository
				.findAll(new DeallocationSpecification(dto, loggedInEmployee));
		return deallocationMapper.allocationToAvailableResourceDtoList(allocations);
	}

	public List<EditReleaseDateResourceListResponseDto> getReleaseDateAllocationResourceList(
			EditReleaseDateResourceListRequestDto dto) {
		logger.debug("getReleaseDateAllocationResourceList");
		final Employee loggedInEmployee = jwtEmployeeUtil.getLoggedInEmployee();

		dto.setAllProjects(projectRepository.findAll());
		dto.setAllWorkgroups(workgroupRepository.findAll());

		final List<Allocation> allocations = allocationRepository
				.findAll(new EditReleaseDateSpecification(dto, loggedInEmployee));
		return editReleaseDateMapper.allocationToAvailableResourceDtoList(allocations);
	}

	@Transactional
	public void changeReleaseDate(ChangeReleaseDateDto dto) {
		logger.debug("changeReleaseDate");
		final Employee loggedInEmployee = jwtEmployeeUtil.getLoggedInEmployee();
		for (final ChangeReleaseDateListDto changeReleaseDateDto : dto.getChangeReleaseDateList()) {
			Optional<Allocation> allocOpt = allocationRepository
					.findById(Long.valueOf(changeReleaseDateDto.getAllocationId()));
			final Allocation allocation = allocOpt.isPresent() ? allocOpt.get() : null;
			if (allocation != null) {
				final List<AllocationHour> allocationHours = allocation.getAllocationHours();
				if (allocation.getReleaseDate().equals(Date.from(
						changeReleaseDateDto.getReleaseDate().atStartOfDay(ZoneId.systemDefault()).toInstant()))) {
					modfiedBillingHour(changeReleaseDateDto, allocationHours);
				} else {
					final Optional<Allocation> existingAllocation = allocationRepository
							.findByEmployeeEmployeeIdAndProjectProjectIdAndProjectLocationProjectLocationIdAndWorkGroupWorkGroupIdAndIsBillableAndReleaseDateAndIsActive(
									allocation.getEmployee().getEmployeeId(), allocation.getProject().getProjectId(),
									allocation.getProjectLocation().getProjectLocationId(),
									allocation.getWorkGroup().getWorkGroupId(), allocation.getIsBillable(),
									Timestamp.valueOf(changeReleaseDateDto.getReleaseDate().atStartOfDay()), true);

					final boolean isNew = !existingAllocation.isPresent();

					if (!isNew) {
						// merge the allocations if there is already an allocation with that release
						// date
						// while merging, move the eligible earmarks whose start date is after the new
						// release date to the merged allocation
						allocation.setReleaseDate(
								DateUtils.convertToDateViaInstant(changeReleaseDateDto.getReleaseDate()));
						final Allocation currentAllocation = existingAllocation.get();
						allocationUtilityService.mergeAllocation(allocation, currentAllocation);
					} else {
						// In NON merging scenarios, check for the earmarks. Retain the ones with start
						// date after the new release date
						// and unearmark the ones whose start date is before the new release date.
						if (changeReleaseDateDto.getReleaseDate()
								.isAfter(DateUtils.convertToLocalDateViaMilisecond(allocation.getReleaseDate()))) {

							final List<Earmark> earmarksList = allocation.getEarmarks().stream()
									.filter(Earmark::getIsActive).collect(Collectors.toList());

							for (final Earmark earmark : earmarksList) {
								if (changeReleaseDateDto.getReleaseDate()
										.isAfter(DateUtils.convertToLocalDateViaMilisecond(earmark.getStartDate()))) {
									earmarkService.unearmarkBySystem(earmark,
											ApplicationConstants.UNEARMARK_EDIT_RELEASE_DATE_RESOURCE_COMMENT);
								}
							}
						}
						allocation.setReleaseDate(
								DateUtils.convertToDateViaInstant(changeReleaseDateDto.getReleaseDate()));
					}

					final List<String> benchProjects = Arrays.asList("Central Bench", "NDBench");
					if (!benchProjects.contains(allocation.getProject().getName())) {
						final Map<String, AllocationHour> monthlyBillableHoursOfToAllocation = allocation
								.getAllocationHours().stream().collect(Collectors.toMap(
										a -> String.join("-", String.valueOf(a.getYear()), a.getMonth()), a -> a));

						final Map<String, AllocationHour> monthlyBillableHoursOfToAllocCopy = allocationHoursService
								.getBillingHours(allocation, allocation.getAllocationDetails());

						monthlyBillableHoursOfToAllocCopy.entrySet().stream().forEach(e -> {
							final Integer year = Integer.valueOf(e.getKey().split("-")[0]);
							final String monthName = e.getKey().split("-")[1];
							final BigDecimal maxHours = e.getValue().getBillingHours().divide(new BigDecimal(8))
									.multiply(new BigDecimal(24));
							boolean addToAllocation = false;
							if (monthlyBillableHoursOfToAllocation.get(e.getKey()) == null) {
								monthlyBillableHoursOfToAllocation.computeIfAbsent(e.getKey(),
										k -> new AllocationHour(new BigDecimal(0), loggedInEmployee.getEmployeeId(),
												monthName, year));
								addToAllocation = true;
							} else {
								monthlyBillableHoursOfToAllocation.get(e.getKey())
										.setModifiedBy(loggedInEmployee.getEmployeeId());
								monthlyBillableHoursOfToAllocation.get(e.getKey()).setModifiedOn(TimeUtils.getToday());
							}
							monthlyBillableHoursOfToAllocation.get(e.getKey())
									.setBillingHours(e.getValue().getBillingHours());
							monthlyBillableHoursOfToAllocation.get(e.getKey()).setBillingHoursRounded(
									e.getValue().getBillingHours().setScale(0, RoundingMode.UP).intValue());
							monthlyBillableHoursOfToAllocation.get(e.getKey())
									.setMaxHours(maxHours.setScale(0, RoundingMode.UP).intValue());
							monthlyBillableHoursOfToAllocation.get(e.getKey()).setAllocation(allocation);

							if (addToAllocation) {
								allocation.getAllocationHours().add(monthlyBillableHoursOfToAllocation.get(e.getKey()));
							}
						});

						monthlyBillableHoursOfToAllocation.entrySet().stream().forEach(e -> {
							if (!monthlyBillableHoursOfToAllocCopy.containsKey(e.getKey())) {
								allocation.getAllocationHours().remove(e.getValue());
							}
						});

					}

					allocationRepository.save(allocation);

					Optional<Allocation> modAlloc = allocationRepository
							.findById(Long.valueOf(changeReleaseDateDto.getAllocationId()));
					final Allocation modifiedAllocation = (modAlloc.isPresent())?modAlloc.get():null;
					if (modifiedAllocation != null) {
						final List<AllocationHour> modifiedallocationHours2 = modifiedAllocation.getAllocationHours();
						modfiedBillingHour(changeReleaseDateDto, modifiedallocationHours2);	
					}
					
				}
				syncToTimesheetService.syncProjectAllocationHours(allocation);
			}
		}
	}



	private void modfiedBillingHour(final ChangeReleaseDateListDto changeReleaseDateDto,
			final List<AllocationHour> allocationHours2) {
		final Map<Integer, List<EditReleaseMonthDto>> map = new HashMap<>();

		final List<EditReleaseDateAllocationHour> allocationHours = changeReleaseDateDto.getAllocationHours();
		for (final EditReleaseDateAllocationHour editReleaseDateAllocationHour : allocationHours) {
			final List<EditReleaseMonthDto> monthList = editReleaseDateAllocationHour.getMonthList();
			final List<EditReleaseMonthDto> months = new ArrayList<>();
			for (final EditReleaseMonthDto changeMonth : monthList) {

				if (changeMonth.getChanged() != null && changeMonth.getChanged()) {
					final EditReleaseMonthDto editReleaseMonthDto = new EditReleaseMonthDto();
					editReleaseMonthDto.setName(changeMonth.getName());
					editReleaseMonthDto.setValue(changeMonth.getValue());
					months.add(editReleaseMonthDto);
				}
			}
			map.put(editReleaseDateAllocationHour.getYear(), months);
		}
		map.entrySet().stream().forEach(e -> {
			for (final AllocationHour allocationHour : allocationHours2) {
				if (allocationHour.getYear().compareTo(e.getKey()) == 0) {
					final List<EditReleaseMonthDto> months = e.getValue();
					for (final EditReleaseMonthDto month : months) {
						if (month.getName().equals(allocationHour.getMonth())) {
							allocationHour.setBillingHoursRounded(month.getValue());
						}
					}
				}
			}
		});
		allocationHourRepository.saveAll(allocationHours2);
	}

}
