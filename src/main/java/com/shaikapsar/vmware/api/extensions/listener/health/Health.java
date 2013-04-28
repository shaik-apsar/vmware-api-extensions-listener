package com.shaikapsar.vmware.api.extensions.listener.health;

/**
 * @author Shaik Apsar | shaik.apsar@live.com
 *
 */
public class Health {
	
	public static final  String	HS_OK 		= "OK";
	public static final  String	HS_ERROR 	= "ERROR";
	
	private String component ;
	private String status;
	private String health;
	
	public String getComponent() {
		return component;
	}
	public void setComponent(String component) {
		this.component = component;
	}
	public String getStatus() {
		return status;
	}
	public void setStatus(String status) {
		this.status = status;
	}
	public String getHealth() {
		return health;
	}
	public void setHealth(String health) {
		this.health = health;
	}
	@Override
	public String toString() {
		return "Health [component=" + component + ", status=" + status
				+ ", health=" + health + "]";
	}
	

}
