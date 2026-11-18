package org.apache.cxf.spring.boot.endpoint;

import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * Unit tests for {@link APIEndpointMethod}.
 *
 * @author <a href="https://github.com/loong10k">Loong Wan</a>
 * @since 1.0.0
 */
class APIEndpointMethodTest {

    @Test
    void defaultValues_shouldBeCorrect() {
        APIEndpointMethod method = new APIEndpointMethod();

        assertThat(method.getOperationName()).isNull();
        assertThat(method.getAction()).isNull();
        assertThat(method.isExclude()).isFalse();
        assertThat(method.getParams()).isNull();
        assertThat(method.getResult()).isNull();
    }

    @Test
    void settersAndGetters_shouldWorkCorrectly() {
        APIEndpointMethod method = new APIEndpointMethod();

        method.setOperationName("testOperation");
        assertThat(method.getOperationName()).isEqualTo("testOperation");

        method.setAction("testAction");
        assertThat(method.getAction()).isEqualTo("testAction");

        method.setExclude(true);
        assertThat(method.isExclude()).isTrue();

        method.setParams(new APIEndpointParam());
        assertThat(method.getParams()).hasSize(1);

        APIEndpointResult result = new APIEndpointResult();
        method.setResult(result);
        assertThat(method.getResult()).isNotNull();
    }

}
