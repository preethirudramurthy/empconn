package com.empconn.service;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Set;

import org.apache.commons.lang3.time.DateUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import com.empconn.constants.ApplicationConstants;
import com.empconn.dto.ChangeEndDateDto;
import com.empconn.dto.PinStatusChangeCommentDto;
import com.empconn.dto.ProjectEndDateChangeDto;
import com.empconn.dto.ProjectInformationDto;
import com.empconn.email.EmailService;
import com.empconn.enums.ProjectStatus;
import com.empconn.mapper.CommonQualifiedMapper;
import com.empconn.mapper.ProjectDtoMapper;
import com.empconn.persistence.entities.Allocation;
import com.empconn.persistence.entities.Employee;
import com.empconn.persistence.entities.Project;
import com.empconn.persistence.entities.ProjectComment;
import com.empconn.persistence.entities.ProjectLocation;
import com.empconn.repositories.EmployeeRepository;
import com.empconn.utilities.IterableUtils;

@Component
public class PinProjectMailService {

	private static final String NEW_PROJECT_STATUS = "newProjectStatus";

	private static final String OLD_PROJECT_STATUS = "oldProjectStatus";

	private static final String VERTICAL_NAME = "verticalName";

	private static final String END_DATE = "endDate";

	private static final String START_DATE = "startDate";

	private static final String PROJECT_NAME = "projectName";

	private static final Logger logger = LoggerFactory.getLogger(PinProjectMailService.class);

	@Autowired
	private EmailService emailService;

	@Autowired
	private EmployeeRepository employeeRepository;

	@Autowired
	ProjectDtoMapper projectDtoMapper;

	@Autowired
	ProjectService projectService;

	@Value("${link.url.application}")
	private String applicationUrl;

	public void sendEmailForPinStatusChange(Project project, String changeStatus) {
		final List<String> emailToList = new ArrayList<>();
		final List<String> emailCCList = new ArrayList<>();
		Employee pinInitiator = null;
		final Map<String, Object> templateModel = new HashMap<>();
		switch (changeStatus) {
		case "INITIATED":
			if (project.getEmployee1() != null) {
				emailToList.add(project.getEmployee1().getEmail());
				if (project.getEmployee2() != null) {
					emailCCList.add(project.getEmployee2().getEmail());
				}
			} else
				emailToList.add(project.getEmployee2().getEmail());
			templateModel.put(PROJECT_NAME,
					project.getName());
			templateModel.put("link", applicationUrl);
			emailService.send("pin-initiated", templateModel, emailToList.toArray(new String[emailToList.size()]),
					emailCCList.toArray(new String[emailCCList.size()]));
			break;
		case "GDM_REVIEWED":
			pinInitiator = employeeRepository.findByEmployeeId(project.getCreatedBy());
			emailCCList.add(pinInitiator.getEmail());
			templateModel.put(PROJECT_NAME, project.getName());
			emailService.send("pin-reviewed", templateModel, emailToList.toArray(new String[emailToList.size()]),
					emailCCList.toArray(new String[emailCCList.size()]));
			break;
		case "GDM_REJECTED":
			pinInitiator = employeeRepository.findByEmployeeId(project.getCreatedBy());
			emailToList.add(pinInitiator.getEmail());
			templateModel.put(PROJECT_NAME, project.getName());
			final Optional<ProjectComment> projectComment = IterableUtils.toList(project.getProjectComments()).stream()
					.filter(c -> c.getStatus().equals(ProjectStatus.GDM_REJECTED.name()))
					.max(Comparator.comparing(ProjectComment::getCreatedOn));
			if (projectComment.isPresent()) templateModel.put("comment", projectComment.get().getValue());
			emailService.send("pin-rejected-gdm", templateModel, emailToList.toArray(new String[emailToList.size()]),
					emailCCList.toArray(new String[emailCCList.size()]));
			break;
		case "SENT_BACK":
			pinInitiator = employeeRepository.findByEmployeeId(project.getCreatedBy());
			emailToList.add(pinInitiator.getEmail());
			templateModel.put(PROJECT_NAME, project.getName());
			templateModel.put("link", applicationUrl);
			emailService.send("pin-sentback", templateModel, emailToList.toArray(new String[emailToList.size()]),
					emailCCList.toArray(new String[emailCCList.size()]));
			break;
		case "RESUBMITTED":
			if (project.getEmployee1() != null) {
				emailToList.add(project.getEmployee1().getEmail());
				if (project.getEmployee2() != null) {
					emailCCList.add(project.getEmployee2().getEmail());
				}
			} else
				emailToList.add(project.getEmployee2().getEmail());
			templateModel.put(PROJECT_NAME, project.getName());
			templateModel.put("link", applicationUrl);
			emailService.send("pin-resubmit", templateModel, emailToList.toArray(new String[emailToList.size()]),
					emailCCList.toArray(new String[emailCCList.size()]));
			break;
		default:
			break;
		}
	}

