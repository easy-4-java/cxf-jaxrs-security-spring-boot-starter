package org.apache.cxf.spring.boot;

import org.apache.cxf.feature.LoggingFeature;
import org.apache.cxf.spring.boot.jaxrs.endpoint.EndpointApiTemplate;
import org.apache.cxf.bus.spring.SpringBus;
import org.apache.cxf.metrics.MetricsFeature;
import org.apache.cxf.validation.BeanValidationFeature;
import org.apache.cxf.validation.BeanValidationProvider;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * Unit tests for {@link CxfJaxrsAutoConfiguration}.
 *
 * @author <a href="https://github.com/loong10k">Loong Wan</a>
 * @since 1.0.0
 */
class CxfJaxrsAutoConfigurationTest {

    @Test
    void constructor_shouldCreateInstance() {
        CxfJaxrsAutoConfiguration config = new CxfJaxrsAutoConfiguration();
        assertThat(config).isNotNull();
    }

    @Test
    void applicationContext_shouldBeSettable() {
        CxfJaxrsAutoConfiguration config = new CxfJaxrsAutoConfiguration();
        assertThat(config.getApplicationContext()).isNull();
    }

    @Test
    void endpointRepository_shouldBeNullByDefault() {
        CxfJaxrsAutoConfiguration config = new CxfJaxrsAutoConfiguration();
        assertThat(config.getEndpointRepository()).isNull();
    }

    @Test
    void configuration_shouldImplementApplicationContextAware() {
        CxfJaxrsAutoConfiguration config = new CxfJaxrsAutoConfiguration();
        assertThat(config).isInstanceOf(org.springframework.context.ApplicationContextAware.class);
    }

    @Test
    void buildsServerFeaturesAndInjectsThemIntoTheEndpointTemplate() {
        CxfJaxrsAutoConfiguration config = new CxfJaxrsAutoConfiguration();
        CxfJaxrsProperties properties = new CxfJaxrsProperties();
        properties.getLoggingFeature().setLimit(321);
        properties.getLoggingFeature().setPrettyLogging(true);
        SpringBus bus = config.springBus();
        try {
            LoggingFeature logging = config.loggingFeature(properties);
            MetricsFeature metrics = config.metricsFeature(bus);
            BeanValidationFeature validation = config.validationFeature();
            BeanValidationProvider provider = config.validationProvider();
            assertThat(config.validationExceptionMapper()).isNotNull();
            assertThat(metrics).isNotNull();
            assertThat(provider).isNotNull();
            EndpointApiTemplate template = config.endpointApiTemplate(bus, logging, metrics, validation, properties);
            assertThat(template.getBus()).isSameAs(bus);
            assertThat(template.getLoggingFeature()).isSameAs(logging);
            assertThat(template.getMetricsFeature()).isSameAs(metrics);
            assertThat(template.getValidationFeature()).isSameAs(validation);
            assertThat(logging.getLimit()).isEqualTo(321);
            assertThat(config.getApplicationContext()).isNull();
        } finally {
            bus.shutdown(true);
        }
    }

}
