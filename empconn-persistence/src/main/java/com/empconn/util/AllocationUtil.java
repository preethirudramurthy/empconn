package com.empconn.util;

import com.empconn.persistence.entities.Allocation;

public class AllocationUtil {

	public static boolean allocationIsActiveAndPrimary(Allocation allocation) {
		return allocation.getIsActive() && allocation == allocation.getEmployee().getPrimaryAllocation();
	}

}
