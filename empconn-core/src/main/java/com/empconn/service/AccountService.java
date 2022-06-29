package com.empconn.service;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.Set;
import java.util.function.Predicate;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import com.empconn.dto.AccountDetailsDto;
import com.empconn.dto.AccountStatusChangeDto;
import com.empconn.dto.AccountSummaryDto;
import com.empconn.dto.IsValidStatusDto;
import com.empconn.dto.SaveAccountDto;
import com.empconn.dto.SavedAccountDto;
import com.empconn.enums.AccountCategory;
import com.empconn.enums.AccountStatus;
import com.empconn.enums.ProjectStatus;
import com.empconn.exception.EmpConnException;
import com.empconn.exception.PreConditionFailedException;
import com.empconn.mapper.AccountAccountDetailsMapper;
import com.empconn.mapper.AccountAccountSummaryMapper;
import com.empconn.mapper.AccountToSaveAccountDtoMapper;
import com.empconn.mapper.AccountUnitValueMapper;
import com.empconn.mapper.ProjectToSaveAccountDtoMapper;
import com.empconn.persistence.entities.Account;
import com.empconn.persistence.entities.ClientLocation;
import com.empconn.persistence.entities.Contact;
import com.empconn.persistence.entities.Employee;
import com.empconn.persistence.entities.Project;
import com.empconn.persistence.entities.ProjectLocation;
import com.empconn.repositories.AccountRepository;
import com.empconn.repositories.ClientLocationRepository;
import com.empconn.repositories.ContactRepository;
import com.empconn.repositories.ProjectRepository;
import com.empconn.security.SecurityUtil;
import com.empconn.util.RolesUtil;
import com.empconn.utilities.IterableUtils;
import com.empconn.vo.UnitValue;

@Service
public class AccountService {

	@Autowired
	private AccountRepository accountRepository;

	@Autowired
	private ProjectRepository projectRepository;

	@Autowired
	private AccountUnitValueMapper accountUnitValueMapper;

	@Autowired
	private AccountAccountDetailsMapper accountAccountDetailsMapper;

	@Autowired
	private AccountAccountSummaryMapper accountAccountSummaryMapper;

	@Autowired
	private AccountToSaveAccountDtoMapper accountToSaveAccountDtoMapper;

	@Autowired
	private ProjectToSaveAccountDtoMapper projectToSaveAccountDtoMapper;

	@Autowired
	ClientLocationRepository clientLocationRepository;

	@Autowired
	ContactRepository contactRepository;

	@Autowired
	private SecurityUtil jwtEmployeeUtil;

	public List<UnitValue> getClientAccounts(String partial) {
		final List<Account> accounts = IterableUtils.toList(accountRepository
				.findByNameContainingIgnoreCaseAndCategoryIgnoreCaseEqualsAndStatusIgnoreCaseNotAndIsActiveTrueOrderByName(
						partial, AccountCategory.CLIENT.name(), AccountStatus.INACTIVE.name()));
		return accountUnitValueMapper.accountsToUnitValues(accounts);

	}

	public List<UnitValue> getInternalAccounts(String partial) {
		final List<Account> accounts = IterableUtils.toList(accountRepository
				.findByNameContainingIgnoreCaseAndCategoryIgnoreCaseEqualsAndStatusIgnoreCaseNotAndIsActiveTrueOrderByName(
						partial, AccountCategory.INTERNAL.name(), AccountStatus.INACTIVE.name()));
		return accountUnitValueMapper.accountsToUnitValues(accounts);
	}

	public IsValidStatusDto isValidNewAccount(String accountName) {
		final Set<Account> accounts = accountRepository.findByNameIgnoreCaseAndIsActiveIsTrue(accountName);
		if (accounts.isEmpty())
			return new IsValidStatusDto(true, null);

		return new IsValidStatusDto(false, accounts.iterator().next().getStatus());
	}

	public SavedAccountDto saveAccount(SaveAccountDto dto) {
		validateSaveAccount(dto);
		if (dto.getAccountId() == null && dto.getProjectId() == null)
			return saveAccountSaveProject(dto);
		else if (dto.getAccountId() != null && dto.getProjectId() == null)
			return updateAccountSaveProject(dto);
		return updateAccountUpdateProject(dto);
	}

	private void validateSaveAccount(SaveAccountDto dto) {

		if ((dto.getAccountId() == null && !isValidNewAccount(dto.getName()).getIsValid()) ||
		(dto.getAccountId() != null
		&& !isValidNewNameForAccount(dto.getName(), Integer.valueOf(dto.getAccountId()))))
			throw new EmpConnException("AccountExists");

	}

