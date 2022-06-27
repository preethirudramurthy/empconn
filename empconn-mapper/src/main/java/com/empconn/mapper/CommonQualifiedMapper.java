package com.empconn.mapper;

import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.time.DayOfWeek;
import java.time.Instant;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.function.Function;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import org.apache.commons.lang3.StringUtils;
import org.mapstruct.Mapper;
import org.mapstruct.Named;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.CollectionUtils;

import com.empconn.constants.ApplicationConstants;
import com.empconn.enums.AccountCategory;
import com.empconn.enums.ProjectStatus;
import com.empconn.persistence.entities.Allocation;
import com.empconn.persistence.entities.AllocationDetail;
import com.empconn.persistence.entities.Earmark;
import com.empconn.persistence.entities.EarmarkSalesforceIdentifier;
import com.empconn.persistence.entities.Employee;
import com.empconn.persistence.entities.EmployeeSkill;
import com.empconn.persistence.entities.Horizontal;
import com.empconn.persistence.entities.Location;
import com.empconn.persistence.entities.PrimarySkill;
import com.empconn.persistence.entities.Project;
import com.empconn.persistence.entities.ProjectComment;
import com.empconn.persistence.entities.SalesforceIdentifier;
import com.empconn.persistence.entities.SecondarySkill;
import com.empconn.persistence.entities.Title;
import com.empconn.persistence.entities.Vertical;
import com.empconn.repositories.EmployeeSkillRepository;
import com.empconn.repositories.SecondarySkillRepository;
import com.empconn.utilities.DateUtils;
import com.empconn.vo.UnitValue;

@Mapper(componentModel = "spring")
public abstract class CommonQualifiedMapper {

	private static final String DD_MMM_YYYY = "dd-MMM-yyyy";

	@Autowired
	private EmployeeSkillRepository employeeSkillRepository;

	@Autowired
	private SecondarySkillRepository secondarySkillRepository;

	@Named("employeeToUnitValue")
	public UnitValue employeeToUnitValue(Employee emp) {
		if (emp != null) {
			return new UnitValue(emp.getEmployeeId(), emp.getFullName());
		}
		return null;
	}

	@Named("employeeToFullName")
	public static String employeeToFullName(Employee employee) {
		if (employee != null)
			return StringUtils.join(employee.getFirstName(), " ", employee.getMiddleName(),
					!StringUtils.isEmpty(employee.getMiddleName()) ? " " : null, employee.getLastName());
		return null;

	}

	@Named("locationToUnitValue")
	public UnitValue locationToUnitValue(Location location) {
		if (location != null) {
			return new UnitValue(location.getLocationId(), location.getName());
		}
		return null;
	}

	@Named("titleToUnitValue")
	public UnitValue titleToUnitValue(Title title) {
		if (title != null) {
			return new UnitValue(title.getTitleId(), title.getName());
		}
		return null;
	}

	@Named("primarySkillToUnitValue")
	UnitValue primarySkillToUnitValue(PrimarySkill primarySkill) {
		if (primarySkill != null) {
			return new UnitValue(primarySkill.getPrimarySkillId(), primarySkill.getName());
		}
		return null;
	}

	@Named("verticalToUnitValue")
	public UnitValue verticalToUnitValue(Vertical vertical) {
		if (vertical != null) {
			return new UnitValue(vertical.getVerticalId(), vertical.getName());
		}
		return null;
	}

	@Named("horizontalToUnitValue")
	public UnitValue horizontalToUnitValue(Horizontal horizontal) {
		if (horizontal != null) {
			return new UnitValue(horizontal.getHorizontalId(), horizontal.getName());
		}
		return null;
	}

	@Named("categoryToUnitValue")
	public UnitValue categoryToUnitValue(String category) {
		if (category != null) {
			final AccountCategory accountCategory = AccountCategory.getByValue(category);
			return new UnitValue(accountCategory.getId(), accountCategory.getValue());
		}
		return null;
	}

	@Named("projectToUnitValue")
	public UnitValue projectToUnitValue(Project project) {
		if (project == null) {
			return null;
		}
		return new UnitValue(project.getProjectId(), project.getName());
	}

	@Named("stringListToCommaStrings")
	public static String stringListToCommaStrings(List<String> list) {
		if (list != null) {
			return String.join(", ", list);
		}
		return null;
	}

	@Named("commaStringsTostringList")
	public static List<String> commaStringsTostringList(String commaString) {
		if (commaString != null) {
			return Stream.of(commaString.split(",", -1)).collect(Collectors.toList());
		}
		return null;
	}

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

