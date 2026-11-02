package org.apache.cxf.spring.boot;

import org.apache.cxf.Bus;
import org.apache.cxf.bus.spring.SpringBus;
import org.apache.cxf.jaxrs.ext.MessageContext;
import org.apache.cxf.jaxrs.impl.tl.ThreadLocalMessageContext;
import org.apache.cxf.jaxrs.provider.RequestDispatcherProvider;
import org.apache.cxf.rs.security.saml.sso.AuthnRequestBuilder;
import org.apache.cxf.rs.security.saml.sso.DefaultAuthnRequestBuilder;
import org.apache.cxf.rs.security.saml.sso.SamlPostBindingFilter;
import org.apache.cxf.rs.security.saml.sso.SamlRedirectBindingFilter;
import org.apache.cxf.rs.security.saml.sso.state.EHCacheSPStateManager;
import org.apache.cxf.rs.security.saml.sso.state.MemorySPStateManager;
import org.apache.cxf.rs.security.saml.sso.state.SPStateManager;
import org.apache.cxf.spring.boot.property.CxfJaxwsSamlBindingProperty;
import org.apache.cxf.spring.boot.property.CxfJaxwsSamlDispatcherProperty;
import org.apache.cxf.spring.boot.property.CxfJaxwsSamlPostBindingProperty;
import org.apache.cxf.spring.boot.property.CxfJaxwsSamlRedirectBindingProperty;
import org.apache.cxf.transport.servlet.CXFServlet;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeansException;
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

//http://cxf.apache.org/docs/saml-web-sso.html

@AutoConfigureAfter( name = {
	"org.apache.cxf.spring.boot.autoconfigure.CxfAutoConfiguration"
})
@Configuration
@ConditionalOnWebApplication
@ConditionalOnClass({ SpringBus.class, CXFServlet.class })
@ConditionalOnProperty(prefix = CxfJaxrsSamlProperties.PREFIX, value = "enabled", havingValue = "true")
@EnableConfigurationProperties({ CxfJaxrsSamlProperties.class })
public class CxfJaxrsSamlConfiguration implements ApplicationContextAware {

	private static final Logger LOG = LoggerFactory.getLogger(CxfJaxrsSamlConfiguration.class);
	private ApplicationContext applicationContext;

	@Bean
	@ConditionalOnMissingBean(MessageContext.class)
	public MessageContext messageContext() {
		return new ThreadLocalMessageContext();
	}
	
	@Bean
	@ConditionalOnMissingBean(RequestDispatcherProvider.class)
	public RequestDispatcherProvider samlRequestFormCreator(Bus bus, MessageContext context, 
			CxfJaxrsSamlProperties properties) {
		
		CxfJaxwsSamlDispatcherProperty property = properties.getDispatcher();
		
		RequestDispatcherProvider dispatcher = new RequestDispatcherProvider();
		dispatcher.setBeanName(property.getBeanName());
		dispatcher.setBeanNames(property.getBeanNames());
		dispatcher.setBus(bus);
		dispatcher.setClassResources(property.getClassResources());
		dispatcher.setConsumeMediaTypes(property.getConsumeMediaTypes());
		dispatcher.setDispatcherName(property.getDispatcherName());
		dispatcher.setEnableBuffering(property.isEnableBuffering());
		dispatcher.setEnableStreaming(property.isEnableStreaming());
		dispatcher.setEnumResources(property.getEnumResources());
		dispatcher.setErrorView(property.getErrorView());
		dispatcher.setIncludeResource(property.isIncludeResource());
		dispatcher.setLocationPrefix(property.getLocationPrefix());
		dispatcher.setLogRedirects(property.isLogRedirects());
		dispatcher.setMessageContext(context);
		dispatcher.setProduceMediaTypes(property.getProduceMediaTypes());
		dispatcher.setResourceExtension(property.getResourceExtension());
		dispatcher.setResourcePath(property.getResourcePath());
		dispatcher.setResourcePaths(property.getResourcePaths());
		dispatcher.setSaveParametersAsAttributes(property.isSaveParametersAsAttributes());
		dispatcher.setScope(property.getScope().get());
		dispatcher.setServletContextPath(property.getServletContextPath());
		dispatcher.setServletPath(property.getServletPath());
		dispatcher.setStrictPathCheck(property.isStrictPathCheck());
		dispatcher.setUseClassNames(property.isUseClassNames());
		dispatcher.setUseCurrentServlet(property.isUseCurrentServlet());
		
		return dispatcher;
	}
	
