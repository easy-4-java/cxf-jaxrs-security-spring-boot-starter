package org.apache.cxf.spring.boot.jaxrs.endpoint;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.nio.charset.StandardCharsets;

import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.MultivaluedHashMap;
import jakarta.xml.bind.annotation.XmlElement;
import jakarta.xml.bind.annotation.XmlRootElement;
import com.fasterxml.jackson.jakarta.rs.json.JacksonXmlBindJsonProvider;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;

class JakartaJsonProviderTest {

    private final JacksonXmlBindJsonProvider provider = new JacksonXmlBindJsonProvider();

    @Test
    void writesAndReadsJakartaXmlBindAnnotationsAsJson() throws IOException {
        Customer customer = new Customer("42", "Ada");
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        provider.writeTo(customer, Customer.class, Customer.class, new java.lang.annotation.Annotation[0],
                MediaType.APPLICATION_JSON_TYPE, new MultivaluedHashMap<>(), out);
        String json = out.toString(StandardCharsets.UTF_8);
        assertThat(json).contains("\"id\":\"42\"", "\"display_name\":\"Ada\"");

        Customer decoded = (Customer) provider.readFrom(customerType(), Customer.class,
                new java.lang.annotation.Annotation[0], MediaType.APPLICATION_JSON_TYPE,
                new MultivaluedHashMap<>(), new ByteArrayInputStream(out.toByteArray()));
        assertThat(decoded.getId()).isEqualTo("42");
        assertThat(decoded.getName()).isEqualTo("Ada");
    }

    @Test
    void malformedJsonFailsWithoutProducingADto() {
        assertThrows(IOException.class, () -> provider.readFrom(customerType(), Customer.class,
                new java.lang.annotation.Annotation[0], MediaType.APPLICATION_JSON_TYPE,
                new MultivaluedHashMap<>(),
                new ByteArrayInputStream("{invalid json".getBytes(StandardCharsets.UTF_8))));
    }

    @SuppressWarnings("unchecked")
    private static Class<Object> customerType() {
        return (Class<Object>) (Class<?>) Customer.class;
    }

    @XmlRootElement(name = "customer")
    public static class Customer {
        private String id;
        private String name;

        public Customer() {
        }

        public Customer(String id, String name) {
            this.id = id;
            this.name = name;
        }

        public String getId() {
            return id;
        }

        public void setId(String id) {
            this.id = id;
        }

        @XmlElement(name = "display_name")
        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }
    }
}
