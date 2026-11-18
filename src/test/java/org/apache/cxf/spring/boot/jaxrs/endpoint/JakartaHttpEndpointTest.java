package org.apache.cxf.spring.boot.jaxrs.endpoint;

import java.net.ServerSocket;
import java.net.InetSocketAddress;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.time.Duration;
import java.util.concurrent.atomic.AtomicInteger;

import jakarta.ws.rs.Consumes;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.POST;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.PathParam;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.core.MediaType;
import org.apache.cxf.Bus;
import org.apache.cxf.BusFactory;
import org.apache.cxf.jaxrs.JAXRSServerFactoryBean;
import org.apache.cxf.spring.boot.CxfJaxrsServerProperties;
import org.apache.cxf.spring.boot.jaxrs.endpoint.JakartaJsonProviderTest.Customer;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

class JakartaHttpEndpointTest {
    @Test
    void servesJakartaJsonOnLoopbackAndCloses() throws Exception {
        int port;
        try (ServerSocket socket = new ServerSocket(0)) {
            port = socket.getLocalPort();
        }
        String address = "http://127.0.0.1:" + port + "/api";
        Bus bus = BusFactory.newInstance().createBus();
        EndpointApiTemplate template = new EndpointApiTemplate(bus, new CxfJaxrsServerProperties());
        CustomerResource resource = new CustomerResource();
        EndpointCallback noExtraFeatures = new EndpointCallback() {
            public void doCallback(JAXRSServerFactoryBean factory, Object... services) {
            }
            public void doCallback(JAXRSServerFactoryBean factory, Class<?>... classes) {
            }
        };
        try {
            template.publish(address, noExtraFeatures, resource);
            try (HttpClient client = HttpClient.newBuilder().connectTimeout(Duration.ofSeconds(5)).build()) {
                HttpResponse<String> get = client.send(HttpRequest.newBuilder(URI.create(address + "/customers/42"))
                        .header("Accept", MediaType.APPLICATION_JSON).GET().build(), HttpResponse.BodyHandlers.ofString());
                assertThat(get.statusCode()).isEqualTo(200);
                assertThat(get.body()).contains("\"id\":\"42\"", "\"display_name\":\"Ada\"");
                HttpResponse<String> post = client.send(HttpRequest.newBuilder(URI.create(address + "/customers"))
                        .header("Content-Type", MediaType.APPLICATION_JSON)
                        .header("Accept", MediaType.APPLICATION_JSON)
                        .POST(HttpRequest.BodyPublishers.ofString("{\"id\":\"7\",\"display_name\":\"Grace\"}"))
                        .build(), HttpResponse.BodyHandlers.ofString());
                assertThat(post.statusCode()).isEqualTo(200);
                assertThat(post.body()).contains("\"id\":\"7\"", "\"display_name\":\"Grace\"");
                assertThat(resource.posts.get()).isEqualTo(1);
                HttpResponse<String> invalid = client.send(HttpRequest.newBuilder(URI.create(address + "/customers"))
                        .header("Content-Type", MediaType.APPLICATION_JSON)
                        .POST(HttpRequest.BodyPublishers.ofString("{invalid json"))
                        .build(), HttpResponse.BodyHandlers.ofString());
                assertThat(invalid.statusCode()).isGreaterThanOrEqualTo(400);
                assertThat(resource.posts.get()).isEqualTo(1);
            }
        } finally {
            template.destroy(address);
            bus.shutdown(true);
        }
        org.awaitility.Awaitility.await().atMost(Duration.ofSeconds(10)).untilAsserted(() -> {
            try (ServerSocket released = new ServerSocket()) {
                released.setReuseAddress(true);
                released.bind(new InetSocketAddress("127.0.0.1", port));
                assertThat(released.getLocalPort()).isEqualTo(port);
            }
        });
    }

    @Path("/customers")
    public static class CustomerResource {
        private final AtomicInteger posts = new AtomicInteger();
        @GET @Path("/{id}") @Produces(MediaType.APPLICATION_JSON)
        public Customer get(@PathParam("id") String id) {
            return new Customer(id, "Ada");
        }
        @POST @Consumes(MediaType.APPLICATION_JSON) @Produces(MediaType.APPLICATION_JSON)
        public Customer post(Customer customer) {
            posts.incrementAndGet();
            return customer;
        }
    }
}
