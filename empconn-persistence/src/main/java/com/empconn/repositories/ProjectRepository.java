package com.empconn.repositories;

import java.util.Date;
import java.util.List;
import java.util.Set;

import javax.persistence.QueryHint;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.jpa.repository.QueryHints;
import org.springframework.transaction.annotation.Transactional;

import com.empconn.persistence.entities.Account;
import com.empconn.persistence.entities.Horizontal;
import com.empconn.persistence.entities.Project;
import com.empconn.persistence.entities.ProjectSubCategory;
import com.empconn.persistence.entities.Vertical;

public interface ProjectRepository extends JpaRepository<Project, Long>, JpaSpecificationExecutor<Project> {

	@Query("select p from Project p where p.account.id = ?2 and upper(p.name) = upper(?1)")
	public Set<Project> findProjectsInAccount(String projectName, Integer accountId);

	Set<Project> findByAccountAccountIdAndNameIgnoreCase(Integer accountId, String projectName);

	@Query("select p from Project p where p.account.id = ?1 and p.isActive = true AND p.name NOT IN ('Central Bench','NDBench')")
	public Set<Project> findProjectsInAccount(Integer accountId);

	Set<Project> findByAccountAccountId(Integer accountId);

	@Query("select p from Project p where upper(p.account.category) = 'INTERNAL' and upper(p.name) = upper(?1) and p.currentStatus NOT IN ('GDM_REJECTED', 'PMO_REJECTED') and p.isActive='true'")
	public Set<Project> findInternalProjectsByName(String projectName);

	@Query("select p from Project p where upper(p.account.category) = 'INTERNAL' and upper(p.name) = upper(:projectName) and p.projectId != :projectId and p.currentStatus NOT IN ('GDM_REJECTED', 'PMO_REJECTED') and p.isActive='true'")
	public Set<Project> findInternalProjectsByNameOtherThanProject(String projectName, Long projectId);

	Set<Project> findByAccountCategoryAndNameIgnoreCase(String categoryName, String projectName);

	public Project findByProjectId(Long projectId);

	@Transactional
	@Modifying
	@Query("UPDATE Project p SET p.currentStatus = :status, p.currentComment = :comment WHERE p.projectId = :projectId")
	public Integer changeCurrentStatusWithComment(Long projectId, String status, String comment);

	@Transactional
	@Modifying
	@Query("UPDATE Project p SET p.currentStatus = :status WHERE p.projectId = :projectId")
	public Integer changeCurrentStatus(Long projectId, String status);

	@Modifying
	@Query("UPDATE Project p SET p.endDate = :endDate WHERE p.projectId = :projectId")
	public Integer changeProjectEndDate(Long projectId, Date endDate);

	@Query("SELECT DISTINCT p FROM Project p WHERE p.currentStatus IN ('DRAFT', 'OPEN', 'INITIATED', 'GDM_REVIEWED', 'SENT_BACK', 'RESUBMITTED') and p.isActive = 'true' and p.createdBy = :empId Order by p.createdOn desc")
	public Iterable<Project> findMyPins(Long empId);

	@Transactional
	@Modifying
	@Query("UPDATE Project p SET p.isActive = 'FALSE' WHERE p.projectId = :projectId")
	public void softDelete(long projectId);

	public List<Project> findByCurrentStatusOrderByCreatedOnDesc(String currentStatus);

	@Query("SELECT DISTINCT p FROM Project p WHERE (p.employee1.employeeId = :empId OR (p.employee1 = null AND p.employee2.employeeId = :empId)) AND p.isActive = 'true' AND p.currentStatus IN('INITIATED', 'RESUBMITTED') Order by p.createdOn desc")
	public List<Project> getPinsForReviewList(Long empId);

	@Query("SELECT p FROM Project p WHERE p.account.accountId IN (:Ids) AND p.isActive = :isActive")
	@QueryHints({ @QueryHint(name = "org.hibernate.cacheable", value = "true") })
	public Set<Project> findByAccount(List<Integer> ids, Boolean isActive);

	@Query("SELECT distinct(p.horizontal) FROM Project p WHERE p.employee1.employeeId = :empId OR p.employee2.employeeId = :empId OR p.employee3.employeeId = :empId")
	public Set<Horizontal> getProjectHorizontalsWhereEmployeeIsGdmOrManager(Long empId);

	@Query("SELECT distinct(p.account.vertical) FROM Project p WHERE p.employee1.employeeId = :empId OR p.employee2.employeeId = :empId OR p.employee3.employeeId = :empId")
	public Set<Vertical> getProjectVerticalsWhereEmployeeIsGdmOrManager(Long empId);

	@Query("SELECT distinct(p.projectSubCategory) FROM Project p WHERE p.employee1.employeeId = :empId OR p.employee2.employeeId = :empId OR p.employee3.employeeId = :empId")
	public Set<ProjectSubCategory> getProjectSubCategoriesWhereEmployeeIsGdmOrManager(Long empId);

