package com.empconn.mapper;

import java.util.Set;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import com.empconn.persistence.entities.Opportunity;
import com.empconn.vo.UnitValue;

@Mapper(componentModel = "spring")
public interface OpportunityUnitValueMapper {

	@Mapping(source = "opportunityId", target = "id")
	@Mapping(source = "name", target = "value")
	UnitValue opportunityToUnitValue(Opportunity source);

	Set<UnitValue> opportunitiesToUnitValues(Set<Opportunity> opportunities);

	@Mapping(source = "id", target = "opportunityId")
	@Mapping(source = "value", target = "name")
	Opportunity unitValueToOpportunity(UnitValue destination);
}