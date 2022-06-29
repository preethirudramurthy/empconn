package com.empconn.service;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;

import com.google.common.base.Strings;
import com.empconn.constants.ApplicationConstants;
import com.empconn.constants.ExceptionConstants;
import com.empconn.dto.ChecklistInactivateDto;
import com.empconn.dto.ChecklistUpdateDto;
import com.empconn.dto.EmployeeRoleAssignmentDto;
import com.empconn.dto.EmployeeSkillRequest;
import com.empconn.dto.HorizontalDto;
import com.empconn.dto.HorizontalRequestDto;
import com.empconn.dto.MasterSkillsResponseDto;
import com.empconn.dto.MasterSkillsUpdateRequestDto;
import com.empconn.dto.MasterSkillsUpdateResponseDto;
import com.empconn.dto.ProjectSubcategoryInactivateDto;
import com.empconn.dto.ProjectSubcategoryUpdateDto;
import com.empconn.dto.Skills;
import com.empconn.dto.VerticalDto;
import com.empconn.dto.VerticalRequestDto;
import com.empconn.email.StringUtils;
import com.empconn.enums.ProjectStatus;
import com.empconn.exception.EmpConnException;
import com.empconn.exception.PreConditionFailedException;
import com.empconn.mapper.ChecklistMapper;
import com.empconn.mapper.ProjectSubcategoryMapper;
import com.empconn.mapper.SkillsMapper;
import com.empconn.persistence.entities.Checklist;
import com.empconn.persistence.entities.Employee;
import com.empconn.persistence.entities.EmployeeSkill;
import com.empconn.persistence.entities.Horizontal;
import com.empconn.persistence.entities.PrimarySkill;
import com.empconn.persistence.entities.ProjectSubCategory;
import com.empconn.persistence.entities.SecondarySkill;
import com.empconn.persistence.entities.Vertical;
import com.empconn.repositories.ChecklistRespository;
import com.empconn.repositories.EmployeeRepository;
import com.empconn.repositories.EmployeeSkillRepository;
import com.empconn.repositories.HorizontalRepository;
import com.empconn.repositories.PrimarySkillRepository;
import com.empconn.repositories.ProjectRepository;
import com.empconn.repositories.ProjectSubCategoryRepository;
import com.empconn.repositories.SecondarySkillRepository;
import com.empconn.repositories.VerticalRepository;
import com.empconn.security.SecurityUtil;

@Service
public class BootstrapService {

	private static final Logger logger = LoggerFactory.getLogger(BootstrapService.class);

	@Autowired
	private EmployeeRepository employeeRepository;

	@Autowired
	private EmployeeRoleService employeeRoleService;

	@Autowired
	private EmployeeSkillRepository employeeSkillRepository;

	@Autowired
	private SecondarySkillRepository secondarySkillRepository;

	@Autowired
	private SecurityUtil securityUtil;

	@Autowired
	private VerticalRepository verticalRepository;

	@Autowired
	private HorizontalRepository horizontalRepository;

	@Autowired
	private ChecklistRespository checklistRespository;

	@Autowired
	private ProjectSubCategoryRepository projectSubCategoryRepository;

	@Autowired
	ProjectSubcategoryMapper projectSubcategoryMapper;

	@Autowired
	ChecklistMapper checklistMapper;

	@Autowired
	private SkillsMapper skillsMapper;

	@Autowired
	private PrimarySkillRepository primarySkillRepository;

	@Autowired
	ProjectRepository projectRepository;

	@Transactional
	public void assignDefaultRole() {
		logger.debug("Assign Default role to all active employees");
		for (final Employee employee : employeeRepository.findAllByIsActiveTrue())
			employeeRoleService.assignRoleIfNotSet(employee, ApplicationConstants.DEFAULT_ROLE);
	}

	@Transactional
	public void assignEmployeeRoles(Set<EmployeeRoleAssignmentDto> employeeRoleAssignments) {
		logger.debug("Asign roles to employees");
		if (CollectionUtils.isEmpty(employeeRoleAssignments))
			return;

		for (final EmployeeRoleAssignmentDto employeeRoleAssignmentDto : employeeRoleAssignments) {

			final String empCode = employeeRoleAssignmentDto.getEmpCode();
			final Set<String> roleNamesInUpperCase = StringUtils.toUpperCase(employeeRoleAssignmentDto.getRoleNames());

			final Employee employee = employeeRepository.findByEmpCodeAndIsActiveTrue(empCode);

			if (null == employee) {
				logger.error("Employee with empCode {} doesn't exist. Skipping this employee.", empCode);
				continue;
			}

			employeeRoleService.removeCurrentRolesNotPartOf(employee, roleNamesInUpperCase);
			roleNamesInUpperCase.forEach(roleName -> employeeRoleService.assignRoleIfNotSet(employee, roleName));
		}

	}