	@Query("SELECT count(p) FROM Project p WHERE p.currentStatus IN ('DRAFT', 'OPEN', 'INITIATED', 'GDM_REVIEWED', 'SENT_BACK', 'RESUBMITTED') and p.isActive = 'true' and p.createdBy = :empId")
	public Integer getMyPinCount(Long empId);

	@Query("SELECT count(p) FROM Project p WHERE (p.employee1.employeeId = :empId OR (p.employee1 = null AND p.employee2.employeeId = :empId)) AND p.isActive = 'true' AND p.currentStatus IN('INITIATED', 'RESUBMITTED')")
	public Integer getPinsForReviewCount(Long empId);

	public Integer countByCurrentStatusAndIsActiveIsTrue(String currentStatus);

	@Query("SELECT p FROM Project p")
	public Set<Project> findProjectSummaryList();

	// @Query("SELECT p FROM Project p WHERE (p.employee1.employeeId = :empId OR
	// p.employee2.employeeId = :empId OR p.employee3.employeeId = :empId) AND
	// p.isActive = true")
	@Query("SELECT p FROM Project p WHERE p.isActive = true")
	public Set<Project> getMyProjectDropdown();

	@QueryHints({ @QueryHint(name = "org.hibernate.cacheable", value = "true") })
	Project findByAccountCategoryAndName(String categoryName, String projectName);

	@Query("SELECT p FROM Project p WHERE (p.employee1.employeeId = :empId OR p.employee2.employeeId = :empId OR p.employee3.employeeId = :empId) AND p.isActive = true")
	public Set<Project> getMyProjectDropdown(Long empId);

	@Query("SELECT a FROM Account a WHERE a.isActive = true")
	public Set<Account> getMyAccountDropdown();

	@Query("SELECT p from Project p WHERE p.projectId IN(:projectIds) AND p.currentStatus IN('PMO_APPROVED', 'PROJECT_ON_HOLD') AND p.isActive = 'TRUE'")
	public List<Project> findProjectSummaryForManager(Set<Long> projectIds);

	@Query("SELECT p FROM Project p WHERE p.createdBy = :empId AND p.isActive = true AND p.name NOT IN ('Central Bench','NDBench') AND p.account.accountId IN ( :accountIds )")
	public Set<Project> getMyProjectDropdown(Long empId, Set<Integer> accountIds);

	@Query("SELECT p FROM Project p WHERE p.isActive = true AND p.name NOT IN ('Central Bench','NDBench') AND p.account.accountId IN ( :accountIds )")
	public Set<Project> getMyProjectDropdown(Set<Integer> accountIds);

	@Query("select p from Project p where p.account.id = :accountId and upper(p.name) = upper(:projectName) AND p.currentStatus NOT IN ('GDM_REJECTED', 'PMO_REJECTED')  AND p.isActive = 'true'")
	public Set<Project> findProjectsInAccountForNameValidation(String projectName, Integer accountId);

	@Query("select p from Project p where p.account.id = :accountId and upper(p.name) = upper(:projectName) AND p.projectId != :projectId AND p.currentStatus NOT IN ('GDM_REJECTED', 'PMO_REJECTED')  AND p.isActive = 'true'")
	public Set<Project> findProjectsInAccountForNameValidationOtherThanProject(String projectName, Long projectId,
			Integer accountId);

	@Query("SELECT p FROM Project p WHERE p.employee1.employeeId IN (:Ids) OR  p.employee2.employeeId IN (:Ids)")
	public Set<Project> findGdmForProject(List<Long> ids);

	@Query("SELECT p FROM Project p WHERE p.currentStatus IN('PMO_APPROVED', 'PROJECT_ON_HOLD') AND p.isActive = 'TRUE'")
	public List<Project> getActiveAndOnHoldProjects();

	@Transactional
	@Modifying
	@Query("UPDATE Project p SET p.mapProjectId = :mapProjectId WHERE p.projectId = :projectId")
	public Integer updateMapProjectId(String mapProjectId, Long projectId);

	@Query("SELECT p.id FROM Project p WHERE (p.employee1.employeeId = :employeeId OR p.employee2.employeeId = :employeeId) AND p.currentStatus IN(:status) AND p.isActive ='TRUE'")
	public Set<Long> findProjectIdsForTheGdm(Long employeeId, List<String> status);

	@Query("select p from Project p where upper(p.name) = upper(:projectName) and p.isActive = 'true'")
	@QueryHints({ @QueryHint(name = "org.hibernate.cacheable", value = "true") })
	public Project findByName(String projectName);

	@Query("SELECT p FROM Project p WHERE p.isActive = true AND upper(p.name) like concat('%', upper(:partialProjectName), '%') AND p.account.name = 'Bench' AND p.name != 'NDBench' ORDER BY p.name ASC")
	public List<Project> findBenchProjects(String partialProjectName);

	public Integer countByCurrentStatusInAndProjectSubCategoryEquals(String[] statuses,
			ProjectSubCategory projectSubCategory);

	public Integer countByCurrentStatusInAndAccountVerticalEquals(String[] statuses, Vertical vertical);

	public Integer countByCurrentStatusInAndHorizontalEquals(String[] statuses, Horizontal horizontal);

}
