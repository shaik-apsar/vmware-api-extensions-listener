/**
 * 
 */
package com.shaikapsar.vmware.api.extensions.listener.config;

import org.aopalliance.aop.Advice;
import org.apache.http.HttpHost;
import org.apache.http.auth.AuthScope;
import org.apache.http.auth.UsernamePasswordCredentials;
import org.apache.http.client.HttpClient;
import org.apache.http.conn.scheme.PlainSocketFactory;
import org.apache.http.conn.scheme.Scheme;
import org.apache.http.conn.scheme.SchemeRegistry;
import org.apache.http.conn.ssl.SSLSocketFactory;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.impl.conn.tsccm.ThreadSafeClientConnManager;
import org.codehaus.jackson.map.ObjectMapper;
import org.springframework.amqp.core.AcknowledgeMode;
import org.springframework.amqp.core.AmqpAdmin;
import org.springframework.amqp.core.AmqpTemplate;
import org.springframework.amqp.rabbit.config.StatefulRetryOperationsInterceptorFactoryBean;
import org.springframework.amqp.rabbit.connection.CachingConnectionFactory;
import org.springframework.amqp.rabbit.connection.ConnectionFactory;
import org.springframework.amqp.rabbit.core.RabbitAdmin;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.amqp.rabbit.listener.SimpleMessageListenerContainer;
import org.springframework.amqp.rabbit.retry.MessageKeyGenerator;
import org.springframework.amqp.rabbit.retry.MessageRecoverer;
import org.springframework.amqp.rabbit.transaction.RabbitTransactionManager;
import org.springframework.beans.factory.annotation.Autowire;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.client.ClientHttpRequestFactory;
import org.springframework.http.client.HttpComponentsClientHttpRequestFactory;
import org.springframework.http.client.SimpleClientHttpRequestFactory;
import org.springframework.retry.policy.SimpleRetryPolicy;
import org.springframework.retry.support.RetryTemplate;
import org.springframework.web.client.RestTemplate;

import com.shaikapsar.vmware.api.extensions.listener.exception.ApplicationErrorHandler;
import com.shaikapsar.vmware.api.extensions.listener.exception.RestResponseErrorHandler;
import com.shaikapsar.vmware.api.extensions.listener.model.RabbitMQConfig;
import com.shaikapsar.vmware.api.extensions.listener.retry.BodyBasedKeyGenerator;
import com.shaikapsar.vmware.api.extensions.listener.retry.ErrorQueueMessageRecoverer;
import com.shaikapsar.vmware.api.extensions.listener.util.ApplicationUtils;
import com.vmware.vcloud.api.rest.schema.ErrorType;
import com.vmware.vcloud.api.rest.schema.ObjectFactory;
import com.vmware.vcloud.sdk.JAXBUtil;

/**
 * @author Shaik Apsar | shaik.apsar@live.com
 *
 */
@Configuration
public class ApplicationConfiguration {

	private @Value("#{rabbitConfig['rabbitmq.host']}") String rabbitMqHost;
	private @Value("#{rabbitConfig['rabbitmq.port']}") Integer rabbitMqPort;
	private @Value("#{rabbitConfig['rabbitmq.username']}") String rabbitMqUser;
	private @Value("#{rabbitConfig['rabbitmq.password']}") String rabbitMqPassword;
	private @Value("#{rabbitConfig['rabbitmq.management.port']}") Integer rabbitMqManagementPort;
	private @Value("#{rabbitConfig['rabbitmq.extension.queue']}") String rabbitMqExtensionQueue;
	private @Value("#{rabbitConfig['rabbitmq.extension.exchange']}") String rabbitMqExtensionExchange;
	private @Value("#{rabbitConfig['rabbitmq.requestedHeartbeat']}") Integer rabbitMqRequestedHeartbeat;
	private @Value("#{rabbitConfig['rabbitmq.management.protocal']}") String rabbitMqManagementProtocal;
	private @Value("#{rabbitConfig['rabbitmq.concurrentConsumers']}") Integer rabbitMqConcurrentConsumers;
	private @Value("#{rabbitConfig['rabbitmq.prefetchCount']}") Integer rabbitMqPrefetchCount;
	private @Value("#{rabbitConfig['rabbitmq.virtualHost']}") String rabbitMqVirtualHost;

