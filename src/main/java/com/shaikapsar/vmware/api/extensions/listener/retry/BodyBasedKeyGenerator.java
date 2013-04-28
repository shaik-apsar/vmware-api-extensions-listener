package com.shaikapsar.vmware.api.extensions.listener.retry;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

import org.springframework.amqp.core.Message;
import org.springframework.amqp.rabbit.retry.MessageKeyGenerator;

/**
 * @author Shaik Apsar | shaik.apsar@live.com
 *
 */
public class BodyBasedKeyGenerator implements MessageKeyGenerator {

	public Object getKey(Message message) {
		try {
			final MessageDigest md = MessageDigest.getInstance("SHA");
			md.update(message.getBody());
			return new String(md.digest());
		} catch (NoSuchAlgorithmException e) {
			throw new RuntimeException(e);
		}
	}
	
		
}
