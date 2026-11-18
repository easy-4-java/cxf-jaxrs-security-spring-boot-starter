package org.apache.cxf.spring.boot.property;

import org.junit.jupiter.api.Test;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * Unit tests for {@link CxfJaxwsSamlDispatcherProperty}.
 *
 * @author <a href="https://github.com/loong10k">Loong Wan</a>
 * @since 1.0.0
 */
class CxfJaxwsSamlDispatcherPropertyTest {

    @Test
    void defaultValues_shouldBeCorrect() {
        CxfJaxwsSamlDispatcherProperty property = new CxfJaxwsSamlDispatcherProperty();

        assertThat(property.getBeanName()).isNull();
        assertThat(property.getBeanNames()).isNotNull().isEmpty();
        assertThat(property.getClassResources()).isNotNull().isEmpty();
        assertThat(property.getConsumeMediaTypes()).isNull();
        assertThat(property.isEnableBuffering()).isFalse();
        assertThat(property.isEnableStreaming()).isFalse();
        assertThat(property.getEnumResources()).isNotNull().isEmpty();
        assertThat(property.getErrorView()).isEqualTo("/error");
        assertThat(property.getLocationPrefix()).isNull();
        assertThat(property.getProduceMediaTypes()).isNull();
        assertThat(property.isUseClassNames()).isTrue();
        assertThat(property.isStrictPathCheck()).isFalse();
    }

    @Test
    void settersAndGetters_shouldWorkCorrectly() {
        CxfJaxwsSamlDispatcherProperty property = new CxfJaxwsSamlDispatcherProperty();

        property.setBeanName("testBean");
        assertThat(property.getBeanName()).isEqualTo("testBean");

        Map<String, String> beanNames = new HashMap<>();
        beanNames.put("key1", "value1");
        property.setBeanNames(beanNames);
        assertThat(property.getBeanNames()).hasSize(1);
        assertThat(property.getBeanNames().get("key1")).isEqualTo("value1");

        Map<String, String> classResources = new HashMap<>();
        classResources.put("key1", "value1");
        property.setClassResources(classResources);
        assertThat(property.getClassResources()).hasSize(1);

        property.setConsumeMediaTypes(List.of("application/json"));
        assertThat(property.getConsumeMediaTypes()).hasSize(1);

        property.setEnableBuffering(true);
        assertThat(property.isEnableBuffering()).isTrue();

        property.setEnableStreaming(true);
        assertThat(property.isEnableStreaming()).isTrue();

        property.setErrorView("customError");
        assertThat(property.getErrorView()).isEqualTo("customError");

        property.setLocationPrefix("/prefix");
        assertThat(property.getLocationPrefix()).isEqualTo("/prefix");

        property.setProduceMediaTypes(List.of("text/html"));
        assertThat(property.getProduceMediaTypes()).hasSize(1);

        property.setUseClassNames(false);
        assertThat(property.isUseClassNames()).isFalse();

        property.setStrictPathCheck(true);
        assertThat(property.isStrictPathCheck()).isTrue();
    }

    @Test
    void settersAndGetters_additionalProperties() {
        CxfJaxwsSamlDispatcherProperty property = new CxfJaxwsSamlDispatcherProperty();

        property.setResourcePath("/resources");
        assertThat(property.getResourcePath()).isEqualTo("/resources");

        Map<String, String> resourcePaths = new HashMap<>();
        resourcePaths.put("key1", "value1");
        property.setResourcePaths(resourcePaths);
        assertThat(property.getResourcePaths()).hasSize(1);

        property.setResourceExtension(".xml");
        assertThat(property.getResourceExtension()).isEqualTo(".xml");
    }

}