	public void mailForRejectPin(Project project, PinStatusChangeCommentDto rejectDto) {
		logger.debug("preparing data to send mail");
		final Map<String, Object> templateModel = new HashMap<>();
		final List<String> emailCCList = getDevGdmOrQaGdm(project);
		Optional<Employee> emOpt = employeeRepository.findById(project.getCreatedBy());
		final String[] emailToList = new String[] {
				emOpt.isPresent()? emOpt.get().getEmail():"" };
		templateModel.put(PROJECT_NAME, project.getName());
		templateModel.put("comments", rejectDto.getComment());
		emailService.send("pin-reject-email", templateModel, emailToList,
				emailCCList.toArray(new String[emailCCList.size()]));
	}

	public void sendEmailForPinApprove(Project project) {
		String templateName = "";
		boolean sendAttachment = false;
		if (!project.getAccount().getCategory().equalsIgnoreCase("Internal")) {
			if (project.getProjectKickoffIsRequired()) {
				templateName = "PIN-approval-external-project-kickOff-yes";
			} else {
				templateName = "PIN-approval-external-project-kickOff-no";
			}
			sendAttachment = true;
		} else {
			if (project.getProjectKickoffIsRequired() && project.getSendNotificationToPinGroup()) {
				templateName = "PIN-approval-internal-project-kickOff-yes-Notification-yes";
				sendAttachment = true;
			} else if (project.getProjectKickoffIsRequired() && !project.getSendNotificationToPinGroup()) {
				templateName = "PIN-approval-internal-project-kickOff-yes-Notification-no";
			} else {
				templateName = "PIN-approval-internal-project-kickOff-no-Notification-no";
			}
		}
		mailForPinApproved(project, templateName);
		if (sendAttachment) {
			final ProjectInformationDto informationDto = projectDtoMapper.projectToProjectInformationDto(project);
			mailForPinApprovedAttachment(project, informationDto);
		}
	}

	private void mailForPinApproved(Project project, String templateName) {
		logger.debug("preparing data to send mail");
		final Map<String, Object> templateModel = new HashMap<>();
		final List<String> toEmailId = getManagerByProjectLocation(project.getProjectLocations());
		final List<String> ccEmailId = getDevGdmOrQaGdm(project);
		templateModel.put(PROJECT_NAME, project.getName());
		templateModel.put("accountName", project.getAccount().getName());
		templateModel.put("GDMName", projectService.getProjectGdm(project).getFullName());
		templateModel.put(START_DATE, new SimpleDateFormat(ApplicationConstants.DATE_FORMAT_DD_MMM_YYYY).format(project.getStartDate()));
		templateModel.put(END_DATE, new SimpleDateFormat(ApplicationConstants.DATE_FORMAT_DD_MMM_YYYY).format(project.getEndDate()));
		templateModel.put("date", new SimpleDateFormat(ApplicationConstants.DATE_FORMAT_DD_MMM_YYYY).format(project.getEndDate()));
		templateModel.put("link", applicationUrl);
		emailService.send(templateName, templateModel, toEmailId.toArray(new String[toEmailId.size()]),
				ccEmailId.toArray(new String[ccEmailId.size()]));
	}

