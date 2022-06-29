package com.empconn.email;

import java.text.SimpleDateFormat;
import java.util.HashMap;
import java.util.Map;

import org.springframework.stereotype.Component;

import com.empconn.constants.ApplicationConstants;
import com.empconn.dto.deallocation.DeallocateDto;
import com.empconn.persistence.entities.Allocation;
import com.empconn.persistence.entities.AllocationDetail;
import com.empconn.utilities.TimeUtils;

@Component
public class DeallocationEmailUtil {

	public Map<String, Object> mailForPartialDeallocation(Allocation a, DeallocateDto request, Allocation oldAllocation) {
		final Map<String, Object> templateModel = commonParameters(a);
		templateModel.put("remainingPercentage", oldAllocation.getAllocationDetails().stream().filter(AllocationDetail::getIsActive)
				.map(AllocationDetail::getAllocatedPercentage).reduce(0, Integer::sum));
		templateModel.put("deallocatedPercentage", request.getPercentage());
		templateModel.put("dateOfDeallocation", new SimpleDateFormat(ApplicationConstants.DATE_FORMAT_DD_MMM_YYYY).format(TimeUtils.getToday()));
		templateModel.put("projectName", a.getProject().getName());
		return templateModel;

	}

	public Map<String, Object> mailForCompleteDeallocation(Allocation a) {
		final Map<String, Object> templateModel = commonParameters(a);

		templateModel.put("oldProjectName", a.getProject().getName());
		templateModel.put("dateOfDeallocation", new SimpleDateFormat(ApplicationConstants.DATE_FORMAT_DD_MMM_YYYY).format(TimeUtils.getToday()));
		templateModel.put("oldManagerName",
				a.getReportingManagerId().getFirstName() + " " + a.getReportingManagerId().getLastName());
		templateModel.put("oldManagerId", a.getReportingManagerId().getEmpCode());
		return templateModel;

	}

	private Map<String, Object> commonParameters(Allocation a) {
		final Map<String, Object> templateModel = new HashMap<>();
		templateModel.put("empName", a.getEmployee().getFullName());
		templateModel.put("empId", a.getEmployee().getEmpCode());
		templateModel.put("empTitle", a.getEmployee().getTitle().getName());
		return templateModel;
	}

}
