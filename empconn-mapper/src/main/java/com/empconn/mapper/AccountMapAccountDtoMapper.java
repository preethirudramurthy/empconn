package com.empconn.mapper;

import java.util.Arrays;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Named;

import com.empconn.dto.map.MapAccountDto;
import com.empconn.dto.map.MapAccountLocationContactDto;
import com.empconn.dto.map.MapAccountLocationDto;
import com.empconn.enums.AccountStatus;
import com.empconn.enums.ProjectStatus;
import com.empconn.persistence.entities.Account;
import com.empconn.persistence.entities.ClientLocation;
import com.empconn.persistence.entities.Contact;

@Mapper(componentModel = "spring")
public abstract class AccountMapAccountDtoMapper {

	@Mapping(source = "name", target = "clientName")
	@Mapping(source = "clientWebsiteLink", target = "clientLink")
	@Mapping(source = "clientLocations", target = "locations")
	@Mapping(source = "source", target = "field", qualifiedByName = "accountToAllHorizontalsAndVerticals")
	@Mapping(target = "practice", constant = "Vertical")
	@Mapping(source = "startDate", target = "projectSince")
	@Mapping(source = "status", target = "isClientActive", qualifiedByName = "statusToIsClientActive")
	@Mapping(source = "mapAccountId", target = "_id")
	public abstract MapAccountDto accountToMapAccountDto(Account source);

	@Named("statusToIsClientActive")
	public boolean statusToIsClientActive(String status) {
		return (status.equals(AccountStatus.ACTIVE.name()));
			
	}

	@Named("accountToAllHorizontalsAndVerticals")
	public Set<String> accountToAllHorizontalsAndVerticals(Account account) {
		final List<String> allowedProjectStatus = Arrays.asList(ProjectStatus.PMO_APPROVED.name(),
				ProjectStatus.PROJECT_INACTIVE.name(), ProjectStatus.PROJECT_ON_HOLD.name());
		final Set<String> set = account.getProjects().stream()
				.filter(p -> p.getHorizontal() != null && allowedProjectStatus.contains(p.getCurrentStatus()))
				.map(p -> p.getHorizontal().getName()).collect(Collectors.toSet());
		set.add(account.getVertical().getName());
		return set;
	}

	public abstract MapAccountLocationDto clientLocationToMapAccountLocationDto(ClientLocation clientLocation);

	@Mapping(target = "contactPerson", source = "name")
	@Mapping(target = "contactNumber", source = "phoneNumber")
	public abstract MapAccountLocationContactDto contactToMapAccountLocationContactDto(Contact contact);

}
