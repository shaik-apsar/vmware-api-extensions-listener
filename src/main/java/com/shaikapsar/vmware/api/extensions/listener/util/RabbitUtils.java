/**
 * 
 */
package com.shaikapsar.vmware.api.extensions.listener.util;

import java.io.IOException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.AmqpException;
import org.springframework.amqp.rabbit.connection.CachingConnectionFactory;
import org.springframework.amqp.rabbit.connection.Connection;

import com.rabbitmq.client.Channel;
import com.rabbitmq.client.DefaultConsumer;

/**
 * @author Shaik Apsar | shaik.apsar@live.com
 *
 */
public class RabbitUtils {

	private static Logger logger = LoggerFactory.getLogger(RabbitUtils.class);

	public static CachingConnectionFactory createConnectionFactory(String rabbitHost,int rabbitPort,String rabbitUser,String rabbitPassword){
		com.rabbitmq.client.ConnectionFactory rabbitConnectionFactory = new com.rabbitmq.client.ConnectionFactory();
		rabbitConnectionFactory.setHost(rabbitHost);
		rabbitConnectionFactory.setPort(rabbitPort);
		rabbitConnectionFactory.setUsername(rabbitUser);
		rabbitConnectionFactory.setPassword(rabbitPassword);
		CachingConnectionFactory connectionFactory = new CachingConnectionFactory(rabbitConnectionFactory);
		return connectionFactory;		
	}

	public static Connection createConnection(CachingConnectionFactory connectionFactory){
		Connection connection=null;
		try {
			connection=connectionFactory.createConnection();
		} catch (AmqpException e) {
			throw e;
		}
		return connection;
	}

	public static Channel createChannel(Connection connection){
		Channel channel;
		try {
			channel = connection.createChannel(true);
		} catch (AmqpException e) {
			throw e;
		}
		return channel;
	}

	public static void connectQueue(Connection connection,String queue) throws IOException{
		try {
			Channel channel=connection.createChannel(true);
			channel.basicConsume(queue,false, new DefaultConsumer(channel));
			channel.abort();
		} catch (IOException e) {
			throw e;
		}
	}
	public static void closeChannel(Channel channel) {
		if(ApplicationUtil.isNotEmpty(channel))
			if(channel.isOpen())
				try {
					channel.close();
				} catch (IOException e) {
					logger.error(e.getMessage());
				}catch (Exception e) {
					logger.error(e.getMessage());
				}
	}


	public static void closeConnection(Connection connection){
		if(ApplicationUtil.isNotEmpty(connection))
			if(connection.isOpen())
				try {
					connection.close();
				} catch (AmqpException e) {
					logger.error(e.getMessage());
				}

	}

	public static void closeConnectionFactory(CachingConnectionFactory connectionFactory) {
		if(ApplicationUtil.isNotEmpty(connectionFactory))
			try {
				connectionFactory.destroy();
			} catch (Exception e) {
				logger.error(e.getMessage());
			}
	}

}