	@Transactional
	public void assignEmployeeSkills(List<EmployeeSkillRequest> dto) {
		logger.debug("Assign new skills to employees");

		if (CollectionUtils.isEmpty(dto))
			return;

		for (final EmployeeSkillRequest skillRequest : dto) {
			final Employee employee = employeeRepository
					.findByEmpCodeIgnoreCaseAndIsActiveTrue(skillRequest.getEmpCode().toLowerCase().trim());

			for (final Skills skills : skillRequest.getSkills()) {
				final List<EmployeeSkill> employeeSkillsList = employeeSkillRepository
						.findByEmployeeAndSecondarySkillPrimarySkillNameIgnoreCase(employee,
								skills.getPrimarySkill().toLowerCase().trim());
				final List<String> alreadyPresentSkills = new ArrayList<>();
				if (!CollectionUtils.isEmpty(employeeSkillsList)) {
					employeeSkillsList.stream().forEach(es -> 
						alreadyPresentSkills.add(es.getSecondarySkill().getName().toLowerCase());
					);
				}
				if (!CollectionUtils.isEmpty(skills.getSecondarySkills())) {
					for (final String skill : skills.getSecondarySkills()) {
						if (!alreadyPresentSkills.contains(skill.toLowerCase().trim())) {
							populateEmployeeSkillBySecondarySkill(employee, skill.toLowerCase().trim(),
									skills.getPrimarySkill().toLowerCase().trim());
						}
					}
				} else {
					if (!alreadyPresentSkills.contains(ApplicationConstants.DEFAULT_SECONDARY_SKILL.toLowerCase())) {
						populateEmployeeSkillBySecondarySkill(employee, ApplicationConstants.DEFAULT_SECONDARY_SKILL,
								skills.getPrimarySkill().toLowerCase().trim());
					}
				}
			}
		}
	}

	private void populateEmployeeSkillBySecondarySkill(final Employee employee, final String secondarySkillName,
			final String primarySkillName) {
		final SecondarySkill secondarySkill = secondarySkillRepository
				.findByNameIgnoreCaseAndPrimarySkillNameIgnoreCaseAndIsActiveTrue(secondarySkillName, primarySkillName);
		if (secondarySkill == null) {
			throw new EmpConnException(ExceptionConstants.SKILL_NOT_AVAILABLE);
		}
		populateAndSaveEntity(employee, secondarySkill);
	}

	private void populateAndSaveEntity(final Employee employee, final SecondarySkill secondarySkill) {
		final EmployeeSkill employeeSkill = new EmployeeSkill(employee, secondarySkill);
		employeeSkill.setCreatedBy(securityUtil.getLoggedInEmployeeId());
		employeeSkill.setIsActive(true);
		employeeSkillRepository.save(employeeSkill);
	}

	@Transactional
	public void vertical(VerticalRequestDto dto) {
		final Optional<Vertical> newVertical = verticalRepository
				.findByNameIgnoreCaseAndIsActiveTrue(dto.getNewVertical().trim().toLowerCase());

		if (newVertical.isPresent())
			throw new EmpConnException(ExceptionConstants.NEW_VERTICAL_ALREADY_AVAILABLE);

		if (Strings.isNullOrEmpty(dto.getOldVertical())) {
			final Vertical vertical = new Vertical();
			vertical.setName(dto.getNewVertical().trim());
			vertical.setCreatedBy(securityUtil.getLoggedInEmployeeId());
			vertical.setIsActive(true);
			verticalRepository.save(vertical);
		} else {
			final Optional<Vertical> oldVertical = verticalRepository
					.findByNameIgnoreCaseAndIsActiveTrue(dto.getOldVertical().trim().toLowerCase());
			if (oldVertical.isPresent()) {
				final Vertical vertical = oldVertical.get();
				vertical.setName(dto.getNewVertical().trim());
				verticalRepository.save(vertical);
			} else {
				throw new EmpConnException(ExceptionConstants.OLD_VERTICAL_NOT_AVAILABLE);
			}
		}
	}

