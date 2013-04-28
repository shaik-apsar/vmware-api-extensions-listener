/**
 * 
 */
package com.shaikapsar.vmware.api.extensions.listener.util;

import org.springframework.http.HttpStatus;

/**
 * @author Shaik Apsar | shaik.apsar@live.com
 *
 */
public class HttpStatusUtil {
	
	public static HttpStatus getHttpStatus(String reasonPhrase){
		for(HttpStatus status:HttpStatus.values()){
			if(status.getReasonPhrase().equalsIgnoreCase(reasonPhrase)){
				return status;
			}
		}
		return HttpStatus.INTERNAL_SERVER_ERROR;
	}

}
