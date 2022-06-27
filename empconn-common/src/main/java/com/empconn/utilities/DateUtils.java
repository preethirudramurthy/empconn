package com.empconn.utilities;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.DayOfWeek;
import java.time.Instant;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;
import java.util.Date;
import java.util.List;
import java.util.function.Predicate;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.empconn.constants.ApplicationConstants;

public class DateUtils {

	private static final Logger logger = LoggerFactory.getLogger(DateUtils.class);

	public static Date toDate(String input, String dateFormat) {
		if (!StringUtils.isEmpty(StringUtils.trim(input))) {
			try {
				final SimpleDateFormat sdf = new SimpleDateFormat(dateFormat);
				return sdf.parse(input);
			} catch (final ParseException e) {
				logger.error("Exception in converting the date", e);
			}
		}
		return null;
	}

	public static String toString(Date input, String dateFormat) {
		if (null != input) {
			final SimpleDateFormat sdf = new SimpleDateFormat(dateFormat);
			return sdf.format(input);
		}
		return "";
	}

	public static String currentDatetoString() {
		return DateTimeFormatter.ofPattern(ApplicationConstants.DATE_FORMAT_YYYYMMDD).format(LocalDateTime.now());
	}

	public static String currentDatetoStringForReport() {
		return DateTimeFormatter.ofPattern(ApplicationConstants.DATE_FORMAT_YYYYMMDD_HHMMSS).format(LocalDateTime.now());
	}

	public static String currentDatetoString(String dateFormat) {
		return DateTimeFormatter.ofPattern(dateFormat).format(LocalDateTime.now());
	}

	public static LocalDate addDaysSkippingWeekends(LocalDate date, int days) {
		LocalDate result = date;
		int addedDays = 0;
		while (addedDays < days) {
			result = result.plusDays(1);
			if (!(result.getDayOfWeek() == DayOfWeek.SATURDAY || result.getDayOfWeek() == DayOfWeek.SUNDAY)) {
				++addedDays;
			}
		}
		return result;
	}

	public static List<LocalDate> BusinessDaysBetweenIncludingEndDate(LocalDate startDate, LocalDate endDate) {
		if (startDate == null || endDate == null) {
			throw new IllegalArgumentException(
					"Invalid method argument(s) to countBusinessDaysBetween(" + startDate + "," + endDate + ")");
		}

		final Predicate<LocalDate> isWeekend = date -> date.getDayOfWeek() == DayOfWeek.SATURDAY
				|| date.getDayOfWeek() == DayOfWeek.SUNDAY;

		final long daysBetween = ChronoUnit.DAYS.between(startDate, endDate.plusDays(1));

		return Stream.iterate(startDate, date -> date.plusDays(1)).limit(daysBetween).filter((isWeekend).negate())
				.collect(Collectors.toList());
	}

	public static List<LocalDate> BusinessDaysBetweenIncludingEndDate(Date startDate, Date endDate) {

		return BusinessDaysBetweenIncludingEndDate(
				Instant.ofEpochMilli(startDate.getTime()).atZone(ZoneId.systemDefault()).toLocalDate(),
				Instant.ofEpochMilli(endDate.getTime()).atZone(ZoneId.systemDefault()).toLocalDate());
	}

	public static List<LocalDate> BusinessDaysBetweenExcludingEndDate(LocalDate startDate, LocalDate endDate) {
		if (startDate == null || endDate == null) {
			throw new IllegalArgumentException(
					"Invalid method argument(s) to countBusinessDaysBetween(" + startDate + "," + endDate + ")");
		}

		final Predicate<LocalDate> isWeekend = date -> date.getDayOfWeek() == DayOfWeek.SATURDAY
				|| date.getDayOfWeek() == DayOfWeek.SUNDAY;

		final long daysBetween = ChronoUnit.DAYS.between(startDate, endDate);

		return Stream.iterate(startDate, date -> date.plusDays(1)).limit(daysBetween).filter((isWeekend).negate())
				.collect(Collectors.toList());
	}

	public static List<LocalDate> BusinessDaysBetweenExcludingEndDate(Date startDate, Date endDate) {

		return BusinessDaysBetweenExcludingEndDate(
				Instant.ofEpochMilli(startDate.getTime()).atZone(ZoneId.systemDefault()).toLocalDate(),
				Instant.ofEpochMilli(endDate.getTime()).atZone(ZoneId.systemDefault()).toLocalDate());
	}

	public static int getMonth(String month) {
		switch (month) {
		case "Jan":
			return 1;
		case "Feb":
			return 2;
		case "Mar":
			return 3;
		case "Apr":
			return 4;
		case "May":
			return 5;
		case "Jun":
			return 6;
		case "Jul":
			return 7;
		case "Aug":
			return 8;
		case "Sep":
			return 9;
		case "Oct":
			return 10;
		case "Nov":
			return 11;
		case "Dec":
			return 12;
		default:
			return 0;
		}
	}

	public static LocalDate convertToLocalDateViaMilisecond(Date dateToConvert) {
		return Instant.ofEpochMilli(dateToConvert.getTime()).atZone(ZoneId.systemDefault()).toLocalDate();
	}

	public static Date convertToDateViaInstant(LocalDate dateToConvert) {
		return java.util.Date.from(dateToConvert.atStartOfDay().atZone(ZoneId.systemDefault()).toInstant());
	}
}
