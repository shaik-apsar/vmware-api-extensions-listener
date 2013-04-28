/**
 * 
 */
package com.shaikapsar.vmware.api.extensions.listener.enums;


/**
 * @author Shaik Apsar | shaik.apsar@live.com
 *
 */
public enum MessageType {
	
REQUEST_TYPE ("ProcessHttpRequest"){},
	
	RESPONSE_TYPE ("ProcessHttpResponse"){};
	
	private String type;

	private MessageType(String type) {
		this.type = type;
	}	
	
	public static String getType(MessageType messageType){
		for(MessageType msgtype:MessageType.values()){
			if(msgtype.type.equals(messageType.type))
				return msgtype.type;
		}
		return null;
	}
	
	public static MessageType getMessageType(String messageType){
		for(MessageType msgtype:MessageType.values()){
			if(msgtype.type.equals(messageType))
				return msgtype;
		}
		return null;
	}

}
