package com.empconn.successfactor.cron;

public class CronUtil {

	public static String getProcessingStatus(boolean isProcessed) {
		return isProcessed ? "Processed" : "Failed";
	}
}