	@Named("TimestampToLocalDate")
	public static LocalDate timestampToLocalDate(Timestamp timestamp) {
		if (null == timestamp)
			return null;
		return LocalDateTime.ofInstant(timestamp.toInstant(), ZoneId.of("UTC")).toLocalDate();
	}

	@Named("currentStatusToDtoPinStatus")
	public String currentStatusToDtoPinStatus(String currentStatus) {
		if (null == currentStatus)
			return null;
		if (currentStatus.equals(ProjectStatus.DRAFT.name()))
			return ProjectStatus.OPEN.getValue();
		if (currentStatus.equals(ProjectStatus.PMO_APPROVED.name()))
			return "Active";
		if (currentStatus.equals(ProjectStatus.PROJECT_ON_HOLD.name()))
			return "OnHold";
		if (currentStatus.equals(ProjectStatus.PROJECT_INACTIVE.name()))
			return "Inactive";
		return ProjectStatus.getValueByName(currentStatus);
	}

	@Named("DateToFormattedDate")
	public static String DateToFormattedDate(Date date) {
		if (null == date)
			return null;
		return new SimpleDateFormat("dd-MMM-YYYY").format(date);
	}

	@Named("localDateTimeToFormattedDate")
	public static String localDateTimeToFormattedDate(LocalDateTime localDateTime) {
		if (null == localDateTime)
			return null;
		return new SimpleDateFormat("dd-MMM-YYYY")
				.format(java.util.Date.from(localDateTime.atZone(ZoneId.systemDefault()).toInstant()));
	}

	@Named("TimestampToFormattedDate")
	public static String TimestampToFormattedDate(Timestamp timestamp) {
		if (null == timestamp)
			return null;
		final Date date = timestamp;
		return new SimpleDateFormat("dd-MMM-YYYY").format(date);
	}

	@Named("latestCommentForProjectStatus")
	public static ProjectComment latestCommentForProjectStatus(Set<ProjectComment> comments, String status) {
		if (CollectionUtils.isEmpty(comments))
			return null;
		final Optional<ProjectComment> comment = comments.stream().filter(c -> c.getStatus().equals(status))
				.max(Comparator.comparing(ProjectComment::getCreatedOn));
		if (comment.isPresent())
			return comment.get();
		return null;
	}

	@Named("DateToLocalDateTime")
	public static LocalDateTime dateToLocalDateTime(Date date) {
		if (null == date)
			return null;
		return Instant.ofEpochMilli(date.getTime()).atZone(ZoneId.systemDefault()).toLocalDateTime();
	}

	@Named("localDateTimeToDate")
	public static Date localDateTimeToDate(LocalDateTime dateTime) {
		if (null == dateTime)
			return null;
		return java.sql.Timestamp.valueOf(dateTime);
	}

	@Named("employeeToPrimarySkillList")
	List<String> employeeToPrimarySkillList(Employee employee) {
		final List<EmployeeSkill> employeeSkills = employeeSkillRepository
				.findByEmployeeEmployeeId(employee.getEmployeeId());
		final List<Integer> secondarySkillIds = employeeSkills.stream()
				.map(e -> e.getSecondarySkill().getSecondarySkillId()).collect(Collectors.toList());
		final Set<SecondarySkill> secondarySkills = secondarySkillRepository
				.findBySecondarySkillIdInAndIsActiveTrue(secondarySkillIds);

		return secondarySkills.stream().map(s -> s.getPrimarySkill().getName()).collect(Collectors.toList());

	}

	@Named("employeeToSecondarySkillList")
	List<String> employeeToSecondarySkillList(Employee employee) {
		final List<EmployeeSkill> employeeSkills = employeeSkillRepository
				.findByEmployeeEmployeeId(employee.getEmployeeId());
		final List<Integer> secondarySkillIds = employeeSkills.stream()
				.map(e -> e.getSecondarySkill().getSecondarySkillId()).collect(Collectors.toList());
		final Set<SecondarySkill> secondarySkills = secondarySkillRepository
				.findBySecondarySkillIdInAndIsActiveTrue(secondarySkillIds);

		return secondarySkills.stream()
				.filter(ss -> !StringUtils.equalsIgnoreCase(ss.getName(), ApplicationConstants.DEFAULT_SECONDARY_SKILL))
				.map(SecondarySkill::getName).collect(Collectors.toList());

	}