	@Transactional
	public void horizontal(HorizontalRequestDto dto) {
		final Optional<Horizontal> newHorizontal = horizontalRepository
				.findByNameIgnoreCaseAndIsActiveTrue(dto.getNewHorizontal().trim().toLowerCase());
		if (newHorizontal.isPresent())
			throw new EmpConnException(ExceptionConstants.NEW_HORIZONTAL_ALREADY_AVAILABLE);

		if (Strings.isNullOrEmpty(dto.getOldHorizontal())) {
			final Horizontal horizontal = new Horizontal();
			horizontal.setName(dto.getNewHorizontal().trim());
			horizontal.setCreatedBy(securityUtil.getLoggedInEmployeeId());
			horizontal.setIsActive(true);
			horizontalRepository.save(horizontal);
		} else {
			final Optional<Horizontal> oldHorizontal = horizontalRepository
					.findByNameIgnoreCaseAndIsActiveTrue(dto.getOldHorizontal().trim().toLowerCase());
			if (oldHorizontal.isPresent()) {
				final Horizontal horizontal = oldHorizontal.get();
				horizontal.setName(dto.getNewHorizontal().trim());
				horizontalRepository.save(horizontal);
			} else {
				throw new EmpConnException(ExceptionConstants.OLD_HORIZONTAL_NOT_AVAILABLE);
			}
		}
	}

	public Set<String> getVertical() {
		final Set<Vertical> verticals = verticalRepository.findByIsActiveTrue();
		final Set<String> verticalSet = new HashSet<>();
		verticals.stream().forEach(v -> verticalSet.add(v.getName()));
		return verticalSet;
	}

	public Set<String> getHorizontal() {
		final Set<Horizontal> horizontals = horizontalRepository.findByIsActiveTrue();
		final Set<String> horizontalSet = new HashSet<>();
		horizontals.stream().forEach(h -> horizontalSet.add(h.getName()));
		return horizontalSet;
	}

	public void softDeleteVertical(VerticalDto dto) {
		final Optional<Vertical> vertical = verticalRepository
				.findByNameIgnoreCaseAndIsActiveTrue(dto.getVerticalName().trim().toLowerCase());
		if (!vertical.isPresent())
			throw new EmpConnException(ExceptionConstants.VERTICAL_NOT_AVAILABLE);

		if (isVerticalHasActiveOrOnHoldProjects(vertical.get()))
			throw new PreConditionFailedException("activeOrOnHoldProjectsWithVertical");

		verticalRepository.softDelete(dto.getVerticalName());
	}

	private boolean isVerticalHasActiveOrOnHoldProjects(Vertical vertical) {
		final String[] statuses = { ProjectStatus.PMO_APPROVED.name(), ProjectStatus.PROJECT_ON_HOLD.name() };
		final Integer projectCount = projectRepository.countByCurrentStatusInAndAccountVerticalEquals(statuses,
				vertical);

		return projectCount > 0;
	}

	public void softDeleteHorizontal(HorizontalDto dto) {
		final Optional<Horizontal> horizontal = horizontalRepository
				.findByNameIgnoreCaseAndIsActiveTrue(dto.getHorizontalName().trim().toLowerCase());
		if (!horizontal.isPresent())
			throw new EmpConnException(ExceptionConstants.HORIZONTAL_NOT_AVAILABLE);

		if (isHorizontalHasActiveOrOnHoldProjects(horizontal.get()))
			throw new PreConditionFailedException("activeOrOnHoldProjectsWithHorizontal");

		horizontalRepository.softDelete(dto.getHorizontalName());
	}

	private boolean isHorizontalHasActiveOrOnHoldProjects(Horizontal horizontal) {
		final String[] statuses = { ProjectStatus.PMO_APPROVED.name(), ProjectStatus.PROJECT_ON_HOLD.name() };
		final Integer projectCount = projectRepository.countByCurrentStatusInAndHorizontalEquals(statuses, horizontal);

		return projectCount > 0;
	}

	public List<String> getProjectSubCategoryNames() {
		final List<ProjectSubCategory> projectSubCategories = projectSubCategoryRepository
				.findByIsActiveTrue(Sort.by(Sort.Direction.ASC, "name"));
		return projectSubCategories.stream().map(ProjectSubCategory::getName).collect(Collectors.toList());
	}

