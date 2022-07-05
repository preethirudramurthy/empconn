package com.empconn.repositories;

import java.util.List;
import java.util.Optional;
import java.util.Set;

import javax.persistence.QueryHint;

import org.springframework.data.jpa.repository.QueryHints;
import org.springframework.data.repository.CrudRepository;

import com.empconn.persistence.entities.PrimarySkill;
import com.empconn.persistence.entities.SecondarySkill;

public interface SecondarySkillRepository extends CrudRepository<SecondarySkill, Integer> {
	List<SecondarySkill> findByIsActiveTrueAndPrimarySkillOrderByName(PrimarySkill primarySkill);

	@QueryHints({ @QueryHint(name = "org.hibernate.cacheable", value = "true") })
	Set<SecondarySkill> findBySecondarySkillIdInAndIsActiveTrue(List<Integer> ids);

	@QueryHints({ @QueryHint(name = "org.hibernate.cacheable", value = "true") })
	List<SecondarySkill> findByPrimarySkillPrimarySkillIdAndIsActiveTrue(Integer primarySkillId);

	@QueryHints({ @QueryHint(name = "org.hibernate.cacheable", value = "true") })
	List<SecondarySkill> findByPrimarySkillPrimarySkillIdInAndIsActiveTrueAndNameNotOrderByName(
			List<Integer> primarySkillIds, String excludeName);

	@QueryHints({ @QueryHint(name = "org.hibernate.cacheable", value = "true") })
	Optional<SecondarySkill> findByPrimarySkillPrimarySkillIdAndNameIgnoreCaseAndIsActiveTrue(Integer primarySkillId,
			String secondarySkillName);

	@QueryHints({ @QueryHint(name = "org.hibernate.cacheable", value = "true") })
	SecondarySkill findByNameIgnoreCaseAndPrimarySkillNameIgnoreCaseAndIsActiveTrue(String secondarySkillName,
			String primarySkillName);

	List<SecondarySkill> findByPrimarySkillNameIgnoreCaseAndNameIgnoreCaseIn(String name,
			List<String> secondarySkillNames);

}
