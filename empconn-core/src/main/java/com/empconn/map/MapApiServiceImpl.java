package com.empconn.map;

import java.util.concurrent.Future;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.scheduling.annotation.AsyncResult;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

@Component
public class MapApiServiceImpl implements MapApiService {

	private static final Logger logger = LoggerFactory.getLogger(MapApiServiceImpl.class);

	@Autowired
	RestTemplate restTemplate;

	@Value("${map.session.cookie.id}")
	private String mapSessionId;

	@Override
	public <T> Future<ResponseEntity<String>> doMapRequest(String url, HttpMethod requestMethod, T requestObject) {
		final HttpHeaders headers = new HttpHeaders();
		headers.add("content-type", "application/json");
		headers.add("Cookie", mapSessionId);
		final HttpEntity<Object> entity = new HttpEntity<Object>(requestObject, headers);
		final ResponseEntity<String> response = restTemplate.exchange(url, requestMethod, entity, String.class);
		return new AsyncResult<ResponseEntity<String>>(response);
	}

	@Override
	public <T> Future<ResponseEntity<String>> onError(Exception e, String url, HttpMethod requestMethod,
			T requestObject) {
		final String apiError = e.getMessage() + " :: " + "URL: " + url + " :: " + "requestMethod: " + requestMethod
				+ " :: " + "requestObject: " + requestObject;
		logger.error("Map API error: " + apiError);
		return null;
	}

}