	private boolean isValidNewNameForAccount(String name, Integer accountId) {
		final Set<Account> accounts = accountRepository.findByAccountIdNotAndNameIgnoreCaseAndIsActiveIsTrue(accountId,
				name);
		return (accounts.isEmpty());
	}

	private SavedAccountDto saveAccountSaveProject(SaveAccountDto dto) {
		final Account account = accountToSaveAccountDtoMapper.saveAccountDtoToAccount(dto);
		final Account savedAccount = accountRepository.save(account);

		final Project project = projectToSaveAccountDtoMapper.saveAccountDtoMapperToProject(dto);
		project.setAccount(savedAccount);
		final Project savedProject = projectRepository.save(project);
		softDeleteOnSaveAccount(savedAccount);
		return new SavedAccountDto(savedAccount.getAccountId().toString(), savedProject.getProjectId().toString());
	}

	private SavedAccountDto updateAccountSaveProject(SaveAccountDto dto) {
		Optional<Account> a = accountRepository.findById(Integer.valueOf(dto.getAccountId()));
		final Account existAccount = a.isPresent()? a.get():null;

		final Account account = accountToSaveAccountDtoMapper.saveAccountDtoToAccount(dto, existAccount);
		if (existAccount != null && existAccount.getStatus().equals(AccountStatus.TEMP.name()))
			account.setStatus(AccountStatus.ACTIVE.name());
		final Account savedAccount = accountRepository.save(account);
		final Project project = projectToSaveAccountDtoMapper.saveAccountDtoMapperToProject(dto);
		project.setAccount(savedAccount);
		final Project savedProject = projectRepository.save(project);
		softDeleteOnSaveAccount(savedAccount);
		return new SavedAccountDto(savedAccount.getAccountId().toString(), savedProject.getProjectId().toString());
	}

	private SavedAccountDto updateAccountUpdateProject(SaveAccountDto dto) {
		
		Optional<Account> a = accountRepository.findById(Integer.valueOf(dto.getAccountId()));
		final Account existAccount = a.isPresent()? a.get(): null;
		final Account account = accountToSaveAccountDtoMapper.saveAccountDtoToAccount(dto, existAccount);
		if (existAccount != null)
			account.setStatus(existAccount.getStatus());
		final Account savedAccount = accountRepository.save(account);
		softDeleteOnSaveAccount(savedAccount);
		return new SavedAccountDto(savedAccount.getAccountId().toString(), dto.getProjectId());
	}

	public void softDeleteOnSaveAccount(Account account) {
		if (CollectionUtils.isEmpty(account.getClientLocations())) {
			clientLocationRepository.softDeleteAllClientLocationsForAccount(account.getAccountId());
			final Set<Long> deletedLocationIds = clientLocationRepository
					.findSoftDeletedIdsForAccount(account.getAccountId());
			contactRepository.softDeleteAllContactsForClientLocations(deletedLocationIds);
		} else {
			final Set<Long> savedLocationIds = account.getClientLocations().stream()
					.map(ClientLocation::getClientLocationId).collect(Collectors.toSet());
			clientLocationRepository.softDeleteClientLocationsForAccount(savedLocationIds, account.getAccountId());
			final Set<Long> deletedLocationIds = clientLocationRepository
					.findSoftDeletedIdsForAccount(account.getAccountId());
			final Set<Long> savedContactIds = account.getClientLocations().stream().map(cl -> {
				if (!CollectionUtils.isEmpty(cl.getContacts()))
					return cl.getContacts().stream().map(Contact::getContactId).collect(Collectors.toSet());
				else
					return null;

			}).filter(Objects::nonNull).flatMap(Collection::stream).collect(Collectors.toSet());
			contactRepository.softDeleteContactsForClientLocations(savedContactIds,
					Stream.of(savedLocationIds, deletedLocationIds).flatMap(Set::stream).collect(Collectors.toSet()));
		}
	}

	public AccountDetailsDto getAccountDetails(String accountId) {
		final Optional<Account> account = accountRepository.findById(Integer.parseInt(accountId));
		if (!account.isPresent())
			return null;
		return accountAccountDetailsMapper.accountToAccountDetailsDto(account.get());
	}

	// Added for CRAN-12
	public List<AccountSummaryDto> getAccountSummaryList(final Date fromStartDate, final Date toStartDate) {
		List<Account> dataListFromDB = null;
		if (fromStartDate != null && toStartDate == null) {
			dataListFromDB = accountRepository.findByStartDateGreaterThanEqualOrderByStartDateDesc(fromStartDate);
		} else if (fromStartDate == null && toStartDate != null) {
			dataListFromDB = accountRepository.findByStartDateLessThanEqualOrderByStartDateDesc(toStartDate);
		} else if (fromStartDate != null && toStartDate != null) {
			dataListFromDB = accountRepository
					.findByStartDateGreaterThanEqualAndStartDateLessThanEqualOrderByStartDateDesc(fromStartDate,
							toStartDate);
		} else {
			dataListFromDB = accountRepository.findAllByOrderByStartDateDesc();
		}

		return accountAccountSummaryMapper.accountsToAccountSummaryDtos(dataListFromDB);
	}

