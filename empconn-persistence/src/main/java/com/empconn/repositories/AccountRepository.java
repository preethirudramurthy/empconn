package com.empconn.repositories;

import java.util.Date;
import java.util.List;
import java.util.Optional;
import java.util.Set;

import javax.persistence.QueryHint;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.jpa.repository.QueryHints;
import org.springframework.transaction.annotation.Transactional;

import com.empconn.persistence.entities.Account;

public interface AccountRepository extends JpaRepository<Account, Integer> {

	@QueryHints({ @QueryHint(name = "org.hibernate.cacheable", value = "true") })
	List<Account> findByNameContainingIgnoreCaseAndCategoryIgnoreCaseEqualsAndStatusIgnoreCaseNotAndIsActiveTrueOrderByName(
			String partialName, String category, String Status);

	@QueryHints({ @QueryHint(name = "org.hibernate.cacheable", value = "true") })
	Account findByNameIgnoreCase(String name);

	@QueryHints({ @QueryHint(name = "org.hibernate.cacheable", value = "true") })
	Set<Account> findByNameIgnoreCaseAndIsActiveIsTrue(String name);

	public Optional<Account> findByName(String accountName);

	// Added for CRAN-12
	@QueryHints({ @QueryHint(name = "org.hibernate.cacheable", value = "true") })
	List<Account> findAllByOrderByStartDateDesc();

	public List<Account> findByStartDateGreaterThanEqualAndStartDateLessThanEqualOrderByStartDateDesc(
			Date fromStartDate, Date toStartDate);

	public List<Account> findByStartDateLessThanEqualOrderByStartDateDesc(Date toStartDate);

	public List<Account> findByStartDateGreaterThanEqualOrderByStartDateDesc(Date fromStartDate);

	@Transactional
	@Modifying
	@Query("UPDATE Account a SET a.status = :status WHERE a.accountId = :accountId")
	public Integer changeStatus(Integer accountId, String status);

	@QueryHints({ @QueryHint(name = "org.hibernate.cacheable", value = "true") })
	Set<Account> findAllByIsActiveAndStatusIgnoreCaseNot(Boolean isActive, String Status);

	@QueryHints({ @QueryHint(name = "org.hibernate.cacheable", value = "true") })
	Set<Account> findAllByIsActiveAndStatusIn(Boolean isActive, List<String> status);

	@Query("SELECT a FROM Account a WHERE (a.createdBy = :empId) AND a.isActive = true")
	public Set<Account> getMyAccountDropdown(Long empId);

	@Query("SELECT a FROM Account a WHERE  a.isActive = true")
	public Set<Account> getAllAccountDropdown();

	@Transactional
	@Modifying
	@Query("UPDATE Account a SET a.mapAccountId = :mapAccountId WHERE a.accountId = :accountId")
	public Integer updateMapAccountId(String mapAccountId, Integer accountId);

	@QueryHints({ @QueryHint(name = "org.hibernate.cacheable", value = "true") })
	Set<Account> findByVerticalVerticalIdIn(List<Integer> verticalId);

	Set<Account> findByStatus(String status);

	public Set<Account> findByAccountIdNotAndNameIgnoreCaseAndIsActiveIsTrue(Integer accountId, String name);

	public Set<Account> findByCategoryIgnoreCaseNotAndStatusInAndIsActiveIsTrue(String category,
			String[] allowedStatus);

}