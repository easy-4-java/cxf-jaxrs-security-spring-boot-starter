package org.apache.cxf.spring.boot;

import java.util.Map;

import org.apache.cxf.spring.boot.property.CxfJaxwsSamlDispatcherProperty;
import org.junit.jupiter.api.Test;
import org.springframework.boot.context.properties.bind.Binder;
import org.springframework.boot.context.properties.source.MapConfigurationPropertySource;

import static org.junit.jupiter.api.Assertions.*;

class CxfJaxrsSamlBindingTest {

    @Test
    void bindsExternalSamlDispatcherConfiguration() {
        MapConfigurationPropertySource source = new MapConfigurationPropertySource(Map.ofEntries(
                Map.entry("cxf.jaxws.saml.enabled", "true"),
                Map.entry("cxf.jaxws.saml.dispatcher.bean-name", "loginView"),
                Map.entry("cxf.jaxws.saml.dispatcher.resource-path", "/saml"),
                Map.entry("cxf.jaxws.saml.dispatcher.resource-extension", ".jsp"),
                Map.entry("cxf.jaxws.saml.dispatcher.location-prefix", "/WEB-INF/views"),
                Map.entry("cxf.jaxws.saml.dispatcher.servlet-context-path", "/auth"),
                Map.entry("cxf.jaxws.saml.dispatcher.dispatcher-name", "saml-jsp"),
                Map.entry("cxf.jaxws.saml.dispatcher.error-view", "/saml-error"),
                Map.entry("cxf.jaxws.saml.dispatcher.enable-buffering", "true"),
                Map.entry("cxf.jaxws.saml.dispatcher.enable-streaming", "true"),
                Map.entry("cxf.jaxws.saml.dispatcher.strict-path-check", "true"),
                Map.entry("cxf.jaxws.saml.dispatcher.log-redirects", "true"),
                Map.entry("cxf.jaxws.saml.dispatcher.use-current-servlet", "true"),
                Map.entry("cxf.jaxws.saml.dispatcher.save-parameters-as-attributes", "true"),
                Map.entry("cxf.jaxws.saml.dispatcher.include-resource", "true"),
                Map.entry("cxf.jaxws.saml.dispatcher.use-class-names", "false")));
        source.put("cxf.jaxws.saml.dispatcher.servlet-path", "/view");
        source.put("cxf.jaxws.saml.dispatcher.bean-names.login", "loginBean");
        source.put("cxf.jaxws.saml.dispatcher.resource-paths.invoice", "/invoice.jsp");
        source.put("cxf.jaxws.saml.dispatcher.class-resources.customer", "/customer.jsp");
        source.put("cxf.jaxws.saml.dispatcher.consume-media-types[0]", "application/xml");
        source.put("cxf.jaxws.saml.dispatcher.produce-media-types[0]", "text/html");
        source.put("cxf.jaxws.saml.post-binding.use-deflate-encoding", "true");
        source.put("cxf.jaxws.saml.ehcache.config-file-url", "/custom-saml-cache.xml");
        CxfJaxrsSamlProperties bound = new Binder(source)
                .bind(CxfJaxrsSamlProperties.PREFIX, CxfJaxrsSamlProperties.class)
                .orElseThrow(() -> new AssertionError("SAML properties did not bind"));
        assertTrue(bound.isEnabled());
        CxfJaxwsSamlDispatcherProperty dispatcher = bound.getDispatcher();
        assertEquals("loginView", dispatcher.getBeanName());
        assertEquals("/saml", dispatcher.getResourcePath());
        assertEquals(".jsp", dispatcher.getResourceExtension());
        assertEquals("/WEB-INF/views", dispatcher.getLocationPrefix());
        assertEquals("/auth", dispatcher.getServletContextPath());
        assertEquals("saml-jsp", dispatcher.getDispatcherName());
        assertEquals("/saml-error", dispatcher.getErrorView());
        assertTrue(dispatcher.isEnableBuffering());
        assertTrue(dispatcher.isEnableStreaming());
        assertTrue(dispatcher.isStrictPathCheck());
        assertTrue(dispatcher.isLogRedirects());
        assertTrue(dispatcher.isUseCurrentServlet());
        assertTrue(dispatcher.isSaveParametersAsAttributes());
        assertTrue(dispatcher.isIncludeResource());
        assertFalse(dispatcher.isUseClassNames());
        assertEquals("/view", dispatcher.getServletPath());
        assertEquals("loginBean", dispatcher.getBeanNames().get("login"));
        assertEquals("/invoice.jsp", dispatcher.getResourcePaths().get("invoice"));
        assertEquals("/customer.jsp", dispatcher.getClassResources().get("customer"));
        assertEquals("application/xml", dispatcher.getConsumeMediaTypes().get(0));
        assertEquals("text/html", dispatcher.getProduceMediaTypes().get(0));
        assertTrue(bound.getPostBinding().isUseDeflateEncoding());
        assertEquals("/custom-saml-cache.xml", bound.getEhcache().getConfigFileUrl());
    }
}
