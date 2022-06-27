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

			Timestamp timeStampDate = new Timestamp(date.getTime());

			return timeStampDate;
		} catch (ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return null;

	}
}
