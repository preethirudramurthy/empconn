package com.empconn.mapper;

import java.sql.Timestamp;
import java.util.Arrays;
import java.util.Comparator;
import java.util.Date;
import java.util.List;
import java.util.Set;
import java.util.function.Function;
import java.util.function.Predicate;
import java.util.stream.Collectors;

import org.apache.commons.lang3.StringUtils;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Named;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.CollectionUtils;

import com.empconn.dto.BenchReportRowDto;
import com.empconn.persistence.entities.Allocation;
import com.empconn.persistence.entities.AllocationDetail;
import com.empconn.persistence.entities.Earmark;

@Mapper(componentModel = "spring", uses = {CommonQualifiedMapper.class})
public abstract class AllocationToBenchReportRowDtoMapper {

	@Autowired
	private CommonQualifiedMapper commonQualifiedMapper;

	@Mapping(source = "allocationId", target = "benchAllocationId")
	@Mapping(source = "employee.empCode", target = "empCode")
	@Mapping(source = "employee.fullName", target = "empFullName")
	@Mapping(source = "employee.title.name", target = "title")
	@Mapping(source = "employee.location.name", target = "location")
	@Mapping(source = "project.name", target = "projectName")
	@Mapping(source = "reportingManagerId.fullName", target = "reportingManager")
	@Mapping(source = "employee", target = "primarySkillSet", qualifiedByName = "getPrimarySkillNames")
	@Mapping(source = "employee", target = "secondSkillSet", qualifiedByName = "getSecondarySkillNames")
	@Mapping(source = "allocation", target = "percentage", qualifiedByName = "mergedAllocatedPercentage")
	@Mapping(source = "allocation", target = "startDate", qualifiedByName = "getLatestStartDateFormatted")
	@Mapping(source = "allocation", target = "benchAge", qualifiedByName = "getBenchAge")
	@Mapping(target = "allocationStatus", expression = "java(getAllocationStatus(allocation))")
	@Mapping(source = "earmarks", target = "earmarkProjectNames", qualifiedByName = "getEarmarkProjectNames")
	@Mapping(source = "employee.employeeAllocations", target = "lastProjectName", qualifiedByName = "getLastProjectName")
	@Mapping(source = "employee.dateOfJoining", target = "dateOfJoining", qualifiedByName = "Date_ddmmmyyyy_To_String")
	public abstract BenchReportRowDto convert(Allocation allocation);

	public abstract List<BenchReportRowDto> convert(List<Allocation> allocations);
	
	@Named("getBenchAge")
	public Integer getBenchAge(Allocation allocation) {

		final Date allocationStartDate = commonQualifiedMapper.getLatestStartDate(allocation);

		final long diff = new Date().getTime() - allocationStartDate.getTime();
		return (int) (diff / (1000 * 60 * 60 * 24));

	}

	@Named("getLastProjectName")
	public static String getLastProjectName(Set<Allocation> employeeAllocations) {

		final Comparator<AllocationDetail> allocationDetailDeallocatedOnComparator = Comparator
				.comparing(AllocationDetail::getModifiedOn, Timestamp::compareTo);
		final List<String> benchProjects = Arrays.asList("Central Bench", "NDBench");
		final Predicate<? super AllocationDetail> isInactive = a -> !a.getIsActive();
		final Predicate<? super Allocation> isNonBench = a -> !benchProjects.contains(a.getProject().getName());
		AllocationDetail allocationDetail = null;

		allocationDetail = filterMapSortAndGetAllocationDetail(employeeAllocations,
				allocationDetailDeallocatedOnComparator, isInactive, isNonBench);

		if (null == allocationDetail)
			return "";
		return allocationDetail.getAllocation().getProject().getName();

	}

	private static AllocationDetail filterMapSortAndGetAllocationDetail(Set<Allocation> allocations,
			final Comparator<AllocationDetail> comparator, Predicate<? super AllocationDetail> filterPredicate, Predicate<? super Allocation> filterNonBenchPredicate) {
		final List<AllocationDetail> allocationDetailsOfInactiveAllocation = allocations.stream()
				.filter(filterNonBenchPredicate).map(Allocation::getAllocationDetails).flatMap(List::stream)
				.filter(filterPredicate)
				.collect(Collectors.toList());

		return sortAndGetLatestAllocationDetail(allocationDetailsOfInactiveAllocation, comparator);
	}

	//	Note: This method doesn't check if the allocation detail is currently active or not and the consumer methods should filter active/inactive allocation details before calling this method
	private static AllocationDetail sortAndGetLatestAllocationDetail(final List<AllocationDetail> allocationDetails,
			final Comparator<AllocationDetail> comparator) {
		AllocationDetail allocationDetail;
		allocationDetails.sort(comparator);

		if (allocationDetails.isEmpty())
			allocationDetail = null;
		else
			allocationDetail = allocationDetails.get(allocationDetails.size() - 1);
		return allocationDetail;
	}

	@Named("getEarmarkProjectNames")
	public static String getEarmarkProjectNames(Set<Earmark> earmarks) {
		final Predicate<? super Earmark> projectAndProjectNameExists = e -> null != e.getProject()
				&& !StringUtils.isEmpty(e.getProject().getName());
		final Function<? super Earmark, ? extends String> getProjectName = e -> e.getProject().getName();

		final Predicate<? super Earmark> opportunityAndOppurtunityNameExists = e -> null != e.getOpportunity()
				&& !StringUtils.isEmpty(e.getOpportunity().getName());
		final Function<? super Earmark, ? extends String> getOpportunityName = e -> e.getOpportunity().getName();

		final Set<String> projectNames = getProjectOrOpportunityNames(earmarks, projectAndProjectNameExists,
				getProjectName);
		final Set<String> opportunityNames = getProjectOrOpportunityNames(earmarks, opportunityAndOppurtunityNameExists,
				getOpportunityName);

		projectNames.addAll(opportunityNames);
		return String.join(", ", projectNames);
	}

	private static Set<String> getProjectOrOpportunityNames(Set<Earmark> earmarks,
			Predicate<? super Earmark> nameExists, Function<? super Earmark, ? extends String> getName) {
		return earmarks.stream().filter(nameExists).map(getName).collect(Collectors.toSet());
	}



	public String getAllocationStatus(Allocation allocation) {
		if (!CollectionUtils.isEmpty(allocation.getEarmarks()))
			return "Earmarked";
		else if (allocation.getAllocationStatus() == null)
			return ("---");
		else if(allocation.getAllocationStatus().getStatus().equals("PB"))
			return "Pure Bench";
		else if(allocation.getAllocationStatus().getStatus().equals("LL"))
			return "Long Leave";
		else if(allocation.getAllocationStatus().getStatus().equals("SBL"))
			return "Sabbatical Leave";
		else
			return "Allocated";
	}

}
