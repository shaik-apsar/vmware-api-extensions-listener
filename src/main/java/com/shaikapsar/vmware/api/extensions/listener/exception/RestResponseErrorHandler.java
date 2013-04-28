/**
 * 
 */
package com.shaikapsar.vmware.api.extensions.listener.exception;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatus.Series;
import org.springframework.http.ResponseEntity;
import org.springframework.http.client.ClientHttpResponse;
import org.springframework.http.converter.ByteArrayHttpMessageConverter;
import org.springframework.http.converter.FormHttpMessageConverter;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.http.converter.ResourceHttpMessageConverter;
import org.springframework.http.converter.StringHttpMessageConverter;
import org.springframework.http.converter.json.MappingJacksonHttpMessageConverter;
import org.springframework.http.converter.xml.Jaxb2RootElementHttpMessageConverter;
import org.springframework.http.converter.xml.SourceHttpMessageConverter;
import org.springframework.util.ClassUtils;
import org.springframework.web.client.HttpMessageConverterExtractor;
import org.springframework.web.client.ResponseErrorHandler;

import com.shaikapsar.vmware.api.extensions.listener.util.ApplicationUtils;

/**
 * @author Shaik Apsar | shaik.apsar@live.com
 *
 */
public class RestResponseErrorHandler<T> implements ResponseErrorHandler {
	
	private static final boolean jaxb2Present =
			ClassUtils.isPresent("javax.xml.bind.Binder", RestResponseErrorHandler.class.getClassLoader());

	private static final boolean jacksonPresent =
			ClassUtils.isPresent("org.codehaus.jackson.map.ObjectMapper", RestResponseErrorHandler.class.getClassLoader()) &&
					ClassUtils.isPresent("org.codehaus.jackson.JsonGenerator", RestResponseErrorHandler.class.getClassLoader());

	private List<HttpMessageConverter<?>> messageConverters = new ArrayList<HttpMessageConverter<?>>();

	private final HttpMessageConverterExtractor<T> delegate;
	
	private Logger logger = LoggerFactory.getLogger(getClass());

	
	@SuppressWarnings("rawtypes")
	public RestResponseErrorHandler(Class<T> responseType) {
		//Set up the message Converters
		this.messageConverters.add(new ByteArrayHttpMessageConverter());
		this.messageConverters.add(new StringHttpMessageConverter());
		this.messageConverters.add(new ResourceHttpMessageConverter());
		this.messageConverters.add(new FormHttpMessageConverter());
		this.messageConverters.add(new SourceHttpMessageConverter());
		if (jaxb2Present) {
			this.messageConverters.add(new Jaxb2RootElementHttpMessageConverter());
		}
		if (jacksonPresent) {
			this.messageConverters.add(new MappingJacksonHttpMessageConverter());
		}
		this.delegate = new HttpMessageConverterExtractor<T>(responseType, this.messageConverters);
	}

	// If a 400 or 500 series error is returned then we want to handle the error, otherwise not
	public boolean hasError(ClientHttpResponse response) throws IOException {
		HttpStatus statusCode = response.getStatusCode();
		if (statusCode.series() == Series.CLIENT_ERROR || statusCode.series() == Series.SERVER_ERROR)
			return true;
		return false;
	}

	public void handleError(ClientHttpResponse response) throws IOException {
		// Create a new generic Response Entity adding the unmarshalled response, headers and status code
		ResponseEntity<?> responseEntity;
		try {
		responseEntity = new ResponseEntity<T>(this.delegate.extractData(response), response.getHeaders(), response.getStatusCode());
		} catch (Exception e) {
			logger.error(e.getMessage());
			if(ApplicationUtils.isNotEmpty(response.getStatusCode()))
				responseEntity = new ResponseEntity<T>(response.getStatusCode());
			else
				responseEntity = new ResponseEntity<T>(HttpStatus.INTERNAL_SERVER_ERROR);
		}
		
		//Create a new RestResponseException and set the ResponseEntity
		RestResponseException responseException;
		if(response!=null && response.getStatusCode()!=null)
			responseException = new RestResponseException(response.getStatusCode().getReasonPhrase());
		else
			responseException = new RestResponseException();
		
		responseException.setResponseEntity(responseEntity);
		
		throw new IOException(responseException);
	}
}