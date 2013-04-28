/**
 * 
 */
package com.shaikapsar.vmware.api.extensions.listener.config;

import java.io.File;
import java.io.IOException;

import org.codehaus.jackson.JsonGenerationException;
import org.codehaus.jackson.JsonParseException;
import org.codehaus.jackson.map.JsonMappingException;
import org.codehaus.jackson.map.ObjectMapper;
import org.springframework.amqp.core.AmqpTemplate;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.core.MessageProperties;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;

import com.shaikapsar.vmware.api.extensions.listener.constant.ApplicationConstant;
import com.shaikapsar.vmware.api.extensions.listener.enums.MessageType;

/**
 * @author Shaik Apsar | shaik.apsar@live.com
 *
 */
public class TaskExecutor {

	
	

	public static void main(String[] args) {
		AnnotationConfigApplicationContext annotationConfigApplicationContext=new AnnotationConfigApplicationContext("com.shaikapsar.vmware.api.extensions.listener");
		AmqpTemplate amqpTemplate = annotationConfigApplicationContext.getBean("amqpTemplate", AmqpTemplate.class);
		ObjectMapper objectMapper=annotationConfigApplicationContext.getBean("objectMapper", ObjectMapper.class);
		Object[] objects=null;
		try {
			objects= objectMapper.readValue(new File("C:/Users/sa709c/Desktop/POST REQUEST.JSON"), Object[].class);
		} catch (JsonParseException e) {
			e.printStackTrace();
		} catch (JsonMappingException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		MessageProperties messageProperties = new MessageProperties();
		messageProperties.getHeaders().put(ApplicationConstant.MSG_TYPE_HEADER, MessageType.getType(MessageType.REQUEST_TYPE));
		messageProperties.setReplyTo("vcd.event.handler.retry");
		messageProperties.getHeaders().put(ApplicationConstant.REPLY_EXCHANGE_HEADER,"vcd-retry");
		Message message=null;
		try {
			message = new Message(objectMapper.writeValueAsBytes(objects), messageProperties);
		} catch (JsonGenerationException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (JsonMappingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		amqpTemplate.send("systemExchange", "system", message);
	}

}
