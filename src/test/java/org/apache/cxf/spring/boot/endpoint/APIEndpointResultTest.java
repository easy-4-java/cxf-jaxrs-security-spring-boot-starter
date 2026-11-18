package org.apache.cxf.spring.boot.endpoint;

import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * Unit tests for {@link APIEndpointResult}.
 *
 * @author <a href="https://github.com/loong10k">Loong Wan</a>
 * @since 1.0.0
 */
class APIEndpointResultTest {

    @Test
    void defaultValues_shouldBeCorrect() {
        APIEndpointResult result = new APIEndpointResult();

        assertThat(result.getName()).isNull();
        assertThat(result.getPartName()).isNull();
        assertThat(result.getTargetNamespace()).isNull();
        assertThat(result.isHeader()).isFalse();
    }

    @Test
    void settersAndGetters_shouldWorkCorrectly() {
        APIEndpointResult result = new APIEndpointResult();

        result.setName("testResult");
        assertThat(result.getName()).isEqualTo("testResult");

        result.setPartName("testPart");
        assertThat(result.getPartName()).isEqualTo("testPart");

        result.setTargetNamespace("http://example.com");
        assertThat(result.getTargetNamespace()).isEqualTo("http://example.com");

        result.setHeader(true);
        assertThat(result.isHeader()).isTrue();
    }

}
