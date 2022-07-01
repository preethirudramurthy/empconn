package com.empconn.utilities;

import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

@Component
public class StringTemplateEngine {

	private static final Logger logger = LoggerFactory.getLogger(StringTemplateEngine.class);

	private static final String TEMPLATE_START = "${";
	private static final String TEMPLATE_END = "}";

	public String render(String content, Map<String, Object> model) {

		try {
			final Set<String> placeholders = getPlaceHolders(content);
			for (final String placeholder : placeholders) {
				content = content.replace(TEMPLATE_START + placeholder + TEMPLATE_END, model.get(placeholder).toString());
			}
		} catch (final Exception e) {
			logger.error("Exception in rendering the string template", e);
		}

		return content;

	}

	private Set<String> getPlaceHolders(String content) {
		final HashSet<String> placeholders = new HashSet<>();

		int startIndex = 0;

		while (true) {
			startIndex = content.indexOf(TEMPLATE_START, startIndex);
			final int endIndex = content.indexOf(TEMPLATE_END, startIndex);
			if (startIndex == -1 || endIndex == -1)
				break;
			final String placeholder = content.substring(startIndex + TEMPLATE_START.length(), endIndex);
			placeholders.add(placeholder.trim());
			startIndex = endIndex;

		}

		return placeholders;
	}

}