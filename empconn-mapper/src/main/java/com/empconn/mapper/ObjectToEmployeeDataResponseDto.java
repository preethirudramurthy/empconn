package com.empconn.mapper;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import org.mapstruct.Mapper;

import com.empconn.dto.EmployeeDataResponseDto;

@Mapper(componentModel = "spring", uses = { CommonQualifiedMapper.class })
public class ObjectToEmployeeDataResponseDto {

	public List<EmployeeDataResponseDto> objectsToEmployeeDataResponseDto(Set<Object[]> object){
		final List<EmployeeDataResponseDto> empList = new ArrayList<>();
		for (final Object[] obj : object) {
			final EmployeeDataResponseDto dto=new EmployeeDataResponseDto();
			dto.setDesignation(obj[0].toString());
			dto.setEmailId(obj[1].toString());
			dto.setEmployeeId(obj[2].toString());
			dto.setEmployeeNumber(obj[2].toString());
			dto.setFirstName(obj[3].toString());
			dto.setLastName(obj[4].toString());
			dto.setMiddleName(obj[5] == null ? "NULL" : obj[5].toString());
			dto.setLoginId(obj[6].toString());
			dto.setName(obj[3].toString() + " " +obj[4].toString());
			dto.setProject(obj[7].toString());
			dto.setProjectName(obj[7].toString());
			dto.setLocation(obj[8].toString());
			empList.add(dto);
		}
		return empList;
	}
}