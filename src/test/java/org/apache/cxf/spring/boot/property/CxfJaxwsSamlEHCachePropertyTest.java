package org.apache.cxf.spring.boot.property;

import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * Unit tests for {@link CxfJaxwsSamlEHCacheProperty}.
 *
 * @author <a href="https://github.com/loong10k">Loong Wan</a>
 * @since 1.0.0
 */
class CxfJaxwsSamlEHCachePropertyTest {

    @Test
    void defaultValues_shouldBeCorrect() {
        CxfJaxwsSamlEHCacheProperty property = new CxfJaxwsSamlEHCacheProperty();

        assertThat(property.getConfigFileUrl()).isEqualTo("/cxf-samlp-ehcache.xml");
    }

    @Test
    void settersAndGetters_shouldWorkCorrectly() {
        CxfJaxwsSamlEHCacheProperty property = new CxfJaxwsSamlEHCacheProperty();

        property.setConfigFileUrl("ehcache.xml");
        assertThat(property.getConfigFileUrl()).isEqualTo("ehcache.xml");
    }

}
