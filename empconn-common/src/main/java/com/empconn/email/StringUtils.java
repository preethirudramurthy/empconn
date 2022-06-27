package com.empconn.email;

import java.util.HashSet;
import java.util.Set;
import java.util.StringTokenizer;
import java.util.stream.Collectors;

import org.springframework.util.CollectionUtils;

public class StringUtils {

	public static String[] toArray(String source) {
		return toArray(source, ",");
	}

	public static String[] toArray(String source, String delimiter) {

		final Set<String> result = new HashSet<>();

		if (!org.springframework.util.StringUtils.isEmpty(source)) {
			final StringTokenizer mailIdTokens = new StringTokenizer(source, delimiter);
			while (mailIdTokens.hasMoreTokens()) {
				result.add(mailIdTokens.nextToken());
			}
		}
		return result.toArray(new String[0]);

	}

	public static Set<String> toUpperCase(Set<String> roleNames) {
		if (CollectionUtils.isEmpty(roleNames))
			roleNames = new HashSet<>();

		return roleNames.stream().map(String::toUpperCase).collect(Collectors.toSet());
	}

}
