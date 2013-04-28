/**
 * 
 */
package com.shaikapsar.vmware.api.extensions.listener.exception;

/**
 * @author Shaik Apsar | shaik.apsar@live.com
 *
 */
public class ApplicationException extends RuntimeException {

	private static final long serialVersionUID = -585375055308953431L;

	public ApplicationException() {
		super();
	}

	public ApplicationException(String message, Throwable cause) {
		super(message, cause);
	}

	public ApplicationException(String message) {
		super(message);
	}

	public ApplicationException(Throwable cause) {
		super(cause);
	}

}