	private void mailForPinApprovedAttachment(Project project, ProjectInformationDto informationDto) {
		final Map<String, Object> templateModel = new HashMap<>();
		final List<String> managerName = getManagerNameByProjectLocation(project.getProjectLocations());
		Optional<Employee> createdOpt = employeeRepository.findById(project.getCreatedBy());
		final Employee employee = createdOpt.isPresent()?createdOpt.get():null;
		if (employee != null)informationDto.setInitiatedBy(employee.getFullName());
		Optional<Employee> approverOpt = employeeRepository.findById(project.getApprovedBy());
		final Employee employeeApproved = approverOpt.isPresent()?approverOpt.get():null;
		if (employeeApproved != null) informationDto.setApprovedBy(employeeApproved.getFullName());
		informationDto.setProjectManagerName(managerName.get(0));
		informationDto
				.setBusinessManagerName(project.getEmployee3() == null ? "" : project.getEmployee3().getFullName());
		String initiationMeetingDate = "-";
		if (project.getProjectKickoffIsRequired()) {
			initiationMeetingDate = new SimpleDateFormat(ApplicationConstants.DATE_FORMAT_DD_MMM_YYYY)
					.format(DateUtils.addDays(project.getStartDate(), 14));
		}
		informationDto.setInitialMeetingDate(initiationMeetingDate);
		templateModel.put("meetingDate", initiationMeetingDate);
		templateModel.put(PROJECT_NAME, project.getName());
		templateModel.put("projectKickoffRequired", project.getProjectKickoffIsRequired());
		templateModel.put("projectInitiator", informationDto.getInitiatedBy());
		templateModel.put("projectManager", informationDto.getProjectManagerName());
		templateModel.put("projectInformation", informationDto);
		emailService.send("pin-approved", templateModel, new String[] {}, new String[] {employee!=null? employee.getEmail():"" });
	}

	public void mailForProjectStatusChangeActive(Project p, String newStatus, String oldStatus) {
		logger.debug("Test Mail");
		final Set<ProjectLocation> pl = p.getProjectLocations();
		final StringBuilder sb = new StringBuilder();
		for (final ProjectLocation prjLoc : pl) {
			final Map<String, Employee> allManagers = prjLoc.getAllManagers();
			for (final Map.Entry<String, Employee> entry : allManagers.entrySet()) {
				sb.append(entry.getValue().getEmail() + ",");
			}
		}
		final Map<String, Object> templateModel = new HashMap<>();
		templateModel.put(PROJECT_NAME, p.getName());
		templateModel.put(VERTICAL_NAME, p.getAccount().getVertical().getName());
		templateModel.put(START_DATE, new SimpleDateFormat(ApplicationConstants.DATE_FORMAT_DD_MMM_YYYY).format(p.getStartDate()));
		templateModel.put(END_DATE, new SimpleDateFormat(ApplicationConstants.DATE_FORMAT_DD_MMM_YYYY).format(p.getEndDate()));
		templateModel.put(OLD_PROJECT_STATUS, oldStatus);
		templateModel.put(NEW_PROJECT_STATUS, newStatus);
		final String[] emailToList = new String[] { sb.toString()};
		final String[] emailCCList = new String[] { p.getEmployee1() == null ? "" : p.getEmployee1().getEmail(),
				p.getEmployee2() == null ? "" : p.getEmployee2().getEmail() };
		emailService.send("project-status-change-active", templateModel, emailToList, emailCCList);
	}

	public void mailForProjectStatusChangeOnHold(Project p, String newStatus, String oldStatus) {
		logger.debug("Test Mail");
		final Set<ProjectLocation> pl = p.getProjectLocations();
		final StringBuilder sb = new StringBuilder();
		for (final ProjectLocation prjLoc : pl) {
			final Map<String, Employee> allManagers = prjLoc.getAllManagers();
			for (final Map.Entry<String, Employee> entry : allManagers.entrySet()) {
				sb.append(entry.getValue().getEmail() + ",");
			}
		}
		final Map<String, Object> templateModel = new HashMap<>();
		templateModel.put(PROJECT_NAME, p.getName());
		templateModel.put(VERTICAL_NAME, p.getAccount().getVertical().getName());
		templateModel.put(START_DATE, new SimpleDateFormat(ApplicationConstants.DATE_FORMAT_DD_MMM_YYYY).format(p.getStartDate()));
		templateModel.put(END_DATE, new SimpleDateFormat(ApplicationConstants.DATE_FORMAT_DD_MMM_YYYY).format(p.getEndDate()));
		templateModel.put(OLD_PROJECT_STATUS, oldStatus);
		templateModel.put(NEW_PROJECT_STATUS, newStatus);
		final String[] emailToList = new String[] {sb.toString() };
		final String[] emailCCList = new String[] { p.getEmployee1() == null ? "" : p.getEmployee1().getEmail(),
				p.getEmployee2() == null ? "" : p.getEmployee2().getEmail() };
		emailService.send("project-status-change-onhold", templateModel, emailToList, emailCCList);
	}

