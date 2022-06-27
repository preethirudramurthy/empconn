package com.empconn.persistence.entities.listeners;

import javax.persistence.PrePersist;
import javax.persistence.PreUpdate;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.empconn.activedirectory.ActiveDirectoryDeltaUpdateService;
import com.empconn.constants.ApplicationConstants;
import com.empconn.persistence.entities.Allocation;
import com.empconn.util.AllocationUtil;
import com.empconn.utilities.BeanUtil;
import com.empconn.utilities.CommonUtil;;

public class AllocationListener {

	private static final Logger logger = LoggerFactory.getLogger(AllocationListener.class);

	private ActiveDirectoryDeltaUpdateService activeDirectoryDeltaUpdateService;

	public void setActiveDirectoryDeltaUpdateService(ActiveDirectoryDeltaUpdateService activeDirectoryDeltaUpdateService) {
		this.activeDirectoryDeltaUpdateService = activeDirectoryDeltaUpdateService;
	}

	public AllocationListener() {
		super();
	}

	@PrePersist
	@PreUpdate
	void invokePreAllocationEvents(Allocation allocation) {

		logger.debug("Trigger allocation related events");
		this.setActiveDirectoryDeltaUpdateService(BeanUtil.getBean(ActiveDirectoryDeltaUpdateService.class));

		if(AllocationUtil.allocationIsActiveAndPrimary(allocation)) {
			logger.debug("Allocation is active and primary and hence proceeding further with the delta update");
			activeDirectoryDeltaUpdateService.update(employeeLoginMailId(allocation), managerLoginMailId(allocation), projectName(allocation));
		}

		logger.debug("Initiated allocation related events");

	}

	private String projectName(Allocation allocation) {
		if(allocation.getProject().getName().equals(ApplicationConstants.NON_DELIVERY_BENCH_PROJECT_NAME)) {
			return allocation.getEmployee().getDepartment().getName();
		}else {
			return allocation.getProject().getName();
		}
	}

	private String managerLoginMailId(Allocation allocation) {
		return CommonUtil.loginIdToMailId(allocation.getEmployee().getPrimaryAllocation().getReportingManagerId().getLoginId());
	}

	private String employeeLoginMailId(Allocation allocation) {
		return CommonUtil.loginIdToMailId(allocation.getEmployee().getLoginId());
	}

	private Long allocationId(Allocation allocation) {
		return allocation.getAllocationId();
	}

}
