package com.empconn.utilities;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.function.Predicate;

public class IterableUtils {

	public static <T> List<T> toList(Iterable<T> iterable) {
		final List<T> result = new ArrayList<>();
		if (null != iterable)
			iterable.forEach(result::add);
		return result;
	}

	public static <T> Set<T> toSet(Iterable<T> iterable) {
		final Set<T> result = new HashSet<>();
		if (null != iterable)
			iterable.forEach(result::add);
		return result;
	}

	public static void removeIfNullOrEmpty(List<String> stringList) {
		final Predicate<String> nonEmptyPredicate = st -> st == null || st.trim().isEmpty();
		stringList.removeIf(nonEmptyPredicate);
	}

}
