package com.empconn.email;

import org.springframework.context.MessageSource;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.support.ReloadableResourceBundleMessageSource;

@Configuration
public class ThymeleafConfiguration {

	@Bean
	public MessageSource messageSource() {
		final ReloadableResourceBundleMessageSource messageSource = new ReloadableResourceBundleMessageSource();
		messageSource.setBasenames("classpath:/messages/messages", "classpath:/messages/pin-approved-messages",
				"classpath:/messages/mail-health-check-messages", "classpath:/messages/pin-reject-email",
				"classpath:/messages/project-status-change-active-message",
				"classpath:/messages/project-status-change-inactive-message",
				"classpath:/messages/project-status-change-onhold-message",
				"classpath:/messages/PIN-approval-external-project-kickOff-no",
				"classpath:/messages/PIN-approval-external-project-kickOff-yes",
				"classpath:/messages/PIN-approval-internal-project-kickOff-no-Notification-no",
				"classpath:/messages/PIN-approval-internal-project-kickOff-yes-Notification-no",
				"classpath:/messages/PIN-approval-internal-project-kickOff-yes-Notification-yes",
				"classpath:/messages/pin-approved-attachment-html",
				"classpath:/messages/resource-earmark-project-messages",
				"classpath:/messages/resource-un-earmark-message", "classpath:/messages/pin-initiated-messages",
				"classpath:/messages/pin-reviewed-messages", "classpath:/messages/pin-rejected-gdm-messages",
				"classpath:/messages/pin-sentback-messages", "classpath:/messages/pin-resubmit-messages",
				"classpath:/messages/pin-sentback-messages", "classpath:/messages/pin-resubmit-messages",
				"classpath:/messages/switch-over-to-message", "classpath:/messages/partial-switch-over-message",
				"classpath:/messages/switch-over-from-message",
				"classpath:/messages/pin-sentback-messages", "classpath:/messages/pin-resubmit-messages",
				"classpath:/messages/project-details-change-message","classpath:/messages/project-endDate-change-message",
				"classpath:/messages/allocate-earmarked-resource-messages",
				"classpath:/messages/allocate-earmarked-resource-unearmarked-messages",
				"classpath:/messages/nd-request-allocation-message", "classpath:/messages/project-manager-change-message",
				"classpath:/messages/allocate-requested-nd-resource-message",
				"classpath:/messages/request-for-allocation-cancel-request-message",
				"classpath:/messages/auto-cancel-allocation-for-request-message",
				"classpath:/messages/complete-deallocation-message",
				"classpath:/messages/partial-deallocation-message",
				"classpath:/messages/change-manager-for-project-message",
				"classpath:/messages/change-gdm-for-project-message",
				"classpath:/messages/change-primary-manager-of-resource-message",
				"classpath:/messages/change-reporting-manager-of-resource-message",
				"classpath:/messages/allocation-release-date-email-notification-message",
				"classpath:/messages/allocation-release-date-past-email-notification-message"

				);
		messageSource.setUseCodeAsDefaultMessage(true);
		messageSource.setDefaultEncoding("UTF-8");
		messageSource.setCacheSeconds(5);
		return messageSource;
	}
}
