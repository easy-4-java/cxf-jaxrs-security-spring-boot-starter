package org.apache.cxf.spring.boot;

import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * Unit tests for {@link CxfJaxrsSamlProperties}.
 *
 * @author <a href="https://github.com/loong10k">Loong Wan</a>
 * @since 1.0.0
 */
class CxfJaxrsSamlPropertiesTest {

    @Test
    void defaultValues_shouldBeCorrect() {
        CxfJaxrsSamlProperties properties = new CxfJaxrsSamlProperties();

        assertThat(properties.isEnabled()).isFalse();
        assertThat(properties.getDispatcher()).isNotNull();
        assertThat(properties.getPostBinding()).isNotNull();
        assertThat(properties.getRedirectBinding()).isNotNull();
        assertThat(properties.getEhcache()).isNotNull();
    }

    @Test
    void settersAndGetters_shouldWorkCorrectly() {
        CxfJaxrsSamlProperties properties = new CxfJaxrsSamlProperties();

        properties.setEnabled(true);
        assertThat(properties.isEnabled()).isTrue();

        properties.setEnabled(false);
        assertThat(properties.isEnabled()).isFalse();
    }

    @Test
    void prefix_shouldBeCorrect() {
        assertThat(CxfJaxrsSamlProperties.PREFIX).isEqualTo("cxf.jaxws.saml");
    }

    @Test
    void nestedProperties_shouldBeSettable() {
        CxfJaxrsSamlProperties properties = new CxfJaxrsSamlProperties();

        properties.getDispatcher().setBeanName("testBean");
        assertThat(properties.getDispatcher().getBeanName()).isEqualTo("testBean");

        properties.getPostBinding().setIssuerId("issuer1");
        assertThat(properties.getPostBinding().getIssuerId()).isEqualTo("issuer1");

        properties.getRedirectBinding().setIssuerId("issuer2");
        assertThat(properties.getRedirectBinding().getIssuerId()).isEqualTo("issuer2");

        properties.getEhcache().setConfigFileUrl("custom-ehcache.xml");
        assertThat(properties.getEhcache().getConfigFileUrl()).isEqualTo("custom-ehcache.xml");
    }

}
