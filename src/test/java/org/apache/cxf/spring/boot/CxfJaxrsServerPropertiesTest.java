package org.apache.cxf.spring.boot;

import org.junit.jupiter.api.Test;

import java.util.HashMap;
import java.util.Map;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * Unit tests for {@link CxfJaxrsServerProperties}.
 *
 * @author <a href="https://github.com/loong10k">Loong Wan</a>
 * @since 1.0.0
 */
class CxfJaxrsServerPropertiesTest {

    @Test
    void defaultValues_shouldBeCorrect() {
        CxfJaxrsServerProperties properties = new CxfJaxrsServerProperties();

        assertThat(properties.getAddress()).isNull();
        assertThat(properties.getPublishedEndpointUrl()).isNull();
    }

    @Test
    void settersAndGetters_shouldWorkCorrectly() {
        CxfJaxrsServerProperties properties = new CxfJaxrsServerProperties();

        properties.setAddress("/api/v1");
        assertThat(properties.getAddress()).isEqualTo("/api/v1");

        properties.setPublishedEndpointUrl("http://localhost:8080/api");
        assertThat(properties.getPublishedEndpointUrl()).isEqualTo("http://localhost:8080/api");
    }

    @Test
    void languageMappings_shouldBeSettable() {
        CxfJaxrsServerProperties properties = new CxfJaxrsServerProperties();

        Map<Object, Object> mappings = new HashMap<>();
        mappings.put("en", "English");
        mappings.put("zh", "Chinese");
        properties.setLanguageMappings(mappings);

        assertThat(properties.getLanguageMappings()).hasSize(2);
        assertThat(properties.getLanguageMappings().get("en")).isEqualTo("English");
    }

    @Test
    void extensionMappings_shouldBeSettable() {
        CxfJaxrsServerProperties properties = new CxfJaxrsServerProperties();

        Map<Object, Object> mappings = new HashMap<>();
        mappings.put("json", "application/json");
        mappings.put("xml", "application/xml");
        properties.setExtensionMappings(mappings);

        assertThat(properties.getExtensionMappings()).hasSize(2);
        assertThat(properties.getExtensionMappings().get("json")).isEqualTo("application/json");
    }

    @Test
    void properties_shouldBeSettable() {
        CxfJaxrsServerProperties properties = new CxfJaxrsServerProperties();

        Map<String, Object> props = new HashMap<>();
        props.put("key1", "value1");
        props.put("key2", 123);
        properties.setProperties(props);

        assertThat(properties.getProperties()).hasSize(2);
        assertThat(properties.getProperties().get("key1")).isEqualTo("value1");
    }

}
