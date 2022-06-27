package com.empconn.mapper;

import java.util.List;
import java.util.Set;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import com.empconn.persistence.entities.Location;
import com.empconn.vo.UnitValue;

@Mapper(componentModel = "spring")
public interface LocationUnitValueMapper {

	@Mapping(source = "locationId", target = "id")
	@Mapping(source = "name", target = "value")
	UnitValue locationToUnitValue(Location source);

	Set<UnitValue> locationsToUnitValues(Set<Location> locations);
	List<UnitValue> locationsToUnitValues(List<Location> locations);

	@Mapping(source = "id", target = "locationId")
	@Mapping(source = "value", target = "name")
	Location unitValueToLocation(UnitValue destination);

}
