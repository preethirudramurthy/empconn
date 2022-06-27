package com.empconn.mapper;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.function.Function;
import java.util.stream.Collectors;

import org.apache.commons.lang3.StringUtils;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Named;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.CollectionUtils;

import com.empconn.constants.ApplicationConstants;
import com.empconn.dto.earmark.AvailableResourceDto;
import com.empconn.persistence.entities.Allocation;
import com.empconn.persistence.entities.AllocationDetail;
import com.empconn.persistence.entities.Earmark;
import com.empconn.persistence.entities.Employee;
import com.empconn.persistence.entities.EmployeeSkill;
import com.empconn.repositories.EarmarkRepository;
import com.empconn.repositories.EmployeeSkillRepository;

@Mapper(componentModel = "spring", uses = { CommonQualifiedMapper.class })
public abstract class AllocationAvailableResourceDtoMapper {

	@Autowired
	private EarmarkRepository earmarkRepository;

	@Autowired
	private EmployeeSkillRepository employeeSkillRepository;


	@Mapping(target = "allocationStatus", expression = "java(getAllocationStatus(source, availableResourceDto))")
	@Mapping(source = "source", target = "earmarkProjectNameList", qualifiedByName = "allocationToEarmarkProjectNameList")
	@Mapping(source = "employee", target = "secondarySkillSetList", qualifiedByName = "employeeToSecondarySkillSetList")
	@Mapping(source = "employee", target = "primarySkillSetList", qualifiedByName = "employeeToPrimarySkillSetList")
	@Mapping(source = "source", target = "benchAge", qualifiedByName = "allocationToBenchAge")
	@Mapping(source = "source.allocationDetails", target = "availablePercentage", qualifiedByName = "allocationDetailsToAllocatedPercentage")
	@Mapping(source = "source", target = "availableFrom", qualifiedByName = "allocationToAvailableFrom")
	@Mapping(source = "employee.location.name", target = "location")
	@Mapping(source = "source.project.name", target = "project")
	@Mapping(source = "source.project.account.name", target = "account")
	@Mapping(source = "employee.title.name", target = "title")
	@Mapping(source = "source.employee.fullName", target = "empName")
	@Mapping(source = "employee.empCode", target = "empCode")
	@Mapping(source = "employee.employeeId", target = "resourceId")
	public abstract AvailableResourceDto allocationToAvailableResourceDto(Allocation source);

	public abstract List<AvailableResourceDto> allocationsToAvailableResourceDtos(List<Allocation> source);

	@Named("allocationDetailsToAllocatedPercentage")
	public Integer allocationDetailsToAllocatedPercentage(List<AllocationDetail> allocDtails) {
		if (CollectionUtils.isEmpty(allocDtails))
			return null;
		return allocDtails.stream().filter(AllocationDetail::getIsActive).map(AllocationDetail::getAllocatedPercentage)
				.reduce(0, Integer::sum);
	}

	@Named("allocationToBenchAge")
	public Integer allocationToBenchAge(Allocation allocation) {
		if (allocation.getProject().getName().equals("Central Bench")
				&& !CollectionUtils.isEmpty(allocation.getAllocationDetails())) {
			final List<AllocationDetail> details = allocation.getAllocationDetails().stream()
					.filter(AllocationDetail::getIsActive).collect(Collectors.toList());
			if (!CollectionUtils.isEmpty(details)) {
				final Date minStartDate = Collections.min(details.stream().filter(AllocationDetail::getIsActive)
						.map(AllocationDetail::getStartDate).collect(Collectors.toList()));
				if (minStartDate != null) {
					final long diff = new Date().getTime() - minStartDate.getTime();
					return (int) (diff / (1000 * 60 * 60 * 24));
				}
			}
		}
		return null;
	}

	@Named("employeeToPrimarySkillSetList")
	public List<String> employeeToPrimarySkillSetList(Employee employee) {
		final List<EmployeeSkill> employeeSkills = employeeSkillRepository.findByEmployeeEmployeeId(employee.getEmployeeId());

		if (CollectionUtils.isEmpty(employeeSkills))
			return new ArrayList<>();
		final List<String> primarySkills = employeeSkills.stream().filter(EmployeeSkill::getIsActive)
				.map(s -> s.getSecondarySkill().getPrimarySkill().getName()).collect(Collectors.toList());
		return primarySkills;
	}

	@Named("employeeToSecondarySkillSetList")
	public List<String> employeeToSecondarySkillSetList(Employee employee) {
		final List<EmployeeSkill> employeeSkills = employeeSkillRepository.findByEmployeeEmployeeId(employee.getEmployeeId());
		final Function<? super EmployeeSkill, ? extends String> getSecondarySkillName = s -> s.getSecondarySkill()
				.getName();
		return convertSkillNamesToString(employeeSkills, getSecondarySkillName);
	}

	private List<String> convertSkillNamesToString(List<EmployeeSkill> employeeSkills,
			Function<? super EmployeeSkill, ? extends String> getNameFromSkill) {
		if (!CollectionUtils.isEmpty(employeeSkills)) {
			return employeeSkills.stream()
					.filter(es -> null != es && null != es.getSecondarySkill()
					&& !StringUtils.equalsIgnoreCase(es.getSecondarySkill().getName(),
							ApplicationConstants.DEFAULT_SECONDARY_SKILL))
					.map(getNameFromSkill).collect(Collectors.toList());
		}
		return new ArrayList<String>();
	}

	@Named("allocationToEarmarkProjectNameList")
	public List<String> allocationToEarmarkProjectNameList(Allocation allocation) {
		final List<Earmark> earmarkList = allocation.getEarmarks().stream().filter(Earmark::getIsActive).collect(Collectors.toList());


		if (earmarkList != null && !earmarkList.isEmpty()) {
			return earmarkList.stream().filter(e -> e.getProject() != null || e.getOpportunity() != null)
					.map(e -> e.getProject() != null ? e.getProject().getName() : e.getOpportunity().getName())
					.collect(Collectors.toList());
		}
		return new ArrayList<>();
	}

	public String getAllocationStatus(Allocation allocation, AvailableResourceDto dto) {
		if (!CollectionUtils.isEmpty(dto.getEarmarkProjectNameList()))
			return "EM";
		else if (allocation.getAllocationStatus() == null)
			return ("---");
		else if (allocation.getAllocationStatus().getStatus().equals("Allocated"))
			return ("--");
		else
			return allocation.getAllocationStatus().getStatus();
	}

}
