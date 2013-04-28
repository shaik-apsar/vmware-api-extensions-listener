/**
 * 
 */
package com.shaikapsar.vmware.api.extensions.listener.health;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.AmqpException;
import org.springframework.amqp.rabbit.connection.CachingConnectionFactory;
import org.springframework.amqp.rabbit.connection.Connection;

import com.rabbitmq.client.Channel;
import com.shaikapsar.vmware.api.extensions.listener.model.RabbitMQConfig;
import com.shaikapsar.vmware.api.extensions.listener.util.ApplicationUtils;
import com.shaikapsar.vmware.api.extensions.listener.util.RabbitUtils;

/**
 * @author Shaik Apsar | shaik.apsar@live.com
 *
 */
public class ChannelHealth implements HealthChecker {

	private static final String COMPONENT = "Rabbit MQ Channel";
	
	private Logger logger = LoggerFactory.getLogger(this.getClass());

	private RabbitMQConfig mqConfig;

	public void setMqConfig(RabbitMQConfig mqConfig) {
		this.mqConfig = mqConfig;
	}
	private CachingConnectionFactory connectionFactory;

	private Connection connection;

	private Channel channel;	

	public Health check(String command) {
		Health health=new Health();
		if(ApplicationUtils.isEmpty(command))
			health.setComponent(COMPONENT);
		else
			health.setComponent(COMPONENT+" "+command);
		try {
			connectionFactory=RabbitUtils.createConnectionFactory(mqConfig.getHost(), mqConfig.getPort(), mqConfig.getUser(), mqConfig.getPassword());
			connection=RabbitUtils.createConnection(connectionFactory);
			channel=RabbitUtils.createChannel(connection);
			health.setComponent(channel.toString());
			health.setStatus(Health.HS_OK);
			health.setHealth("Checked at :"+ ApplicationUtils.rightNow());
			logger.debug("Health.HS_OK "+Health.HS_OK);
		} catch (AmqpException e) {
			logger.error(e.getMessage());
			health.setStatus(Health.HS_ERROR);
			health.setHealth("Check failed. "+ e.getCause());
		}		
		catch (Exception e) {
			logger.error(e.getMessage());
			health.setStatus(Health.HS_ERROR);
			health.setHealth("Check failed. "+ e.getCause());
		}
		try {
			if(ApplicationUtils.isNotEmpty(channel))
				RabbitUtils.closeChannel(channel);
			if(ApplicationUtils.isNotEmpty(connection))
				RabbitUtils.closeConnection(connection);
			if(ApplicationUtils.isNotEmpty(connectionFactory))
				RabbitUtils.closeConnectionFactory(connectionFactory);
		} catch (Exception e) {
			logger.error(e.getMessage());
		}
		return health;
	}
	

}
