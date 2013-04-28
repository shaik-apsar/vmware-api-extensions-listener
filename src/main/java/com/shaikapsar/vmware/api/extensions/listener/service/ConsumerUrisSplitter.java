/**
 * 
 */
package com.shaikapsar.vmware.api.extensions.listener.service;

import java.util.ArrayList;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.integration.Message;
import org.springframework.stereotype.Service;

/**
 * @author Shaik Apsar | shaik.apsar@live.com
 *
 */
@Service
public class ConsumerUrisSplitter {

	private Logger logger = LoggerFactory.getLogger(this.getClass());

	public ArrayList<?> splitMessage(Message<?> message) {
		ArrayList<?> messages = (ArrayList<?>) message.getPayload();
		logger.debug("Total messages: " + messages.size());
		for (Object mess: messages) {
			logger.debug(mess.toString());
		}

		return messages;
	}

}
