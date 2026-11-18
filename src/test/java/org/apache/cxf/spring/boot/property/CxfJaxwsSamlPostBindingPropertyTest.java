package org.apache.cxf.spring.boot.property;

import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * Unit tests for {@link CxfJaxwsSamlPostBindingProperty}.
 *
 * @author <a href="https://github.com/loong10k">Loong Wan</a>
 * @since 1.0.0
 */
class CxfJaxwsSamlPostBindingPropertyTest {

    @Test
    void defaultValues_shouldBeCorrect() {
        CxfJaxwsSamlPostBindingProperty property = new CxfJaxwsSamlPostBindingProperty();

        assertThat(property.getIssuerId()).isNull();
        assertThat(property.getIssuerId()).isNull();
    }

    @Test
    void settersAndGetters_shouldWorkCorrectly() {
        CxfJaxwsSamlPostBindingProperty property = new CxfJaxwsSamlPostBindingProperty();

        property.setIssuerId("issuer1");
        assertThat(property.getIssuerId()).isEqualTo("issuer1");
    }

}
