/*
 * Copyright (c) 2018, hiwepy (https://github.com/hiwepy).
 *
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not
 * use this file except in compliance with the License. You may obtain a copy of
 * the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS, WITHOUT
 * WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the
 * License for the specific language governing permissions and limitations under
 * the License.
 */
package org.apache.cxf.spring.boot.jaxrs.endpoint;

import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

import org.apache.commons.codec.digest.DigestUtils;
import org.apache.cxf.Bus;
import org.apache.cxf.endpoint.ServerImpl;
import org.apache.cxf.ext.logging.LoggingFeature;
import org.apache.cxf.jaxrs.JAXRSServerFactoryBean;
import org.apache.cxf.metrics.MetricsFeature;
import org.apache.cxf.spring.boot.CxfJaxrsServerProperties;
import org.apache.cxf.spring.boot.jaxrs.callback.DefaultEndpointCallback;
import org.apache.cxf.validation.BeanValidationFeature;

import com.fasterxml.jackson.jakarta.rs.json.JacksonXmlBindJsonProvider;

/**
 * TODO
 * @author <a href="https://github.com/loong10k">Loong Wan</a>
 */
public class EndpointApiTemplate {
	
	private ConcurrentMap<String, ServerImpl> endpointServers = new ConcurrentHashMap<String, ServerImpl>();
	private Bus bus;
	private EndpointCallback callback;
	private LoggingFeature loggingFeature;
	private MetricsFeature metricsFeature;
	private BeanValidationFeature validationFeature;
	private CxfJaxrsServerProperties serverProperties;
	

	public EndpointApiTemplate(Bus bus, CxfJaxrsServerProperties serverProperties) {
		this.bus = bus;
		this.serverProperties = serverProperties;
		refreshDefaultCallback();
	}

	/**
	 * 为指定的addr发布Endpoint
	 * @author <a href="https://github.com/loong10k">Loong Wan</a>
	 * @param addr
	 * @param implementors
	 * @return
	 */
	public ServerImpl publish(String addr, Object... implementors) {
		return this.publish(addr, callback, implementors);
	}

	/**
	 * 为指定的addr发布Endpoint
	 * @author <a href="https://github.com/loong10k">Loong Wan</a>
	 * @param addr
	 * @param implementor
	 * @param callback
	 * @return
	 */
	public ServerImpl publish(String addr, EndpointCallback callback, Object... implementors) {
		
		// 1). 服务端工厂类
		JAXRSServerFactoryBean factoryBean = new JAXRSServerFactoryBean();
		
		// 2). 设置属性
		factoryBean.setAddress(addr);
		//factoryBean.setApplication(app);
		factoryBean.setBindingId(DigestUtils.md5Hex(addr));
		factoryBean.setBus(bus);
		factoryBean.setServiceBeanObjects(implementors);
		factoryBean.setExtensionMappings(serverProperties.getExtensionMappings());
		factoryBean.setLanguageMappings(serverProperties.getLanguageMappings());;
		factoryBean.setProperties(serverProperties.getProperties());
		// 添加 Provider
		factoryBean.setProvider(new JacksonXmlBindJsonProvider());
		factoryBean.setPublishedEndpointUrl(serverProperties.getPublishedEndpointUrl());
		
		// 3). 调用回调函数，个性化设置拦截器、Provider、Feature
		callback.doCallback(factoryBean, implementors );
		
		// 4). 创建并发布服务，会发起一个http服务，默认使用Jetty
		ServerImpl server = (ServerImpl) factoryBean.create();
		
		endpointServers.put(addr, server);

		return server;
	}
	
	/**
	 * 为指定的addr发布Endpoint
	 * @author <a href="https://github.com/loong10k">Loong Wan</a>
	 * @param addr
	 * @param callback
	 * @param classes
	 * @return
	 */
	public ServerImpl publish(String addr, EndpointCallback callback, Class<?>... classes) {
		
		// 1). 服务端工厂类
		JAXRSServerFactoryBean factoryBean = new JAXRSServerFactoryBean();
		
		// 2). 设置属性
		factoryBean.setAddress(addr);
		//factoryBean.setApplication(app);
		factoryBean.setBindingId(DigestUtils.md5Hex(addr));
		factoryBean.setBus(bus);
		factoryBean.setExtensionMappings(serverProperties.getExtensionMappings());
		factoryBean.setLanguageMappings(serverProperties.getLanguageMappings());
		factoryBean.setProperties(serverProperties.getProperties());
		// 添加 Provider
		factoryBean.setProvider(new JacksonXmlBindJsonProvider());
		factoryBean.setPublishedEndpointUrl(serverProperties.getPublishedEndpointUrl());
		factoryBean.setResourceClasses(classes);
		
		// 3). 调用回调函数，个性化设置拦截器、Provider、Feature
		callback.doCallback(factoryBean, classes );
		
		// 4). 创建并发布服务，会发起一个http服务，默认使用Jetty
		ServerImpl server = (ServerImpl) factoryBean.create();
		
		endpointServers.put(addr, server);

		return server;
	}

	/**
	 * 销毁指定路径匹配的Endpoint
	 * @author <a href="https://github.com/loong10k">Loong Wan</a>
	 * @param addr
	 */
	public void destroy(String addr) {
		ServerImpl myServer = (ServerImpl) endpointServers.get(addr);
		if (myServer != null) {
			myServer.destroy();
		}
	}

	public Bus getBus() {
		return bus;
	}

	public void setBus(Bus bus) {
		this.bus = bus;
	}

	public LoggingFeature getLoggingFeature() {
		return loggingFeature;
	}

	public void setLoggingFeature(LoggingFeature loggingFeature) {
		this.loggingFeature = loggingFeature;
		refreshDefaultCallback();
	}

	public MetricsFeature getMetricsFeature() {
		return metricsFeature;
	}

	public void setMetricsFeature(MetricsFeature metricsFeature) {
		this.metricsFeature = metricsFeature;
		refreshDefaultCallback();
	}

	public BeanValidationFeature getValidationFeature() {
		return validationFeature;
	}

	public void setValidationFeature(BeanValidationFeature validationFeature) {
		this.validationFeature = validationFeature;
		refreshDefaultCallback();
	}

	private void refreshDefaultCallback() {
		this.callback = new DefaultEndpointCallback(loggingFeature, metricsFeature, validationFeature);
	}
	
}
