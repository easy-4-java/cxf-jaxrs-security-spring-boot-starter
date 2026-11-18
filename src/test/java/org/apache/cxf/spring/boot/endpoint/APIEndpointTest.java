package org.apache.cxf.spring.boot.endpoint;

import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * Unit tests for {@link APIEndpoint}.
 *
 * @author <a href="https://github.com/loong10k">Loong Wan</a>
 * @since 1.0.0
 */
class APIEndpointTest {

    @Test
    void defaultValues_shouldBeCorrect() {
        APIEndpoint endpoint = new APIEndpoint();

        assertThat(endpoint.getName()).isNull();
        assertThat(endpoint.getTargetNamespace()).isNull();
        assertThat(endpoint.getServiceName()).isNull();
        assertThat(endpoint.getPortName()).isNull();
        assertThat(endpoint.getWsdlLocation()).isNull();
        assertThat(endpoint.getEndpointInterface()).isNull();
        assertThat(endpoint.getAddr()).isNull();
        assertThat(endpoint.getMethods()).isNull();
    }

    @Test
    void settersAndGetters_shouldWorkCorrectly() {
        APIEndpoint endpoint = new APIEndpoint();

        endpoint.setName("testEndpoint");
        assertThat(endpoint.getName()).isEqualTo("testEndpoint");

        endpoint.setTargetNamespace("http://example.com");
        assertThat(endpoint.getTargetNamespace()).isEqualTo("http://example.com");

        endpoint.setServiceName("testService");
        assertThat(endpoint.getServiceName()).isEqualTo("testService");

        endpoint.setPortName("testPort");
        assertThat(endpoint.getPortName()).isEqualTo("testPort");

        endpoint.setWsdlLocation("http://example.com?wsdl");
        assertThat(endpoint.getWsdlLocation()).isEqualTo("http://example.com?wsdl");

        endpoint.setEndpointInterface("com.example.Interface");
        assertThat(endpoint.getEndpointInterface()).isEqualTo("com.example.Interface");

        endpoint.setAddr("/api/test");
        assertThat(endpoint.getAddr()).isEqualTo("/api/test");

        endpoint.setMethods(new APIEndpointMethod());
        assertThat(endpoint.getMethods()).hasSize(1);
    }

}
