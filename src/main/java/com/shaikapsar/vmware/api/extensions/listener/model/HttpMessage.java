package com.shaikapsar.vmware.api.extensions.listener.model;

import java.io.Serializable;
import java.util.Arrays;
import java.util.Map;

/**
 * @author Shaik Apsar | shaik.apsar@live.com
 *
 */
public class HttpMessage implements Serializable {

	private static final long serialVersionUID = 8689957420184266316L;
	
	private boolean isRequest;
	
	private String id;
	
	private String method;
	
	private String requestUri;
	
	private String queryString;
	
	private String protocol;
	
	private String scheme;
	
	private String remoteAddr;
	
	private int remotePort;
	
	private String localAddr;
	
	private int localPort;
	
	private Map<String, String> headers;
	
	private byte[] body;
	
	private int statusCode;
	
	public boolean isRequest() {
		return this.isRequest;
	}
	
	public void setRequest(boolean isRequest) {
		this.isRequest = isRequest;
	}
	
	public String getId() {
		return this.id;
	}
	
	public void setId(String id) {
		this.id = id;
	}
	
	public String getMethod() {
		return this.method;
	}
	
	public void setMethod(String method) {
		this.method = method;
	}
	
	public String getRequestUri() {
		return this.requestUri;
	}
	
	public void setRequestUri(String requestUri) {
		this.requestUri = requestUri;
	}
	
	public String getQueryString() {
		return this.queryString;
	}
	
	public void setQueryString(String queryString) {
		this.queryString = queryString;
	}
	
	public String getProtocol() {
		return this.protocol;
	}
	
	public void setProtocol(String protocol) {
		this.protocol = protocol;
	}
	
	public String getScheme() {
		return this.scheme;
	}
	
	public void setScheme(String scheme) {
		this.scheme = scheme;
	}
	
	public String getRemoteAddr() {
		return this.remoteAddr;
	}
	
	public void setRemoteAddr(String remoteAddr) {
		this.remoteAddr = remoteAddr;
	}
	
	public int getRemotePort() {
		return this.remotePort;
	}
	
	public void setRemotePort(int remotePort) {
		this.remotePort = remotePort;
	}
	
	public String getLocalAddr() {
		return this.localAddr;
	}
	
	public void setLocalAddr(String localAddr) {
		this.localAddr = localAddr;
	}
	
	public int getLocalPort() {
		return this.localPort;
	}
	
	public void setLocalPort(int localPort) {
		this.localPort = localPort;
	}
	
	public Map<String, String> getHeaders() {
		return this.headers;
	}
	
	public void setHeaders(Map<String, String> headers) {
		this.headers = headers;
	}
	
	public byte[] getBody() {
		return this.body;
	}
	
	public void setBody(byte[] body) {
		this.body = body;
	}
	
	public int getStatusCode() {
		return this.statusCode;
	}
	
	public void setStatusCode(int statusCode) {
		this.statusCode = statusCode;
	}
	
	@Override
	public String toString() {
		return "HttpMessage [isRequest=" + this.isRequest + ", id=" + this.id
				+ ", method=" + this.method + ", requestUri=" + this.requestUri
				+ ", queryString=" + this.queryString + ", protocol="
				+ this.protocol + ", scheme=" + this.scheme + ", remoteAddr="
				+ this.remoteAddr + ", remotePort=" + this.remotePort
				+ ", localAddr=" + this.localAddr + ", localPort="
				+ this.localPort + ", headers=" + this.headers + ", body="
				+ Arrays.toString(this.body) + ", statusCode="
				+ this.statusCode + "]";
	}

}
