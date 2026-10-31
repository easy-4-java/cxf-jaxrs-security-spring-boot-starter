package org.apache.cxf.spring.boot.jaxrs.endpoint;

import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.time.Duration;
import java.util.List;

import jakarta.ws.rs.GET;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.PathParam;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.core.MediaType;
import org.apache.cxf.spring.boot.endpoint.APIEndpointRepository;
import org.apache.cxf.spring.boot.jaxrs.endpoint.JakartaJsonProviderTest.Customer;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringBootConfiguration;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.context.annotation.Bean;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest(classes = BootServletEndpointTest.TestApplication.class,
        webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT,
        properties = {"cxf.jaxrs.enabled=true", "cxf.path=/services", "server.address=127.0.0.1"})
class BootServletEndpointTest {

    @SpringBootConfiguration
    @EnableAutoConfiguration
    static class TestApplication {
        @Bean
        APIEndpointRepository endpointRepository() {
            return List::of;
        }
    }

    @Autowired
    private EndpointApiTemplate template;

    @LocalServerPort
    private int port;

    @Test
    void autoConfiguredCxfServletServesAndStopsDefaultCallbackEndpoint() throws Exception {
        String url = "http://127.0.0.1:" + port + "/services/customers/42";
        template.publish("/", new CustomerResource());
        try (HttpClient client = HttpClient.newBuilder().connectTimeout(Duration.ofSeconds(5)).build()) {
            HttpRequest get = HttpRequest.newBuilder(URI.create(url))
                    .header("Accept", MediaType.APPLICATION_JSON).GET().build();
            HttpResponse<String> response = client.send(get, HttpResponse.BodyHandlers.ofString());
            assertThat(response.statusCode()).isEqualTo(200);
            assertThat(response.body()).contains("\"id\":\"42\"", "\"display_name\":\"Ada\"");
            template.destroy("/");
            HttpResponse<String> stopped = client.send(get, HttpResponse.BodyHandlers.ofString());
            assertThat(stopped.statusCode()).isGreaterThanOrEqualTo(400);
        } finally {
            template.destroy("/");
        }
    }

    @Path("/customers")
    public static class CustomerResource {
        @GET
        @Path("/{id}")
        @Produces(MediaType.APPLICATION_JSON)
        public Customer get(@PathParam("id") String id) {
            return new Customer(id, "Ada");
        }
    }
}
