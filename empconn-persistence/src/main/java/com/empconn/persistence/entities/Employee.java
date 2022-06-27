package com.empconn.persistence.entities;

import java.io.Serializable;
import java.util.Date;
import java.util.List;
import java.util.Set;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
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

import org.hibernate.annotations.Formula;

/**
 * The persistent class for the employee database table.
 *
 */
@Entity
@NamedQuery(name = "Employee.findAll", query = "SELECT e FROM Employee e")
public class Employee extends Auditable<Long> implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	@SequenceGenerator(name = "EMPLOYEE_EMPLOYEEID_GENERATOR", sequenceName = "EMPLOYEE_ID_SEQUENCE", allocationSize = 1)
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "EMPLOYEE_EMPLOYEEID_GENERATOR")
	@Column(name = "employee_id")
	private Long employeeId;

	@Temporal(TemporalType.DATE)
	@Column(name = "date_of_joining")
	private Date dateOfJoining;

	private String email;

	@Column(name = "emp_code")
	private String empCode;

	@Column(name = "first_name")
	private String firstName;

	@Column(name = "is_manager")
	private Boolean isManager;

	@Column(name = "last_name")
	private String lastName;

	@Temporal(TemporalType.DATE)
	@Column(name = "last_working_day")
	private Date lastWorkingDay;

	@Column(name = "login_id")
	private String loginId;

	@Column(name = "middle_name")
	private String middleName;

	@Formula("concat(first_name, ' ', last_name)")
	private String fullName;

	// bi-directional many-to-one association to Employee
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "primary_allocation_id")
	private Allocation primaryAllocation;

	// bi-directional many-to-one association to Allocation
	@OneToMany(mappedBy = "employee")
	private Set<Allocation> employeeAllocations;

	// bi-directional many-to-one association to Allocation
	@OneToMany(mappedBy = "reportingManagerId")
	private Set<Allocation> reportingMgrAllocations;

	// bi-directional many-to-one association to Allocation
	@OneToMany(mappedBy = "allocationManagerId")
	private Set<Allocation> allocationMgrAllocations;

	// bi-directional many-to-one association to Allocation
	@OneToMany(mappedBy = "deallocatedBy")
	private Set<AllocationDetail> allocationDetails;

	// bi-directional many-to-one association to Employee
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "gdm_id")
	private Employee employee2;

	// bi-directional many-to-one association to Employee
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "nd_reporting_manager_id")
	private Employee ndReportingManagerId;

	// bi-directional many-to-one association to Employee
	@OneToMany(mappedBy = "ndReportingManagerId")
	private Set<Employee> ndReportingMgrEmployees;

	// bi-directional many-to-one association to Employee
	@OneToMany(mappedBy = "employee2")
	private Set<Employee> employees2;

	// bi-directional many-to-one association to Location
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "location_id")
	private Location location;

	// bi-directional many-to-one association to Title
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "title_id")
	private Title title;

	// bi-directional many-to-one association to EmployeeRole
	@OneToMany(mappedBy = "employee", cascade = CascadeType.ALL)
	private Set<EmployeeRole> employeeRoles;

	// bi-directional many-to-one association to EmployeeSkill
	@OneToMany(mappedBy = "employee")
	private Set<EmployeeSkill> employeeSkills;

	// bi-directional many-to-one association to Opportunity
	@OneToMany(mappedBy = "employee1")
	private Set<Opportunity> opportunities1;

	// bi-directional many-to-one association to Opportunity
	@OneToMany(mappedBy = "employee2")
	private Set<Opportunity> opportunities2;

	// bi-directional many-to-one association to Project
	@OneToMany(mappedBy = "employee1")
	private Set<Project> projects1;

	// bi-directional many-to-one association to Project
	@OneToMany(mappedBy = "employee2")
	private Set<Project> projects2;

	// bi-directional many-to-one association to Project
	@OneToMany(mappedBy = "employee3")
	private Set<Project> projects3;

	// bi-directional many-to-one association to ProjectLocation
	@OneToMany(mappedBy = "employee1")
	private Set<ProjectLocation> projectLocations1;

	// bi-directional many-to-one association to ProjectLocation
	@OneToMany(mappedBy = "employee2")
	private Set<ProjectLocation> projectLocations2;

	// bi-directional many-to-one association to ProjectLocation
	@OneToMany(mappedBy = "employee3")
	private Set<ProjectLocation> projectLocations3;

	// bi-directional many-to-one association to ProjectLocation
	@OneToMany(mappedBy = "employee4")
	private Set<ProjectLocation> projectLocations4;

	// bi-directional many-to-one association to ProjectLocation
	@OneToMany(mappedBy = "employee5")
	private Set<ProjectLocation> projectLocations5;

	// bi-directional many-to-one association to Earmark
	@OneToMany(mappedBy = "employee2")
	private Set<Earmark> earmarks;

	// bi-directional many-to-one association to BusinessUnit
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "business_unit_id")
	private BusinessUnit businessUnit;

	// bi-directional many-to-one association to Department
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "department_id")
	private Department department;

	// bi-directional many-to-one association to Division
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "division_id")
	private Division division;

	// bi-directional many-to-one association to LocationHr
	@OneToMany(mappedBy = "employee")
	private Set<LocationHr> locationHrs;

	// bi-directional many-to-one association to ProjectLocation
	@OneToMany(mappedBy = "employee")
	private Set<ManagerChange> employeeChanges;

	// bi-directional many-to-one association to ProjectLocation
	@OneToMany(mappedBy = "newManager")
	private Set<ManagerChange> managerChanges;

	// bi-directional many-to-one association to SystemUser
	@OneToMany(mappedBy = "employee")
	private List<SystemUser> systemUsers;

	// bi-directional many-to-one association to SyncProjectManager
	@OneToMany(mappedBy = "managerId")
	private List<SyncProjectManager> syncProjectManagers;

	// bi-directional many-to-one association to SyncEmployee
	@OneToMany(mappedBy = "employee")
	private List<SyncEmployee> syncEmployees;

	public Employee() {
	}

	public Long getEmployeeId() {
		return this.employeeId;
	}

	public void setEmployeeId(Long employeeId) {
		this.employeeId = employeeId;
	}

	public Date getDateOfJoining() {
		return this.dateOfJoining;
	}

	public void setDateOfJoining(Date dateOfJoining) {
		this.dateOfJoining = dateOfJoining;
	}

	public String getEmail() {
		return this.email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getEmpCode() {
		return this.empCode;
	}

	public void setEmpCode(String empCode) {
		this.empCode = empCode;
	}

	public String getFirstName() {
		return this.firstName;
	}

	public void setFirstName(String firstName) {
		this.firstName = firstName;
	}

	public Boolean getIsManager() {
		return this.isManager;
	}

	public void setIsManager(Boolean isManager) {
		this.isManager = isManager;
	}

	public String getLastName() {
		return this.lastName;
	}

	public void setLastName(String lastName) {
		this.lastName = lastName;
	}

	public Date getLastWorkingDay() {
		return this.lastWorkingDay;
	}

	public void setLastWorkingDay(Date lastWorkingDay) {
		this.lastWorkingDay = lastWorkingDay;
	}

	public String getLoginId() {
		return this.loginId;
	}

	public void setLoginId(String loginId) {
		this.loginId = loginId;
	}

	public String getMiddleName() {
		return this.middleName;
	}

	public void setMiddleName(String middleName) {
		this.middleName = middleName;
	}

	public Allocation addEmployeeAllocations(Allocation employeeAllocation) {
		getEmployeeAllocations().add(employeeAllocation);
		employeeAllocation.setEmployee(this);

		return employeeAllocation;
	}

	public Allocation removeEmployeeAllocation(Allocation employeeAllocation) {
		getEmployeeAllocations().remove(employeeAllocation);
		employeeAllocation.setEmployee(null);

		return employeeAllocation;
	}

	public Allocation addReportingMgrAllocations(Allocation reportingMgrAllocation) {
		getReportingMgrAllocations().add(reportingMgrAllocation);
		reportingMgrAllocation.setReportingManagerId(this);

		return reportingMgrAllocation;
	}

	public Allocation removeReportingMgrAllocations(Allocation reportingMgrAllocation) {
		getReportingMgrAllocations().remove(reportingMgrAllocation);
		reportingMgrAllocation.setReportingManagerId(null);

		return reportingMgrAllocation;
	}

	public Allocation addAllocationMgrAllocations(Allocation allocationMgrAllocation) {
		getAllocationMgrAllocations().add(allocationMgrAllocation);
		allocationMgrAllocation.setAllocationManagerId(this);

		return allocationMgrAllocation;
	}

	public Allocation removeAllocationMgrAllocations(Allocation allocationMgrAllocation) {
		getAllocationMgrAllocations().remove(allocationMgrAllocation);
		allocationMgrAllocation.setAllocationManagerId(null);

		return allocationMgrAllocation;
	}

	public Set<Allocation> getEmployeeAllocations() {
		return employeeAllocations;
	}

	public void setEmployeeAllocations(Set<Allocation> employeeAllocations) {
		this.employeeAllocations = employeeAllocations;
	}

	public Set<Allocation> getReportingMgrAllocations() {
		return reportingMgrAllocations;
	}

	public void setReportingMgrAllocations(Set<Allocation> reportingMgrAllocations) {
		this.reportingMgrAllocations = reportingMgrAllocations;
	}

	public Set<Allocation> getAllocationMgrAllocations() {
		return allocationMgrAllocations;
	}

	public void setAllocationMgrAllocations(Set<Allocation> allocationMgrAllocations) {
		this.allocationMgrAllocations = allocationMgrAllocations;
	}

	public Set<AllocationDetail> getAllocationDetails() {
		return allocationDetails;
	}

	public void setAllocationDetails(Set<AllocationDetail> allocationDetails) {
		this.allocationDetails = allocationDetails;
	}

	public Employee getEmployee2() {
		return this.employee2;
	}

	public void setEmployee2(Employee employee2) {
		this.employee2 = employee2;
	}

	public Set<Employee> getEmployees2() {
		return this.employees2;
	}

	public void setEmployees2(Set<Employee> employees2) {
		this.employees2 = employees2;
	}

	public Employee addEmployees2(Employee employees2) {
		getEmployees2().add(employees2);
		employees2.setEmployee2(this);

		return employees2;
	}

	public Employee removeEmployees2(Employee employees2) {
		getEmployees2().remove(employees2);
		employees2.setEmployee2(null);

		return employees2;
	}

	public Location getLocation() {
		return this.location;
	}

	public void setLocation(Location location) {
		this.location = location;
	}

	public Title getTitle() {
		return this.title;
	}

	public void setTitle(Title title) {
		this.title = title;
	}

	public Set<EmployeeRole> getEmployeeRoles() {
		return this.employeeRoles;
	}

	public void setEmployeeRoles(Set<EmployeeRole> employeeRoles) {
		this.employeeRoles = employeeRoles;
	}

	public EmployeeRole addEmployeeRole(EmployeeRole employeeRole) {
		getEmployeeRoles().add(employeeRole);
		employeeRole.setEmployee(this);

		return employeeRole;
	}

	public EmployeeRole removeEmployeeRole(EmployeeRole employeeRole) {
		getEmployeeRoles().remove(employeeRole);
		employeeRole.setEmployee(null);

		return employeeRole;
	}

	public Set<EmployeeSkill> getEmployeeSkills() {
		return this.employeeSkills;
	}

	public void setEmployeeSkills(Set<EmployeeSkill> employeeSkills) {
		this.employeeSkills = employeeSkills;
	}

	public EmployeeSkill addEmployeeSkill(EmployeeSkill employeeSkill) {
		getEmployeeSkills().add(employeeSkill);
		employeeSkill.setEmployee(this);

		return employeeSkill;
	}

	public EmployeeSkill removeEmployeeSkill(EmployeeSkill employeeSkill) {
		getEmployeeSkills().remove(employeeSkill);
		employeeSkill.setEmployee(null);

		return employeeSkill;
	}

	public Set<Opportunity> getOpportunities1() {
		return this.opportunities1;
	}

	public void setOpportunities1(Set<Opportunity> opportunities1) {
		this.opportunities1 = opportunities1;
	}

	public Opportunity addOpportunities1(Opportunity opportunities1) {
		getOpportunities1().add(opportunities1);
		opportunities1.setEmployee1(this);

		return opportunities1;
	}

	public Opportunity removeOpportunities1(Opportunity opportunities1) {
		getOpportunities1().remove(opportunities1);
		opportunities1.setEmployee1(null);

		return opportunities1;
	}

	public Set<Opportunity> getOpportunities2() {
		return this.opportunities2;
	}

	public void setOpportunities2(Set<Opportunity> opportunities2) {
		this.opportunities2 = opportunities2;
	}

	public Opportunity addOpportunities2(Opportunity opportunities2) {
		getOpportunities2().add(opportunities2);
		opportunities2.setEmployee2(this);

		return opportunities2;
	}

	public Opportunity removeOpportunities2(Opportunity opportunities2) {
		getOpportunities2().remove(opportunities2);
		opportunities2.setEmployee2(null);

		return opportunities2;
	}

	public Set<Project> getProjects1() {
		return this.projects1;
	}

	public void setProjects1(Set<Project> projects1) {
		this.projects1 = projects1;
	}

	public Project addProjects1(Project projects1) {
		getProjects1().add(projects1);
		projects1.setEmployee1(this);

		return projects1;
	}

	public Project removeProjects1(Project projects1) {
		getProjects1().remove(projects1);
		projects1.setEmployee1(null);

		return projects1;
	}

	public Set<Project> getProjects2() {
		return this.projects2;
	}

	public void setProjects2(Set<Project> projects2) {
		this.projects2 = projects2;
	}

	public Project addProjects2(Project projects2) {
		getProjects2().add(projects2);
		projects2.setEmployee2(this);

		return projects2;
	}

	public Project removeProjects2(Project projects2) {
		getProjects2().remove(projects2);
		projects2.setEmployee2(null);

		return projects2;
	}

	public Set<Project> getProjects3() {
		return this.projects3;
	}

	public void setProjects3(Set<Project> projects3) {
		this.projects3 = projects3;
	}

	public Project addProjects3(Project projects3) {
		getProjects3().add(projects3);
		projects3.setEmployee3(this);

		return projects3;
	}

	public Project removeProjects3(Project projects3) {
		getProjects3().remove(projects3);
		projects3.setEmployee3(null);

		return projects3;
	}

	public Set<ProjectLocation> getProjectLocations1() {
		return this.projectLocations1;
	}

	public void setProjectLocations1(Set<ProjectLocation> projectLocations1) {
		this.projectLocations1 = projectLocations1;
	}

	public ProjectLocation addProjectLocations1(ProjectLocation projectLocations1) {
		getProjectLocations1().add(projectLocations1);
		projectLocations1.setEmployee1(this);

		return projectLocations1;
	}

	public ProjectLocation removeProjectLocations1(ProjectLocation projectLocations1) {
		getProjectLocations1().remove(projectLocations1);
		projectLocations1.setEmployee1(null);

		return projectLocations1;
	}

	public Set<ProjectLocation> getProjectLocations2() {
		return this.projectLocations2;
	}

	public void setProjectLocations2(Set<ProjectLocation> projectLocations2) {
		this.projectLocations2 = projectLocations2;
	}

	public ProjectLocation addProjectLocations2(ProjectLocation projectLocations2) {
		getProjectLocations2().add(projectLocations2);
		projectLocations2.setEmployee2(this);

		return projectLocations2;
	}

	public ProjectLocation removeProjectLocations2(ProjectLocation projectLocations2) {
		getProjectLocations2().remove(projectLocations2);
		projectLocations2.setEmployee2(null);

		return projectLocations2;
	}

	public Set<ProjectLocation> getProjectLocations3() {
		return this.projectLocations3;
	}

	public void setProjectLocations3(Set<ProjectLocation> projectLocations3) {
		this.projectLocations3 = projectLocations3;
	}

	public ProjectLocation addProjectLocations3(ProjectLocation projectLocations3) {
		getProjectLocations3().add(projectLocations3);
		projectLocations3.setEmployee3(this);

		return projectLocations3;
	}

	public ProjectLocation removeProjectLocations3(ProjectLocation projectLocations3) {
		getProjectLocations3().remove(projectLocations3);
		projectLocations3.setEmployee3(null);

		return projectLocations3;
	}

	public Set<ProjectLocation> getProjectLocations4() {
		return this.projectLocations4;
	}

	public void setProjectLocations4(Set<ProjectLocation> projectLocations4) {
		this.projectLocations4 = projectLocations4;
	}

	public ProjectLocation addProjectLocations4(ProjectLocation projectLocations4) {
		getProjectLocations4().add(projectLocations4);
		projectLocations4.setEmployee4(this);

		return projectLocations4;
	}

	public ProjectLocation removeProjectLocations4(ProjectLocation projectLocations4) {
		getProjectLocations4().remove(projectLocations4);
		projectLocations4.setEmployee4(null);

		return projectLocations4;
	}

	public Set<ProjectLocation> getProjectLocations5() {
		return this.projectLocations5;
	}

	public void setProjectLocations5(Set<ProjectLocation> projectLocations5) {
		this.projectLocations5 = projectLocations5;
	}

	public ProjectLocation addProjectLocations5(ProjectLocation projectLocations5) {
		getProjectLocations5().add(projectLocations5);
		projectLocations5.setEmployee5(this);

		return projectLocations5;
	}

	public ProjectLocation removeProjectLocations5(ProjectLocation projectLocations5) {
		getProjectLocations5().remove(projectLocations5);
		projectLocations5.setEmployee5(null);

		return projectLocations5;
	}

	public Set<Earmark> getEarmarks() {
		return this.earmarks;
	}

	public void setEarmarks(Set<Earmark> earmarks) {
		this.earmarks = earmarks;
	}

	public Earmark addEarmark(Earmark earmark) {
		getEarmarks().add(earmark);
		earmark.setEmployee2(this);

		return earmark;
	}

	public Earmark removeEarmark(Earmark earmark) {
		getEarmarks().remove(earmark);
		earmark.setEmployee2(null);

		return earmark;
	}

	public BusinessUnit getBusinessUnit() {
		return this.businessUnit;
	}

	public void setBusinessUnit(BusinessUnit businessUnit) {
		this.businessUnit = businessUnit;
	}

	public Department getDepartment() {
		return this.department;
	}

	public void setDepartment(Department department) {
		this.department = department;
	}

	public Division getDivision() {
		return this.division;
	}

	public void setDivision(Division division) {
		this.division = division;
	}

	public Allocation getPrimaryAllocation() {
		return this.primaryAllocation;
	}

	public void setPrimaryAllocation(Allocation primaryAllocation) {
		this.primaryAllocation = primaryAllocation;
	}

	public Set<LocationHr> getLocationHrs() {
		return locationHrs;
	}

	public void setLocationHrs(Set<LocationHr> locationHrs) {
		this.locationHrs = locationHrs;
	}

	public AllocationDetail addAllocationDetails(AllocationDetail allocationDetail) {
		getAllocationDetails().add(allocationDetail);
		allocationDetail.setDeallocatedBy(this);

		return allocationDetail;
	}

	public AllocationDetail removeAllocationDetails(AllocationDetail allocationDetail) {
		getAllocationDetails().remove(allocationDetail);
		allocationDetail.setDeallocatedBy(null);

		return allocationDetail;
	}

	public String getFullName() {
		return fullName;
	}

	public void setFullName(String fullName) {
		this.fullName = fullName;
	}

	public Employee getNdReportingManagerId() {
		return this.ndReportingManagerId;
	}

	public void setNdReportingManagerId(Employee ndReportingManagerId) {
		this.ndReportingManagerId = ndReportingManagerId;
	}

	public Set<Employee> getNdReportingMgrEmployees() {
		return this.ndReportingMgrEmployees;
	}

	public void setNdReportingMgrEmployees(Set<Employee> ndReportingMgrEmployees) {
		this.ndReportingMgrEmployees = ndReportingMgrEmployees;
	}

	public List<SystemUser> getSystemUsers() {
		return this.systemUsers;
	}

	public void setSystemUsers(List<SystemUser> systemUsers) {
		this.systemUsers = systemUsers;
	}

	public SystemUser addSystemUser(SystemUser systemUser) {
		getSystemUsers().add(systemUser);
		systemUser.setEmployee(this);

		return systemUser;
	}

	public SystemUser removeSystemUser(SystemUser systemUser) {
		getSystemUsers().remove(systemUser);
		systemUser.setEmployee(null);

		return systemUser;
	}

	public Employee addNdReportingMgrEmployees(Employee ndReportingMgr) {
		getNdReportingMgrEmployees().add(ndReportingMgr);
		ndReportingMgr.setNdReportingManagerId(this);

		return ndReportingMgr;
	}

	public Employee removeNdReportingMgrEmployees(Employee ndReportingMgr) {
		getNdReportingMgrEmployees().remove(ndReportingMgr);
		ndReportingMgr.setNdReportingMgrEmployees(null);
		return ndReportingMgr;
	}

	public Set<ManagerChange> getEmployeeChanges() {
		return employeeChanges;
	}

	public void setEmployeeChanges(Set<ManagerChange> employeeChanges) {
		this.employeeChanges = employeeChanges;
	}

	public ManagerChange addEmployeeChanges(ManagerChange employeeChange) {
		getEmployeeChanges().add(employeeChange);
		employeeChange.setEmployee(this);

		return employeeChange;
	}

	public ManagerChange removeEmployeeChanges(ManagerChange employeeChange) {
		getEmployeeChanges().remove(employeeChange);
		employeeChange.setEmployee(null);

		return employeeChange;
	}

	public Set<ManagerChange> getManagerChanges() {
		return managerChanges;
	}

	public void setManagerChanges(Set<ManagerChange> managerChanges) {
		this.managerChanges = managerChanges;
	}

	public ManagerChange addManagerChanges(ManagerChange employeeChange) {
		getManagerChanges().add(employeeChange);
		employeeChange.setNewManager(this);

		return employeeChange;
	}

	public ManagerChange removeManagerChanges(ManagerChange employeeChange) {
		getManagerChanges().remove(employeeChange);
		employeeChange.setNewManager(null);

		return employeeChange;
	}

	public List<SyncProjectManager> getSyncProjectManagers() {
		return this.syncProjectManagers;
	}

	public void setSyncProjectManagers(List<SyncProjectManager> syncProjectManagers) {
		this.syncProjectManagers = syncProjectManagers;
	}

	public SyncProjectManager addSyncProjectManager(SyncProjectManager syncProjectManager) {
		getSyncProjectManagers().add(syncProjectManager);
		syncProjectManager.setManagerId(this);

		return syncProjectManager;
	}

	public SyncProjectManager removeSyncProjectManager(SyncProjectManager syncProjectManager) {
		getSyncProjectManagers().remove(syncProjectManager);
		syncProjectManager.setManagerId(null);

		return syncProjectManager;
	}

	public List<SyncEmployee> getSyncEmployees() {
		return this.syncEmployees;
	}

	public void setSyncEmployees(List<SyncEmployee> syncEmployees) {
		this.syncEmployees = syncEmployees;
	}

	public SyncEmployee addSyncEmployee(SyncEmployee syncEmployee) {
		getSyncEmployees().add(syncEmployee);
		syncEmployee.setEmployee(this);

		return syncEmployee;
	}

	public SyncEmployee removeSyncEmployee(SyncEmployee syncEmployee) {
		getSyncEmployees().remove(syncEmployee);
		syncEmployee.setEmployee(null);

		return syncEmployee;
	}

}
