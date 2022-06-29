package com.empconn.service;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.Instant;
import java.time.LocalDate;
import java.time.ZoneId;
import java.time.format.TextStyle;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collection;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.empconn.dto.allocation.AllocationHourDto;
import com.empconn.dto.allocation.CalculateAllocationHoursDto;
import com.empconn.dto.allocation.CalculateAllocationHoursResponseDto;
import com.empconn.dto.allocation.EditReleaseDateAllocationHour;
import com.empconn.dto.allocation.EditReleaseMonthDto;
import com.empconn.persistence.entities.Allocation;
import com.empconn.persistence.entities.AllocationDetail;
import com.empconn.persistence.entities.AllocationHour;
import com.empconn.utilities.DateUtils;

@Service
public class AllocationHoursService {

	@Autowired
	private CommonBenchService benchService;

	public void updateToAllocationBillableHrs(final Allocation toAllocation) {
		final Map<String, AllocationHour> hrsOfToAlloc = toAllocation.getAllocationHours()
				.stream().collect(Collectors.toMap(a -> String.join("-", String.valueOf(a.getYear()), a.getMonth()), a -> a));

		final Map<String, AllocationHour> monthlyBillableHoursOfToAllocCopy = getBillingHours(toAllocation,
				toAllocation.getAllocationDetails());

		updateAllocHours(toAllocation, hrsOfToAlloc, monthlyBillableHoursOfToAllocCopy);
	}

	public void updateCurrLocationBillableHrs(final Allocation currentAllocation) {
		if (!benchService.getBenchProjectNames().contains(currentAllocation.getProject().getName())) {
			final Map<String, AllocationHour> currentAllocHrs = currentAllocation
					.getAllocationHours().stream().collect(
							Collectors.toMap(a -> String.join("-", String.valueOf(a.getYear()), a.getMonth()), a -> a));

			final Map<String, AllocationHour> copyOfCurrAllocHrs = getBillingHours(currentAllocation, currentAllocation.getAllocationDetails());
			if (copyOfCurrAllocHrs == null || copyOfCurrAllocHrs.isEmpty()) {
				currentAllocation.getAllocationHours().clear();
			} else {
				updateAllocHours(currentAllocation, currentAllocHrs, copyOfCurrAllocHrs);
			}
		}
	}

	public void updateAllocHours(final Allocation currentAllocation, final Map<String, AllocationHour> currentAllocHrs,
			final Map<String, AllocationHour> copyOfCurrAllocHrs) {
		copyOfCurrAllocHrs.entrySet().stream().forEach(e -> {
			final Integer year = Integer.valueOf(e.getKey().split("-")[0]);
			final String monthName = e.getKey().split("-")[1];
			final BigDecimal maxHours = e.getValue().getBillingHours().multiply(new BigDecimal(3));
			currentAllocHrs.computeIfAbsent(e.getKey(), k -> new AllocationHour(new BigDecimal(0), 1L, monthName, year));
			currentAllocHrs.get(e.getKey()).setBillingHours(e.getValue().getBillingHours());
			currentAllocHrs.get(e.getKey()).setBillingHoursRounded(e.getValue().getBillingHours().setScale(0, RoundingMode.UP).intValue());
			currentAllocHrs.get(e.getKey()).setMaxHours(maxHours.setScale(0, RoundingMode.UP).intValue());
			currentAllocHrs.get(e.getKey()).setAllocation(currentAllocation);
			currentAllocation.getAllocationHours().add(currentAllocHrs.get(e.getKey()));
		});
	}

	public Map<String, AllocationHour> getBillingHours(final Allocation allocation,
			final List<AllocationDetail> allocationDetails) {

		final Map<String, AllocationHour> byMonthBillingHoursCopy = new HashMap<>();
		for (final AllocationDetail ad : allocationDetails) {
			// For inactive ones, duration is between start date to deallocated on
			// For active ones, duration is start date to release date from allocation table
			List<AllocationHourDto> allocationHours = null;
			if (ad.getIsActive()) {
				allocationHours = getBillingHours(ad.getStartDate(), allocation.getReleaseDate(), true, ad.getAllocatedPercentage());

			} else {
				allocationHours = getBillingHours(ad.getStartDate(), ad.getDeallocatedOn(), false, ad.getAllocatedPercentage());
			}
			allocationHours.stream().forEach(a -> {
				final LocalDate startDate = Instant.ofEpochMilli(a.getStartDate().getTime())
						.atZone(ZoneId.systemDefault()).toLocalDate();

				final String key = String.join("-", String.valueOf(startDate.getYear()),
						startDate.getMonth().getDisplayName(TextStyle.SHORT, Locale.getDefault()));
				byMonthBillingHoursCopy.computeIfAbsent(key,
						k -> new AllocationHour(new BigDecimal(0), 1L,
								startDate.getMonth().getDisplayName(TextStyle.SHORT, Locale.getDefault()),
								startDate.getYear()))
				.setBillingHours(byMonthBillingHoursCopy.get(key).getBillingHours().add(a.getBillingHours()));

			});

		}

		return byMonthBillingHoursCopy;
	}

