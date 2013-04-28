package com.shaikapsar.vmware.api.extensions.listener.service;

import org.apache.commons.lang.exception.ExceptionUtils;
import org.apache.http.HttpHost;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.integration.annotation.Payload;
import org.springframework.integration.support.converter.MessageConversionException;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;

import com.shaikapsar.vmware.api.extensions.listener.constant.ApplicationConstant;
import com.shaikapsar.vmware.api.extensions.listener.enums.MessageType;
import com.shaikapsar.vmware.api.extensions.listener.model.Notification;
import com.shaikapsar.vmware.api.extensions.listener.util.ApplicationUtils;
import com.shaikapsar.vmware.api.extensions.listener.util.ErrorTypeUtils;
import com.shaikapsar.vmware.api.extensions.listener.util.HttpStatusUtil;
import com.shaikapsar.vmware.api.extensions.listener.util.JAXBUtils;
import com.vmware.vcloud.sdk.VCloudException;

/**
 * @author Shaik Apsar | shaik.apsar@live.com
 *
 */
@Service
public class ExtensionService {

	private Logger logger = LoggerFactory.getLogger(this.getClass());	

	@Autowired
	private RestTemplate serviceRestTemplate;

	@Autowired
	private HttpHost httpSericeHost;

	@Autowired
	private RestService restService;

	private @Value("#{httpConfig['http.service.context']}") String httpSerivecontext; 

	public Notification execute(@Payload Notification notification) throws org.springframework.integration.support.converter.MessageConversionException{

		try {			
			logger.debug("started extension executor at "+ApplicationUtils.rightNow());
			String url=httpSericeHost.toURI()+"/"+httpSerivecontext+notification.getHttpMessage().getRequestUri();
			String method=notification.getHttpMessage().getMethod();
			Object clazz = JAXBUtils.unmarshallResource(notification.getHttpMessage().getBody());
			
			HttpEntity<?> httpEntity=new HttpEntity<Object>(clazz);
			ResponseEntity<?> responseEntity = restService.doExecute(url, method, httpEntity, clazz.getClass());
			if(responseEntity.hasBody()){		
				String response=null;
				try {
					response = JAXBUtils.getString(responseEntity.getBody());
					logger.debug(response);
				} catch (Exception exception) {
					logger.error(exception.getMessage());
				}
				if((ApplicationUtils.isNotEmpty(response) && (!HttpMethod.DELETE.name().equalsIgnoreCase(method)))){
					notification.getHttpMessage().setBody(response.getBytes());
					notification.getHttpMessage().getHeaders().put("Content-Length", Integer.toString(response.getBytes().length));
				}else{
					notification.getHttpMessage().setBody(null);
					notification.getHttpMessage().getHeaders().put("Content-Length", Integer.toString(0));
				}				
			}else{
				notification.getHttpMessage().setBody(null);
				notification.getHttpMessage().getHeaders().put("Content-Length", Integer.toString(0));
			}
			notification.getHttpMessage().setStatusCode(responseEntity.getStatusCode().value());
			notification.getHttpMessage().setRequest(Boolean.FALSE);
			notification.getHttpMessage().getHeaders().put("Content-Type",(String) notification.getHttpMessage().getHeaders().get("Accept"));
			notification.getMessageProperties().getHeaders().put(ApplicationConstant.MSG_TYPE_HEADER, MessageType.getType(MessageType.RESPONSE_TYPE));
			logger.debug("completed extension executor at "+ApplicationUtils.rightNow()+" with HTTP response status code "+responseEntity.getStatusCode().value());

		} catch (RestClientException e) {	
			logger.warn("Failed extension executor by "+ExceptionUtils.getRootCauseMessage(e));
			Throwable throwable = ExceptionUtils.getRootCause(e);
			MessageConversionException mce = new MessageConversionException(throwable.getMessage(), new VCloudException(ErrorTypeUtils.createErrorType(HttpStatusUtil.getHttpStatus(throwable.getMessage()), throwable)));
			throw mce;
		}catch (Exception e) {
			logger.warn("Failed extension executor by "+ExceptionUtils.getRootCauseMessage(e));
			Throwable throwable = ExceptionUtils.getRootCause(e);
			MessageConversionException mce = new MessageConversionException(throwable.getMessage(), new VCloudException(ErrorTypeUtils.createErrorType(HttpStatusUtil.getHttpStatus(throwable.getMessage()), throwable)));
			throw mce;
		}
		return notification;		
	}

}
