package org.apache.cxf.spring.boot.jaxrs.security;

import jakarta.ws.rs.container.ContainerRequestContext;
import jakarta.ws.rs.container.ContainerResponseContext;
import jakarta.ws.rs.core.MultivaluedHashMap;
import jakarta.ws.rs.core.MultivaluedMap;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.io.IOException;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

/**
 * Unit tests for {@link ApiOriginFilter}.
 *
 * @author <a href="https://github.com/loong10k">Loong Wan</a>
 * @since 1.0.0
 */
@ExtendWith(MockitoExtension.class)
class ApiOriginFilterTest {

    @Mock
    private ContainerRequestContext requestContext;

    @Mock
    private ContainerResponseContext responseContext;

    @Test
    void filter_shouldAddCorsHeaders() throws IOException {
        MultivaluedMap<String, Object> headers = new MultivaluedHashMap<>();
        when(responseContext.getHeaders()).thenReturn(headers);

        ApiOriginFilter filter = new ApiOriginFilter();
        filter.filter(requestContext, responseContext);

        assertThat(headers.getFirst("Access-Control-Allow-Origin")).isEqualTo("*");
        assertThat(headers.getFirst("Access-Control-Allow-Methods")).isEqualTo("GET, POST, DELETE, PUT");
        assertThat(headers.getFirst("Access-Control-Allow-Headers")).isEqualTo("Content-Type");
    }

    @Test
    void filter_shouldImplementContainerResponseFilter() {
        ApiOriginFilter filter = new ApiOriginFilter();
        assertThat(filter).isInstanceOf(jakarta.ws.rs.container.ContainerResponseFilter.class);
    }

}
