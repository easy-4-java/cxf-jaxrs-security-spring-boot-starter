package org.apache.cxf.spring.boot;

import org.apache.cxf.spring.boot.property.LoggingFeatureProperty;

import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * Unit tests for {@link CxfJaxrsProperties}.
 *
 * @author <a href="https://github.com/loong10k">Loong Wan</a>
 * @since 1.0.0
 */
class CxfJaxrsPropertiesTest {

    @Test
    void defaultValues_shouldBeCorrect() {
        CxfJaxrsProperties properties = new CxfJaxrsProperties();

        assertThat(properties.isComponentScan()).isFalse();
        assertThat(properties.getComponentScanPackages()).isNull();
        assertThat(properties.getComponentScanBeans()).isNull();
        assertThat(properties.isClassesScan()).isFalse();
        assertThat(properties.getClassesScanPackages()).isNull();
        assertThat(properties.getServer()).isNotNull();
        assertThat(properties.getLoggingFeature()).isNotNull();
    }

    @Test
    void settersAndGetters_shouldWorkCorrectly() {
        CxfJaxrsProperties properties = new CxfJaxrsProperties();

        properties.setComponentScan(true);
        assertThat(properties.isComponentScan()).isTrue();

        properties.setComponentScanPackages("com.example");
        assertThat(properties.getComponentScanPackages()).isEqualTo("com.example");

        properties.setComponentScanBeans("bean1,bean2");
        assertThat(properties.getComponentScanBeans()).isEqualTo("bean1,bean2");

        properties.setClassesScan(true);
        assertThat(properties.isClassesScan()).isTrue();

        properties.setClassesScanPackages("com.example.classes");
        assertThat(properties.getClassesScanPackages()).isEqualTo("com.example.classes");
    }

    @Test
    void prefix_shouldBeCorrect() {
        assertThat(CxfJaxrsProperties.PREFIX).isEqualTo("cxf.jaxrs");
    }

    @Test
    void serverProperty_shouldBeSettable() {
        CxfJaxrsProperties properties = new CxfJaxrsProperties();
        CxfJaxrsServerProperties server = new CxfJaxrsServerProperties();
        server.setAddress("/api");
        properties.setServer(server);

        assertThat(properties.getServer().getAddress()).isEqualTo("/api");
    }

    @Test
    void loggingFeatureProperty_shouldBeSettable() {
        CxfJaxrsProperties properties = new CxfJaxrsProperties();
        assertThat(properties.getLoggingFeature()).isNotNull();
        LoggingFeatureProperty logging = new LoggingFeatureProperty();
        logging.setLimit(2048);
        properties.setLoggingFeature(logging);
        assertThat(properties.getLoggingFeature()).isSameAs(logging);
        assertThat(properties.getLoggingFeature().getLimit()).isEqualTo(2048);
    }

}
