package com.empconn.service;

import java.time.LocalDate;
import java.util.Map;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.empconn.email.EmailService;
import com.empconn.email.SwitchOverEmailUtil;
import com.empconn.persistence.entities.Allocation;

@Service
public class SwithOverEmailService {

	@Autowired
	private EmailService emailService;

	@Autowired
	private MasterService masterService;

	@Autowired
	private SwitchOverEmailUtil switchOverEmailUtil;

	public void mailForSwitchOverTo(Allocation a, LocalDate dateOfMovement) {
		final Set<String> locationHr = masterService
				.getLocationHr(a.getEmployee().getLocation().getLocationId());
		final Map<String, Object> templateModel = switchOverEmailUtil.computeSwitchOverToTemplate(a, dateOfMovement);
		emailService.send("switch-over-to", templateModel, switchOverEmailUtil.commonToList(a),
				switchOverEmailUtil.commonCCList(a, locationHr));
	}

	public void mailForSwitchOverFrom(Allocation a, LocalDate dateOfMovement) {
		final Set<String> locationHr = masterService
				.getLocationHr(a.getEmployee().getLocation().getLocationId());
		final Map<String, Object> templateModel = switchOverEmailUtil.computeSwitchOverFromTemplate(a, dateOfMovement);
		emailService.send("switch-over-from", templateModel, switchOverEmailUtil.commonToList(a),
				switchOverEmailUtil.commonCCList(a, locationHr));
	}

	public void mailForPartialSwitchOver(Allocation a, LocalDate dateOfMovement, Allocation newAllocation) {
		final Set<String> locationHr = masterService
				.getLocationHr(a.getEmployee().getLocation().getLocationId());
		final Map<String, Object> templateModel = switchOverEmailUtil.computePartialSwitchOverTemplate(a,
				dateOfMovement,newAllocation);
		emailService.send("partial-switch-over", templateModel, switchOverEmailUtil.commonToList(a),
				switchOverEmailUtil.commonCCList(a, locationHr));
	}

}
