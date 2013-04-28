package com.shaikapsar.vmware.api.extensions.listener.model;

import java.io.Serializable;

import com.shaikapsar.vmware.api.extensions.listener.constant.ApplicationConstant;
import com.shaikapsar.vmware.api.extensions.listener.util.ApplicationUtil;

/**
 * @author Shaik Apsar | shaik.apsar@live.com
 *
 */
public class RabbitMQConfig implements Serializable {

	private static final long serialVersionUID = 865176158769877620L;

	private String host;

	private int port;

	private String user;

	private String password;

	private String protocol;

	private String queues;

	private String[] queueNames;

	private String exchanges;

	private String[] exchangeNames;

	private Integer heartBeat;

	private Integer managementPort;

	private String managementProtocal;
	
	private String virtualHost;

	public String getHost() {
		return this.host;
	}

	public void setHost(String host) {
		this.host = host;
	}

	public int getPort() {
		return this.port;
	}

	public void setPort(int port) {
		this.port = port;
	}

	public String getUser() {
		return this.user;
	}

	public void setUser(String user) {
		this.user = user;
	}

	public String getPassword() {
		return this.password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public String getProtocol() {
		return this.protocol;
	}

	public void setProtocol(String protocol) {
		this.protocol = protocol;
	}

	public String getQueues() {
		return this.queues;
	}

	public void setQueues(String queues) {
		this.queues = queues;
		setQueueNames(ApplicationUtil.toArray(queues, ApplicationConstant.COMMA_SEPARATOR));
	}

	public String[] getQueueNames() {
		return this.queueNames;
	}

	public void setQueueNames(String[] queueNames) {
		this.queueNames = queueNames;
	}

	public String getExchanges() {
		return this.exchanges;
	}

	public void setExchanges(String exchanges) {
		this.exchanges = exchanges;
		setExchangeNames(ApplicationUtil.toArray(exchanges, ApplicationConstant.COMMA_SEPARATOR));
	}

	public String[] getExchangeNames() {
		return this.exchangeNames;
	}

	public void setExchangeNames(String[] exchangeNames) {
		this.exchangeNames = exchangeNames;
	}

	public Integer getHeartBeat() {
		return this.heartBeat;
	}

	public void setHeartBeat(Integer heartBeat) {
		this.heartBeat = heartBeat;
	}

	public Integer getManagementPort() {
		return this.managementPort;
	}

	public void setManagementPort(Integer managementPort) {
		this.managementPort = managementPort;
	}

	public String getManagementProtocal() {
		return this.managementProtocal;
	}

	public void setManagementProtocal(String managementProtocal) {
		this.managementProtocal = managementProtocal;
	}

	public String getVirtualHost() {
		return this.virtualHost;
	}

	public void setVirtualHost(String virtualHost) {
		this.virtualHost = virtualHost;
	}

	@Override
	public String toString() {
		return "RabbitMQConfig [host=" + this.host + ", port=" + this.port
				+ ", user=" + this.user + ", password=" + this.password
				+ ", protocol=" + this.protocol + ", queues=" + this.queues
				+ ", exchanges=" + this.exchanges + ", heartBeat="
				+ this.heartBeat + ", managementPort=" + this.managementPort
				+ ", managementProtocal=" + this.managementProtocal
				+ ", virtualHost=" + this.virtualHost + "]";
	}

	
}
