package org.apache.cxf.spring.boot.jaxrs.endpoint;

import org.apache.cxf.Bus;
import org.apache.cxf.bus.spring.SpringBus;
import org.apache.cxf.feature.LoggingFeature;
import org.apache.cxf.metrics.MetricsFeature;
import org.apache.cxf.spring.boot.CxfJaxrsServerProperties;
import org.apache.cxf.validation.BeanValidationFeature;
import org.apache.cxf.endpoint.ServerImpl;
import org.apache.cxf.jaxrs.JAXRSServerFactoryBean;
import org.apache.cxf.feature.Feature;
import org.apache.cxf.interceptor.Interceptor;
import org.apache.cxf.message.Message;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.mockito.Mockito.*;
import java.util.ArrayList;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * Unit tests for {@link EndpointApiTemplate}.
 *
 * @author <a href="https://github.com/loong10k">Loong Wan</a>
 * @since 1.0.0
 */
class EndpointApiTemplateTest {

    private SpringBus bus;
    private CxfJaxrsServerProperties serverProperties;
    private EndpointApiTemplate template;

    @BeforeEach
    void setUp() {
        bus = new SpringBus();
        serverProperties = new CxfJaxrsServerProperties();
        template = new EndpointApiTemplate(bus, serverProperties);
    }

    @Test
    void constructor_shouldCreateInstance() {
        assertThat(template).isNotNull();
        assertThat(template.getBus()).isSameAs(bus);
    }

    @Test
    void loggingFeature_shouldBeSettable() {
        LoggingFeature loggingFeature = new LoggingFeature();
        template.setLoggingFeature(loggingFeature);
        assertThat(template.getLoggingFeature()).isSameAs(loggingFeature);
    }

    @Test
    void metricsFeature_shouldBeSettable() {
        template.setMetricsFeature(null);
        assertThat(template.getMetricsFeature()).isNull();
    }

    @Test
    void validationFeature_shouldBeSettable() {
        BeanValidationFeature validationFeature = new BeanValidationFeature();
        template.setValidationFeature(validationFeature);
        assertThat(template.getValidationFeature()).isSameAs(validationFeature);
    }

    @Test
    void bus_shouldBeSettable() {
        SpringBus newBus = new SpringBus();
        template.setBus(newBus);
        assertThat(template.getBus()).isSameAs(newBus);
    }

    @Test
    void publish_shouldExist() throws Exception {
        // Verify the publish method exists
        assertThat(EndpointApiTemplate.class.getMethod("publish", String.class, Object[].class)).isNotNull();
    }

    @Test
    void destroy_shouldExist() throws Exception {
        // Verify the destroy method exists
        assertThat(EndpointApiTemplate.class.getMethod("destroy", String.class)).isNotNull();
    }

    @Test
    void publishesConfiguredServiceBeansAndDestroysTheirServer() {
        JAXRSServerFactoryBean factory = mock(JAXRSServerFactoryBean.class);
        ServerImpl server = mock(ServerImpl.class);
        when(factory.create()).thenReturn(server);
        EndpointCallback callback = mock(EndpointCallback.class);
        EndpointApiTemplate publishingTemplate = new EndpointApiTemplate(bus, serverProperties) {
            @Override protected JAXRSServerFactoryBean newServerFactoryBean() { return factory; }
        };
        Object resource = new Object();
        assertThat(publishingTemplate.publish("local://orders", callback, resource)).isSameAs(server);
        verify(factory).setAddress("local://orders");
        verify(factory).setBus(bus);
        verify(factory).setServiceBeanObjects(resource);
        verify(callback).doCallback(factory, resource);
        publishingTemplate.destroy("local://orders");
        verify(server).destroy();
        publishingTemplate.destroy("local://missing");
        verifyNoMoreInteractions(server);
    }

    @Test
    void publishesConfiguredResourceClasses() {
        JAXRSServerFactoryBean factory = mock(JAXRSServerFactoryBean.class);
        ServerImpl server = mock(ServerImpl.class);
        when(factory.create()).thenReturn(server);
        EndpointCallback callback = mock(EndpointCallback.class);
        EndpointApiTemplate publishingTemplate = new EndpointApiTemplate(bus, serverProperties) {
            @Override protected JAXRSServerFactoryBean newServerFactoryBean() { return factory; }
        };
        assertThat(publishingTemplate.publish("local://classes", callback, String.class)).isSameAs(server);
        verify(factory).setResourceClasses(String.class);
        verify(callback).doCallback(factory, String.class);
        publishingTemplate.destroy("local://classes");
        verify(server).destroy();
    }

    @Test
    void defaultPublishRegistersJakartaJsonProviderAndLoggingInterceptors() {
        JAXRSServerFactoryBean factory = mock(JAXRSServerFactoryBean.class);
        ServerImpl server = mock(ServerImpl.class);
        when(factory.create()).thenReturn(server);
        List<Interceptor<? extends Message>> inbound = new ArrayList<>();
        List<Interceptor<? extends Message>> outbound = new ArrayList<>();
        List<Feature> features = new ArrayList<>();
        when(factory.getInInterceptors()).thenReturn(inbound);
        when(factory.getOutInterceptors()).thenReturn(outbound);
        when(factory.getFeatures()).thenReturn(features);
        EndpointApiTemplate publishingTemplate = new EndpointApiTemplate(bus, serverProperties) {
            @Override protected JAXRSServerFactoryBean newServerFactoryBean() { return factory; }
        };
        assertThat(publishingTemplate.publish("local://default", new Object())).isSameAs(server);
        verify(factory).setProviders(argThat(providers -> providers.size() == 1
                && providers.get(0) instanceof com.fasterxml.jackson.jakarta.rs.json.JacksonJsonProvider));
        assertThat(inbound).hasSize(1);
        assertThat(outbound).hasSize(1);
    }

}
