package com.shaikapsar.vmware.api.extensions.listener.health;

import com.shaikapsar.vmware.api.extensions.listener.model.RabbitMQConfig;

/**
 * @author Shaik Apsar | shaik.apsar@live.com
 *
 */
public interface HealthChecker {
	
	public Health check(String command) ;
	
	public void setMqConfig(RabbitMQConfig mqConfig);

}
