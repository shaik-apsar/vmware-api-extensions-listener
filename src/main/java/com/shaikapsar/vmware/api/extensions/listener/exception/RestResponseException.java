/**
 * 
 */
package com.shaikapsar.vmware.api.extensions.listener.exception;

import org.springframework.http.ResponseEntity;

/**
 * @author Shaik Apsar | shaik.apsar@live.com
 *
 */
public class RestResponseException extends RuntimeException {

	private static final long serialVersionUID = -5219157695522236581L;
	protected ResponseEntity<?> responseEntity;

	public RestResponseException() {
		super();
	}

	public RestResponseException(String message, Throwable cause) {
		super(message,cause);
	}

	public RestResponseException(String message) {
		super(message);
	}

	public RestResponseException(Throwable cause) {
		super(cause);
	}

	public ResponseEntity<?> getResponseEntity() {
		return this.responseEntity;
	}

	public void setResponseEntity(ResponseEntity<?> responseEntity) {
		this.responseEntity = responseEntity;
	}

}
