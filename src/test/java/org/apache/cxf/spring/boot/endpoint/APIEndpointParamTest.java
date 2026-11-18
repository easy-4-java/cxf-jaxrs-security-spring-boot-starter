package org.apache.cxf.spring.boot.endpoint;

import jakarta.jws.WebParam.Mode;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * Unit tests for {@link APIEndpointParam}.
 *
 * @author <a href="https://github.com/loong10k">Loong Wan</a>
 * @since 1.0.0
 */
class APIEndpointParamTest {

    @Test
    void defaultValues_shouldBeCorrect() {
        APIEndpointParam param = new APIEndpointParam();

        assertThat(param.getName()).isNull();
        assertThat(param.getPartName()).isNull();
        assertThat(param.getTargetNamespace()).isNull();
        assertThat(param.getMode()).isEqualTo(Mode.IN);
        assertThat(param.isHeader()).isFalse();
    }

    @Test
    void settersAndGetters_shouldWorkCorrectly() {
        APIEndpointParam param = new APIEndpointParam();

        param.setName("testName");
        assertThat(param.getName()).isEqualTo("testName");

        param.setPartName("testPartName");
        assertThat(param.getPartName()).isEqualTo("testPartName");

        param.setTargetNamespace("http://example.com");
        assertThat(param.getTargetNamespace()).isEqualTo("http://example.com");

        param.setMode(Mode.OUT);
        assertThat(param.getMode()).isEqualTo(Mode.OUT);

        param.setHeader(true);
        assertThat(param.isHeader()).isTrue();
    }

}
