package com.empconn.mapper;

import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;
import org.springframework.beans.factory.annotation.Autowired;

import com.empconn.dto.AccountLocationDto;
import com.empconn.persistence.entities.ClientLocation;
import com.empconn.repositories.ClientLocationRepository;
import com.empconn.security.SecurityUtil;

@Mapper(componentModel = "spring", uses = { ContactToAccountLocationContactDtoMapper.class })
public abstract class ClientLocationToAccountLocationDtoMapper {

	@Autowired
	ClientLocationRepository clientLocationRepository;

	@Autowired
	SecurityUtil jwtEmployeeUtil;

	@Mapping(source = "clientLocationId", target = "accountLocationId")
	@Mapping(source = "location", target = "locationName")
	@Mapping(source = "contacts", target = "accountLocationContactList")
	abstract AccountLocationDto clientLocationToAccountLocationDto(ClientLocation source);

	abstract Set<AccountLocationDto> clientLocationsToAccountLocationsDto(Set<ClientLocation> source);

	@Mapping(target = "createdBy", expression = "java(jwtEmployeeUtil.getLoggedInEmployee().getEmployeeId())")
	@Mapping(target = "createdOn", expression = "java(new java.sql.Timestamp(System.currentTimeMillis()))")
	@Mapping(target = "isActive", constant = "true")
	@Mapping(source = "accountLocationId", target = "clientLocationId")
	@Mapping(source = "locationName", target = "location")
	@Mapping(source = "accountLocationContactList", target = "contacts")
	abstract ClientLocation accountLocationDtoToClientLocation(AccountLocationDto destination);

	@Mapping(target = "modifiedBy", expression = "java(jwtEmployeeUtil.getLoggedInEmployee().getEmployeeId())")
	@Mapping(target = "modifiedOn", expression = "java(new java.sql.Timestamp(System.currentTimeMillis()))")
	@Mapping(source = "locationName", target = "location")
	@Mapping(source = "accountLocationId", target = "clientLocationId")
	@Mapping(source = "accountLocationContactList", target = "contacts")
	abstract ClientLocation accountLocationDtoToClientLocation(AccountLocationDto destination,
			@MappingTarget ClientLocation location);

	Set<ClientLocation> accountLocationsDtoToClientLocations(List<AccountLocationDto> destination) {
		final Set<ClientLocation> set = new HashSet<>();
		for (final AccountLocationDto dto : destination) {
			if (dto.getAccountLocationId() != null && clientLocationRepository
					.findById(Long.valueOf(dto.getAccountLocationId())).isPresent()) {
				Optional<ClientLocation> clOpt = clientLocationRepository
						.findById(Long.valueOf(dto.getAccountLocationId()));
				final ClientLocation location = clOpt.orElse(null);
				set.add(accountLocationDtoToClientLocation(dto, location));
			} else
				set.add(accountLocationDtoToClientLocation(dto));
		}
		if (set.isEmpty())
			return Collections.emptySet();
		return set;
	}

}