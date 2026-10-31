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
package org.apache.cxf.spring.boot.jaxrs.callback;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.apache.cxf.feature.LoggingFeature;
import org.apache.cxf.interceptor.LoggingInInterceptor;
import org.apache.cxf.interceptor.LoggingOutInterceptor;
import org.apache.cxf.jaxrs.JAXRSServerFactoryBean;
import org.apache.cxf.metrics.MetricsFeature;
import org.apache.cxf.spring.boot.jaxrs.endpoint.EndpointCallback;
import org.apache.cxf.validation.BeanValidationFeature;

import com.fasterxml.jackson.jakarta.rs.json.JacksonJsonProvider;

/**
 * TODO
 * @author <a href="https://github.com/loong10k">Loong Wan</a>
 * @since 1.0.0
 */
public class DefaultEndpointCallback implements EndpointCallback {

	private LoggingFeature loggingFeature;
	private MetricsFeature metricsFeature;
	private BeanValidationFeature validationFeature;

	public DefaultEndpointCallback(LoggingFeature loggingFeature,
			MetricsFeature metricsFeature, BeanValidationFeature validationFeature) {
		this.loggingFeature = loggingFeature;
		this.metricsFeature = metricsFeature;
		this.validationFeature = validationFeature;
	}

	@Override
	/**
	 * <p>Do callback.</p>
	 * @param factoryBean
	 * @param implementors
	 */
	public void doCallback(JAXRSServerFactoryBean factoryBean, Object... implementors ) {

		// 3). 添加 Provider，用于支持自动解析各种数据格式、如Json
		List<Object> providerList = new ArrayList<Object>();
		providerList.add(new JacksonJsonProvider());
		factoryBean.setProviders(providerList); 
		
		//new Swagger2Feature()
		
		
		// 添加输入& 输出日志（可选）
		factoryBean.getInInterceptors().add(new LoggingInInterceptor());
		factoryBean.getOutInterceptors().add(new LoggingOutInterceptor());
		
		factoryBean.getFeatures().addAll(Arrays.asList(metricsFeature, loggingFeature, validationFeature));

	}

	/**
	 * TODO
	 * @author <a href="https://github.com/loong10k">Loong Wan</a>
	 * @param endpointFactory
	 * @param classes
	 */
	
	@Override
	/**
	 * <p>Do callback.</p>
	 * @param factoryBean
	 * @param classes
	 */
	public void doCallback(JAXRSServerFactoryBean factoryBean, Class<?>... classes) {

		// 3). 添加 Provider，用于支持自动解析各种数据格式、如Json
		List<Object> providerList = new ArrayList<Object>();
		providerList.add(new JacksonJsonProvider());
		factoryBean.setProviders(providerList); 

		// 添加输入& 输出日志（可选）
		factoryBean.getInInterceptors().add(new LoggingInInterceptor());
		factoryBean.getOutInterceptors().add(new LoggingOutInterceptor());
		
		factoryBean.getFeatures().addAll(Arrays.asList(metricsFeature, loggingFeature, validationFeature));

	}

}
