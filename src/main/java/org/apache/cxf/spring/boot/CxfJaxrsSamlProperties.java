package org.apache.cxf.spring.boot;

import java.util.NoSuchElementException;

import org.apache.cxf.spring.boot.property.CxfJaxwsSamlBindingProperty;
import org.apache.cxf.spring.boot.property.CxfJaxwsSamlDispatcherProperty;
import org.apache.cxf.spring.boot.property.CxfJaxwsSamlEHCacheProperty;
import org.apache.cxf.spring.boot.property.CxfJaxwsSamlPostBindingProperty;
import org.apache.cxf.spring.boot.property.CxfJaxwsSamlRedirectBindingProperty;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.context.properties.NestedConfigurationProperty;
/** Configuration properties for Cxf Jaxrs Saml.
 *
 * @author <a href="https://github.com/loong10k">Loong Wan</a>
 * @since 1.0.0
 */

@ConfigurationProperties(CxfJaxrsSamlProperties.PREFIX)
/**
 * <p>Auto-configuration for CxfJaxrsSamlProperties.</p>
 * @author <a href="https://github.com/loong10k">Loong Wan</a>
 * @since 1.0.0
 */
public class CxfJaxrsSamlProperties {

	public static final String PREFIX = "cxf.jaxws.saml";
	/** The Scope.
	 *
	 * @author <a href="https://github.com/loong10k">Loong Wan</a>
	 * @since 1.0.0
	 */

	public enum Scope {

		REQUEST_SCOPE("request"), SESSION_SCOPE("session");

		private final String scope;

		Scope(String scope) {
			this.scope = scope;
		}

		/**
		 * <p>Get.</p>
		 * @return the result
		 */
		public String get() {
			return this.scope;
		}

		/** Indicates whether some other object is equal to this one.
		 * @param scope the scope
		 * @return the result
		 */
		public boolean equals(Scope scope) {
			return this.compareTo(scope) == 0;
		}

		/** Indicates whether some other object is equal to this one.
		 * @param scope the scope
		 * @return the result
		 */
		public boolean equals(String scope) {
			return this.compareTo(Scope.valueOfIgnoreCase(scope)) == 0;
		}

		/**
		 * <p>Value of ignore case.</p>
		 * @param key
		 * @return the result
		 */
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
	

	/** Returns whether the enabled is enabled.
	 * @return the result
	 */
	public boolean isEnabled() {
		return enabled;
	}

	/** Sets the enabled.
	 * @param enabled the enabled
	 */
	public void setEnabled(boolean enabled) {
		this.enabled = enabled;
	}

	/** Returns the dispatcher.
	 * @return the result
	 */
	public CxfJaxwsSamlDispatcherProperty getDispatcher() {
		return dispatcher;
	}

	/** Sets the dispatcher.
	 * @param dispatcher the dispatcher
	 */
	public void setDispatcher(CxfJaxwsSamlDispatcherProperty dispatcher) {
		this.dispatcher = dispatcher;
	}

	/** Returns the post binding.
	 * @return the result
	 */
	public CxfJaxwsSamlPostBindingProperty getPostBinding() {
		return postBinding;
	}

	/** Sets the post binding.
	 * @param postBinding the postBinding
	 */
	public void setPostBinding(CxfJaxwsSamlPostBindingProperty postBinding) {
		this.postBinding = postBinding;
	}

	/** Returns the redirect binding.
	 * @return the result
	 */
	public CxfJaxwsSamlRedirectBindingProperty getRedirectBinding() {
		return redirectBinding;
	}

	/** Sets the redirect binding.
	 * @param redirectBinding the redirectBinding
	 */
	public void setRedirectBinding(CxfJaxwsSamlRedirectBindingProperty redirectBinding) {
		this.redirectBinding = redirectBinding;
	}

	/** Returns the ehcache.
	 * @return the result
	 */
	public CxfJaxwsSamlEHCacheProperty getEhcache() {
		return ehcache;
	}

	/** Sets the ehcache.
	 * @param ehcache the ehcache
	 */
	public void setEhcache(CxfJaxwsSamlEHCacheProperty ehcache) {
		this.ehcache = ehcache;
	}

}
