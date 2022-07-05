package com.empconn.enums;

import java.util.Optional;
import java.util.function.Predicate;
import java.util.stream.Stream;

public enum ProjectStatus {

	DRAFT("Draft"), OPEN("Open"), INITIATED("Initiated"), GDM_REVIEWED("GDM Reviewed"), GDM_REJECTED("GDM Rejected"),
	SENT_BACK("Sent Back"), RESUBMITTED("Resubmitted"), PMO_REJECTED("PMO Rejected"), PMO_APPROVED("PMO Approved"),
	PROJECT_INACTIVE("Project Inactive"), PROJECT_ON_HOLD("Project On Hold");

	public final String value;

	ProjectStatus(String value) {
		this.value = value;
	}

	public String getValue() {
		return value;
	}

	public static ProjectStatus getByValue(String value) {
		return findBy((Predicate<? super ProjectStatus>) ps -> ps.getValue().equalsIgnoreCase(value));
	}

	private static ProjectStatus findBy(final Predicate<? super ProjectStatus> predicate) {
		final Optional<ProjectStatus> matchingProjectStatus = Stream.of(ProjectStatus.values()).filter(predicate)
				.findFirst();
		return matchingProjectStatus.orElse(null);
	}

	public static String getValueByName(String name) {
		final ProjectStatus status = findBy((Predicate<? super ProjectStatus>) ps -> ps.name().equals(name));
		if (status != null)
			return status.getValue();
		return null;

	}
}
