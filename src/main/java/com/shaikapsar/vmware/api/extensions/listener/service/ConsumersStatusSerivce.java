/**
 * 
 */
package com.shaikapsar.vmware.api.extensions.listener.service;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.MalformedURLException;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

import org.apache.http.MethodNotSupportedException;
import org.apache.http.client.ClientProtocolException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.http.ResponseEntity;
import org.springframework.integration.annotation.Payload;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;

import com.shaikapsar.vmware.api.extensions.listener.model.RabbitMQConfig;

/**
 * @author Shaik Apsar | shaik.apsar@live.com
 *
 */
@Lazy
@Service
public class ConsumersStatusSerivce {
	
	private Logger logger = LoggerFactory.getLogger(this.getClass());

	@Autowired
	private RabbitMQConfig rabbitMQConfig;
	
	@Autowired
	private RestTemplate restTemplate;

	public String consume() {
		if(logger.isDebugEnabled())
			logger.debug("consumers");
		return "consume";
	}

	public List<URL> consumersUris() throws MalformedURLException, UnsupportedEncodingException, URISyntaxException {
		List<URL> consumersUris=new ArrayList<URL>();
		for(String queue:rabbitMQConfig.getQueueNames()){
			URL url = new URL(rabbitMQConfig.getManagementProtocal()+"://"+rabbitMQConfig.getHost()+":"+rabbitMQConfig.getManagementPort()
					+"/api/queues/"+rabbitMQConfig.getVirtualHost()+"/"+queue);
			if(logger.isDebugEnabled())
				logger.debug(url.toString());
			consumersUris.add(url);
		}
		return consumersUris;
	}
	
	public void consumerStatus(@Payload String consumersUri) throws MethodNotSupportedException, ClientProtocolException, IOException, RestClientException, URISyntaxException  {
		ResponseEntity<String> responseEntity = restTemplate.getForEntity(new URI(consumersUri), String.class);
		String status=responseEntity.getBody();
		int noOfConsumers = Integer.parseInt(status.split("\"consumers\":")[1].split(",")[0]);		
		if(noOfConsumers==0){
			String queueName = status.split("\"name\":")[1].split(",")[0];
			if(logger.isErrorEnabled()){
				logger.error("No. of consumers "+noOfConsumers+" for the Queue "+queueName);
				logger.error("No. of consumers "+noOfConsumers+" ,Call reload uri to set back consumers ");
			}				
			
		}	else{
			String queueName = status.split("queue_details")[1].split("\"name\":")[1].split(",")[0].replace("\"", "");
			if(logger.isDebugEnabled())
				logger.debug("No. of consumers "+noOfConsumers+" for the Queue "+queueName);
		}
	}

}
