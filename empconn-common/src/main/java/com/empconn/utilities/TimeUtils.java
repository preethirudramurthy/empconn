package com.empconn.utilities;

import java.sql.Timestamp;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class TimeUtils {
	public static Timestamp getCreatedOn() {
		return new Timestamp(System.currentTimeMillis());
	}

	public static Timestamp getToday() {
		return new Timestamp(System.currentTimeMillis());
	}

	public static Timestamp convertStringToTimestamp(String strDate) {
		try {
			DateFormat formatter = new SimpleDateFormat("yyyy/MM/dd");
			// you can change format of date
			Date date = formatter.parse(strDate);

			return new Timestamp(date.getTime());
		} catch (ParseException e) {
			e.printStackTrace();
		}
		return null;

	}
}
