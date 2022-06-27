package com.empconn.mapper;

import java.util.List;
import java.util.Set;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.springframework.beans.factory.annotation.Autowired;

import com.empconn.persistence.entities.ManagerChange;
import com.empconn.security.SecurityUtil;
import com.empconn.successfactors.dto.GdmChangeDto;

@Mapper(componentModel = "spring", uses = {CommonQualifiedMapper.class })
public abstract class GdmChangeDtoToManagerChangeMapper {

	@Autowired
	SecurityUtil jwtEmployeeUtil;

	@Mapping(source = "empId", target = "employee.employeeId")
	@Mapping(source = "newManagerId", target = "newManager.employeeId")
	@Mapping(source = "date", target = "effectiveStartDate", qualifiedByName = "DateToLocalDate")
	@Mapping(target = "status", constant = "Pending")
	@Mapping(target = "isGdm", constant = "true")

	//Added audit-log fields
	@Mapping(target = "createdBy", expression = "java(jwtEmployeeUtil.getLoggedInEmployee().getEmployeeId())")
	@Mapping(target = "createdOn", expression = "java(new java.sql.Timestamp(System.currentTimeMillis()))")
	@Mapping(target = "modifiedBy", expression = "java(jwtEmployeeUtil.getLoggedInEmployee().getEmployeeId())")
	@Mapping(target = "modifiedOn", expression = "java(new java.sql.Timestamp(System.currentTimeMillis()))")
	@Mapping(target = "isActive", constant = "true")
	public abstract ManagerChange mapToEntity(GdmChangeDto gdmChangeDto);

	public abstract List<ManagerChange> mapToEntitys(List<GdmChangeDto> gdmChangeDtoSet);
	public abstract Set<ManagerChange> mapToEntitys(Set<GdmChangeDto> gdmChangeDtoSet);

	@Mapping(source = "employee.empCode", target = "empId")
	@Mapping(target = "typeOfRelation", constant = "GDM")
	@Mapping(source = "newManager.empCode", target = "newManagerId")
	@Mapping(source = "effectiveStartDate", target = "date")
	public abstract GdmChangeDto mapToDto(ManagerChange managerChange);

	public abstract Set<GdmChangeDto> mapToDtos(Set<ManagerChange> managerChangeSet);

}