	private @Value("#{httpConfig['http.maxTotalConnections']}") Integer httpMaxTotalConnections;
	private @Value("#{httpConfig['http.maxConnectionsPerRoute']}") Integer httpMaxConnectionsPerRoute;    
	private @Value("#{httpConfig['http.service.host']}") String httpSeriveHost;
	private @Value("#{httpConfig['http.service.port']}") Integer httpSerivePort;    
	private @Value("#{httpConfig['http.service.scheme']}") String httpSeriveScheme;  
	private @Value("#{httpConfig['http.service.context']}") String httpSerivecontext; 

	private @Value("#{rabbitConfig['rabbitmq.dead.letter.exchange']}") String deadLetterExchange;
	private @Value("#{rabbitConfig['rabbitmq.dead.letter.routingkey']}") String deadLetterRoutingKey;

	private @Value("#{appConfig['app.retryAttemps']}") Integer appRetryAttemps;

	public @Bean ConnectionFactory rabbitConnectionFactory() {
		com.rabbitmq.client.ConnectionFactory rabbitConnectionFactory = new com.rabbitmq.client.ConnectionFactory();
		rabbitConnectionFactory.setHost(rabbitMQConfig().getHost());
		rabbitConnectionFactory.setPort(rabbitMQConfig().getPort());
		rabbitConnectionFactory.setUsername(rabbitMQConfig().getUser());
		rabbitConnectionFactory.setPassword(rabbitMQConfig().getPassword());
		rabbitConnectionFactory.setRequestedHeartbeat(rabbitMQConfig().getHeartBeat());
		return new CachingConnectionFactory(rabbitConnectionFactory);
	}

	public @Bean ClientHttpRequestFactory clientHttpRequestFactory() {
		SchemeRegistry schemeRegistry = new SchemeRegistry();
		schemeRegistry.register(new Scheme("http", 80, PlainSocketFactory.getSocketFactory()));
		schemeRegistry.register(new Scheme("https", 443, socketFactory()));
		ThreadSafeClientConnManager connectionManager = new ThreadSafeClientConnManager(schemeRegistry);
		connectionManager.setMaxTotal(httpMaxTotalConnections);
		connectionManager.setDefaultMaxPerRoute(httpMaxConnectionsPerRoute);
		HttpClient httpClient = new DefaultHttpClient(connectionManager);
		ClientHttpRequestFactory clientHttpRequestFactory = new HttpComponentsClientHttpRequestFactory(httpClient);
		return clientHttpRequestFactory;
	}

	public @Bean ClientHttpRequestFactory rabbitClientHttpRequestFactory() {
		HttpHost targetHost = new HttpHost(rabbitMQConfig().getHost(),rabbitMQConfig().getManagementPort(),rabbitMQConfig().getManagementProtocal());
		DefaultHttpClient httpClient = new DefaultHttpClient();
		httpClient.getCredentialsProvider().setCredentials(
				new AuthScope(targetHost.getHostName(), targetHost.getPort()),
				new UsernamePasswordCredentials(rabbitMQConfig().getUser(),rabbitMQConfig().getPassword()));
		ClientHttpRequestFactory clientHttpRequestFactory = new HttpComponentsClientHttpRequestFactory(httpClient);	
		return clientHttpRequestFactory;
	}

	public @Bean RabbitMQConfig rabbitMQConfig() {
		RabbitMQConfig rabbitMQConfig=new RabbitMQConfig();
		rabbitMQConfig.setHost(rabbitMqHost);
		rabbitMQConfig.setPort(rabbitMqPort);
		rabbitMQConfig.setUser(rabbitMqUser);
		rabbitMQConfig.setPassword(rabbitMqPassword);
		rabbitMQConfig.setQueues(rabbitMqExtensionQueue);		
		rabbitMQConfig.setHeartBeat(rabbitMqRequestedHeartbeat);
		rabbitMQConfig.setManagementPort(rabbitMqManagementPort);
		rabbitMQConfig.setManagementProtocal(rabbitMqManagementProtocal);
		rabbitMQConfig.setExchanges(rabbitMqExtensionExchange);
		rabbitMQConfig.setVirtualHost(rabbitMqVirtualHost);
		return rabbitMQConfig;
	}

	public @Bean JAXBUtil jaxbUtil() {
		JAXBUtil jaxbUtil=new JAXBUtil();
		return jaxbUtil;
	}