	public List<UnitValue> getActiveAccounts(Boolean withBench) {
		final List<String> status = Arrays.asList("ACTIVE");

		Set<Account> accounts = IterableUtils.toSet(accountRepository.findAllByIsActiveAndStatusIn(true, status));

		final Employee loggedInUser = jwtEmployeeUtil.getLoggedInEmployee();

		final List<Predicate<Account>> accountPredicate = new ArrayList<>();

		final Predicate<Project> activeProject = p -> p.getIsActive()
				&& (p.getCurrentStatus().equalsIgnoreCase(ProjectStatus.PMO_APPROVED.toString()));

		final Predicate<Project> gdmPredicate = p -> ((p.getEmployee1() != null
				&& p.getEmployee1().getEmployeeId().equals(loggedInUser.getEmployeeId()))
				|| (p.getEmployee2() != null && p.getEmployee2().getEmployeeId().equals(loggedInUser.getEmployeeId())));

		if (withBench != null && !withBench) {
			accountPredicate.add(a -> !a.getName().equalsIgnoreCase("Bench"));
		}

		final Predicate<ProjectLocation> managerPredicate = p -> ((p.getEmployee1() != null
				&& p.getEmployee1().getEmployeeId().equals(loggedInUser.getEmployeeId()))
				|| (p.getEmployee2() != null && p.getEmployee2().getEmployeeId().equals(loggedInUser.getEmployeeId()))
				|| (p.getEmployee3() != null && p.getEmployee3().getEmployeeId().equals(loggedInUser.getEmployeeId())));

		if (RolesUtil.isRMG(loggedInUser)) {
			accounts = accounts.stream().filter(accountPredicate.stream().reduce(x -> true, Predicate::and))
					.flatMap(a -> a.getProjects().stream()).filter(activeProject).map(Project::getAccount)
					.collect(Collectors.toSet());
		} else if (RolesUtil.isGDMAndManager(loggedInUser)) {
			accounts = accounts.stream().filter(accountPredicate.stream().reduce(x -> true, Predicate::and))
					.flatMap(a -> a.getProjects().stream()).filter(activeProject)
					.filter(gdmPredicate.or(p -> p.getProjectLocations().stream().anyMatch(managerPredicate)))
					.map(Project::getAccount).collect(Collectors.toSet());
		} else if (RolesUtil.isOnlyGDM(loggedInUser)) {
			accounts = accounts.stream().filter(accountPredicate.stream().reduce(x -> true, Predicate::and))
					.flatMap(a -> a.getProjects().stream()).filter(activeProject).filter(gdmPredicate)
					.map(Project::getAccount).collect(Collectors.toSet());
		} else if (RolesUtil.isAManager(loggedInUser)) {
			accounts = accounts.stream().filter(accountPredicate.stream().reduce(x -> true, Predicate::and))
					.flatMap(a -> a.getProjects().stream()).filter(activeProject)
					.filter(p -> p.getProjectLocations().stream().anyMatch(managerPredicate)).map(Project::getAccount)
					.collect(Collectors.toSet());
		}

		final Set<UnitValue> values = accountUnitValueMapper.accountsToUnitValues(accounts);
		final List<UnitValue> value = values.stream().collect(Collectors.toList());
		Collections.sort(value, (p1, p2) -> p1.getValue().compareToIgnoreCase(p2.getValue()));
		return value;
	}

	public void activateAccount(AccountStatusChangeDto dto) {
		Account account = null;
		if (dto.getAccountId() != null) {
			Optional<Account> a = accountRepository.findById(Integer.valueOf(dto.getAccountId()));
			account = a.isPresent()? a.get():null;
		}
			
		else if (dto.getAccountName() != null)
			account = accountRepository.findByNameIgnoreCase(dto.getAccountName());

		if (account != null) {
			if (!account.getStatus().equals(AccountStatus.INACTIVE.name()))
				throw new PreConditionFailedException("CheckAcccountStatusInactive");

			account.setStatus(AccountStatus.ACTIVE.name());
			if (account.getEndDate() != null && account.getEndDate().before(new Date()))
				account.setEndDate(null);
			accountRepository.save(account);

		}
	}

}
