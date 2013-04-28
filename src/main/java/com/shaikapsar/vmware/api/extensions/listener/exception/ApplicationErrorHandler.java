/**
 * 
 */
package com.shaikapsar.vmware.api.extensions.listener.exception;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.AmqpConnectException;
import org.springframework.amqp.AmqpIOException;
import org.springframework.amqp.AmqpIllegalStateException;
import org.springframework.amqp.AmqpUnsupportedEncodingException;
import org.springframework.amqp.ImmediateAcknowledgeAmqpException;
import org.springframework.amqp.UncategorizedAmqpException;
import org.springframework.amqp.rabbit.listener.FatalListenerStartupException;
import org.springframework.util.ErrorHandler;

import com.rabbitmq.client.ShutdownSignalException;

/**
 * @author Shaik Apsar | shaik.apsar@live.com
 *
 */
public class ApplicationErrorHandler implements ErrorHandler {

	Logger logger = LoggerFactory.getLogger(this.getClass());

	public void handleError(Throwable throwable) {

		if(throwable instanceof AmqpConnectException){
			logger.error(throwable.getMessage());
		}else if(throwable instanceof AmqpIOException){
			logger.error(throwable.getMessage());
		}else if(throwable instanceof AmqpIllegalStateException){
			logger.error(throwable.getMessage());
		}else if(throwable instanceof AmqpUnsupportedEncodingException){
			logger.error(throwable.getMessage());
		}else if(throwable instanceof ImmediateAcknowledgeAmqpException){
			logger.error(throwable.getMessage());
		}else if(throwable instanceof UncategorizedAmqpException){
			logger.error(throwable.getMessage());
		}else if(throwable instanceof ShutdownSignalException){
			logger.error(throwable.getMessage());
		}else if(throwable instanceof FatalListenerStartupException){
			logger.error(throwable.getMessage());
		}
	}

}
