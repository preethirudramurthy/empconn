package com.empconn.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import com.empconn.dto.AccountLocationDto;
import com.empconn.persistence.entities.ClientLocation;

@Mapper(componentModel = "spring", uses = { ContactAccountLocationContactMapper.class })
public interface ClientLocationAccountLocationMapper {
	@Mapping(source = "clientLocationId", target = "accountLocationId")
	@Mapping(source = "location", target = "locationName")
	@Mapping(source = "contacts", target = "accountLocationContactList")
	AccountLocationDto clientLocationToAccountLocationDto(ClientLocation source);

	@Mapping(source = "accountLocationId", target = "clientLocationId")
	@Mapping(source = "locationName", target = "location")
	@Mapping(source = "accountLocationContactList", target = "contacts")
	ClientLocation accountLocationDtoToclientLocation(AccountLocationDto source);
}
