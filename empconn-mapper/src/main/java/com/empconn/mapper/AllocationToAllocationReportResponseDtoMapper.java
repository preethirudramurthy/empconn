package com.empconn.mapper;

import java.util.List;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Named;

import com.empconn.constants.ApplicationConstants;
import com.empconn.dto.AllocationReportResponseDto;
import com.empconn.persistence.entities.Allocation;
import com.google.common.base.Strings;

@Mapper(componentModel = "spring", uses = {CommonQualifiedMapper.class})
public abstract class AllocationToAllocationReportResponseDtoMapper {

	@Mapping(source = "allocationId", target = "allocationDetailId")
	@Mapping(source = "project.account.name", target = "accountName")
	@Mapping(source = "employee.empCode", target = "empCode")
	@Mapping(source = "employee.fullName", target = "empFullName")
	@Mapping(source = "allocation", target = "gdmFullName", qualifiedByName = "getGdmName")
	@Mapping(source = "employee.location.name", target = "location")
	@Mapping(source = "allocation", target = "percentage", qualifiedByName = "mergedAllocatedPercentage")
	@Mapping(source = "employee", target = "primarySkillSet", qualifiedByName = "getPrimarySkillNames")
	@Mapping(source = "project.name", target = "projectName")
	@Mapping(source = "employee.primaryAllocation.reportingManagerId.fullName", target = "reportingManager")
	@Mapping(source = "employee", target = "secondSkillSet", qualifiedByName = "getSecondarySkillNames")
	@Mapping(source = "employee.title.name", target = "title")
	@Mapping(source = "workGroup.name", target = "workgroup")
	@Mapping(source = "releaseDate", target = "releaseDate", qualifiedByName = "Date_ddmmmyyyy_To_String")
	@Mapping(source = "allocation", target = "startDate", qualifiedByName = "allocationStartDateFormatted")
	public abstract AllocationReportResponseDto convert(Allocation allocation);

	public abstract List<AllocationReportResponseDto> convert(List<Allocation> allocations);

	@Named("getGdmName")
	public static String getGdmName(Allocation allocation) {
		final String qeGdmId = allocation.getProject().getEmployee2() != null ? allocation.getProject().getEmployee2().getFullName() : "";
		final String devGdmId = allocation.getProject().getEmployee1() != null ? allocation.getProject().getEmployee1().getFullName() : "";

		String gdm = allocation.getWorkGroup().getName().equalsIgnoreCase(ApplicationConstants.QA_WORK_GROUP) ? qeGdmId : devGdmId;

		//If it is still not assigned then assign whichever GDM is present.
		if(Strings.isNullOrEmpty(gdm)) {
			if(!Strings.isNullOrEmpty(devGdmId)) {
				gdm = devGdmId;
			}else {
				gdm = qeGdmId;
			}
		}
		return gdm;
	}

}
