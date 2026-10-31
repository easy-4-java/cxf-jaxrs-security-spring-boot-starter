package org.apache.cxf.spring.boot.jaxrs.callback;

import org.apache.cxf.feature.LoggingFeature;
import org.apache.cxf.spring.boot.jaxrs.endpoint.EndpointCallback;
import org.apache.cxf.jaxrs.JAXRSServerFactoryBean;
import org.apache.cxf.feature.Feature;
import org.apache.cxf.interceptor.Interceptor;
import org.apache.cxf.message.Message;
import org.apache.cxf.metrics.MetricsFeature;
import org.apache.cxf.validation.BeanValidationFeature;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.*;
import java.util.ArrayList;
import java.util.List;

/**
 * Unit tests for {@link DefaultEndpointCallback}.
 *
 * @author <a href="https://github.com/loong10k">Loong Wan</a>
 * @since 1.0.0
 */
class DefaultEndpointCallbackTest {

    private LoggingFeature loggingFeature;
    private BeanValidationFeature validationFeature;
    private DefaultEndpointCallback callback;

    @BeforeEach
    void setUp() {
        loggingFeature = new LoggingFeature();
        validationFeature = new BeanValidationFeature();
        callback = new DefaultEndpointCallback(loggingFeature, null, validationFeature);
    }

    @Test
    void constructor_shouldCreateInstance() {
        assertThat(callback).isNotNull();
    }

    @Test
    void callback_shouldImplementEndpointCallback() {
        assertThat(callback).isInstanceOf(EndpointCallback.class);
    }

    @Test
    void doCallback_shouldExist() throws Exception {
        assertThat(DefaultEndpointCallback.class.getMethod("doCallback",
                org.apache.cxf.jaxrs.JAXRSServerFactoryBean.class, Object[].class)).isNotNull();
    }

    @Test
    void configuresProvidersInterceptorsAndFeaturesForServiceObjects() {
        JAXRSServerFactoryBean factory = mock(JAXRSServerFactoryBean.class);
        List<Interceptor<? extends Message>> inbound = new ArrayList<>();
        List<Interceptor<? extends Message>> outbound = new ArrayList<>();
        List<Feature> features = new ArrayList<>();
        when(factory.getInInterceptors()).thenReturn(inbound);
        when(factory.getOutInterceptors()).thenReturn(outbound);
        when(factory.getFeatures()).thenReturn(features);
        MetricsFeature metrics = mock(MetricsFeature.class);
        new DefaultEndpointCallback(loggingFeature, metrics, validationFeature)
                .doCallback(factory, new Object());
        verify(factory).setProviders(argThat(providers -> providers.size() == 1
                && providers.get(0) instanceof com.fasterxml.jackson.jakarta.rs.json.JacksonJsonProvider));
        assertThat(inbound).hasSize(1);
        assertThat(outbound).hasSize(1);
        assertThat(features).containsExactly(metrics, loggingFeature, validationFeature);
    }

    @Test
    void configuresTheSameFeaturesForResourceClasses() {
        JAXRSServerFactoryBean factory = mock(JAXRSServerFactoryBean.class);
        List<Interceptor<? extends Message>> inbound = new ArrayList<>();
        List<Interceptor<? extends Message>> outbound = new ArrayList<>();
        List<Feature> features = new ArrayList<>();
        when(factory.getInInterceptors()).thenReturn(inbound);
        when(factory.getOutInterceptors()).thenReturn(outbound);
        when(factory.getFeatures()).thenReturn(features);
        callback.doCallback(factory, String.class);
        verify(factory).setProviders(argThat(providers -> providers.size() == 1
                && providers.get(0) instanceof com.fasterxml.jackson.jakarta.rs.json.JacksonJsonProvider));
        assertThat(inbound).hasSize(1);
        assertThat(outbound).hasSize(1);
        assertThat(features).containsExactly(null, loggingFeature, validationFeature);
    }

}
