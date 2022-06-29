package com.empconn.mapper;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Named;

import com.empconn.dto.allocation.EditReleaseDateAllocationHour;
import com.empconn.dto.allocation.EditReleaseDateResourceListResponseDto;
import com.empconn.dto.allocation.EditReleaseMonthDto;
import com.empconn.persistence.entities.Allocation;
import com.empconn.persistence.entities.AllocationHour;
import com.empconn.utilities.DateUtils;

@Mapper(componentModel = "spring", uses = {CommonQualifiedMapper.class})
public abstract class EditReleaseDateMapper {


	@Mapping(source = "allocationId", target = "allocationId")
	@Mapping(source = "employee.empCode", target = "empCode")
	@Mapping(source = "employee", target = "empName", qualifiedByName = "employeeToFullName")
	@Mapping(source = "employee.title.name", target = "title")
	@Mapping(source = "employee.location.name", target = "projectLocation")
	@Mapping(source = "project.name", target = "projectName")
	@Mapping(source = "allocation", target = "percentage", qualifiedByName = "mergedAllocatedPercentage")
	@Mapping(source = "project.endDate", target = "projectEndDate", qualifiedByName = "DateToLocalDate")
	@Mapping(source = "allocation", target = "startDate", qualifiedByName = "allocationStartDate")
	@Mapping(source = "releaseDate", target = "releaseDate", qualifiedByName = "DateToLocalDate")
	@Mapping(source = "isBillable", target = "billable")
	@Mapping(source = "allocationHours", target = "allocationHourList", qualifiedByName = "allocationtoAllocationHrsList")
	public abstract EditReleaseDateResourceListResponseDto allocationToDeallocationResoucrListDto(Allocation allocation);

	public abstract List<EditReleaseDateResourceListResponseDto> allocationToAvailableResourceDtoList(
			List<Allocation> allocations);

	@Named("allocationtoAllocationHrsList")
	List<EditReleaseDateAllocationHour> allocationtoAllocationHrsList(List<AllocationHour> allocationHrs) {
		final List<EditReleaseDateAllocationHour> allocationHrsDto = new ArrayList<>();
		final int year = Calendar.getInstance().get(Calendar.YEAR);
		final Map<Integer,List<EditReleaseMonthDto>> map = new HashMap<>();
		allocationHrs.stream().forEach(ah->{
			final EditReleaseMonthDto editReleaseMonthDto = new EditReleaseMonthDto();
			editReleaseMonthDto.setName(ah.getMonth());
			editReleaseMonthDto.setValue(ah.getBillingHoursRounded());
			editReleaseMonthDto.setMax(ah.getMaxHours());
			if(ah.getYear() >= year) {
				if(map.containsKey(ah.getYear())){
					final List<EditReleaseMonthDto> monthList = map.get(ah.getYear());
					monthList.add(editReleaseMonthDto);
					map.put(ah.getYear(),monthList);
				}else {
					final List<EditReleaseMonthDto> monthList = new ArrayList<>();
					monthList.add(editReleaseMonthDto);
					map.put(ah.getYear(), monthList);
				}
			}
		});
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

}
