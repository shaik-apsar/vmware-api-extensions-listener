package com.shaikapsar.vmware.api.extensions.listener.retry;


import java.io.IOException;
import java.util.Map;

import org.apache.commons.lang.exception.ExceptionUtils;
import org.codehaus.jackson.JsonGenerationException;
import org.codehaus.jackson.JsonParseException;
import org.codehaus.jackson.map.JsonMappingException;
import org.codehaus.jackson.map.ObjectMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.core.AmqpTemplate;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.rabbit.retry.MessageRecoverer;
import org.springframework.beans.factory.annotation.Autowired;

import com.shaikapsar.vmware.api.extensions.listener.constant.ApplicationConstant;
import com.shaikapsar.vmware.api.extensions.listener.enums.MessageType;
import com.shaikapsar.vmware.api.extensions.listener.model.HttpMessage;
import com.shaikapsar.vmware.api.extensions.listener.model.HttpSecurityContext;
import com.shaikapsar.vmware.api.extensions.listener.model.Notification;
import com.shaikapsar.vmware.api.extensions.listener.util.ApplicationUtils;
import com.shaikapsar.vmware.api.extensions.listener.util.JAXBUtils;
import com.vmware.vcloud.api.rest.schema.ErrorType;
import com.vmware.vcloud.api.rest.schema.ObjectFactory;
import com.vmware.vcloud.sdk.VCloudException;

/**
 * @author Shaik Apsar | shaik.apsar@live.com
 *
 */
public class ErrorQueueMessageRecoverer implements MessageRecoverer {

	private Logger logger = LoggerFactory.getLogger(this.getClass());

	@Autowired
	private ObjectFactory objectFactory;

	@Autowired
	private ObjectMapper objectMapper;

	@Autowired
	private AmqpTemplate amqpTemplate;

	@SuppressWarnings("unchecked")
	public void recover(Message message, Throwable cause) {	

		ErrorType errorType=null;
		String errorMessage=null;

		errorType=((VCloudException)ExceptionUtils.getRootCause(cause)).getVcloudError();
		errorMessage=JAXBUtils.getString(errorType);
		byte[] errorBody = errorMessage.getBytes();

		logger.warn(errorMessage);		

		Notification notification=new Notification();
		byte[] body = message.getBody();        
		Object[] args = null;
		try {
			args = objectMapper.readValue(body, Object[].class);
		} catch (JsonParseException e) {
			logger.error(e.getMessage());
		} catch (JsonMappingException e) {
			logger.error(e.getMessage());
		} catch (IOException e) {
			logger.error(e.getMessage());

		}
		
		Message finalMessage=null;

		if(ApplicationUtils.isNotEmpty(args)){
			notification.setHttpMessage(objectMapper.convertValue(args[0], HttpMessage.class));
			notification.setHttpSecurityContext(objectMapper.convertValue(args[1], HttpSecurityContext.class));
			notification.setContext(objectMapper.convertValue(args[2], Map.class)); 
			notification.getHttpMessage().getHeaders().put("Content-Type",(String) notification.getHttpMessage().getHeaders().get("Accept"));
			notification.getHttpMessage().setRequest(Boolean.FALSE);		
			notification.getHttpMessage().getHeaders().put("Content-Length",Integer.toString(errorBody.length));
			notification.getHttpMessage().setBody(errorBody);
			notification.getHttpMessage().setStatusCode(errorType.getMajorErrorCode());
			message.getMessageProperties().getHeaders().put(ApplicationConstant.MSG_TYPE_HEADER, MessageType.getType(MessageType.RESPONSE_TYPE));
			try {
				finalMessage = new Message(objectMapper.writeValueAsBytes(notification.getHttpMessage()), message.getMessageProperties());
			} catch (JsonGenerationException e) {
				logger.error(e.getMessage(), e);
			} catch (JsonMappingException e) {
				logger.error(e.getMessage(), e);
			} catch (IOException e) {
				logger.error(e.getMessage(), e);
			}

			logger.debug("Publishing Exchange "+((String)message.getMessageProperties().getHeaders().get(ApplicationConstant.REPLY_EXCHANGE_HEADER)+" Routing Key"+message.getMessageProperties().getReplyTo()));

			amqpTemplate.send((String)message.getMessageProperties().getHeaders().get(ApplicationConstant.REPLY_EXCHANGE_HEADER),
					message.getMessageProperties().getReplyTo(),finalMessage);
		}
	}

}
