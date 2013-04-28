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
public class QueueHealth implements HealthChecker{

	private Logger logger = LoggerFactory.getLogger(getClass());
	private static final String COMPONENT = "Rabbit MQ Queue";
	private RabbitMQConfig mqConfig;
	private CachingConnectionFactory connectionFactory;
	private Connection connection;
	private Channel channel;	
	
	public void setMqConfig(RabbitMQConfig mqConfig) {
		this.mqConfig = mqConfig;
	}

	public Health check(String command) {
		
		Health health=new Health();
		health.setComponent(COMPONENT);

		if(ApplicationUtils.isNotEmpty(command)){
			health.setComponent(COMPONENT+" '"+command+"'");
		}

		try {
			connectionFactory=RabbitUtils.createConnectionFactory(mqConfig.getHost(), mqConfig.getPort(), mqConfig.getUser(), mqConfig.getPassword());
			connection=RabbitUtils.createConnection(connectionFactory);
			channel=RabbitUtils.createChannel(connection);
			RabbitUtils.connectQueue(connection, command);
			health.setStatus(Health.HS_OK);
			health.setHealth("Checked at :"+ ApplicationUtils.rightNow());
			System.out.println("Health.HS_OK "+Health.HS_OK);
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
	

	public boolean isQueueExist(String queueName){
		if(logger.isDebugEnabled())
			logger.debug("Checking Queue "+queueName+" existance in RabbitMQ Server.");
		boolean isQueueExist=false;
		try {
			connectionFactory=RabbitUtils.createConnectionFactory(mqConfig.getHost(), mqConfig.getPort(), mqConfig.getUser(), mqConfig.getPassword());
			connection=RabbitUtils.createConnection(connectionFactory);
			channel=RabbitUtils.createChannel(connection);
			RabbitUtils.connectQueue(connection, queueName);
			isQueueExist=true;
		} catch (AmqpException e) {
			logger.error(e.getMessage());
			isQueueExist=false;
			logger.error(e.getMessage(), e);

		}		
		catch (Exception e) {
			logger.error(e.getMessage());
			isQueueExist=false;
			logger.error(e.getMessage(), e);
		}
		finally{
			try {
				if(!ApplicationUtils.isEmpty(channel))
					RabbitUtils.closeChannel(channel);
				if(!ApplicationUtils.isEmpty(connection))
					RabbitUtils.closeConnection(connection);
				if(!ApplicationUtils.isEmpty(connectionFactory))
					RabbitUtils.closeConnectionFactory(connectionFactory);
				if(isQueueExist){
					if (logger.isDebugEnabled()) 
						logger.debug("Queue "+queueName+" availabel in RabbitMQ Server.");			
				}else{
					if(logger.isErrorEnabled())
						logger.error("Queue "+queueName+" not availabel in RabbitMQ Server.");
				}
			} catch (Exception e) {
				logger.error(e.getMessage());
			}

		}
		return isQueueExist;
	}


}
