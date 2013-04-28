/**
 * 
 */
package com.shaikapsar.vmware.api.extensions.listener.service;

import java.io.IOException;

import org.codehaus.jackson.JsonGenerationException;
import org.codehaus.jackson.map.JsonMappingException;
import org.codehaus.jackson.map.ObjectMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.core.AmqpTemplate;
import org.springframework.amqp.core.Message;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.integration.annotation.Payload;
import org.springframework.stereotype.Service;

import com.shaikapsar.vmware.api.extensions.listener.constant.ApplicationConstant;
import com.shaikapsar.vmware.api.extensions.listener.model.Notification;

/**
 * @author Shaik Apsar | shaik.apsar@live.com
 *
 */
@Service
public class PublisherService {

	private Logger logger = LoggerFactory.getLogger(this.getClass());	

	@Autowired
	private ObjectMapper objectMapper;

	@Autowired
	private AmqpTemplate amqpTemplate;

	public void publish(@Payload Notification notification) {
		
		logger.debug("Preparing response payload to response back to Extension Serivce");

		Message finalMessage=null;

		try {
			finalMessage = new Message(objectMapper.writeValueAsBytes(notification.getHttpMessage()), notification.getMessageProperties());
		} catch (JsonGenerationException e) {
			logger.error(e.getMessage(),e);
		} catch (JsonMappingException e) {
			logger.error(e.getMessage(),e);
		} catch (IOException e) {
			logger.error(e.getMessage(),e);
		}

		logger.debug("Service Orchestrator Event Listener Publshing Exchange "
				+((String)notification.getMessageProperties().getHeaders().get(ApplicationConstant.REPLY_EXCHANGE_HEADER)
				+" Routing Key "+notification.getMessageProperties().getReplyTo()));

		amqpTemplate.send((String)notification.getMessageProperties().getHeaders().get(ApplicationConstant.REPLY_EXCHANGE_HEADER),
				notification.getMessageProperties().getReplyTo(),finalMessage);
		
		logger.debug("Compelted full operation");
	}

}
