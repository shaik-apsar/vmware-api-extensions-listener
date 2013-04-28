package com.shaikapsar.vmware.api.extensions.listener.model;

import java.io.Serializable;
import java.util.Map;

import org.springframework.amqp.core.MessageProperties;

/**
 * @author Shaik Apsar | shaik.apsar@live.com
 *
 */
public class Notification implements Serializable {

	private static final long serialVersionUID = 3564845449031063371L;
	
//	private Logger logger = LoggerFactory.getLogger(this.getClass());

	private HttpMessage httpMessage=new HttpMessage();
	
	private HttpSecurityContext httpSecurityContext=new HttpSecurityContext();
	
	private Map<String, Object> context=null;
	
	private MessageProperties messageProperties=null;

	public HttpMessage getHttpMessage() {
		return this.httpMessage;
	}

	public void setHttpMessage(HttpMessage httpMessage) {
		this.httpMessage = httpMessage;
	}

	public HttpSecurityContext getHttpSecurityContext() {
		return this.httpSecurityContext;
	}

	public void setHttpSecurityContext(HttpSecurityContext httpSecurityContext) {
		this.httpSecurityContext = httpSecurityContext;
	}

	public Map<String, Object> getContext() {
		return this.context;
	}

	public void setContext(Map<String, Object> context) {
		this.context = context;
	}

	public MessageProperties getMessageProperties() {
		return this.messageProperties;
	}

	public void setMessageProperties(MessageProperties messageProperties) {
		this.messageProperties = messageProperties;
	}
}