	@Named("allocationStartDate")
	public LocalDate allocationStartDate(Allocation allocation) {
		if (allocation.getAllocationDetails() != null && !allocation.getAllocationDetails().isEmpty()) {
			final Date minStartDate = Collections.min(allocation.getAllocationDetails().stream()
					.map(AllocationDetail::getStartDate).collect(Collectors.toList()));
			return dateToLocalDate(minStartDate);
		} else {
			return null;
		}
	}

	public Date getAllocationStartDate(Allocation allocation) {
		if (allocation.getAllocationDetails() != null && !allocation.getAllocationDetails().isEmpty()) {
			return Collections.min(allocation.getAllocationDetails().stream().map(AllocationDetail::getStartDate)
					.collect(Collectors.toList()));
		} else {
			return null;
		}
	}

	@Named("getAllocationStartDateForND")
	Date getAllocationStartDateForND(Allocation allocation) {
		if (allocation.getAllocationDetails() != null && !allocation.getAllocationDetails().isEmpty()) {
			final Date minStartDate = Collections.min(allocation.getAllocationDetails().stream()
					.map(AllocationDetail::getStartDate).collect(Collectors.toList()));
			return minStartDate;
		} else {
			return null;
		}
	}

	@Named("mergedAllocatedPercentage")
	public Integer mergedAllocatedPercentage(Allocation allocation) {
		if (CollectionUtils.isEmpty(allocation.getAllocationDetails())) {
			return 0;
		}
		return allocation.getAllocationDetails().stream().filter(AllocationDetail::getIsActive)
				.map(AllocationDetail::getAllocatedPercentage).reduce(0, Integer::sum);

	}

	@Named("dateOfMovement")
	public static LocalDate getDateOfMovement() {
		LocalDate movementDate = LocalDate.now();
		while (movementDate.getDayOfWeek() == DayOfWeek.SATURDAY || movementDate.getDayOfWeek() == DayOfWeek.SUNDAY) {
			movementDate = movementDate.plusDays(1);
		}
		return movementDate;

	}

	@Named("primaryManagerName")
	public static String primaryManagerName(Employee employee) {
		if (employee != null && employee.getPrimaryAllocation() != null)
			return employee.getPrimaryAllocation().getReportingManagerId().getFullName();
		return null;
	}

	@Named("earmarkToEarmarkAccountName")
	public static String earmarkToEarmarkAccountName(Earmark earmark) {
		if (earmark != null) {
			if (earmark.getProject() != null)
				return earmark.getProject().getAccount().getName();
			else if (earmark.getOpportunity() != null) {
				if (earmark.getOpportunity().getAccount() != null)
					return earmark.getOpportunity().getAccount().getName();
				else
					return earmark.getOpportunity().getAccountName();
			}
		}
		return null;
	}

	@Named("earmarkToEarmarkProjectName")
	public static String earmarkToEarmarkProjectName(Earmark earmark) {
		if (earmark != null) {
			if (earmark.getProject() != null)
				return earmark.getProject().getName();
			else if (earmark.getOpportunity() != null) {
				return earmark.getOpportunity().getName();
			}
		}
		return null;
	}

	@Named("earmarkToSalesforceIdList")
	public List<String> earmarkTosalesforceIdList(Earmark earmark) {
		final List<String> salesforceIdList = new ArrayList<>();
		if (!CollectionUtils.isEmpty(earmark.getEarmarkSalesforceIdentifiers())) {
			final List<String> earmarkSflist = earmark.getEarmarkSalesforceIdentifiers().stream()
					.filter(EarmarkSalesforceIdentifier::getIsActive).map(EarmarkSalesforceIdentifier::getValue)
					.collect(Collectors.toList());
			salesforceIdList.addAll(earmarkSflist);
		}
		if (earmark.getProject() != null && !CollectionUtils.isEmpty(earmark.getProject().getSalesforceIdentifiers())) {
			final List<String> projectSflist = earmark.getProject().getSalesforceIdentifiers().stream()
					.filter(SalesforceIdentifier::getIsActive).map(SalesforceIdentifier::getValue)
					.collect(Collectors.toList());
			salesforceIdList.addAll(projectSflist);
		}
		return salesforceIdList.stream().distinct().collect(Collectors.toList());
	}