	@Bean
	@ConditionalOnMissingBean(AuthnRequestBuilder.class)
	public AuthnRequestBuilder authnRequestBuilder(CxfJaxrsSamlProperties properties) {
		
		DefaultAuthnRequestBuilder builder = new DefaultAuthnRequestBuilder();

		CxfJaxwsSamlBindingProperty property = properties.getRedirectBinding();
		
		builder.setForceAuthn(property.isForceAuthn());
		builder.setNameIDFormat(property.getNameIDFormat());
		builder.setPassive(property.isPassive());
		builder.setProtocolBinding(property.getProtocolBinding());
		
		return builder;
	}

	@Bean
	@ConditionalOnMissingBean(SPStateManager.class)
	public SPStateManager stateManager(Bus bus) throws ReflectiveOperationException {

		boolean ehcacheIsAvailable = false;
		try {
            if(null != Class.forName("net.sf.ehcache.Ehcache")) {
            	ehcacheIsAvailable = true;
            }
        } catch (Throwable t) {
        	ehcacheIsAvailable = false;
        }
		
		if(ehcacheIsAvailable) {
			return new EHCacheSPStateManager(bus);
		} else {
			return new MemorySPStateManager();
		}
		
    }
	
	@Bean
	public SamlRedirectBindingFilter redirectGetFilter(AuthnRequestBuilder authnRequestBuilder, 
			SPStateManager stateManager, CxfJaxrsSamlProperties properties) {
		
		CxfJaxwsSamlRedirectBindingProperty property = properties.getRedirectBinding();
		
		SamlRedirectBindingFilter redirectFilter = new SamlRedirectBindingFilter();
		
		redirectFilter.setAddEndpointAddressToContext(property.isAddEndpointAddressToContext());
		redirectFilter.setAddWebAppContext(property.isAddWebAppContext());
		redirectFilter.setAssertionConsumerServiceAddress(property.getAssertionConsumerServiceAddress());
		redirectFilter.setAuthnRequestBuilder(authnRequestBuilder);
		redirectFilter.setCallbackHandlerClass(property.getCallbackHandlerClass());
		redirectFilter.setIdpServiceAddress(property.getIdpServiceAddress());
		redirectFilter.setIssuerId(property.getIssuerId());
		if(property.isSignRequest()) {
			//redirectFilter.setSignatureCrypto(crypto);
			redirectFilter.setSignaturePropertiesFile(property.getSignaturePropertiesFile());
			redirectFilter.setSignatureUsername(property.getSignatureUsername());
			redirectFilter.setSignRequest(property.isSignRequest());
		}
		
		redirectFilter.setStateProvider(stateManager);
		redirectFilter.setStateTimeToLive(property.getStateTimeToLive());
		redirectFilter.setSupportUnsolicited(property.isSupportUnsolicited());
		redirectFilter.setWebAppDomain(property.getWebAppDomain());
		
		return redirectFilter;
	}
	
	@Bean
	public SamlPostBindingFilter postBindingFilter(AuthnRequestBuilder authnRequestBuilder, 
			SPStateManager stateManager, CxfJaxrsSamlProperties properties) {
		
		CxfJaxwsSamlPostBindingProperty property = properties.getPostBinding();
		
		SamlPostBindingFilter postFilter = new SamlPostBindingFilter();
		postFilter.setAddEndpointAddressToContext(property.isAddEndpointAddressToContext());
		postFilter.setAddWebAppContext(property.isAddWebAppContext());
		postFilter.setAssertionConsumerServiceAddress(property.getAssertionConsumerServiceAddress());
		postFilter.setAuthnRequestBuilder(authnRequestBuilder);
		postFilter.setCallbackHandlerClass(property.getCallbackHandlerClass());
		postFilter.setIdpServiceAddress(property.getIdpServiceAddress());
		postFilter.setIssuerId(property.getIssuerId());
		if(property.isSignRequest()) {
			//postFilter.setSignatureCrypto(crypto);
			postFilter.setSignaturePropertiesFile(property.getSignaturePropertiesFile());
			postFilter.setSignatureUsername(property.getSignatureUsername());
			postFilter.setSignRequest(property.isSignRequest());
		}
		
		postFilter.setStateProvider(stateManager);
		postFilter.setStateTimeToLive(property.getStateTimeToLive());
		postFilter.setSupportUnsolicited(property.isSupportUnsolicited());
		postFilter.setUseDeflateEncoding(property.isUseDeflateEncoding());
		postFilter.setWebAppDomain(property.getWebAppDomain());
		
		return postFilter;
	}
	
	@Override
	public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
		this.applicationContext = applicationContext;
	}

	public ApplicationContext getApplicationContext() {
		return applicationContext;
	}
	
}
