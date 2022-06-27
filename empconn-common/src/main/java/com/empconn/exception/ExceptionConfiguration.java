package com.empconn.exception;

import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;

@Configuration
@PropertySource("classpath:error-messages.properties")
public class ExceptionConfiguration {

}
