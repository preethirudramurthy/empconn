package com.empconn.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.fasterxml.jackson.databind.ObjectMapper;

@Configuration
public class MapperConfig {
	
	@Bean
	public ObjectMapper configMapper() {
		return new ObjectMapper();
	}
}
