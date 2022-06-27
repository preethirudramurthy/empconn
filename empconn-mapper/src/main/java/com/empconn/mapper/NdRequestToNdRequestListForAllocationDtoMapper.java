package com.empconn.mapper;

import java.sql.Timestamp;
import java.time.Instant;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Date;
import java.util.List;

import org.apache.commons.lang3.StringUtils;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Named;
import org.springframework.beans.factory.annotation.Autowired;

import com.empconn.constants.ApplicationConstants;
import com.empconn.dto.earmark.NdRequestListForAllocationResponseDto;
import com.empconn.persistence.entities.Allocation;
import com.empconn.persistence.entities.Employee;
import com.empconn.persistence.entities.NdRequest;
import com.empconn.repositories.AllocationRepository;

@Mapper(componentModel = "spring")
public abstract class NdRequestToNdRequestListForAllocationDtoMapper {

	@Autowired
	AllocationRepository allocationRepository;

	@Mapping(source = "ndRequestId", target = "ndRequestId")
	@Mapping(source = "isBillable", target = "billable")

	@Mapping(source = "percentage", target = "requestedPercentage")
	@Mapping(source = "releaseDate", target = "releaseDate", qualifiedByName = "DateToLocalDate")
	@Mapping(source = "startDate", target = "startDate", qualifiedByName = "DateToLocalDate")

	@Mapping(source = "employee1.empCode", target = "empCode")
	@Mapping(target = "empName", expression = "java(getFullName(source.getEmployee1()))")
	@Mapping(source = "employee1.title.name", target = "title")

	@Mapping(source = "employee2.fullName", target = "reportingManagerName")
	@Mapping(source = "project.name", target = "projectName")
	@Mapping(source = "project.account.name", target = "accountName")
	@Mapping(source = "employee1.location.name", target = "empLocation")
	@Mapping(source = "project.currentStatus", target = "projectStatus")
	@Mapping(target = "availablePercentage", expression = "java(getAvailablePercentage(source.getEmployee1().getEmployeeId()))")
	public abstract NdRequestListForAllocationResponseDto ndRequestToNdRequestListForAllocationDtoMapper(
			NdRequest source);

	public abstract List<NdRequestListForAllocationResponseDto> mapToDtos(List<NdRequest> source);

	@Named("DateToLocalDate")
	public static LocalDate dateToLocalDate(Date date) {
		if (null == date)
			return null;
		return Instant.ofEpochMilli(date.getTime()).atZone(ZoneId.systemDefault()).toLocalDate();
	}

	@Named("LocalDateToDate")
	public static Date localDateToDate(LocalDate localDate) {
		if (null == localDate)
			return null;
		return Date.from(localDate.atStartOfDay(ZoneId.systemDefault()).toInstant());
	}

	@Named("TimestampToLocalDateTime")
	public static LocalDateTime timestampToLocalDateTime(Timestamp timestamp) {
		if (null == timestamp)
			return null;
		return LocalDateTime.ofInstant(timestamp.toInstant(), ZoneId.of("UTC"));
	}

	@Named("LocalDateTimeToTimestamp")
	public static Timestamp localDateTimeToTimestamp(LocalDateTime localDateTime) {
		if (null == localDateTime)
			return null;
		return Timestamp.valueOf(localDateTime);
	}

	@Named("getAvailablePercentage")
	public Integer getAvailablePercentage(final Long employeeId) {
		final Allocation allocation = allocationRepository.getAllocation(employeeId, ApplicationConstants.NON_DELIVERY_BENCH_PROJECT_NAME);
		if(null == allocation)
			return 0;
		return allocation.getAllocationDetails().stream().filter(a -> a.getIsActive()).map(a -> a.getAllocatedPercentage()).reduce(0, Integer::sum);

	}

	@Named("getFullName")
	public static String getFullName(Employee employee) {
		if (employee != null)
			return StringUtils.join(employee.getFirstName(), " ", employee.getLastName());
		return null;

	}
}
