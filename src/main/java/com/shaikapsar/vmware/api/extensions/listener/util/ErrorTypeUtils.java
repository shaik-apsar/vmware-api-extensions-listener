/**
 * 
 */
package com.shaikapsar.vmware.api.extensions.listener.util;

import org.apache.commons.lang.exception.ExceptionUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;

import com.vmware.vcloud.api.rest.schema.ErrorType;
import com.vmware.vcloud.api.rest.schema.ObjectFactory;
import com.vmware.vcloud.sdk.JAXBUtil;

/**
 * @author Shaik Apsar | shaik.apsar@live.com
 *
 */

public class ErrorTypeUtils {
	
	private static Logger logger = LoggerFactory.getLogger(ErrorTypeUtils.class);	
	
	public static ErrorType createErrorType(HttpStatus status,Throwable cause) {		
		ErrorType errorType=new ErrorType();
		errorType.setMajorErrorCode(status.value());
		errorType.setMinorErrorCode(status.getReasonPhrase());
		if(ApplicationUtils.isNotEmpty(cause))	{	
			errorType.setStackTrace(ApplicationUtils.toSting(ExceptionUtils.getRootCauseStackTrace(cause)));
			errorType.setMessage(cause.getLocalizedMessage());
		}else{
			errorType.setStackTrace("java.lang.NullPointerException");
			errorType.setMessage("null");
		}
				
		return errorType;
	}
	
	public static String errorTypeMarshall(ErrorType errorType){
		ObjectFactory objectFactory=new ObjectFactory();
		String errorMessage=JAXBUtil.marshal(objectFactory.createError(errorType));
		logger.warn(errorMessage);
		return errorMessage;
	}
	
	public static String errorTypeMarshall(HttpStatus status,Throwable cause){
		return errorTypeMarshall(createErrorType(status, cause));
	}
}
