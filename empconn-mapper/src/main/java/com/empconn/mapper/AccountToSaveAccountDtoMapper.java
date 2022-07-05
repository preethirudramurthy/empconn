package com.empconn.mapper;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Date;
import java.util.Optional;

import org.mapstruct.AfterMapping;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;
import org.mapstruct.Named;
import org.springframework.beans.factory.annotation.Autowired;

import com.empconn.dto.SaveAccountDto;
import com.empconn.enums.AccountCategory;
import com.empconn.persistence.entities.Account;
import com.empconn.persistence.entities.Vertical;
import com.empconn.repositories.VerticalRepository;
import com.empconn.security.SecurityUtil;

@Mapper(componentModel = "spring", uses = { ClientLocationToAccountLocationDtoMapper.class,
		VerticalUnitValueMapper.class })
public abstract class AccountToSaveAccountDtoMapper {

	@Autowired
	VerticalRepository verticalRepository;
	
	@Autowired
	SecurityUtil jwtEmployeeUtil;

	@Mapping(target = "createdBy", expression = "java(jwtEmployeeUtil.getLoggedInEmployee().getEmployeeId())")
	@Mapping(target = "createdOn", expression = "java(new java.sql.Timestamp(System.currentTimeMillis()))")
	@Mapping(target = "status", expression = "java(com.empconn.enums.AccountStatus.TEMP.name())")
	@Mapping(target = "isActive", constant = "true")
	@Mapping(source = "categoryId", target = "category", qualifiedByName = "CategoryIdToCategory")
	@Mapping(source = "verticalId", target = "vertical", qualifiedByName = "VerticalIdToVertical")
	@Mapping(source = "startDate", target = "startDate", qualifiedByName = "LocalDateTimeToDate")
	@Mapping(source = "endDate", target = "endDate", qualifiedByName = "LocalDateTimeToDate")
	@Mapping(source = "websiteLink", target = "clientWebsiteLink")
	@Mapping(source = "accountLocationList", target = "clientLocations")
	public abstract Account saveAccountDtoToAccount(SaveAccountDto destination);

	@Mapping(target = "modifiedBy", expression = "java(jwtEmployeeUtil.getLoggedInEmployee().getEmployeeId())")
	@Mapping(target = "modifiedOn", expression = "java(new java.sql.Timestamp(System.currentTimeMillis()))")
	@Mapping(source = "categoryId", target = "category", qualifiedByName = "CategoryIdToCategory")
	@Mapping(source = "verticalId", target = "vertical", qualifiedByName = "VerticalIdToVertical")
	@Mapping(source = "startDate", target = "startDate", qualifiedByName = "LocalDateTimeToDate")
	@Mapping(source = "endDate", target = "endDate", qualifiedByName = "LocalDateTimeToDate")
	@Mapping(source = "websiteLink", target = "clientWebsiteLink")
	@Mapping(source = "accountLocationList", target = "clientLocations")
	public abstract Account saveAccountDtoToAccount(SaveAccountDto destination, @MappingTarget Account account);

	@AfterMapping
	protected void asignMappingRelations(SaveAccountDto destination, @MappingTarget Account account) {
		if(account.getClientLocations()!=null) {
			account.getClientLocations().forEach(cl -> {
				cl.setAccount(account);
				if(cl.getContacts()!=null) {
					cl.getContacts().forEach(c ->
						c.setClientLocation(cl)
					);
				}
			});
		}
	}

	@Named("CategoryIdToCategory")
	public String categoryIdToCategory(String categoryId) {
		return AccountCategory.getValue(categoryId);
	}

	@Named("VerticalIdToVertical")
	public Vertical verticalIdToVertical(String verticalId) {
		final Optional<Vertical> vertical = verticalRepository.findById(Integer.parseInt(verticalId));
		return vertical.orElse(null);
	}

	@Named("LocalDateTimeToDate")
	public Date localDateTimeToTimestamp(LocalDateTime localDateTime) {
		if (null == localDateTime)
			return null;
		return Date.from(localDateTime.atZone(ZoneId.systemDefault()).toInstant());
	}

}