	@Named("earmarkToEarmarkSalesforceIdList")
	public List<String> earmarkToEarmarkSalesforceIdList(Earmark earmark) {
		final List<String> salesforceIdList = new ArrayList<>();
		if (!CollectionUtils.isEmpty(earmark.getEarmarkSalesforceIdentifiers())) {
			final List<String> earmarkSflist = earmark.getEarmarkSalesforceIdentifiers().stream()
					.filter(EarmarkSalesforceIdentifier::getIsActive).map(EarmarkSalesforceIdentifier::getValue)
					.distinct().collect(Collectors.toList());
			salesforceIdList.addAll(earmarkSflist);
		}
		return salesforceIdList;
	}

	@Named("allocationToAvailableFrom")
	public LocalDate allocationToAvailableFrom(Allocation allocation) {
		if (allocation.getProject().getAccount().getName().equalsIgnoreCase("Bench")) {
			return LocalDate.now();
		} else if (allocation.getReleaseDate() != null) {
			return CommonQualifiedMapper.dateToLocalDate(allocation.getReleaseDate());
		} else
			return LocalDate.now();
	}

	@Named("locationName")
	String locationName(Allocation allocation) {
		return allocation.getEmployee().getLocation().getName();
	}

	@Named("Date_ddmmmyyyy_To_String")
	public static String dateToStringForReport(Date input) {
		return DateUtils.toString(input, DD_MMM_YYYY);
	}

	@Named("allocationStartDateFormatted")
	public String allocationStartDateFormatted(Allocation allocation) {
		if (allocation.getAllocationDetails() != null && !allocation.getAllocationDetails().isEmpty()) {
			final Date minStartDate = Collections.min(allocation.getAllocationDetails().stream()
					.map(AllocationDetail::getStartDate).collect(Collectors.toList()));
			return dateToStringForReport(minStartDate);
		} else {
			return null;
		}
	}

	@Named("getLatestStartDateFormatted")
	public String getLatestStartDateFormatted(Allocation allocation) {
		if (allocation.getAllocationDetails() != null && !allocation.getAllocationDetails().isEmpty()) {
			final Date maxStartDate = Collections.max(allocation.getAllocationDetails().stream()
					.map(AllocationDetail::getStartDate).collect(Collectors.toList()));
			return dateToStringForReport(maxStartDate);
		} else {
			return null;
		}
	}

	public Date getLatestStartDate(Allocation allocation) {
		if (allocation.getAllocationDetails() != null && !allocation.getAllocationDetails().isEmpty()) {
			return Collections.max(allocation.getAllocationDetails().stream().map(AllocationDetail::getStartDate)
					.collect(Collectors.toList()));
		} else {
			return null;
		}
	}

	@Named("getPrimarySkillNames")
	public String getPrimarySkillNames(Employee employee) {
		final List<EmployeeSkill> employeeSkills = employeeSkillRepository
				.findByEmployeeEmployeeId(employee.getEmployeeId());
		final Function<? super EmployeeSkill, ? extends String> getPrimarySkillName = s -> s.getSecondarySkill()
				.getPrimarySkill().getName();
		return convertPrimarySkillNamesToString(employeeSkills, getPrimarySkillName);
	}

	@Named("getSecondarySkillNames")
	public String getSecondarySkillNames(Employee employee) {
		final List<EmployeeSkill> employeeSkills = employeeSkillRepository
				.findByEmployeeEmployeeId(employee.getEmployeeId());
		final Function<? super EmployeeSkill, ? extends String> getSecondarySkillName = s -> s.getSecondarySkill()
				.getName();
		return convertSkillNamesToString(employeeSkills, getSecondarySkillName);
	}

	private String convertSkillNamesToString(List<EmployeeSkill> employeeSkills,
			Function<? super EmployeeSkill, ? extends String> getNameFromSkill) {
		if (!CollectionUtils.isEmpty(employeeSkills)) {
			return employeeSkills.stream()
					.filter(es -> null != es && null != es.getSecondarySkill()
							&& !StringUtils.equalsIgnoreCase(es.getSecondarySkill().getName(),
									ApplicationConstants.DEFAULT_SECONDARY_SKILL))
					.map(getNameFromSkill).collect(Collectors.joining(","));
		}
		return "";
	}

	private String convertPrimarySkillNamesToString(List<EmployeeSkill> employeeSkills,
			Function<? super EmployeeSkill, ? extends String> getNameFromSkill) {
		if (!CollectionUtils.isEmpty(employeeSkills)) {
			return employeeSkills.stream().filter(es -> null != es && null != es.getSecondarySkill())
					.map(getNameFromSkill).collect(Collectors.joining(","));
		}
		return "";
	}

}
