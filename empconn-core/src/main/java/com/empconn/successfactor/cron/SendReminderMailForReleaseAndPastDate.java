package com.empconn.successfactor.cron;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import com.empconn.service.ReleaseDateApproachingEmailService;

@Component
public class SendReminderMailForReleaseAndPastDate {
	private static final Logger logger = LoggerFactory.getLogger(SendReminderMailForReleaseAndPastDate.class);

	@Autowired
	private ReleaseDateApproachingEmailService releaseDateApproachingEmailService;

	@Value("${reminder.email.days}")
	private String reminderEmailDays;


	@Scheduled(cron="${cron.schedule.remindermail}", zone = "${cron.timezone}")
	private void sendReminderMailForReleaseDate() {
		final String METHOD_NAME = "sendReminderMailForReleaseDate";
		logger.info("{} starts execution successfully", METHOD_NAME);
		final String[] days = reminderEmailDays.split(",");
		try {
			for(final String day :days)
				if(Integer.valueOf(day) > 0 )
					releaseDateApproachingEmailService.sendReminderEmail(Integer.valueOf(day));
				else
					releaseDateApproachingEmailService.sendReminderEmailForPast();
		}catch(final Exception exception) {
			logger.error("{} exception raised as : {}", METHOD_NAME, exception);
		}
		logger.info("{} exits successfully.", METHOD_NAME);
	}

}
