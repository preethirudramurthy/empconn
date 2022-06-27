package com.empconn.successfactor.cron;

public class CronUtil {

	public static String getProcessingStatus(Boolean isProcessed) {
		return isProcessed ? "Processed" : "Failed";
	}
}