	private List<AllocationHourDto> getBillingHours(Date startDate, Date endDate, boolean inclusiveOfEndDate, Integer percentage) {

		Map<String, List<LocalDate>> byMonth = null;
		if (inclusiveOfEndDate) {
			byMonth = DateUtils
					.businessDaysBetweenIncludingEndDate(startDate, endDate).stream()
					.collect(Collectors.groupingBy(d -> String.join("-", String.valueOf(d.getYear()),
							d.getMonth().getDisplayName(TextStyle.SHORT, Locale.getDefault()))));
		} else {
			byMonth = DateUtils
					.businessDaysBetweenExcludingEndDate(startDate, endDate).stream()
					.collect(Collectors.groupingBy(d -> String.join("-", String.valueOf(d.getYear()),
							d.getMonth().getDisplayName(TextStyle.SHORT, Locale.getDefault()))));
		}
		return getMonthlyHours(percentage, byMonth);

	}

	public List<AllocationHourDto> getMonthlyHours(final Integer percentage,
			final Map<String, List<LocalDate>> byMonth) {
		return byMonth.entrySet().stream().map(e -> {
			final BigDecimal billingHours = BigDecimal
					.valueOf((e.getValue().size() * 8) * (((double) percentage) / 100));
			final BigDecimal maxHours = billingHours.setScale(2, RoundingMode.HALF_UP).multiply(new BigDecimal(3));
			return new AllocationHourDto(billingHours.setScale(2, RoundingMode.HALF_UP),
					billingHours.setScale(0, RoundingMode.UP).intValue(),
					Date.from(Collections.max(e.getValue()).atStartOfDay(ZoneId.systemDefault()).toInstant()),
					e.getValue().size(),
					Date.from(Collections.min(e.getValue()).atStartOfDay(ZoneId.systemDefault()).toInstant()),
					Collections.min(e.getValue()).getMonth().getDisplayName(TextStyle.SHORT, Locale.getDefault()),
					Collections.min(e.getValue()).getYear(), maxHours.setScale(0, RoundingMode.UP).intValue());
		}).collect(Collectors.toList());
	}

	public List<EditReleaseDateAllocationHour> getAllocationHrsList(
			Collection<AllocationHourDto> allocationHrs) {
		final List<EditReleaseDateAllocationHour> allocationHrsDto = new ArrayList<>();
		final int year = Calendar.getInstance().get(Calendar.YEAR);
		final Map<Integer, List<EditReleaseMonthDto>> map = new HashMap<>();
		for (final AllocationHourDto ah : allocationHrs) {
			final EditReleaseMonthDto editReleaseMonthDto = new EditReleaseMonthDto();
			editReleaseMonthDto.setName(ah.getMonth());
			editReleaseMonthDto.setValue(ah.getBillingHoursRounded());
			editReleaseMonthDto.setMax(ah.getMaxHours());
			if(ah.getYear() >= year) {
				if (map.containsKey(ah.getYear())) {
					final List<EditReleaseMonthDto> monthList = map.get(ah.getYear());
					monthList.add(editReleaseMonthDto);
					map.put(ah.getYear(), monthList);
				} else {
					final List<EditReleaseMonthDto> monthList = new ArrayList<>();
					monthList.add(editReleaseMonthDto);
					map.put(ah.getYear(), monthList);
				}
			}
		}
		map.entrySet().stream().forEach(e -> {
			final EditReleaseDateAllocationHour editReleaseDateAllocationHour = new EditReleaseDateAllocationHour();
			editReleaseDateAllocationHour.setYear(e.getKey());
			final List<EditReleaseMonthDto> list = e.getValue();
			Collections.sort(list, (o1, o2) -> {
				final Integer x1 = DateUtils.getMonth(o1.getName());
				final Integer x2 = DateUtils.getMonth(o2.getName());
				return x1.compareTo(x2);
			});
			editReleaseDateAllocationHour.setMonthList(list);
			allocationHrsDto.add(editReleaseDateAllocationHour);
		});

		return allocationHrsDto;
	}

	public CalculateAllocationHoursResponseDto getCalculatedAllocationHours(CalculateAllocationHoursDto request) {
		final CalculateAllocationHoursResponseDto response = new CalculateAllocationHoursResponseDto();
		response.setAllocationHourList(getCalculatedHours(request.getFromDate(), request.getToDate(), request.getPercentage()));
		return response;

	}

	public List<EditReleaseDateAllocationHour> getCalculatedHours(Date fromDate, Date toDate, Integer percentage) {
		final Map<String, List<LocalDate>> byMonth = DateUtils
				.businessDaysBetweenIncludingEndDate(fromDate, toDate).stream()
				.collect(Collectors.groupingBy(d -> String.join("-", String.valueOf(d.getYear()),
						d.getMonth().getDisplayName(TextStyle.SHORT, Locale.getDefault()))));
		return getAllocationHrsList(getMonthlyHours(percentage, byMonth));
	}

}
