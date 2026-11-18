package org.apache.cxf.spring.boot.property;

import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * Unit tests for {@link CxfJaxwsSamlBindingProperty}.
 *
 * @author <a href="https://github.com/loong10k">Loong Wan</a>
 * @since 1.0.0
 */
class CxfJaxwsSamlBindingPropertyTest {

    @Test
    void defaultValues_shouldBeCorrect() {
        CxfJaxwsSamlBindingProperty property = new CxfJaxwsSamlBindingProperty();

        assertThat(property.isAddEndpointAddressToContext()).isFalse();
        assertThat(property.isAddWebAppContext()).isTrue();
        assertThat(property.getAssertionConsumerServiceAddress()).isNull();
        assertThat(property.getStateTimeToLive()).isEqualTo(120000L);
        assertThat(property.getSignaturePropertiesFile()).isNull();
        assertThat(property.getCallbackHandlerClass()).isNull();
        assertThat(property.getSignatureUsername()).isNull();
        assertThat(property.getIdpServiceAddress()).isEqualTo("https://localhost:9443/idp");
        assertThat(property.getIssuerId()).isNull();
        assertThat(property.isSupportUnsolicited()).isFalse();
        assertThat(property.isSignRequest()).isFalse();
        assertThat(property.getWebAppDomain()).isNull();
        assertThat(property.isForceAuthn()).isFalse();
        assertThat(property.isPassive()).isFalse();
        assertThat(property.getProtocolBinding()).isEqualTo("urn:oasis:names:tc:SAML:2.0:bindings:HTTP-POST");
        assertThat(property.getNameIDFormat()).isEqualTo("urn:oasis:names:tc:SAML:2.0:nameid-format:persistent");
    }

    @Test
    void settersAndGetters_shouldWorkCorrectly() {
        CxfJaxwsSamlBindingProperty property = new CxfJaxwsSamlBindingProperty();

        property.setAddEndpointAddressToContext(true);
        assertThat(property.isAddEndpointAddressToContext()).isTrue();

        property.setAddWebAppContext(false);
        assertThat(property.isAddWebAppContext()).isFalse();

        property.setAssertionConsumerServiceAddress("/saml/consumer");
        assertThat(property.getAssertionConsumerServiceAddress()).isEqualTo("/saml/consumer");

        property.setStateTimeToLive(600000L);
        assertThat(property.getStateTimeToLive()).isEqualTo(600000L);

        property.setSignaturePropertiesFile("signature.properties");
        assertThat(property.getSignaturePropertiesFile()).isEqualTo("signature.properties");

        property.setCallbackHandlerClass("com.example.Handler");
        assertThat(property.getCallbackHandlerClass()).isEqualTo("com.example.Handler");

        property.setSignatureUsername("user1");
        assertThat(property.getSignatureUsername()).isEqualTo("user1");

        property.setIdpServiceAddress("https://idp.example.com");
        assertThat(property.getIdpServiceAddress()).isEqualTo("https://idp.example.com");

        property.setIssuerId("issuer1");
        assertThat(property.getIssuerId()).isEqualTo("issuer1");

        property.setSupportUnsolicited(true);
        assertThat(property.isSupportUnsolicited()).isTrue();

        property.setSignRequest(true);
        assertThat(property.isSignRequest()).isTrue();

        property.setWebAppDomain("example.com");
        assertThat(property.getWebAppDomain()).isEqualTo("example.com");

        property.setForceAuthn(true);
        assertThat(property.isForceAuthn()).isTrue();

        property.setPassive(true);
        assertThat(property.isPassive()).isTrue();

        property.setProtocolBinding("urn:oasis:names:tc:SAML:2.0:bindings:HTTP-Redirect");
        assertThat(property.getProtocolBinding()).isEqualTo("urn:oasis:names:tc:SAML:2.0:bindings:HTTP-Redirect");

        property.setNameIDFormat("urn:oasis:names:tc:SAML:1.1:nameid-format:emailAddress");
        assertThat(property.getNameIDFormat()).isEqualTo("urn:oasis:names:tc:SAML:1.1:nameid-format:emailAddress");
    }

}