	public void addOrUpdateProjectSubCategory(ProjectSubcategoryUpdateDto dto) {
		validateForProjectSubcategoryNameDuplicate(dto.getNewProjectSubcategory());

		if (org.apache.commons.lang3.StringUtils.isEmpty(dto.getOldProjectSubcategory())) {
			final ProjectSubCategory category = projectSubcategoryMapper
					.stringToProjectSubCategory(dto.getNewProjectSubcategory());
			projectSubCategoryRepository.save(category);
			return;
		}
		final Optional<ProjectSubCategory> categoryExist = projectSubCategoryRepository
				.findByNameIgnoreCaseEqualsAndIsActiveTrue(dto.getOldProjectSubcategory());
		if (categoryExist.isPresent()) {
			final ProjectSubCategory category = categoryExist.get();
			category.setName(dto.getNewProjectSubcategory());
			projectSubCategoryRepository.save(category);
			return;
		}
		throw new EmpConnException("");
	}

	public void validateForProjectSubcategoryNameDuplicate(String projectSubcategoryName) {
		final Optional<ProjectSubCategory> categoryExist = projectSubCategoryRepository
				.findByNameIgnoreCaseEqualsAndIsActiveTrue(projectSubcategoryName);
		if (categoryExist.isPresent())
			throw new EmpConnException("projectSubcategoryDuplicate");
	}

	public void inactivateProjectSubCategory(ProjectSubcategoryInactivateDto dto) {
		final Optional<ProjectSubCategory> categoryExist = projectSubCategoryRepository
				.findByNameIgnoreCaseEqualsAndIsActiveTrue(dto.getProjectSubcategory());
		if (categoryExist.isPresent()) {

			if (isProjectSubcategoryHasActiveOrOnHoldProjects(categoryExist.get()))
				throw new PreConditionFailedException("activeOrOnHoldProjectsWithSubCategory");

			final ProjectSubCategory category = categoryExist.get();
			category.setIsActive(false);
			projectSubCategoryRepository.save(category);
			return;
		}
		throw new EmpConnException("");
	}

	private boolean isProjectSubcategoryHasActiveOrOnHoldProjects(ProjectSubCategory projectSubCategory) {
		final String[] statuses = { ProjectStatus.PMO_APPROVED.name(), ProjectStatus.PROJECT_ON_HOLD.name() };
		final Integer projectCount = projectRepository.countByCurrentStatusInAndProjectSubCategoryEquals(statuses,
				projectSubCategory);

		return projectCount > 0;

	}

	public List<String> getChecklistNames() {
		final List<Checklist> checklists = checklistRespository
				.findByIsActiveTrue(Sort.by(Sort.Direction.ASC, "checklistId"));
		return checklists.stream().map(Checklist::getName).collect(Collectors.toList());
	}

	public void addOrUpdateChecklist(ChecklistUpdateDto dto) {
		validateForChecklistNameDuplicate(dto.getNewChecklist());
		if (org.apache.commons.lang3.StringUtils.isEmpty(dto.getOldChecklist())) {
			final Checklist checklist = checklistMapper.stringToChecklist(dto.getNewChecklist());
			checklistRespository.save(checklist);
			return;
		}
		final Optional<Checklist> checklistExist = checklistRespository
				.findByNameIgnoreCaseEqualsAndIsActiveTrue(dto.getOldChecklist());
		if (checklistExist.isPresent()) {
			final Checklist checklist = checklistExist.get();
			checklist.setName(dto.getNewChecklist());
			checklistRespository.save(checklist);
			return;
		}
		throw new EmpConnException("");

	}

	public void validateForChecklistNameDuplicate(String checklistName) {
		final Optional<Checklist> checklistExist = checklistRespository
				.findByNameIgnoreCaseEqualsAndIsActiveTrue(checklistName);
		if (checklistExist.isPresent())
			throw new EmpConnException("checklistDuplicate");
	}

	public void inactivateChecklist(ChecklistInactivateDto dto) {
		final Optional<Checklist> checklistExist = checklistRespository
				.findByNameIgnoreCaseEqualsAndIsActiveTrue(dto.getChecklist());
		if (checklistExist.isPresent()) {
			final Checklist checklist = checklistExist.get();
			checklist.setIsActive(false);
			checklistRespository.save(checklist);
			return;
		}
		throw new EmpConnException("");

	}

