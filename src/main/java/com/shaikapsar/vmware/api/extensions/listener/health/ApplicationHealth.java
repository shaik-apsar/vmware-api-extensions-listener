/**
 * 
 */
package com.shaikapsar.vmware.api.extensions.listener.health;

import java.lang.reflect.Method;
import java.util.HashSet;
import java.util.Set;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Service;

import com.shaikapsar.vmware.api.extensions.listener.constant.ApplicationConstant;
import com.shaikapsar.vmware.api.extensions.listener.model.RabbitMQConfig;
import com.shaikapsar.vmware.api.extensions.listener.util.ApplicationUtils;



/**
 * @author Shaik Apsar | shaik.apsar@live.com
 *
 */
@Lazy
@Service
public class ApplicationHealth {

	private Logger logger = LoggerFactory.getLogger(this.getClass());

	private static String[] COMPONENTS = {
		"com.shaikapsar.vmware.api.extensions.listener.health.ConnectionHealth",
		"com.shaikapsar.vmware.api.extensions.listener.health.ChannelHealth",
	};

	@Autowired
	private RabbitMQConfig rabbitMQConfig;	

	public String runHealthCheck() {

		Set<String> components = new HashSet<String>();
		setUpAllCompoents(components, rabbitMQConfig.getExchangeNames(), rabbitMQConfig.getQueueNames());

		StringBuffer sb = new StringBuffer("<TABLE border=0 >");

		for (String component : components) {			
			try {
				Class<?> c = Class.forName(ApplicationUtils.componentPrefix(component, ApplicationConstant.SLASH_SEPARATOR));
				HealthChecker hc = (HealthChecker) c.newInstance();
				hc.setMqConfig(rabbitMQConfig);
				Class<?> partypes[] = new Class[1];
				partypes[0] = String.class;
				Method m = c.getMethod("check", partypes);
				Object arglist[] = new Object[1];
				arglist[0] = ApplicationUtils.componentSuffix(component,ApplicationConstant.SLASH_SEPARATOR);
				if(logger.isDebugEnabled())
					logger.debug("starting "+arglist[0]+" "+ c.getSimpleName()+" health check.");
				System.out.println("starting "+arglist[0]+" "+ c.getSimpleName()+" health check.");
				Health h = (Health) m.invoke(hc, arglist);
				sb.append("<TR style=\""
						+ (h.getStatus().equals(Health.HS_OK) ? "color:black;background-color:lime"
								: "color:white;background-color:red") + "\">"
								+ "<TD width=500>" + h.getComponent() + "</TD>"
								+ "<TD width=100>" + h.getStatus() + "</TD>"
								+ "<TD width=500>" + h.getHealth() + "</TD>" + "</TR>");
				if(logger.isDebugEnabled())
					logger.debug("health check " +arglist[0]+" "+ c.getSimpleName()+"ended. ");
				System.out.println("health check " +arglist[0]+" "+ c.getSimpleName()+" ended.");

			} catch (Exception e) {
				e.printStackTrace();
			}
		}

		sb.append("</TABLE>");
		return sb.toString();

	}

	private void setUpAllCompoents(Set<String> components,String[] exchangeNames, String[] queueNames) {

		for (int i = 0; i < COMPONENTS.length; i++) {
			components.add(COMPONENTS[i]);
		}

		if (ApplicationUtils.isNotEmpty(queueNames)) {
			for(String queue:queueNames){
				components.add(QueueHealth.class.getName()+"/"+queue);
			}
		}

		if (ApplicationUtils.isNotEmpty(exchangeNames)) {
			for(String exchange:exchangeNames){
				components.add(ExchangeHealth.class.getName()+"/"+exchange);
			}
		}

	}

	public String exchanges() {

		StringBuffer sb = new StringBuffer("Exchanges Details");
		String[] exchanges = rabbitMQConfig.getExchangeNames();
		sb.append("<br><TABLE border=0 >");
		if(ApplicationUtils.isEmpty(exchanges) || (ApplicationUtils.isNotEmpty(exchanges) && (exchanges.length== 0))){
			sb.append("<TR style=\"color:white;background-color:red\"><TD width=1100 >Not found site specific exchanges details</TD></TR>");
		}else{
			sb.append("<TR style=\"color:black;background-color:lime\"><TD width=1100 >Exchange</TD></TR>");
			System.out.println("\n"+"***************EXCHANGE DETAILS******************");
			for (String exchange:exchanges) {
				sb.append("<TR style=\"color:black;background-color:lime\">"
						+ "<TD width=1100>" + exchange + "</TD>");
				logger.debug(exchange +" ------> "+exchange);
			}
			logger.debug("***************EXCHANGE DETAILS******************");
		}
		sb.append("</TABLE>");
		return sb.toString();
	}

	public String queues() {
		
		StringBuffer sb = new StringBuffer("Queues Details");
		String[] queues = rabbitMQConfig.getQueueNames();
		sb.append("<br><TABLE border=0 >");

		if(ApplicationUtils.isEmpty(queues) || (ApplicationUtils.isNotEmpty(queues) && (queues.length== 0))){
			sb.append("<TR style=\"color:white;background-color:red\"><TD width=1100 >Not found site specific queues details</TD></TR>");
		}else{
			sb.append("<TR style=\"color:black;background-color:lime\"><TD width=1100>Queues</TD></TR>");

			System.out.println("\n"+"***************QUEUE DETAILS********************");
			for (String queue:queues) {
				sb.append("<TR style=\"color:black;background-color:lime\">"
						+ "<TD width=1100>" + queue + "</TD>");
				logger.debug(queue +" ------> "+queue);
			}
			logger.debug("***************QUEUE DETAILS********************");
		}
		sb.append("</TABLE>");
		return sb.toString();
	}

	public String rabbitMQConfig() {

		StringBuffer sb = new StringBuffer("Rabbit MQ component details");
		sb.append("<br><TABLE border=0 >");

		if (ApplicationUtils.isEmpty(rabbitMQConfig)) {
			sb.append("<TR style=\"color:white;background-color:red\"><TD width=550>Rabbit MQ deatils not available</TD></TR>");
		} else {
			sb.append("<TR style=\"color:black;background-color:lime\"><TD width=1>HOST</TD><TD width=250>PORT</TD><TD width=250>USERNAME</TD><TD width=350>QUEUES</TD></TR>");
			sb.append("<TR style=\"color:black;background-color:lime\"><TD width=250>"
					+ rabbitMQConfig.getHost()
					+ "</TD><TD width=250>"
					+ rabbitMQConfig.getPort()
					+ "</TD><TD width=250>"
					+ rabbitMQConfig.getUser()
					+ "</TD><TD width=350>"
					+ rabbitMQConfig.getQueues() + "</TD></TR>");
		}
		sb.append("</TABLE>");
		return sb.toString();
	}

}



