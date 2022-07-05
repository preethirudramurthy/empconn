package com.empconn.mapper;

import java.sql.Timestamp;
import java.time.Instant;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Date;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Named;

import com.empconn.dto.AccountDetailsDto;
import com.empconn.enums.AccountCategory;
import com.empconn.persistence.entities.Account;
import com.empconn.vo.UnitValue;

@Mapper(componentModel = "spring", uses = { ClientLocationToAccountLocationDtoMapper.class,
		VerticalUnitValueMapper.class })
public interface AccountAccountDetailsMapper {

	@Mapping(source = "category", target = "category", qualifiedByName = "AccountCategoryNameToUnitValue")
	@Mapping(source = "name", target = "accountName")
	@Mapping(source = "clientWebsiteLink", target = "websiteLink")
	@Mapping(source = "startDate", target = "startDate", qualifiedByName = "DateToLocalDate")
	@Mapping(source = "endDate", target = "endDate", qualifiedByName = "DateToLocalDate")
	@Mapping(source = "createdOn", target = "createdDate", qualifiedByName = "TimestampToLocalDateTime")
	@Mapping(source = "vertical", target = "vertical")
	@Mapping(source = "clientLocations", target = "accountLocationList")
	AccountDetailsDto accountToAccountDetailsDto(Account source);

	@Mapping(source = "category", target = "category", qualifiedByName = "UnitValueToAccountCategory")
	@Mapping(source = "accountName", target = "name")
	@Mapping(source = "websiteLink", target = "clientWebsiteLink")
	@Mapping(source = "startDate", target = "startDate", qualifiedByName = "LocalDateToDate")
	@Mapping(source = "endDate", target = "endDate", qualifiedByName = "LocalDateToDate")
	@Mapping(source = "createdDate", target = "createdOn", qualifiedByName = "LocalDateTimeToTimestamp")
	@Mapping(source = "vertical", target = "vertical")
	@Mapping(source = "accountLocationList", target = "clientLocations")
	Account accountDetailsToAccount(AccountDetailsDto destination);

	@Named("AccountCategoryToUnitValue")
	static UnitValue accountCategoryToUnitValue(AccountCategory category) {
		return new UnitValue(category.getId(), category.getValue());
	}

	@Named("AccountCategoryNameToUnitValue")
	static UnitValue accountCategoryToUnitValue(String categoryName) {
		return accountCategoryToUnitValue(AccountCategory.getByValue(categoryName));
	}

	@Named("UnitValueToAccountCategory")
	static AccountCategory unitValueToAccountCategory(UnitValue unitValue) {
		return AccountCategory.getById(unitValue.getId());
	}

	@Named("DateToLocalDate")
	static LocalDate dateToLocalDate(Date date) {
		if (null == date)
			return null;
		return Instant.ofEpochMilli(date.getTime()).atZone(ZoneId.systemDefault()).toLocalDate();
	}

	@Named("LocalDateToDate")
	static Date localDateToDate(LocalDate localDate) {
		if (null == localDate)
			return null;
		return Date.from(localDate.atStartOfDay(ZoneId.systemDefault()).toInstant());
	}

	@Named("TimestampToLocalDateTime")
	static LocalDateTime timestampToLocalDateTime(Timestamp timestamp) {
		if (null == timestamp)
			return null;
		return LocalDateTime.ofInstant(timestamp.toInstant(), ZoneId.of("UTC"));
	}

	@Named("LocalDateTimeToTimestamp")
	static Timestamp localDateTimeToTimestamp(LocalDateTime localDateTime) {
		if (null == localDateTime)
			return null;
		return Timestamp.valueOf(localDateTime);
	}
}