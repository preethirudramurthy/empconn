package com.empconn.email;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

import com.empconn.persistence.entities.EmailConfiguration;
import com.empconn.repositories.EmailConfigurationRepository;

@Component
public class EmailConfigUtil {

	@Autowired
	private EmailConfigurationRepository emailConfigurationRepository;

	public EmailConfig getEmailConfig(String template) {
		final EmailConfiguration emailConfiguration = emailConfigurationRepository.findFirstByNameIgnoreCase(template);

		String[] to = new String[] {};
		String[] cc = new String[] {};
		boolean appendMailIds = true;

		if (null != emailConfiguration) {
			final String ccMailIds = emailConfiguration.getCcMailIds();
			final String toMailIds = emailConfiguration.getToMailIds();
			if (!StringUtils.isEmpty(toMailIds))
				to = com.empconn.email.StringUtils.toArray(toMailIds);
			if (!StringUtils.isEmpty(ccMailIds))
				cc = com.empconn.email.StringUtils.toArray(ccMailIds);
			appendMailIds = emailConfiguration.getAppendMailIds();
		}
		return new EmailConfig(to, cc, appendMailIds);
	}

}