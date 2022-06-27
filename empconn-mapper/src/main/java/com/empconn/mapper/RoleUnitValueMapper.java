package com.empconn.mapper;

import java.util.List;
import java.util.Set;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import com.empconn.persistence.entities.Role;
import com.empconn.vo.UnitValue;

@Mapper(componentModel = "spring")
public interface RoleUnitValueMapper {

	@Mapping(source = "name", target = "id")
	@Mapping(source = "name", target = "value")
	UnitValue roleToUnitValue(Role source);

	Set<UnitValue> rolesToUnitValues(Set<Role> roles);

	Set<UnitValue> rolesToUnitValues(List<Role> roles);

}
