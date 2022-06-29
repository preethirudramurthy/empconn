package com.empconn.utilities;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.DayOfWeek;
import java.time.Instant;
import java.time.LocalDate;
import java.time.ZoneId;
import java.time.temporal.ChronoUnit;
import java.util.Date;
import java.util.List;
import java.util.function.Predicate;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


public class DateUtils2 {

	private static final Logger logger = LoggerFactory.getLogger(DateUtils2.class);

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

	public static List<LocalDate> businessDaysBetween(LocalDate startDate, LocalDate endDate)
	{
		if (startDate == null || endDate == null) {
			throw new IllegalArgumentException("Invalid method argument(s) to countBusinessDaysBetween(" + startDate
					+ "," + endDate + ")");
		}

		final Predicate<LocalDate> isWeekend = date -> date.getDayOfWeek() == DayOfWeek.SATURDAY
				|| date.getDayOfWeek() == DayOfWeek.SUNDAY;

		final long daysBetween = ChronoUnit.DAYS.between(startDate, endDate);

		return Stream.iterate(startDate, date -> date.plusDays(1)).limit(daysBetween)
				.filter((isWeekend).negate()).collect(Collectors.toList());
	}

	public static List<LocalDate> businessDaysBetween(Date startDate, Date endDate){

		return businessDaysBetween(Instant.ofEpochMilli(startDate.getTime()).atZone(ZoneId.systemDefault()).toLocalDate(),
				Instant.ofEpochMilli(endDate.getTime()).atZone(ZoneId.systemDefault()).toLocalDate());
	}

	public static LocalDate convertToLocalDateViaMilisecond(Date dateToConvert) {
		return Instant.ofEpochMilli(dateToConvert.getTime())
				.atZone(ZoneId.systemDefault())
				.toLocalDate();
	}

	public static Date convertToDateViaInstant(LocalDate dateToConvert) {
		return java.util.Date.from(dateToConvert.atStartOfDay()
				.atZone(ZoneId.systemDefault())
				.toInstant());
	}


}
