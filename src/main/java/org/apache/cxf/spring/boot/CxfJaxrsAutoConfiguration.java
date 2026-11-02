package org.apache.cxf.spring.boot;

import org.apache.cxf.Bus;
import org.apache.cxf.BusFactory;
import org.apache.cxf.bus.spring.SpringBus;
import org.apache.cxf.ext.logging.LoggingFeature;
import org.apache.cxf.jaxrs.validation.ValidationExceptionMapper;
import org.apache.cxf.metrics.MetricsFeature;
import org.apache.cxf.metrics.MetricsProvider;
import org.apache.cxf.metrics.codahale.CodahaleMetricsProvider;
import org.apache.cxf.rs.security.oauth2.grants.code.EHCacheCodeDataProvider;
import org.apache.cxf.rs.security.oauth2.provider.OAuthDataProvider;
import org.apache.cxf.rs.security.oauth2.services.AccessTokenService;
import org.apache.cxf.spring.boot.endpoint.APIEndpointRepository;
import org.apache.cxf.spring.boot.jaxrs.endpoint.EndpointApiTemplate;
import org.apache.cxf.spring.boot.property.LoggingFeatureProperty;
import org.apache.cxf.transport.servlet.CXFServlet;
import org.apache.cxf.validation.BeanValidationFeature;
import org.apache.cxf.validation.BeanValidationProvider;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.AutoConfigureAfter;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.autoconfigure.condition.ConditionalOnWebApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

//http://cxf.apache.org/docs/springboot.html
@AutoConfigureAfter(name = { "org.apache.cxf.spring.boot.autoconfigure.CxfAutoConfiguration" })
@Configuration
@ConditionalOnWebApplication
@ConditionalOnClass({ SpringBus.class, CXFServlet.class })
@ConditionalOnProperty(prefix = CxfJaxrsProperties.PREFIX, value = "enabled", havingValue = "true")
@EnableConfigurationProperties({ CxfJaxrsProperties.class })
public class CxfJaxrsAutoConfiguration implements ApplicationContextAware {

	private ApplicationContext applicationContext;

	@Autowired
	private Bus bus;
	@Autowired
	private APIEndpointRepository endpointRepository;

	@Bean
	@ConditionalOnMissingBean(Bus.class)
	public Bus bus() {
		SpringBus bus = new SpringBus();
		BusFactory.setDefaultBus(bus);
		return bus;
	}

	@Bean
	@ConditionalOnMissingBean(BeanValidationProvider.class)
	public BeanValidationProvider validationProvider() {
		return new BeanValidationProvider();
	}

	@Bean
	public BeanValidationFeature validationFeature(BeanValidationProvider validationProvider) {
		BeanValidationFeature feature = new BeanValidationFeature();
		feature.setProvider(validationProvider);
		return feature;
	}

	@Bean
	public ValidationExceptionMapper exceptionMapper() {
		return new ValidationExceptionMapper();
	}

	@Bean
	public LoggingFeature loggingFeature(CxfJaxrsProperties properties) {
		
		LoggingFeatureProperty property = properties.getLoggingFeature();

		LoggingFeature feature = new LoggingFeature();
		feature.setInMemThreshold(property.getThreshold());
		feature.setLimit(property.getLimit());
		feature.setLogBinary(property.isLogBinary());
		feature.setLogMultipart(property.isLogMultipart());
		feature.setPrettyLogging(property.isPrettyLogging());
		feature.setVerbose(property.isVerbose());
		
		return feature;
	}
	
	@Bean
	@ConditionalOnMissingBean(MetricsProvider.class)
	public MetricsProvider metricsProvider(Bus bus) {
		return new CodahaleMetricsProvider(bus);
	}

	@Bean
	public MetricsFeature metricsFeature(MetricsProvider metricsProvider) {
		return new MetricsFeature(metricsProvider);
	}
	
	@Bean
	public EndpointApiTemplate endpointTemplate(Bus bus,
			LoggingFeature loggingFeature,
			MetricsFeature metricsFeature,
			BeanValidationFeature validationFeature,
			CxfJaxrsProperties properties) {
		
		EndpointApiTemplate template = new EndpointApiTemplate(bus, properties.getServer());

		template.setLoggingFeature(loggingFeature);
		template.setMetricsFeature(metricsFeature);
		template.setValidationFeature(validationFeature);
		
		return template;
	}
	
	

	/**
	 * 决定一个消费者将如何等待生产者将Event置入Disruptor的策略。用来权衡当生产者无法将新的事件放进RingBuffer时的处理策略。
	 * （例如：当生产者太快，消费者太慢，会导致生成者获取不到新的事件槽来插入新事件，则会根据该策略进行处理，默认会堵塞）
	 */
	@Bean
	@ConditionalOnMissingBean
	public EHCacheCodeDataProvider oauthProvider() {

		/*
		 * <bean id="oauthProvider" class=
		 * "org.apache.cxf.systest.jaxrs.security.oauth2.common.OAuthDataProviderImpl">
		 *        <property name="useJwtFormatForAccessTokens" value="true"/>
		 *        <property name="storeJwtTokenKeyOnly" value="true"/> </bean>
		 */
		/*
		 * <bean id="oauthProvider" class=
		 * "org.apache.cxf.rs.security.oauth2.grants.code.EHCacheCodeDataProvider">
		 *        <property name="useJwtFormatForAccessTokens" value="true"/> </bean>
		 */

		EHCacheCodeDataProvider dataProvider = new EHCacheCodeDataProvider();

		dataProvider.setUseJwtFormatForAccessTokens(true);

		return dataProvider;
	}

	@Bean
	@ConditionalOnMissingBean
	public AccessTokenService accessTokenService(OAuthDataProvider dataProvider) {
		/*
		 * <bean id="oauthProvider" class="oauth2.manager.OAuthManager"/>
		 * 
		 * <bean id="accessTokenService"
		 * class="org.apache.cxf.rs.security.oauth2.services.AccessTokenService">
		 * <property name="dataProvider" ref="oauthProvider"/> <property
		 * name="writeCustomErrors" value="true"/> </bean>
		 */

		AccessTokenService accessTokenService = new AccessTokenService();

		accessTokenService.setDataProvider(dataProvider);
		accessTokenService.setWriteCustomErrors(true);

		return accessTokenService;
	}

	@Override
	public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
		this.applicationContext = applicationContext;
	}

	public ApplicationContext getApplicationContext() {
		return applicationContext;
	}

	public APIEndpointRepository getEndpointRepository() {
		return endpointRepository;
	}

	public void setEndpointRepository(APIEndpointRepository endpointRepository) {
		this.endpointRepository = endpointRepository;
	}

}
