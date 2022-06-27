package com.empconn.email;

import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import org.thymeleaf.context.Context;
import org.thymeleaf.spring5.SpringTemplateEngine;

@Service("TemplateEngine")
public class TemplateEngine {


	@Autowired
	private SpringTemplateEngine thymeleafTemplateEngine;

	public String render(String layout, Map<String, Object> templateModel) {
		if(StringUtils.isEmpty(layout))
			return null;
		final Context thymeleafContext = new Context();
		thymeleafContext.setVariables(templateModel);
		final String htmlBody = thymeleafTemplateEngine.process(layout, thymeleafContext);
		return htmlBody;
	}
}