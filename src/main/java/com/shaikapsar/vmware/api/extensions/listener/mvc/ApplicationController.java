/**
 * 
 */
package com.shaikapsar.vmware.api.extensions.listener.mvc;

import java.util.Date;

import javax.servlet.ServletContext;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextException;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.context.support.WebApplicationContextUtils;

import com.shaikapsar.vmware.api.extensions.listener.health.ApplicationHealth;

/**
 * @author Shaik Apsar | shaik.apsar@live.com
 *
 */
@Lazy
@RequestMapping
@Controller
public class ApplicationController {
	
	private Logger logger = LoggerFactory.getLogger(getClass());

	@Autowired
	private ServletContext servletContext; 
	
	@Autowired
	private ApplicationHealth applicationHealth ;
	
	@RequestMapping(value = "/")
	public String home(ModelMap modelMap) {
		return "index";
	}

	@RequestMapping(value = "/healthcheck", method = RequestMethod.GET)
	public String healthCheck(ModelMap modelMap) {
		if (logger.isInfoEnabled())
			logger.info("Rabbit MQ Server Health checking...");
		
		long started=System.currentTimeMillis();
		if(logger.isDebugEnabled())
			logger.debug("api-extensions-listener health check started at "+new Date(started));
		System.out.println("api-extensions-listener health check started at "+new Date(started));
		modelMap.addAttribute("status", applicationHealth.runHealthCheck()
				+"<BR>"+applicationHealth.rabbitMQConfig()
				+ "<BR>"
				+ applicationHealth.exchanges()
				+"<BR>"
				+applicationHealth.queues()
				+"<BR>");
		long endeded=System.currentTimeMillis();
		if(logger.isDebugEnabled())
			logger.debug("api-extensions-listener health check ended at "+new Date(endeded)+" "+"total time in ms "+(endeded-started));
		System.out.println("api-extensions-listener health check ended at "+new Date(endeded));
		System.out.println("api-extensions-listener health check completed in ms "+(endeded-started));
		
		return "healthcheck";
	}
	
	@RequestMapping(value = "/rabbit", method = RequestMethod.GET)
	public String exchanges(ModelMap modelMap) {
		if (logger.isInfoEnabled())
			logger.info("Rabbit MQ Components in SO checking ...");

		modelMap.addAttribute(
				"status",
				applicationHealth.rabbitMQConfig()
						+ "<BR>"
						+ applicationHealth.exchanges()
						+ "<BR>"
						+ applicationHealth.queues());
		return "rabbit";
	}
	
	
	@RequestMapping(value = "/reload", method = RequestMethod.GET)
	@ExceptionHandler({ApplicationContextException.class})
	public String reload(ModelMap modelMap) {
		if (logger.isInfoEnabled())
			logger.info("api-extensions-listener reloading web application ...");
		
		ApplicationContext applicationContext = WebApplicationContextUtils.getWebApplicationContext(servletContext);
		ConfigurableApplicationContext context=(ConfigurableApplicationContext) applicationContext;
		long started=System.currentTimeMillis();
		if(logger.isDebugEnabled())
			logger.debug("api-extensions-listener reload started at "+new Date(started));
		System.out.println("api-extensions-listener reload started at "+new Date(started));
		context.refresh();
		long endeded=System.currentTimeMillis();
		if(logger.isDebugEnabled())
			logger.debug("api-extensions-listener reload ended at "+new Date(endeded)+" "+"total time in ms "+(endeded-started));
		System.out.println("api-extensions-listener reload ended at "+new Date(endeded));
		System.out.println("api-extensions-listener reload completed in ms "+(endeded-started));
		String healthCheckUri="<a href=\"healthcheck\">Click Health Check</a>";
		modelMap.addAttribute("status", healthCheckUri);
		return "reload";
	}


}