	public void mailForProjectStatusChangeInactive(Project p, String newStatus, String oldStatus) {
		final Set<ProjectLocation> pl = p.getProjectLocations();
		final StringBuilder sb = new StringBuilder();
		for (final ProjectLocation prjLoc : pl) {
			final Map<String, Employee> allManagers = prjLoc.getAllManagers();
			for (final Map.Entry<String, Employee> entry : allManagers.entrySet()) {
				sb.append(entry.getValue().getEmail() + ",");
			}
		}
		final Map<String, Object> templateModel = new HashMap<>();
		templateModel.put(PROJECT_NAME, p.getName());
		templateModel.put(VERTICAL_NAME, p.getAccount().getVertical().getName());
		templateModel.put(START_DATE, new SimpleDateFormat(ApplicationConstants.DATE_FORMAT_DD_MMM_YYYY).format(p.getStartDate()));
		templateModel.put(END_DATE, p.getEndDate().toString());
		templateModel.put(OLD_PROJECT_STATUS, oldStatus);
		templateModel.put(NEW_PROJECT_STATUS, newStatus);
		final String[] emailToList = new String[] {sb.toString()};
		final String[] emailCCList = new String[] { p.getEmployee1() == null ? "" : p.getEmployee1().getEmail(),
				p.getEmployee2() == null ? "" : p.getEmployee2().getEmail() };
		emailService.send("project-status-change-inactive", templateModel, emailToList, emailCCList);
	}

	public void mailForProjectDetailsChange(Project p) {
		logger.debug("Test Mail for mailForProjectDetailsChange");
		final Set<ProjectLocation> pl = p.getProjectLocations();
		final StringBuilder sb = new StringBuilder();
		for (final ProjectLocation prjLoc : pl) {
			final Map<String, Employee> allManagers = prjLoc.getAllManagers();
			for (final Map.Entry<String, Employee> entry : allManagers.entrySet()) {
				sb.append(entry.getValue().getEmail() + ",");
			}
		}
		final Map<String, Object> templateModel = new HashMap<>();
		templateModel.put(PROJECT_NAME, p.getName());
		templateModel.put("technology", p.getTechnology());
		templateModel.put("os", p.getOperatingSystem());
		templateModel.put("db", p.getDatabase());
		templateModel.put("description", p.getDescription());
		templateModel.put("link", applicationUrl);
		final String[] emailToList = new String[] {sb.toString()};
		final String[] emailCCList = new String[] { p.getEmployee1() == null ? "" : p.getEmployee1().getEmail(),
				p.getEmployee2() == null ? "" : p.getEmployee2().getEmail() };
		emailService.send("project-details-change", templateModel, emailToList, emailCCList);
	}

