package com.empconn.dto;

import java.io.Serializable;

public class EmailDto implements Serializable {

	private static final long serialVersionUID = 1L;

	private String[] to;
	private String[] cc;
	private String subject;
	private String subjectTemplateContent;
	private String template;
	private String layout;
	private String attachmentTemplate;
	private String attachmentLayout;
	private String attachmentFileName;

	public EmailDto() {
		super();
	}

	public EmailDto(String[] to, String[] cc, String subject, String subjectTemplateContent, String template, String layout, String attachmentTemplate, String attachmentLayout, String attachmentFileName) {
		super();
		this.to = getNotNullArray(to);
		this.cc = getNotNullArray(cc);
		this.subject = subject;
		this.subjectTemplateContent = subjectTemplateContent;
		this.template = template;
		this.layout = layout;
		this.attachmentTemplate = attachmentTemplate;
		this.attachmentLayout = attachmentLayout;
		this.attachmentFileName = attachmentFileName;
	}

	public EmailDto(String to, String cc, String subject, String subjectTemplateContent, String template, String layout, String attachmentTemplate, String attachmentLayout, String attachmentFileName) {
		super();
		this.to = getNotNullArray(to);
		this.cc = getNotNullArray(cc);
		this.subject = subject;
		this.subjectTemplateContent = subjectTemplateContent;
		this.template = template;
		this.layout = layout;
		this.attachmentTemplate = attachmentTemplate;
		this.attachmentLayout = attachmentLayout;
		this.attachmentFileName = attachmentFileName;
	}

	public String getAttachmentLayout() {
		return attachmentLayout;
	}

	public String getAttachmentFileName() {
		return attachmentFileName;
	}

	public String getAttachmentTemplate() {
		return attachmentTemplate;
	}

	public String[] getTo() {
		return to;
	}

	public String[] getCc() {
		return cc;
	}

	public String getSubject() {
		return subject;
	}

	public String getTemplate() {
		return template;
	}

	public String getLayout() {
		return layout;
	}

	public void setTo(String[] to) {
		this.to = to;
	}

	public void setCc(String[] cc) {
		this.cc = cc;
	}

	public void setSubject(String subject) {
		this.subject = subject;
	}

	public String getSubjectTemplateContent() {
		return subjectTemplateContent;
	}

	private String[] getNotNullArray(String[] input) {
		if (null == input)
			return new String[] {};
		return input;
	}

	private String[] getNotNullArray(String input) {
		if (null == input)
			return new String[] {};
		return new String[] { input };
	}

}
