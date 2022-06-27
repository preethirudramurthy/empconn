package com.empconn.email;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Map;

import javax.activation.DataSource;
import javax.mail.MessagingException;
import javax.mail.internet.MimeMessage;
import javax.mail.util.ByteArrayDataSource;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import com.empconn.constants.ApplicationConstants;
import com.empconn.dto.EmailDto;
import com.empconn.utilities.EmailConfigReader;
import com.empconn.utilities.StringTemplateEngine;

import io.jsonwebtoken.lang.Assert;

@Service("EmailService")
public class EmailServiceImpl implements EmailService {

	private static final Logger logger = LoggerFactory.getLogger(EmailServiceImpl.class);

	private static final String EMPCONN_LOGO_PATH = "images/cranium-logo.png";

	private static final String EMPCONN_LOGO = "cranium-logo";

	@Value("${no.reply.address}")
	private String noReplyAddress;

	@Autowired
	private EmailUtil emailUtil;

	@Autowired
	private JavaMailSender emailSender;

	@Autowired
	private EmailSenderService emailSenderService;

	@Autowired
	private TemplateEngine templateEngine;

	@Autowired
	private StringTemplateEngine stringTemplateEngine;

	@Autowired
	private EmailConfigReader emailConfigReader;

	@Value("classpath:images/cranium-logo.png")
	private Resource resourceFile;

	@Value("${spring.profiles.active:Local}")
	private String activeProfile;

	@SuppressWarnings("unchecked")
	public void sendMessageUsingThymeleafTemplate(EmailDto emailDto, Map<String, Object> templateModel) {

		try {

			final Map<String, Object> mailAttributes = emailUtil.getMailAttributes(emailDto, templateModel);
			templateModel.putAll((Map<String, Object>) mailAttributes.get(ApplicationConstants.MAIL_MODEL_VALUES));

			emailDto.setTo((String[]) mailAttributes.get(ApplicationConstants.FINAL_TO_MAIL_ADDRESS));
			emailDto.setCc((String[]) mailAttributes.get(ApplicationConstants.FINAL_CC_MAIL_ADDRESS));
			emailDto.setSubject(getRenderedSubject(emailDto, templateModel));

			final String mailContent = templateEngine.render(emailDto.getLayout(), templateModel);
			final String attachmentContent = templateEngine.render(emailDto.getAttachmentLayout(), templateModel);
			sendHtmlMessage(emailDto, mailContent, attachmentContent);

		} catch (final Exception e) {
			logger.error("Exception in sending mail", e);
		}
	}

	private String getRenderedSubject(EmailDto emailDto, Map<String, Object> templateModel) {
		if (!StringUtils.isEmpty(emailDto.getSubjectTemplateContent())) {
			return stringTemplateEngine.render(emailDto.getSubjectTemplateContent(), templateModel);
		}
		return emailDto.getSubject();
	}

	private void sendHtmlMessage(EmailDto emailDto, String mailContent, String attachmentContent)
			throws MessagingException, IOException {

		final MimeMessage message = emailSender.createMimeMessage();
		final MimeMessageHelper helper = new MimeMessageHelper(message, true, "UTF-8");
		helper.setFrom(noReplyAddress);
		helper.setTo(emailDto.getTo());
		helper.setCc(emailDto.getCc());
		helper.setSubject(emailDto.getSubject());
		helper.setText(mailContent, true);
		helper.addInline(EMPCONN_LOGO, new ClassPathResource(EMPCONN_LOGO_PATH));

		if (!StringUtils.isEmpty(attachmentContent)) {
			final ByteArrayOutputStream document = MailAttachmentUtil.createInMemoryDocument(attachmentContent);
			final InputStream inputStream = new ByteArrayInputStream(document.toByteArray());
			final DataSource attachment = new ByteArrayDataSource(inputStream,
					ApplicationConstants.APPLICATION_OCTET_STREAM);
			helper.addAttachment(emailDto.getAttachmentFileName(), attachment);
		}

		emailSenderService.send(message);

	}

	@Override
	public void send(String templateName, Map<String, Object> templateModel, String[] to, String[] cc) {
		Assert.notNull(templateModel);

		final EmailDto emailDto = emailConfigReader.getEmailDto(templateName);
		addEnvironment(templateModel);
		templateModel.put(ApplicationConstants.CONTENT_TEMPLATE,
				(StringUtils.isEmpty(emailDto.getTemplate())) ? templateName : emailDto.getTemplate());
		templateModel.put(ApplicationConstants.ATTACHMENT_TEMPLATE, emailDto.getAttachmentTemplate());
		templateModel.put(ApplicationConstants.EMAIL_TO, to);
		templateModel.put(ApplicationConstants.EMAIL_CC, cc);

		sendMessageUsingThymeleafTemplate(emailDto, templateModel);

	}

	@Override
	public void send(String templateName, Map<String, Object> templateModel) {
		addEnvironment(templateModel);
		send(templateName, templateModel, new String[] {}, new String[] {});
	}

	private void addEnvironment(Map<String, Object> templateModel) {
		if(!activeProfile.toLowerCase().contains(ApplicationConstants.PROFILE_PROD))
			templateModel.put(ApplicationConstants.ENV_NAME, activeProfile+" - ");
		else{
			templateModel.put(ApplicationConstants.ENV_NAME, "");
		}
	}

}