	public List<MasterSkillsUpdateResponseDto> createOrUpdateMasterSkills(
			List<MasterSkillsUpdateRequestDto> masterSkillDtos) {
		final List<MasterSkillsUpdateResponseDto> responseDto = new ArrayList<>();
		for (final MasterSkillsUpdateRequestDto masterSkillDto : masterSkillDtos) {
			final PrimarySkill primarySkill = createPrimarySkillIfNotExists(masterSkillDto.getPrimarySkill());
			final List<SecondarySkill> secondarySkills = new ArrayList<>();
			if (!CollectionUtils.isEmpty(masterSkillDto.getSecondarySkills())) {
				for (final String secondarySkillName : masterSkillDto.getSecondarySkills()) {
					final SecondarySkill secondarySkill = createSecondarySkillIfNotExists(secondarySkillName,
							primarySkill);
					secondarySkills.add(secondarySkill);
				}
			}
			responseDto.add(skillsMapper.primarySkillAndSecondarySkillsToMasterSkillsUpdateResponseDto(primarySkill,
					secondarySkills));
		}
		return responseDto;
	}

	private PrimarySkill createPrimarySkillIfNotExists(String primarySkillName) {
		final Optional<PrimarySkill> primarySkill = primarySkillRepository
				.findByNameIgnoreCaseAndIsActiveTrue(primarySkillName);
		if (primarySkill.isPresent())
			return primarySkill.get();
		return createPrimarySkillAndDefaultSecondarySkill(primarySkillName);
	}

	private PrimarySkill createPrimarySkillAndDefaultSecondarySkill(String primarySkillName) {
		final PrimarySkill primarySkill = primarySkillRepository
				.save(skillsMapper.primarySkillNameToPrimarySkill(primarySkillName));
		secondarySkillRepository.save(skillsMapper.primarySkillAndNameToSecondarySkillMapper(primarySkill,
				ApplicationConstants.DEFAULT_SECONDARY_SKILL));
		return primarySkill;
	}

	private SecondarySkill createSecondarySkillIfNotExists(String secondarySkillName, PrimarySkill primarySkill) {
		final Optional<SecondarySkill> secondarySkill = secondarySkillRepository
				.findByPrimarySkillPrimarySkillIdAndNameIgnoreCaseAndIsActiveTrue(primarySkill.getPrimarySkillId(),
						secondarySkillName);
		if (secondarySkill.isPresent())
			return secondarySkill.get();
		return secondarySkillRepository
				.save(skillsMapper.primarySkillAndNameToSecondarySkillMapper(primarySkill, secondarySkillName));
	}

	public List<MasterSkillsResponseDto> getSkills() {
		final List<PrimarySkill> primarySkills = primarySkillRepository.findByIsActiveTrue();
		return skillsMapper.primarySkillsToMasterSkillResponseDtos(primarySkills);
	}

	public void softDeleteSkills(MasterSkillsUpdateRequestDto dto) {
		final Optional<PrimarySkill> primarySkill = primarySkillRepository
				.findByNameIgnoreCaseAndIsActiveTrue(dto.getPrimarySkill());
		if (primarySkill.isPresent()) {
			if (CollectionUtils.isEmpty(dto.getSecondarySkills()))
				softDeletePrimaryAndAllItsSecondarySkills(primarySkill.get());
			else
				softDeleteSecondarySkillsOfPrimary(primarySkill.get(), dto.getSecondarySkills());
			return;
		}
		throw new EmpConnException("");

	}

	private void softDeleteSecondarySkillsOfPrimary(PrimarySkill primarySkill, List<String> secondarySkillNames) {
		final List<SecondarySkill> secondarySkills = secondarySkillRepository
				.findByPrimarySkillNameIgnoreCaseAndNameIgnoreCaseIn(primarySkill.getName(), secondarySkillNames);
		for (final SecondarySkill skill : secondarySkills) {
			skill.setIsActive(false);
		}
		secondarySkillRepository.saveAll(secondarySkills);
	}

	public void softDeletePrimaryAndAllItsSecondarySkills(PrimarySkill primarySkill) {
		primarySkill.setIsActive(false);
		primarySkillRepository.save(primarySkill);

		final Set<SecondarySkill> secondarySkills = primarySkill.getSecondarySkills();
		for (final SecondarySkill skill : secondarySkills) {
			skill.setIsActive(false);
		}
		secondarySkillRepository.saveAll(secondarySkills);
	}
}
