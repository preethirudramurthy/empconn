package com.empconn.enums;

import java.util.Optional;
import java.util.function.Predicate;
import java.util.stream.Stream;

public enum AccountStatus {

	TEMP("Temp"), ACTIVE("Active"), INACTIVE("Inactive");

	public final String value;

	AccountStatus(String value) {
		this.value = value;
	}

	public String getValue() {
		return value;
	}

	public static AccountStatus getByValue(String value) {
		return findBy((Predicate<? super AccountStatus>) as -> as.getValue().equalsIgnoreCase(value));
	}

	private static AccountStatus findBy(final Predicate<? super AccountStatus> predicate) {
		final Optional<AccountStatus> matchingAccountStatus = Stream.of(AccountStatus.values()).filter(predicate)
				.findFirst();
		return matchingAccountStatus.orElse(null);
	}

}
