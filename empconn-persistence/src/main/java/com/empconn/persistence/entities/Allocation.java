package com.empconn.persistence.entities;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EntityListeners;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQuery;
import javax.persistence.OneToMany;
import javax.persistence.SequenceGenerator;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

import org.apache.commons.collections4.CollectionUtils;
import org.hibernate.annotations.Fetch;
import org.hibernate.annotations.FetchMode;

import com.empconn.persistence.entities.listeners.AllocationListener;
import com.empconn.utilities.TimeUtils;

/**
 * The persistent class for the allocation database table.
 *
 */
@EntityListeners(AllocationListener.class)
@Entity
@NamedQuery(name = "Allocation.findAll", query = "SELECT a FROM Allocation a")
public class Allocation extends Auditable<Long> implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	@SequenceGenerator(name = "ALLOCATION_ALLOCATIONID_GENERATOR", sequenceName = "ALLOCATION_ID_SEQUENCE", allocationSize = 1)
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "ALLOCATION_ALLOCATIONID_GENERATOR")
	@Column(name = "allocation_id")
	private Long allocationId;

	@Column(name = "is_billable")
	private Boolean isBillable;

	@Temporal(TemporalType.DATE)
	@Column(name = "release_date")
	private Date releaseDate;

	// bi-directional many-to-one association to AllocationStatus
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "allocation_status_id")
	private AllocationStatus allocationStatus;

	// bi-directional many-to-one association to Employee
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "employee_id")
	private Employee employee;

	// bi-directional many-to-one association to Employee
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "reporting_manager_id")
	private Employee reportingManagerId;

	// bi-directional many-to-one association to Employee
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "allocation_manager_id")
	private Employee allocationManagerId;

	// bi-directional many-to-one association to Project
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "project_id")
	private Project project;

	// bi-directional many-to-one association to ProjectLocation
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "project_location_id")
	private ProjectLocation projectLocation;

	// bi-directional many-to-one association to WorkGroup
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "work_group_id")
	private WorkGroup workGroup;

	//bi-directional many-to-one association to Allocation
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name="timesheet_allocation_id")
	private Allocation timesheetAllocation;

	//bi-directional many-to-one association to Allocation
	@OneToMany(mappedBy="timesheetAllocation")
	private List<Allocation> timesheetAllocations;

	// bi-directional many-to-one association to EmployeeRole
	@OneToMany(mappedBy = "allocation", cascade = CascadeType.ALL)
	@Fetch(FetchMode.SUBSELECT)
	private List<AllocationDetail> allocationDetails;

	// bi-directional many-to-one association to Earmark
	@OneToMany(mappedBy = "allocation", cascade = CascadeType.ALL)
	@Fetch(FetchMode.SUBSELECT)
	private Set<Earmark> earmarks;

	// bi-directional many-to-one association to Earmark
	@OneToMany(mappedBy = "primaryAllocation")
	private Set<Employee> employees;

	// bi-directional many-to-one association to AllocationFeedback
	@OneToMany(mappedBy = "allocation", cascade = CascadeType.ALL)
	private List<AllocationFeedback> allocationFeedbacks;

	// bi-directional many-to-one association to AllocationHour
	@OneToMany(mappedBy = "allocation", cascade = CascadeType.ALL, orphanRemoval = true)
	private List<AllocationHour> allocationHours = new ArrayList<>();

	// bi-directional many-to-one association to SyncProjectAllocation
	@OneToMany(mappedBy = "allocation", cascade = CascadeType.ALL, orphanRemoval = true)
	private List<SyncProjectAllocation> syncProjectAllocations;

	// bi-directional many-to-one association to SyncProjectAllocationHour
	@OneToMany(mappedBy = "allocation", cascade = CascadeType.ALL, orphanRemoval = true)
	private List<SyncProjectAllocationHour> syncProjectAllocationHours;

	public Allocation() {
	}

	public Allocation(Employee employee, Project project, ProjectLocation projectLocation, WorkGroup workgroup,
			Employee reportingManagerId, Employee allocationManagerId, Boolean billable, Date releaseDate,
			Long createdBy, AllocationStatus allocationStatus) {
		this.employee = employee;
		this.project = project;
		this.projectLocation = projectLocation;
		this.workGroup = workgroup;
		this.reportingManagerId = reportingManagerId;
		this.allocationManagerId = allocationManagerId;
		this.isBillable = billable;
		this.releaseDate = releaseDate;
		this.setIsActive(true);
		this.setCreatedOn(TimeUtils.getCreatedOn());
		this.setCreatedBy(createdBy);
		this.allocationDetails = new ArrayList<>();
		this.earmarks = new HashSet<>();
		this.allocationStatus = allocationStatus;
	}

	public Long getAllocationId() {
		return this.allocationId;
	}

	public void setAllocationId(Long allocationId) {
		this.allocationId = allocationId;
	}

	public AllocationStatus getAllocationStatus() {
		return this.allocationStatus;
	}

	public void setAllocationStatus(AllocationStatus allocationStatus) {
		this.allocationStatus = allocationStatus;
	}

	public Employee getEmployee() {
		return employee;
	}

	public void setEmployee(Employee employee) {
		this.employee = employee;
	}

	public Employee getReportingManagerId() {
		return reportingManagerId;
	}

	public void setReportingManagerId(Employee reportingManagerId) {
		this.reportingManagerId = reportingManagerId;
	}

	public Employee getAllocationManagerId() {
		return allocationManagerId;
	}

	public void setAllocationManagerId(Employee allocationManagerId) {
		this.allocationManagerId = allocationManagerId;
	}

	public Project getProject() {
		return this.project;
	}

	public void setProject(Project project) {
		this.project = project;
	}

	public ProjectLocation getProjectLocation() {
		return this.projectLocation;
	}

	public void setProjectLocation(ProjectLocation projectLocation) {
		this.projectLocation = projectLocation;
	}

	public WorkGroup getWorkGroup() {
		return this.workGroup;
	}

	public void setWorkGroup(WorkGroup workGroup) {
		this.workGroup = workGroup;
	}

	public Boolean getIsBillable() {
		return isBillable;
	}

	public void setIsBillable(Boolean isBillable) {
		this.isBillable = isBillable;
	}

	public Set<Earmark> getEarmarks() {
		return this.earmarks;
	}

	public void setEarmarks(Set<Earmark> earmarks) {
		this.earmarks = earmarks;
	}

	public Earmark addEarmarks(Earmark earmark) {
		getEarmarks().add(earmark);
		earmark.setAllocation(this);

		return earmark;
	}

	public Earmark removeEarmarks(Earmark earmark) {
		getEarmarks().remove(earmark);
		earmark.setAllocation(null);

		return earmark;
	}

	public Date getReleaseDate() {
		return releaseDate;
	}

	public void setReleaseDate(Date releaseDate) {
		this.releaseDate = releaseDate;
	}

	public Set<Employee> getEmployees() {
		return this.employees;
	}

	public void setEmployees(Set<Employee> employees) {
		this.employees = employees;
	}

	public Employee addEmployee(Employee employee) {
		getEmployees().add(employee);
		employee.setPrimaryAllocation(this);

		return employee;
	}

	public Employee removeEmployee(Employee employee) {
		getEmployees().remove(employee);
		employee.setPrimaryAllocation(null);

		return employee;
	}

	public List<AllocationDetail> getAllocationDetails() {
		return allocationDetails;
	}

	public void setAllocationDetails(List<AllocationDetail> allocationDetails) {
		this.allocationDetails = allocationDetails;
	}

	public AllocationDetail addAllocationDetails(AllocationDetail allocationDetail) {
		getAllocationDetails().add(allocationDetail);
		allocationDetail.setAllocation(this);

		return allocationDetail;
	}

	public AllocationDetail removeAllocationDetails(AllocationDetail allocationDetail) {
		getAllocationDetails().remove(allocationDetail);
		allocationDetail.setAllocation(null);

		return allocationDetail;
	}

	public List<AllocationFeedback> getAllocationFeedbacks() {
		return this.allocationFeedbacks;
	}

	public void setAllocationFeedbacks(List<AllocationFeedback> allocationFeedbacks) {
		this.allocationFeedbacks = allocationFeedbacks;
	}

	public AllocationFeedback addAllocationFeedbacks(AllocationFeedback allocationFeedback) {
		getAllocationFeedbacks().add(allocationFeedback);
		allocationFeedback.setAllocation(this);

		return allocationFeedback;
	}

	public AllocationFeedback removeAllocationFeedbacks(AllocationFeedback allocationFeedback) {
		getAllocationFeedbacks().remove(allocationFeedback);
		allocationFeedback.setAllocation(null);

		return allocationFeedback;
	}

	public List<AllocationHour> getAllocationHours() {
		return this.allocationHours;
	}

	public void setAllocationHours(List<AllocationHour> allocationHours) {
		this.allocationHours = allocationHours;
	}

	public AllocationHour addAllocationHour(AllocationHour allocationHour) {
		getAllocationHours().add(allocationHour);
		allocationHour.setAllocation(this);

		return allocationHour;
	}

	public AllocationHour removeAllocationHour(AllocationHour allocationHour) {
		getAllocationHours().remove(allocationHour);
		allocationHour.setAllocation(null);

		return allocationHour;
	}

	public List<SyncProjectAllocation> getSyncProjectAllocations() {
		return this.syncProjectAllocations;
	}

	public void setSyncProjectAllocations(List<SyncProjectAllocation> syncProjectAllocations) {
		this.syncProjectAllocations = syncProjectAllocations;
	}

	public SyncProjectAllocation addSyncProjectAllocation(SyncProjectAllocation syncProjectAllocation) {
		getSyncProjectAllocations().add(syncProjectAllocation);
		syncProjectAllocation.setAllocation(this);

		return syncProjectAllocation;
	}

	public SyncProjectAllocation removeSyncProjectAllocation(SyncProjectAllocation syncProjectAllocation) {
		getSyncProjectAllocations().remove(syncProjectAllocation);
		syncProjectAllocation.setAllocation(null);

		return syncProjectAllocation;
	}

	public List<SyncProjectAllocationHour> getSyncProjectAllocationHours() {
		return this.syncProjectAllocationHours;
	}

	public void setSyncProjectAllocationHours(List<SyncProjectAllocationHour> syncProjectAllocationHours) {
		this.syncProjectAllocationHours = syncProjectAllocationHours;
	}

	public SyncProjectAllocationHour addSyncProjectAllocationHour(SyncProjectAllocationHour syncProjectAllocationHour) {
		getSyncProjectAllocationHours().add(syncProjectAllocationHour);
		syncProjectAllocationHour.setAllocation(this);

		return syncProjectAllocationHour;
	}

	public SyncProjectAllocationHour removeSyncProjectAllocationHour(
			SyncProjectAllocationHour syncProjectAllocationHour) {
		getSyncProjectAllocationHours().remove(syncProjectAllocationHour);
		syncProjectAllocationHour.setAllocation(null);

		return syncProjectAllocationHour;
	}

	public void deactivate(Employee loggedInEmployee) {
		if (CollectionUtils.isNotEmpty(getAllocationDetails()))
			getAllocationDetails().forEach(ad -> ad.deactivate(loggedInEmployee));
		if (CollectionUtils.isNotEmpty(getAllocationHours()))
			getAllocationHours().forEach(AllocationHour::deactivate);
		if (CollectionUtils.isNotEmpty(getAllocationFeedbacks()))
			getAllocationFeedbacks().forEach(AllocationFeedback::deactivate);

		setReleaseDate(Calendar.getInstance().getTime());
		setIsActive(false);
	}

	public Allocation getTimesheetAllocation() {
		return this.timesheetAllocation;
	}

	public void setTimesheetAllocation(Allocation timesheetAllocation) {
		this.timesheetAllocation = timesheetAllocation;
	}

	public List<Allocation> getTimesheetAllocations() {
		return this.timesheetAllocations;
	}

	public void setTimesheetAllocations(List<Allocation> allocations) {
		this.timesheetAllocations = allocations;
	}

	public Allocation addAllocation(Allocation allocation) {
		getTimesheetAllocations().add(allocation);
		allocation.setTimesheetAllocation(this);

		return allocation;
	}

	public Allocation removeAllocation(Allocation allocation) {
		getTimesheetAllocations().remove(allocation);
		allocation.setTimesheetAllocation(null);

		return allocation;
	}

}