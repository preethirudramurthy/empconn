package com.empconn.service;

import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.empconn.constants.ApplicationConstants;
import com.empconn.dto.MailForReleaseDateDto;
import com.empconn.email.EmailService;
import com.empconn.persistence.entities.Allocation;
import com.empconn.persistence.entities.Employee;
import com.empconn.repositories.AllocationRepository;

@Service
@Transactional(readOnly = true)
public class ReleaseDateApproachingEmailService {

	private static final Logger logger = LoggerFactory.getLogger(ReleaseDateApproachingEmailService.class);

	@Autowired
	private AllocationRepository allocationRepository;

	@Autowired
	private EmailService emailService;

	@Autowired
	private EmailUtilityService emailUtilityService;

	// CRAN-136 For Release Date Approaching User Story for Thirty Days
	public void sendReminderEmail(Integer noOfDays) {
		final ZoneId defaultZoneId = ZoneId.systemDefault();
		final LocalDate date = LocalDate.now();
		final LocalDate localDate = date.plusDays(noOfDays);
		final Date daysApproaching = Date.from(localDate.atStartOfDay(defaultZoneId).toInstant());
		final List<Allocation> allocation = allocationRepository.findByReleaseDateAndIsActiveIsTrue(daysApproaching);
		mailForSendingReleaseDateApproaching(allocation, noOfDays);
	}

	private void mailForSendingReleaseDateApproaching(List<Allocation> releaseDateforAll, Integer noOfDays) {

		for (final Allocation a1 : releaseDateforAll) {
			final Map<String, Object> templateModel = new HashMap<>();
			final MailForReleaseDateDto mailForReleaseDateDto = new MailForReleaseDateDto(a1.getEmployee().getEmpCode(),
					a1.getEmployee().getFullName(), a1.getProject().getAccount().getName(), a1.getProject().getName(),
					new SimpleDateFormat(ApplicationConstants.DATE_FORMAT_DD_MMM_YYYY).format(a1.getReleaseDate()));
			final StringBuilder managerEmails = new StringBuilder();
			final Employee repotingManager = a1.getEmployee().getPrimaryAllocation().getReportingManagerId();
			if (repotingManager != null && !managerEmails.toString().contains(repotingManager.getEmail())) {
					managerEmails.append(repotingManager.getEmail() + ",");
			}
			if(!managerEmails.toString().contains(a1.getEmployee().getPrimaryAllocation().getAllocationManagerId().getEmail()))
				managerEmails.append(a1.getEmployee().getPrimaryAllocation().getAllocationManagerId().getEmail() + ",");

			final String gdmEmail = emailUtilityService.getGdmEmail(a1.getWorkGroup(), a1.getProject());
			templateModel.put("noOfDays", noOfDays);
			templateModel.put("allocation", mailForReleaseDateDto);
			try {
				emailService.send("allocation-release-date-email-notification", templateModel, new String[] { managerEmails == null ? "" : managerEmails.toString() + ""},
						new String[] { gdmEmail + "" });
			} catch (final Exception exception) {
				exception.printStackTrace();
				logger.error("Exception raised while sending Allocation email Reminder for Notication : {}",exception.getMessage());
			}
		}
	}

	// CRAN-138 for Release Date Past
	public void sendReminderEmailForPast() {
		final Date todayDate = new Date();
		final List<Allocation> releaseDatePastAllocation = allocationRepository.findReleaseDatePast(todayDate);
		for (final Allocation a1 : releaseDatePastAllocation) {
			final Map<String, Object> templateModel = new HashMap<>();
			final MailForReleaseDateDto mailForReleaseDateDto = new MailForReleaseDateDto(a1.getEmployee().getEmpCode(),
					a1.getEmployee().getFullName(), a1.getProject().getAccount().getName(), a1.getProject().getName(),
					new SimpleDateFormat(ApplicationConstants.DATE_FORMAT_DD_MMM_YYYY).format(a1.getReleaseDate()));
			final StringBuilder managerEmails = new StringBuilder();
			final Employee repotingManager = a1.getEmployee().getPrimaryAllocation().getReportingManagerId();
			if (repotingManager != null && (!managerEmails.toString().contains(repotingManager.getEmail()))) {
					managerEmails.append(repotingManager.getEmail() + ",");
			}
			if(!managerEmails.toString().contains(a1.getEmployee().getPrimaryAllocation().getAllocationManagerId().getEmail()))
				managerEmails.append(a1.getEmployee().getPrimaryAllocation().getAllocationManagerId().getEmail() + ",");

			final String gdmEmail = emailUtilityService.getGdmEmail(a1.getWorkGroup(), a1.getProject());

			templateModel.put("allocation", mailForReleaseDateDto);
			try {
				emailService.send("allocation-release-date-past-email-notification", templateModel, new String[] { managerEmails == null ? "" : managerEmails.toString() + ""},
						new String[] { gdmEmail + "" });
			} catch (final Exception exception) {
				exception.printStackTrace();
				logger.error("Exception raised while sending Allocation release date past email Reminder for Notication : {}", exception.getMessage());
			}
		}
	}
}
