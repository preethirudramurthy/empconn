package com.empconn.mapper;

import java.util.List;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Named;

import com.empconn.dto.allocation.AllocationForEarmarkDto;
import com.empconn.persistence.entities.Earmark;

@Mapper(componentModel = "spring", uses = { CommonQualifiedMapper.class })
public abstract class EarmarkToAllocationForEarmarkDtoMapper {

	public abstract List<AllocationForEarmarkDto> earmarkToAllocationForEarmarkDtoList(List<Earmark> earMarks);

	@Mapping(target = "secondarySkillList", source = "allocation.employee", qualifiedByName = "employeeToSecondarySkillList")
	@Mapping(source = "employee2", target = "earmarkManager", qualifiedByName = "employeeToFullName")
	@Mapping(target = "devGdm", source = "source", qualifiedByName = "earmarkToDevGdm")
	@Mapping(target = "qaGdm", source = "source", qualifiedByName = "earmarkToQaGdm")
	@Mapping(target = "isOpp", expression = "java(source.getOpportunity() != null)")
	@Mapping(source = "allocation.employee", target = "empName", qualifiedByName = "employeeToFullName")
	@Mapping(source = "allocation.employee.empCode", target = "empCode")
	@Mapping(source = "allocation.employee.title.name", target = "title")
	@Mapping(source = "percentage", target = "earmarkPercentage")
	@Mapping(source = "allocation", target = "availablePercentage", qualifiedByName = "mergedAllocatedPercentage")
	@Mapping(source = "earmarkId", target = "earmarkId")
	@Mapping(source = "billable", target = "billable")
	@Mapping(source = "startDate", target = "startDate", qualifiedByName = "DateToLocalDate")
	@Mapping(source = "endDate", target = "endDate", qualifiedByName = "DateToLocalDate")
	@Mapping(source = "allocation", target = "availableFrom", qualifiedByName = "allocationToAvailableFrom")
	@Mapping(target = "earmarkProjectName", expression = "java(getEarmarkName(source))")
	@Mapping(target = "primarySkillList", source = "allocation.employee", qualifiedByName = "employeeToPrimarySkillList")
	public abstract AllocationForEarmarkDto earmarkToAllocationForEarmarkDto(Earmark source);

	public String getEarmarkName(Earmark earmark) {
		if (earmark.getProject() != null) {
			return earmark.getProject().getName();
		} else if (earmark.getOpportunity() != null) {
			return earmark.getOpportunity().getName();
		}
		return "";
	}

	@Named("earmarkToDevGdm")
	public String earmarkToDevGdm(Earmark earmark) {
		if (earmark.getProject() != null) {
			return CommonQualifiedMapper.employeeToFullName(earmark.getProject().getEmployee1());
		} else if (earmark.getOpportunity() != null) {
			return CommonQualifiedMapper.employeeToFullName(earmark.getOpportunity().getEmployee1());
		}
		return "";
	}

	@Named("earmarkToQaGdm")
	public String earmarkToQaGdm(Earmark earmark) {
		if (earmark.getProject() != null) {
			return CommonQualifiedMapper.employeeToFullName(earmark.getProject().getEmployee2());
		} else if (earmark.getOpportunity() != null) {
			return CommonQualifiedMapper.employeeToFullName(earmark.getOpportunity().getEmployee2());
		}
		return "";
	}

}
