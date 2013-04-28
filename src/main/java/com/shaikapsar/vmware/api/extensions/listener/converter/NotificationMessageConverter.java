package com.shaikapsar.vmware.api.extensions.listener.converter;

import java.io.IOException;
import java.util.Map;

import org.apache.commons.lang.exception.ExceptionUtils;
import org.codehaus.jackson.JsonParseException;
import org.codehaus.jackson.map.JsonMappingException;
import org.codehaus.jackson.map.ObjectMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.core.MessageProperties;
import org.springframework.amqp.support.converter.MessageConversionException;
import org.springframework.amqp.support.converter.MessageConverter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.shaikapsar.vmware.api.extensions.listener.model.HttpMessage;
import com.shaikapsar.vmware.api.extensions.listener.model.HttpSecurityContext;
import com.shaikapsar.vmware.api.extensions.listener.model.Notification;
import com.shaikapsar.vmware.api.extensions.listener.util.ErrorTypeUtils;
import com.shaikapsar.vmware.api.extensions.listener.util.HttpStatusUtil;
import com.vmware.vcloud.sdk.VCloudException;

/**
 * @author Shaik Apsar | shaik.apsar@live.com
 *
 */
@Service("notificationMessageConverter")
public class NotificationMessageConverter implements MessageConverter{

	private Logger logger = LoggerFactory.getLogger(this.getClass());

	@Autowired
	private ObjectMapper objectMapper;

	@SuppressWarnings("unchecked")
	public Object fromMessage(Message message) throws MessageConversionException {

		Notification notification=new Notification();
		try {
			Map<String, Object> headers = message.getMessageProperties().getHeaders();

			logger.debug("Incoming Extension message headers (total " + headers.size() + "):");
			for (String key : headers.keySet()) {
				logger.debug("key:" + key + " value:" + headers.get(key));
			}			

			notification.setMessageProperties(message.getMessageProperties());

			byte[] body = message.getBody();        

			logger.debug(new String(body));

			Object[] args = null;
			try {
				args = objectMapper.readValue(body, Object[].class);
			} catch (JsonParseException e) {			
				throw e;
			} catch (JsonMappingException e) {
				throw e;
			} catch (IOException e) {
				throw e;
			}
			notification.setHttpMessage(objectMapper.convertValue(args[0], HttpMessage.class));
			notification.setHttpSecurityContext(objectMapper.convertValue(args[1], HttpSecurityContext.class));
			notification.setContext(objectMapper.convertValue(args[2], Map.class));  

		}catch(Exception e){
			logger.error(ExceptionUtils.getRootCauseMessage(e));		
			MessageConversionException mce = new MessageConversionException(ExceptionUtils.getRootCauseMessage(e));	
			mce.initCause(new VCloudException(ErrorTypeUtils.createErrorType(HttpStatusUtil.getHttpStatus(ExceptionUtils.getRootCauseMessage(e)), ExceptionUtils.getRootCause(e))));
			throw mce;
		}
		return notification;
	}

	public Message toMessage(Object object, MessageProperties messageProperties) throws MessageConversionException {
		return null;
	}



}
