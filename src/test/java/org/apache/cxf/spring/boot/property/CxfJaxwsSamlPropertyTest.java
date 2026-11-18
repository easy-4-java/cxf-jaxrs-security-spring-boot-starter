package org.apache.cxf.spring.boot.property;

import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * Unit tests for {@link CxfJaxwsSamlProperty}.
 *
 * @author <a href="https://github.com/loong10k">Loong Wan</a>
 * @since 1.0.0
 */
class CxfJaxwsSamlPropertyTest {

    @Test
    void defaultValues_shouldBeCorrect() {
        CxfJaxwsSamlProperty property = new CxfJaxwsSamlProperty();
        assertThat(property).isNotNull();
    }

}
