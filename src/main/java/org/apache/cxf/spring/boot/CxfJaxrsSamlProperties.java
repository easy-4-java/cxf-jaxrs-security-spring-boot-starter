package org.apache.cxf.spring.boot;

import java.util.NoSuchElementException;

import org.apache.cxf.spring.boot.property.CxfJaxwsSamlBindingProperty;
import org.apache.cxf.spring.boot.property.CxfJaxwsSamlDispatcherProperty;
import org.apache.cxf.spring.boot.property.CxfJaxwsSamlEHCacheProperty;
import org.apache.cxf.spring.boot.property.CxfJaxwsSamlPostBindingProperty;
import org.apache.cxf.spring.boot.property.CxfJaxwsSamlRedirectBindingProperty;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.context.properties.NestedConfigurationProperty;

@ConfigurationProperties(CxfJaxrsSamlProperties.PREFIX)
public class CxfJaxrsSamlProperties {

	public static final String PREFIX = "cxf.jaxws.saml";

	public enum Scope {

		REQUEST_SCOPE("request"), SESSION_SCOPE("session");

		private final String scope;

		Scope(String scope) {
			this.scope = scope;
		}

		public String get() {
			return this.scope;
		}

		public boolean equals(Scope scope) {
			return this.compareTo(scope) == 0;
		}

		public boolean equals(String scope) {
			return this.compareTo(Scope.valueOfIgnoreCase(scope)) == 0;
		}

		public static Scope valueOfIgnoreCase(String key) {
			for (Scope scope : Scope.values()) {
				if (scope.get().equalsIgnoreCase(key)) {
					return scope;
				}
			}
			throw new NoSuchElementException("Cannot found Scope with key '" + key + "'.");
		}

	}
	 

	/**
	 * If the JMX integration should be enabled or not
	 */
	private boolean enabled = false;

	@NestedConfigurationProperty
	private CxfJaxwsSamlDispatcherProperty dispatcher= new CxfJaxwsSamlDispatcherProperty();
	@NestedConfigurationProperty
	private CxfJaxwsSamlPostBindingProperty postBinding = new CxfJaxwsSamlPostBindingProperty();
	@NestedConfigurationProperty
	private CxfJaxwsSamlRedirectBindingProperty redirectBinding = new CxfJaxwsSamlRedirectBindingProperty();
	
	/**
	 * EHCacheSPStateManager
	 */
	@NestedConfigurationProperty
	private CxfJaxwsSamlEHCacheProperty ehcache = new CxfJaxwsSamlEHCacheProperty();
	

	public boolean isEnabled() {
		return enabled;
	}

	public void setEnabled(boolean enabled) {
		this.enabled = enabled;
	}

	public CxfJaxwsSamlDispatcherProperty getDispatcher() {
		return dispatcher;
	}

	public void setDispatcher(CxfJaxwsSamlDispatcherProperty dispatcher) {
		this.dispatcher = dispatcher;
	}

	public CxfJaxwsSamlPostBindingProperty getPostBinding() {
		return postBinding;
	}

	public void setPostBinding(CxfJaxwsSamlPostBindingProperty postBinding) {
		this.postBinding = postBinding;
	}

	public CxfJaxwsSamlRedirectBindingProperty getRedirectBinding() {
		return redirectBinding;
	}

	public void setRedirectBinding(CxfJaxwsSamlRedirectBindingProperty redirectBinding) {
		this.redirectBinding = redirectBinding;
	}

	public CxfJaxwsSamlEHCacheProperty getEhcache() {
		return ehcache;
	}

	public void setEhcache(CxfJaxwsSamlEHCacheProperty ehcache) {
		this.ehcache = ehcache;
	}

}