package com.empconn.email;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

import org.springframework.stereotype.Component;

import com.empconn.persistence.entities.Allocation;
import com.empconn.persistence.entities.AllocationDetail;
import com.empconn.persistence.entities.Auditable;

@Component
public class SwitchOverEmailUtil {

	private static final String OLD_PROJECT_NAME = "oldProjectName";

	public Map<String, Object> computeSwitchOverToTemplate(Allocation a, LocalDate dateOfMovement) {
		final Map<String, Object> templateModel = commonParameters(a, dateOfMovement);
		templateModel.put("newProjectName", a.getProject().getName());
		templateModel.put("projectName", a.getProject().getName());
		templateModel.put("managerName",
				a.getReportingManagerId().getFirstName() + " " + a.getReportingManagerId().getLastName());
		templateModel.put("managerId", a.getReportingManagerId().getEmpCode());

		return templateModel;

	}

	public Map<String, Object> computePartialSwitchOverTemplate(Allocation a, LocalDate dateOfMovement, Allocation newAllocation) {
		final Map<String, Object> templateModel = commonParameters(a, dateOfMovement);
		templateModel.put("existingProject", a.getProject().getName());
		templateModel.put("existingProjectPercentage", a.getAllocationDetails().stream().filter(Auditable::getIsActive)
				.map(AllocationDetail::getAllocatedPercentage).reduce(0, Integer::sum));
		templateModel.put("newProject", newAllocation.getProject().getName());
		templateModel.put("newProjectPercentage", newAllocation.getAllocationDetails().stream().filter(Auditable::getIsActive)
				.map(AllocationDetail::getAllocatedPercentage).reduce(0, Integer::sum));
		return templateModel;

	}

	public Map<String, Object> computeSwitchOverFromTemplate(Allocation a, LocalDate dateOfMovement) {
		final Map<String, Object> templateModel = commonParameters(a, dateOfMovement);
		templateModel.put(OLD_PROJECT_NAME, a.getProject().getName());
		templateModel.put("oldManagerName",
				a.getReportingManagerId().getFirstName() + " " + a.getReportingManagerId().getLastName());
		templateModel.put("oldManagerId", a.getReportingManagerId().getEmpCode());

		return templateModel;

	}

	private Map<String, Object> commonParameters(Allocation a, LocalDate dateOfMovement) {
		final Map<String, Object> templateModel = new HashMap<>();
		templateModel.put("empName", a.getEmployee().getFirstName() + " " + a.getEmployee().getLastName());
		templateModel.put("empId", a.getEmployee().getEmpCode());
		templateModel.put("empTitle", a.getEmployee().getTitle().getName());
		templateModel.put("allocationPercentage", a.getAllocationDetails().stream().filter(Auditable::getIsActive)
				.map(AllocationDetail::getAllocatedPercentage).reduce(0, Integer::sum));
		templateModel.put("dateOfMovement", dateOfMovement.format(DateTimeFormatter.ofPattern("dd-MMM-yy")));
		return templateModel;
	}

	public String[] commonToList(Allocation a) {
		return new String[] { a.getEmployee() == null ? "" : a.getEmployee().getEmail(),
				a.getReportingManagerId() == null ? "" : a.getReportingManagerId().getEmail(),
						(a.getAllocationManagerId() == null || a.getAllocationManagerId().equals(a.getReportingManagerId())) ? "" : a.getAllocationManagerId().getEmail()};

	}

	public String[] commonCCList(Allocation a, Set<String> locationHr) {
		return new String[] { a.getProject().getEmployee1() == null ? "" : a.getProject().getEmployee1().getEmail(),
				a.getProject().getEmployee2() == null ? "" : a.getProject().getEmployee2().getEmail(),
						String.join(",", locationHr) };

	}
}
