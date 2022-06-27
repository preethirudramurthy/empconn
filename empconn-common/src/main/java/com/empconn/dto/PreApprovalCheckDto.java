package com.empconn.dto;

import java.time.LocalDateTime;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.datatype.jsr310.deser.LocalDateTimeDeserializer;
import com.fasterxml.jackson.datatype.jsr310.ser.LocalDateTimeSerializer;

public class PreApprovalCheckDto {

	@JsonProperty
	private boolean kickOffMeetingRequired;
	@JsonProperty
	private boolean sendNotificationToPinGroup;

	@JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd'T'HH:mm:ss.SSS'Z'")
	@JsonDeserialize(using = LocalDateTimeDeserializer.class)
	@JsonSerialize(using = LocalDateTimeSerializer.class)
	private LocalDateTime initiationMeetingDeadline;
	private String accountTOKLink;
	private String projectTOKLink;

	public PreApprovalCheckDto() {
		super();
	}

	public PreApprovalCheckDto(boolean kickOffMeetingRequired, boolean sendNotificationToPinGroup,
			LocalDateTime initiationMeetingDeadline, String accountTOKLink, String projectTOKLink) {
		super();
		this.kickOffMeetingRequired = kickOffMeetingRequired;
		this.sendNotificationToPinGroup = sendNotificationToPinGroup;
		this.initiationMeetingDeadline = initiationMeetingDeadline;
		this.accountTOKLink = accountTOKLink;
		this.projectTOKLink = projectTOKLink;
	}

	public boolean isKickOffMeetingRequired() {
		return kickOffMeetingRequired;
	}

	public void setKickOffMeetingRequired(boolean kickOffMeetingRequired) {
		this.kickOffMeetingRequired = kickOffMeetingRequired;
	}

	public boolean isSendNotificationToPinGroup() {
		return sendNotificationToPinGroup;
	}

	public void setSendNotificationToPinGroup(boolean sendNotificationToPinGroup) {
		this.sendNotificationToPinGroup = sendNotificationToPinGroup;
	}

	public LocalDateTime getInitiationMeetingDeadline() {
		return initiationMeetingDeadline;
	}

	public void setInitiationMeetingDeadline(LocalDateTime initiationMeetingDeadline) {
		this.initiationMeetingDeadline = initiationMeetingDeadline;
	}

	public String getAccountTOKLink() {
		return accountTOKLink;
	}

	public void setAccountTOKLink(String accountTOKLink) {
		this.accountTOKLink = accountTOKLink;
	}

	public String getProjectTOKLink() {
		return projectTOKLink;
	}

	public void setProjectTOKLink(String projectTOKLink) {
		this.projectTOKLink = projectTOKLink;
	}

}
