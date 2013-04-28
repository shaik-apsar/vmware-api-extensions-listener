package com.shaikapsar.vmware.api.extensions.listener.model;

import java.io.Serializable;
import java.util.List;
import java.util.Map;

/**
 * @author Shaik Apsar | shaik.apsar@live.com
 *
 */
public class HttpSecurityContext implements Serializable {

	private static final long serialVersionUID = -4427133437289491707L;
	
	private String user;
	
	private String org;
	
	private List<String> rights;
	
	private Map<String, Object> parameters;

	public String getUser() {
		return this.user;
	}

	public void setUser(String user) {
		this.user = user;
	}

	public String getOrg() {
		return this.org;
	}

	public void setOrg(String org) {
		this.org = org;
	}

	public List<String> getRights() {
		return this.rights;
	}

	public void setRights(List<String> rights) {
		this.rights = rights;
	}

	public Map<String, Object> getParameters() {
		return this.parameters;
	}

	public void setParameters(Map<String, Object> parameters) {
		this.parameters = parameters;
	}

	@Override
	public String toString() {
		return "HttpSecurityContext [user=" + this.user + ", org=" + this.org
				+ ", rights=" + this.rights + ", parameters=" + this.parameters
				+ "]";
	}
	
}
