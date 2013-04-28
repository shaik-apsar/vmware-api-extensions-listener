/**
 * 
 */
package com.shaikapsar.vmware.api.extensions.listener.service;

import java.net.URI;
import java.net.URISyntaxException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;

/**
 * @author Shaik Apsar | shaik.apsar@live.com
 *
 */
@Service
public class RestService {
	
	private Logger logger = LoggerFactory.getLogger(RestService.class);
	
	@Autowired
	private RestTemplate serviceRestTemplate;
	
	public <T> ResponseEntity<?> doExecute(String url, String method,HttpEntity<?> requestEntity, Class<?> responseType) throws RestClientException, URISyntaxException {
		logger.debug("Executing "+method+" "+url);
		ResponseEntity<?> responseEntity=serviceRestTemplate.exchange(new URI(url) ,HttpMethod.valueOf(method),requestEntity,responseType);
		logger.debug("Executed "+method+" "+url);
		return responseEntity;
	}
}
