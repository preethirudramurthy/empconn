package com.empconn.persistence.entities;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.persistence.Cacheable;
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
import javax.persistence.QueryHint;
import javax.persistence.SequenceGenerator;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;
import org.hibernate.annotations.Where;
import org.springframework.data.annotation.CreatedDate;

/**
 * The persistent class for the project database table.
 *
 */
@Entity
@NamedQuery(name = "Project.findAll", query = "SELECT p FROM Project p", hints = { @QueryHint(name = "org.hibernate.cacheable", value ="true")})
@Cacheable
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
public class Project extends Auditable<Long> implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	@SequenceGenerator(name = "PROJECT_PROJECTID_GENERATOR", sequenceName = "PROJECT_ID_SEQUENCE", allocationSize = 1)
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "PROJECT_PROJECTID_GENERATOR")
	@Column(name = "project_id")
	private Long projectId;

	@Column(name = "approved_by")
	private Long approvedBy;

	@CreatedDate
	@Temporal(TemporalType.DATE)
	@Column(name = "approved_on")
	private Date approvedOn;

	@Column(name = "current_comment")
	private String currentComment;

	@Column(name = "current_status")
	private String currentStatus;

	private String database;

	private String description;

	@Temporal(TemporalType.DATE)
	@Column(name = "end_date")
	private Date endDate;

	@Column(name = "is_sub_project")
	private boolean isSubProject;

	private String name;

	@Column(name = "operating_system")
	private String operatingSystem;

	@Column(name = "project_kickoff_is_required")
	private boolean projectKickoffIsRequired;

	@Column(name = "project_tok_link")
	private String projectTokLink;

	@Column(name = "send_notification_to_pin_group")
	private boolean sendNotificationToPinGroup;

	@Temporal(TemporalType.DATE)
	@Column(name = "start_date")
	private Date startDate;

	private String technology;

	// bi-directional many-to-one association to Account
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "account_id")
	private Account account;

	// bi-directional many-to-one association to Employee
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "dev_gdm_id")
	private Employee employee1;

	// bi-directional many-to-one association to Employee
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "qe_gdm_id")
	private Employee employee2;

	// bi-directional many-to-one association to Employee
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "business_manager_id")
	private Employee employee3;

	// bi-directional many-to-one association to Horizontal
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "horizontal_id")
	private Horizontal horizontal;

	// bi-directional many-to-one association to Project
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "parent_project_id")
	private Project project;

	// bi-directional many-to-one association to Project
	@OneToMany(mappedBy = "project")
	@Where(clause = "is_active = true")
	private Set<Project> projects;

	// bi-directional many-to-one association to ProjectSubCategory
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "project_sub_category_id")
	private ProjectSubCategory projectSubCategory;

	// bi-directional many-to-one association to ProjectChecklist
	@OneToMany(mappedBy = "project", cascade = CascadeType.ALL)
	@Where(clause = "is_active = true")
	private Set<ProjectChecklist> projectChecklists;

	// bi-directional many-to-one association to ProjectLocation
	@OneToMany(mappedBy = "project", cascade = CascadeType.ALL)
	@Where(clause = "is_active = true")
	private Set<ProjectLocation> projectLocations;

	// bi-directional many-to-one association to ProjectReview
	@OneToMany(mappedBy = "project")
	@Where(clause = "is_active = true")
	private Set<ProjectReview> projectReviews;

	// bi-directional many-to-one association to SalesforceIdentifier
	@OneToMany(mappedBy = "project", cascade = CascadeType.ALL)
	@Where(clause = "is_active = true")
	private Set<SalesforceIdentifier> salesforceIdentifiers;

	// bi-directional many-to-one association to Allocation
	@OneToMany(mappedBy = "project")
	@Where(clause = "is_active = true")
	private Set<Allocation> allocations;

	// bi-directional many-to-one association to Earmark
	@OneToMany(mappedBy = "project")
	@Where(clause = "is_active = true")
	private Set<Earmark> earmarks;

	// bi-directional many-to-one association to ProjectComment
	@OneToMany(mappedBy = "project", cascade = CascadeType.ALL)
	@Where(clause = "is_active = true")
	private Set<ProjectComment> projectComments;

	@Column(name = "map_project_id")
	private String mapProjectId;

	//bi-directional many-to-one association to SyncProject
	@OneToMany(mappedBy="project", cascade = CascadeType.ALL)
	private List<SyncProject> syncProjects;

	//bi-directional many-to-one association to SyncProjectManager
	@OneToMany(mappedBy="project")
	private List<SyncProjectManager> syncProjectManagers;


	public Long getProjectId() {
		return this.projectId;
	}

	public void setProjectId(Long projectId) {
		this.projectId = projectId;
	}

	public String getCurrentComment() {
		return this.currentComment;
	}

	public void setCurrentComment(String currentComment) {
		this.currentComment = currentComment;
	}

	public String getCurrentStatus() {
		return this.currentStatus;
	}

	public void setCurrentStatus(String currentStatus) {
		this.currentStatus = currentStatus;
	}

	public String getDatabase() {
		return this.database;
	}

	public void setDatabase(String database) {
		this.database = database;
	}

	public String getDescription() {
		return this.description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public Date getEndDate() {
		return this.endDate;
	}

	public void setEndDate(Date endDate) {
		this.endDate = endDate;
	}

	public boolean getIsSubProject() {
		return this.isSubProject;
	}

	public void setIsSubProject(boolean isSubProject) {
		this.isSubProject = isSubProject;
	}

	public String getName() {
		return this.name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getOperatingSystem() {
		return this.operatingSystem;
	}

	public void setOperatingSystem(String operatingSystem) {
		this.operatingSystem = operatingSystem;
	}

	public boolean getProjectKickoffIsRequired() {
		return this.projectKickoffIsRequired;
	}

	public void setProjectKickoffIsRequired(boolean projectKickoffIsRequired) {
		this.projectKickoffIsRequired = projectKickoffIsRequired;
	}

	public String getProjectTokLink() {
		return this.projectTokLink;
	}

	public void setProjectTokLink(String projectTokLink) {
		this.projectTokLink = projectTokLink;
	}

	public boolean getSendNotificationToPinGroup() {
		return this.sendNotificationToPinGroup;
	}

	public void setSendNotificationToPinGroup(boolean sendNotificationToPinGroup) {
		this.sendNotificationToPinGroup = sendNotificationToPinGroup;
	}

	public Date getStartDate() {
		return this.startDate;
	}

	public void setStartDate(Date startDate) {
		this.startDate = startDate;
	}

	public String getTechnology() {
		return this.technology;
	}

	public void setTechnology(String technology) {
		this.technology = technology;
	}

	public Account getAccount() {
		return this.account;
	}

	public void setAccount(Account account) {
		this.account = account;
	}

	public Employee getEmployee1() {
		return this.employee1;
	}

	public void setEmployee1(Employee employee1) {
		this.employee1 = employee1;
	}

	public Employee getEmployee2() {
		return this.employee2;
	}

	public void setEmployee2(Employee employee2) {
		this.employee2 = employee2;
	}

	public Employee getEmployee3() {
		return this.employee3;
	}

	public void setEmployee3(Employee employee3) {
		this.employee3 = employee3;
	}

	public Horizontal getHorizontal() {
		return this.horizontal;
	}

	public void setHorizontal(Horizontal horizontal) {
		this.horizontal = horizontal;
	}

	public Project getProject() {
		return this.project;
	}

	public void setProject(Project project) {
		this.project = project;
	}

	public Set<Project> getProjects() {
		return this.projects;
	}

	public void setProjects(Set<Project> projects) {
		this.projects = projects;
	}

	public Project addProject(Project project) {
		getProjects().add(project);
		project.setProject(this);

		return project;
	}

	public Project removeProject(Project project) {
		getProjects().remove(project);
		project.setProject(null);

		return project;
	}

	public ProjectSubCategory getProjectSubCategory() {
		return this.projectSubCategory;
	}

	public void setProjectSubCategory(ProjectSubCategory projectSubCategory) {
		this.projectSubCategory = projectSubCategory;
	}

	public Set<ProjectComment> getProjectComments() {
		return this.projectComments;
	}

	public void setProjectComments(Set<ProjectComment> projectComments) {
		this.projectComments = projectComments;
	}

	public ProjectComment addProjectComment(ProjectComment projectComment) {
		getProjectComments().add(projectComment);
		projectComment.setProject(this);

		return projectComment;
	}

	public ProjectComment removeProjectComment(ProjectComment projectComment) {
		getProjectComments().remove(projectComment);
		projectComment.setProject(null);

		return projectComment;
	}

	public Set<ProjectChecklist> getProjectChecklists() {
		return this.projectChecklists;
	}

	public void setProjectChecklists(Set<ProjectChecklist> projectChecklists) {
		this.projectChecklists = projectChecklists;
	}

	public ProjectChecklist addProjectChecklist(ProjectChecklist projectChecklist) {
		getProjectChecklists().add(projectChecklist);
		projectChecklist.setProject(this);

		return projectChecklist;
	}

	public ProjectChecklist removeProjectChecklist(ProjectChecklist projectChecklist) {
		getProjectChecklists().remove(projectChecklist);
		projectChecklist.setProject(null);

		return projectChecklist;
	}

	public Set<ProjectLocation> getProjectLocations() {
		return this.projectLocations;
	}

	public void setProjectLocations(Set<ProjectLocation> projectLocations) {
		this.projectLocations = projectLocations;
	}

	public ProjectLocation addProjectLocation(ProjectLocation projectLocation) {
		getProjectLocations().add(projectLocation);
		projectLocation.setProject(this);

		return projectLocation;
	}

	public ProjectLocation removeProjectLocation(ProjectLocation projectLocation) {
		getProjectLocations().remove(projectLocation);
		projectLocation.setProject(null);

		return projectLocation;
	}

	public Set<ProjectReview> getProjectReviews() {
		return this.projectReviews;
	}

	public void setProjectReviews(Set<ProjectReview> projectReviews) {
		this.projectReviews = projectReviews;
	}

	public ProjectReview addProjectReview(ProjectReview projectReview) {
		getProjectReviews().add(projectReview);
		projectReview.setProject(this);

		return projectReview;
	}

	public ProjectReview removeProjectReview(ProjectReview projectReview) {
		getProjectReviews().remove(projectReview);
		projectReview.setProject(null);

		return projectReview;
	}

	public Set<SalesforceIdentifier> getSalesforceIdentifiers() {
		return this.salesforceIdentifiers;
	}

	public void setSalesforceIdentifiers(Set<SalesforceIdentifier> salesforceIdentifiers) {
		this.salesforceIdentifiers = salesforceIdentifiers;
	}

	public SalesforceIdentifier addSalesforceIdentifier(SalesforceIdentifier salesforceIdentifier) {
		getSalesforceIdentifiers().add(salesforceIdentifier);
		salesforceIdentifier.setProject(this);

		return salesforceIdentifier;
	}

	public SalesforceIdentifier removeSalesforceIdentifier(SalesforceIdentifier salesforceIdentifier) {
		getSalesforceIdentifiers().remove(salesforceIdentifier);
		salesforceIdentifier.setProject(null);

		return salesforceIdentifier;
	}

	public Long getApprovedBy() {
		return this.approvedBy;
	}

	public void setApprovedBy(Long approvedBy) {
		this.approvedBy = approvedBy;
	}

	public Date getApprovedOn() {
		return this.approvedOn;
	}

	public void setApprovedOn(Date approvedOn) {
		this.approvedOn = approvedOn;
	}

	public Set<Allocation> getAllocations() {
		return this.allocations;
	}

	public void setAllocations(Set<Allocation> allocations) {
		this.allocations = allocations;
	}

	public Allocation addAllocation(Allocation allocation) {
		getAllocations().add(allocation);
		allocation.setProject(this);

		return allocation;
	}

	public Allocation removeAllocation(Allocation allocation) {
		getAllocations().remove(allocation);
		allocation.setProject(null);

		return allocation;
	}

	public Set<Earmark> getEarmarks() {
		return this.earmarks;
	}

	public void setEarmarks(Set<Earmark> earmarks) {
		this.earmarks = earmarks;
	}

	public Earmark addEarmark(Earmark earmark) {
		getEarmarks().add(earmark);
		earmark.setProject(this);

		return earmark;
	}

	public Earmark removeEarmark(Earmark earmark) {
		getEarmarks().remove(earmark);
		earmark.setProject(null);

		return earmark;
	}

	public String getMapProjectId() {
		return mapProjectId;
	}

	public void setMapProjectId(String mapProjectId) {
		this.mapProjectId = mapProjectId;
	}

	public Map<String, Employee> getGdms() {
		final Map<String, Employee> gdms = new LinkedHashMap<>();
		gdms.computeIfAbsent("DEV", val -> getEmployee1());
		gdms.computeIfAbsent("QA", val -> getEmployee2());

		return gdms;
	}

	public List<SyncProject> getSyncProjects() {
		if (this.syncProjects == null) {
			return new ArrayList<>();
		}
		return this.syncProjects;
	}

	public void setSyncProjects(List<SyncProject> syncProjects) {
		this.syncProjects = syncProjects;
	}

	public SyncProject addSyncProject(SyncProject syncProject) {
		getSyncProjects().add(syncProject);
		syncProject.setProject(this);

		return syncProject;
	}

	public SyncProject removeSyncProject(SyncProject syncProject) {
		getSyncProjects().remove(syncProject);
		syncProject.setProject(null);

		return syncProject;
	}

	public List<SyncProjectManager> getSyncProjectManagers() {
		return this.syncProjectManagers;
	}

	public void setSyncProjectManagers(List<SyncProjectManager> syncProjectManagers) {
		this.syncProjectManagers = syncProjectManagers;
	}

	public SyncProjectManager addSyncProjectManager(SyncProjectManager syncProjectManager) {
		getSyncProjectManagers().add(syncProjectManager);
		syncProjectManager.setProject(this);

		return syncProjectManager;
	}

	public SyncProjectManager removeSyncProjectManager(SyncProjectManager syncProjectManager) {
		getSyncProjectManagers().remove(syncProjectManager);
		syncProjectManager.setProject(null);

		return syncProjectManager;
	}
}
