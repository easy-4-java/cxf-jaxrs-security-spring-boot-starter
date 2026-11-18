package org.apache.cxf.spring.boot.jaxrs.endpoint;

import org.apache.cxf.ext.logging.LoggingFeature;
import org.apache.cxf.jaxrs.JAXRSServerFactoryBean;
import org.apache.cxf.metrics.MetricsFeature;
import org.apache.cxf.spring.boot.CxfJaxrsServerProperties;
import org.apache.cxf.validation.BeanValidationFeature;
import org.junit.jupiter.api.Test;
import org.springframework.test.util.ReflectionTestUtils;

import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.mock;

/** Verifies that the starter's default endpoint callback receives configured features. */
public class EndpointApiTemplateTest {

    @Test
    public void defaultCallbackUsesFeaturesConfiguredAfterTemplateConstruction() {
        EndpointApiTemplate template = new EndpointApiTemplate(
            mock(org.apache.cxf.Bus.class), new CxfJaxrsServerProperties());
        LoggingFeature logging = new LoggingFeature();
        MetricsFeature metrics = new MetricsFeature();
        BeanValidationFeature validation = new BeanValidationFeature();
        template.setLoggingFeature(logging);
        template.setMetricsFeature(metrics);
        template.setValidationFeature(validation);

        EndpointCallback callback = (EndpointCallback) ReflectionTestUtils.getField(template, "callback");
        JAXRSServerFactoryBean factory = new JAXRSServerFactoryBean();
        callback.doCallback(factory, new Object());

        assertTrue(factory.getFeatures().contains(logging));
        assertTrue(factory.getFeatures().contains(metrics));
        assertTrue(factory.getFeatures().contains(validation));
    }
}
