package com.empconn.mapper;

import java.time.Instant;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

import org.apache.commons.lang3.StringUtils;
import org.mapstruct.Named;

import com.empconn.constants.ApplicationConstants;
import com.empconn.persistence.entities.Employee;
import com.empconn.persistence.entities.EmployeeSkill;
import com.empconn.persistence.entities.SecondarySkill;
import com.empconn.utilities.DateUtils;

public abstract class BaseMapper {

	private static final String DD_MM_YYYY = "dd-MM-yyyy";
	private static final String YYYY_MM_DD = "yyyy-MM-dd";
	private static final String DD_MMM_YYYY = "dd-MMM-YYYY";

	@Named("DateToLocalDate")
	public static LocalDate dateToLocalDate(Date date) {
		if (null == date)
			return null;
		return Instant.ofEpochMilli(date.getTime()).atZone(ZoneId.systemDefault()).toLocalDate();
	}

	@Named("DateToLocalDateTime")
	public static LocalDateTime dateToLocalDateTime(Date date) {
		if (null == date)
			return null;
		return Instant.ofEpochMilli(date.getTime()).atZone(ZoneId.systemDefault()).toLocalDateTime();
	}

	@Named("String_ddmmyyyy_ToDate")
	public static Date dateToLocalDateTime(String input) {
		return DateUtils.toDate(input, DD_MM_YYYY);
	}

	@Named("Date_yyyymmdd_To_String")
	public static String dateToString(Date input) {
		return DateUtils.toString(input, YYYY_MM_DD);
	}

	@Named("Date_ddmmmyyyy_To_String")
	public static String dateToStringForReport(Date input) {
		return DateUtils.toString(input, DD_MMM_YYYY);
	}

	@Named("employeeToPrimarySkillList")
	List<String> employeeToPrimarySkillList(Employee employee) {
		final List<SecondarySkill> secondarySkills = employee.getEmployeeSkills().stream()
				.map(EmployeeSkill::getSecondarySkill).collect(Collectors.toList());
		return secondarySkills.stream().map(s -> s.getPrimarySkill().getName()).collect(Collectors.toList());

	}

	@Named("employeeToSecondarySkillList")
	List<String> employeeToSecondarySkillList(Employee employee) {
		final List<SecondarySkill> secondarySkills = employee.getEmployeeSkills().stream()
				.map(EmployeeSkill::getSecondarySkill).collect(Collectors.toList());
		return secondarySkills.stream()
				.filter(ss -> !StringUtils.equalsIgnoreCase(ss.getName(), ApplicationConstants.DEFAULT_SECONDARY_SKILL))
				.map(SecondarySkill::getName).collect(Collectors.toList());

	}

	@Named("employeeToFullName")
	String employeeToFullName(Employee employee) {
		return employee == null ? "" : StringUtils.join(employee.getFirstName(), " ", employee.getLastName());
	}

}
