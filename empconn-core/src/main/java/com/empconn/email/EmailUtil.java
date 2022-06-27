package com.empconn.email;

import java.util.HashMap;
import java.util.Map;

import org.apache.commons.lang3.ArrayUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.empconn.constants.ApplicationConstants;
import com.empconn.dto.EmailDto;

@Component
public class EmailUtil {

	private static final Logger logger = LoggerFactory.getLogger(EmailUtil.class);

	@Autowired
	private EmailConfigUtil emailConfigUtil;

	@Autowired
	private MailModelCreator mailModelCreator;

	public Map<String, Object> getMailAttributes(EmailDto emailDto, Map<String, Object> templateModel) {

		final Map<String, Object> mailAttributes = new HashMap<>();

		final EmailConfig templateEmailConfig = emailConfigUtil.getEmailConfig(emailDto.getTemplate());

		final String[] to = getMergedTo(emailDto, templateModel, templateEmailConfig);
		final String[] cc = getMergedCc(emailDto, templateModel, templateEmailConfig);

		final Map<String, Object> mailModelValues = mailModelCreator.getMailModelValues(to, cc);
		mailAttributes.put(ApplicationConstants.MAIL_MODEL_VALUES, mailModelValues);

		if (displayOriginalMailAddressesInMailContent(mailModelValues)) {
			// Set the to and cc values based on the DB record of testing team
			final EmailConfig testingTeamEmailConfig = emailConfigUtil.getEmailConfig(ApplicationConstants.TESTING_TEAM_MAILS);

			mailAttributes.put(ApplicationConstants.FINAL_TO_MAIL_ADDRESS, testingTeamEmailConfig.getTo());
			mailAttributes.put(ApplicationConstants.FINAL_CC_MAIL_ADDRESS, testingTeamEmailConfig.getCc());
		} else {
			// Set the to and cc values computed based on the merged values of email-config, programmatic values and DB records of the current template
			mailAttributes.put(ApplicationConstants.FINAL_TO_MAIL_ADDRESS, to);
			mailAttributes.put(ApplicationConstants.FINAL_CC_MAIL_ADDRESS, cc);
		}

		return mailAttributes;

	}

	private String[] getMergedCc(EmailDto emailDto, Map<String, Object> templateModel, final EmailConfig emailConfig) {
		return getMergedMailList(emailDto.getCc(), templateModel.get(ApplicationConstants.EMAIL_CC),
				emailConfig.getCc(), emailConfig.isShouldAppendMailIds());
	}

	private String[] getMergedTo(EmailDto emailDto, Map<String, Object> templateModel, final EmailConfig emailConfig) {
		return getMergedMailList(emailDto.getTo(), templateModel.get(ApplicationConstants.EMAIL_TO),
				emailConfig.getTo(), emailConfig.isShouldAppendMailIds());
	}

	private boolean displayOriginalMailAddressesInMailContent(final Map<String, Object> mailModelValues) {
		return ApplicationConstants.TRUE
				.equals(mailModelValues.get(ApplicationConstants.DISPLAY_ORIGINAL_MAIL_ADDRESSES_IN_MAIL_CONTENT));
	}

	private String[] getMergedMailList(String[] mailFromDto, final Object mailFromModel, String[] mailIdsFromDB,
			Boolean shouldAppendMailIds) {

		try {
			// if append flag is set to false, mail id from the DB will replace everything
			if (!shouldAppendMailIds)
				return mailIdsFromDB;

			String[] result = new String[] {};
			result = ArrayUtils.addAll(result, mailFromDto);
			result = ArrayUtils.addAll(result, mailIdsFromDB);

			if (null != mailFromModel) {
				result = ArrayUtils.addAll(result, (String[]) mailFromModel);
			}

			return result;
		} catch (final Exception e) {
			logger.error("Exception in merging the mail ids from config and model. Proceeding with the config mails",
					e);
		}
		return mailFromDto;
	}

}
