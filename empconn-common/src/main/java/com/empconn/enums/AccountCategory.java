package com.empconn.enums;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.function.Predicate;
import java.util.stream.Stream;

import com.empconn.vo.UnitValue;

public enum AccountCategory {
	CLIENT("1", "Client"), INTERNAL("2", "Internal");

	public final String id;
	public final String value;

	AccountCategory(String id, String value) {
		this.id = id;
		this.value = value;
	}

	public String getId() {
		return id;
	}

	public String getValue() {
		return value;
	}

	public static String getValue(String id) {
		AccountCategory ac = getById(id);
		if (ac != null)
			return ac.getValue();
		return null;
	}

	public static List<UnitValue> getUnitValues() {
		final List<UnitValue> categories = new ArrayList<>();
		for (final AccountCategory accountCategory : AccountCategory.values()) {
			categories.add(new UnitValue(accountCategory.getId(), accountCategory.getValue()));
		}
		return categories;
	}

	public static AccountCategory getById(String id) {
		return findBy((Predicate<? super AccountCategory>) ac -> ac.getId().equalsIgnoreCase(id));
	}

	public static AccountCategory getByValue(String value) {
		return findBy((Predicate<? super AccountCategory>) ac -> ac.getValue().equalsIgnoreCase(value));
	}

	private static AccountCategory findBy(final Predicate<? super AccountCategory> predicate) {
		final Optional<AccountCategory> matchingAccountCategory = Stream.of(AccountCategory.values()).filter(predicate)
				.findFirst();
		return matchingAccountCategory.orElse(null);
	}

}
