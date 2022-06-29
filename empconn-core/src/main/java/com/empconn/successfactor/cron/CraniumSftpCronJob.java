package com.empconn.successfactor.cron;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.Scheduled;

@Configuration
public class CraniumSftpCronJob {

	@Autowired
	private ChangeGDMCron changeGDMCron;

	@Autowired
	private ChangeManagerCron changeManagerCron;

	@Autowired
	private ChangeProjectOrAccountCron changeProjectOrAccountCron;

	@Scheduled(cron="${cron1.schedule.sftp.job}", zone = "${cron.timezone}")
	public void processRequestsForCron1() {
		changeGDMCron.processRequests();
		changeManagerCron.processRequests();
		changeProjectOrAccountCron.processRequests();
	}

	
}