	public void mailForProjectEndDateChange(Project p, Set<Allocation> allocation, ProjectEndDateChangeDto dto) {
		logger.debug("Test Mail for mailForProjectEndDateChange");
		final Set<ProjectLocation> pl = p.getProjectLocations();
		final StringBuilder sb = new StringBuilder();
		for (final ProjectLocation prjLoc : pl) {
			final Map<String, Employee> allManagers = prjLoc.getAllManagers();
			for (final Map.Entry<String, Employee> entry : allManagers.entrySet()) {
				sb.append(entry.getValue().getEmail() + ",");
			}
		}
		final Set<ChangeEndDateDto> dtoSet = new HashSet<>();
		for (final Allocation a : allocation) {
			final ChangeEndDateDto changeEndDateDto = new ChangeEndDateDto();
			changeEndDateDto.setEmpCode(a.getEmployee().getEmpCode());
			changeEndDateDto.setEmpName(a.getEmployee().getFirstName() + " " + a.getEmployee().getLastName());
			changeEndDateDto.setOriginalDate(new SimpleDateFormat(ApplicationConstants.DATE_FORMAT_DD_MMM_YYYY).format(a.getReleaseDate()));
			changeEndDateDto.setReleaseDate(new SimpleDateFormat(ApplicationConstants.DATE_FORMAT_DD_MMM_YYYY)
					.format(CommonQualifiedMapper.localDateToDate(dto.getEndDate())));
			dtoSet.add(changeEndDateDto);
		}
		final Map<String, Object> templateModel = new HashMap<>();
		templateModel.put("originalDate", new SimpleDateFormat(ApplicationConstants.DATE_FORMAT_DD_MMM_YYYY).format(p.getEndDate()));
		templateModel.put("newDate",
				new SimpleDateFormat(ApplicationConstants.DATE_FORMAT_DD_MMM_YYYY).format(CommonQualifiedMapper.localDateToDate(dto.getEndDate())));
		templateModel.put(PROJECT_NAME, p.getName());
		templateModel.put("allocation", dtoSet);
		templateModel.put("link", applicationUrl);
		final String[] emailToList = new String[] {sb.toString()};
		final String[] emailCCList = new String[] { p.getEmployee1() == null ? "" : p.getEmployee1().getEmail(),
				p.getEmployee2() == null ? "" : p.getEmployee2().getEmail() };
		emailService.send("project-endDate-change", templateModel, emailToList, emailCCList);
	}

	public void mailForProjectManagerChange(Project p, Employee manager, ProjectLocation projectLocation,
			String workGroup) {
		logger.debug("Test Mail for mailForProjectManagerChange");
		final Map<String, Object> templateModel = new HashMap<>();
		templateModel.put("managerName", manager.getFullName());
		templateModel.put(START_DATE, new SimpleDateFormat(ApplicationConstants.DATE_FORMAT_DD_MMM_YYYY).format(new Date()));
		templateModel.put(PROJECT_NAME, p.getName());
		templateModel.put("accountName", p.getAccount().getName());
		templateModel.put("location", projectLocation.getLocation().getName());
		templateModel.put("workgroup", workGroup);

		final String[] emailToList = new String[] { manager.getEmail() };
		final String[] emailCCList = new String[] {};
		emailService.send("project-manager-change", templateModel, emailToList, emailCCList);

	}

	private List<String> getDevGdmOrQaGdm(Project project) {
		final List<String> emailId = new ArrayList<>();
		if (project.getEmployee2() != null)
			emailId.add(project.getEmployee2().getEmail());
		if (project.getEmployee1() != null)
			emailId.add(project.getEmployee1().getEmail());
		return emailId;
	}

	private List<String> getManagerByProjectLocation(Set<ProjectLocation> projectLocation) {
		final List<String> emailId = new ArrayList<>();
		projectLocation.forEach(e -> {
			if (e.getIsActive()) {
				if (e.getEmployee1() != null)
					emailId.add(e.getEmployee1().getEmail());
				if (e.getEmployee2() != null)
					emailId.add(e.getEmployee2().getEmail());
				if (e.getEmployee3() != null)
					emailId.add(e.getEmployee3().getEmail());
				if (e.getEmployee4() != null)
					emailId.add(e.getEmployee4().getEmail());
			}
		});
		return emailId;
	}

	private List<String> getManagerNameByProjectLocation(Set<ProjectLocation> projectLocation) {
		final List<String> name = new ArrayList<>();
		projectLocation.forEach(e -> {
			if (e.getIsActive()) {
				if (e.getEmployee1() != null)
					name.add(e.getEmployee1().getFullName());
				if (e.getEmployee2() != null)
					name.add(e.getEmployee2().getFullName());
				if (e.getEmployee3() != null)
					name.add(e.getEmployee3().getFullName());
				if (e.getEmployee4() != null)
					name.add(e.getEmployee4().getFullName());
				if (e.getEmployee5() != null)
					name.add(e.getEmployee5().getFullName());
			}
		});
		return name;
	}

}
