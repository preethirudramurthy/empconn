package com.empconn.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import com.empconn.dto.ChangeEndDateDto;
import com.empconn.dto.ProjectEndDateChangeDto;
import com.empconn.persistence.entities.Allocation;

@Mapper(componentModel = "spring", uses = {CommonQualifiedMapper.class})
public interface AllocationToChangeEndDateDtoMapper {
	
	@Mapping(source = "a.employee.empCode", target = "empCode")
	@Mapping(source = "a.employee.firstName", target = "empName")
	@Mapping(source = "a.releaseDate", target = "releaseDate", qualifiedByName="DateToLocalDate")
	@Mapping(source = "dto.endDate", target = "originalDate")
	public abstract ChangeEndDateDto allocationToChangeEndDateDtoMapper(Allocation a, ProjectEndDateChangeDto dto);

}
