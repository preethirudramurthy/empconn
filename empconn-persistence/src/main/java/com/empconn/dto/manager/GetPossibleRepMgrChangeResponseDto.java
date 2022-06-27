package com.empconn.dto.manager;

public class GetPossibleRepMgrChangeResponseDto {

	public GetPossibleRepMgrChangeResponseDto(Long gdmId, Long allocationId, String empName, String projectLocation,
			String workgroup, String reportingMangerName) {
		super();
		this.gdmId = gdmId;
		this.allocationId = allocationId;
		this.empName = empName;
		this.projectLocation = projectLocation;
		this.workgroup = workgroup;
		this.reportingMangerName = reportingMangerName;
	}
	private Long gdmId;
	private Long allocationId;
	private String empName;
	private String projectLocation;
	private String workgroup;
	private String reportingMangerName;
	public Long getGdmId() {
		return gdmId;
	}
	public void setGdmId(Long gdmId) {
		this.gdmId = gdmId;
	}
	public Long getAllocationId() {
		return allocationId;
	}
	public void setAllocationId(Long allocationId) {
		this.allocationId = allocationId;
	}
	public String getEmpName() {
		return empName;
	}
	public void setEmpName(String empName) {
		this.empName = empName;
	}
	public String getProjectLocation() {
		return projectLocation;
	}
	public void setProjectLocation(String projectLocation) {
		this.projectLocation = projectLocation;
	}
	public String getWorkgroup() {
		return workgroup;
	}
	public void setWorkgroup(String workgroup) {
		this.workgroup = workgroup;
	}
	public String getReportingMangerName() {
		return reportingMangerName;
	}
	public void setReportingMangerName(String reportingMangerName) {
		this.reportingMangerName = reportingMangerName;
	}



}
