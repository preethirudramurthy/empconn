package com.empconn.email;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Component;

@Component
public class EmailComponent {

	@Autowired
	private JavaMailSender javaMailSender;

	@Value("${no.reply.address}")
	private String fromEmail;

	public void sendEmail(String from, String to, String subject, String text) {

		final SimpleMailMessage msg = new SimpleMailMessage();
		msg.setFrom(fromEmail);
		msg.setTo(to);

		msg.setSubject(subject);
		msg.setText(text);

		javaMailSender.send(msg);

	}

	public void sendMail(String from, String[] to, String subject, String msg) {
		final SimpleMailMessage message = new SimpleMailMessage();
		message.setFrom(fromEmail);
		message.setTo(to);
		message.setSubject(subject);
		message.setText(msg);
		javaMailSender.send(message);
	}

}