	public @Bean ObjectFactory objectFactory() {
		ObjectFactory objectFactory=new ObjectFactory();
		return objectFactory;
	}

	public @Bean ObjectMapper objectMapper() {
		ObjectMapper objectMapper = new ObjectMapper();	
		return objectMapper;
	}

	public @Bean SSLSocketFactory socketFactory() {
		return FakeSSLSocketFactory.getInstance();
	}

	public @Bean RestResponseErrorHandler<?> restResponseErrorHandler(){
		return new RestResponseErrorHandler<ErrorType>(ErrorType.class);
	}

	public @Bean ApplicationErrorHandler errorHandler() {
		return new ApplicationErrorHandler();
	}

	public @Bean AmqpTemplate amqpTemplate() {
		return new RabbitTemplate(rabbitConnectionFactory());
	}

	@Bean
	public AmqpAdmin amqpAdmin() {
		return new RabbitAdmin(rabbitConnectionFactory());
	}

	public @Bean RabbitTransactionManager rabbitTransactionManager() {
		RabbitTransactionManager rabbitTransactionManager=new RabbitTransactionManager(rabbitConnectionFactory());
		rabbitTransactionManager.setTransactionSynchronization(RabbitTransactionManager.SYNCHRONIZATION_ALWAYS);
		return rabbitTransactionManager; 
	}	

	public @Bean RestTemplate restTemplate(){
		RestTemplate restTemplate = new RestTemplate(rabbitClientHttpRequestFactory());
		restTemplate.setErrorHandler(new RestResponseErrorHandler<String>(String.class));
		return restTemplate;
	}

	public @Bean Advice[] adviceChain(){
		return new Advice[]{retryInterceptor()};
	}

	public @Bean Advice retryInterceptor(){
		StatefulRetryOperationsInterceptorFactoryBean retry = new StatefulRetryOperationsInterceptorFactoryBean();
		retry.setRetryOperations(retryTemplate());
		retry.setMessageKeyGeneretor(bodyBasedKeyGenerator());
		retry.setMessageRecoverer(errorQueueRecoverer());
		return retry.getObject();
	}

	public @Bean RetryTemplate retryTemplate(){
		RetryTemplate retryTemplate = new RetryTemplate();
		SimpleRetryPolicy retryPolicy = new SimpleRetryPolicy();
		retryPolicy.setMaxAttempts(appRetryAttemps);
		retryTemplate.setRetryPolicy(retryPolicy);
		return retryTemplate;
	}

	public @Bean MessageKeyGenerator bodyBasedKeyGenerator() {
		return new BodyBasedKeyGenerator();
	}

	public @Bean(autowire=Autowire.BY_NAME) MessageRecoverer errorQueueRecoverer() {
		return new ErrorQueueMessageRecoverer();
	}

	public @Bean SimpleMessageListenerContainer listenerContainer(){
		SimpleMessageListenerContainer listenerContainer=new SimpleMessageListenerContainer(rabbitConnectionFactory());
		listenerContainer.setAcknowledgeMode(AcknowledgeMode.AUTO);
		listenerContainer.setAdviceChain(adviceChain()); 
		listenerContainer.setAutoStartup(true);
		listenerContainer.setChannelTransacted(Boolean.TRUE);
		listenerContainer.setConcurrentConsumers(rabbitMqConcurrentConsumers);
		listenerContainer.setErrorHandler(errorHandler()); 
		listenerContainer.setPrefetchCount(rabbitMqPrefetchCount);
		String[] validQueues = null;
		do{
			validQueues=rabbitMQConfig().getQueueNames();
		}while(ApplicationUtils.isEmpty(validQueues)); 
		listenerContainer.setQueueNames(validQueues);
		listenerContainer.setTransactionManager(rabbitTransactionManager());
		return listenerContainer;
	}
	
	public @Bean RestTemplate serviceRestTemplate(){
		RestTemplate restTemplate = new RestTemplate(clientHttpRequestFactory());
		restTemplate.setErrorHandler(restResponseErrorHandler());
		SimpleClientHttpRequestFactory requestFactory = new SimpleClientHttpRequestFactory();
		restTemplate.setRequestFactory(requestFactory);
		return restTemplate;
	}


	public @Bean HttpHost httpSericeHost() {
		HttpHost httpHost = new HttpHost(httpSeriveHost, httpSerivePort, httpSeriveScheme);
		return httpHost;
	}
}
