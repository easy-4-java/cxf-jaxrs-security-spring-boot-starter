package org.apache.cxf.spring.boot;

import org.apache.cxf.Bus;
import org.apache.cxf.jaxrs.ext.MessageContext;
import org.apache.cxf.jaxrs.provider.RequestDispatcherProvider;
import org.apache.cxf.rs.security.saml.sso.AuthnRequestBuilder;
import org.apache.cxf.rs.security.saml.sso.SamlPostBindingFilter;
import org.apache.cxf.rs.security.saml.sso.SamlRedirectBindingFilter;
import org.apache.cxf.rs.security.saml.sso.state.SPStateManager;
import org.junit.jupiter.api.Test;
import org.springframework.boot.autoconfigure.AutoConfigurations;
import org.springframework.boot.test.context.runner.WebApplicationContextRunner;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.mock;

class CxfJaxrsSamlConfigurationTest {

    private final WebApplicationContextRunner contextRunner = new WebApplicationContextRunner()
            .withConfiguration(AutoConfigurations.of(CxfJaxrsSamlConfiguration.class))
            .withBean(Bus.class, () -> mock(Bus.class));

    @Test
    void leavesSamlComponentsDisabledByDefault() {
        contextRunner.run(context -> {
            assertThat(context).hasNotFailed();
            assertThat(context).doesNotHaveBean(SamlRedirectBindingFilter.class);
            assertThat(context).doesNotHaveBean(SamlPostBindingFilter.class);
            assertThat(context).doesNotHaveBean(SPStateManager.class);
        });
    }

    @Test
    void configuresSeparateBindingsWithSharedStateWhenEnabled() {
        contextRunner.withPropertyValues(
                "cxf.jaxws.saml.enabled=true",
                "cxf.jaxws.saml.redirect-binding.idp-service-address=https://idp.example/redirect",
                "cxf.jaxws.saml.redirect-binding.web-app-domain=redirect.example",
                "cxf.jaxws.saml.redirect-binding.state-time-to-live=45000",
                "cxf.jaxws.saml.post-binding.idp-service-address=https://idp.example/post",
                "cxf.jaxws.saml.post-binding.web-app-domain=post.example",
                "cxf.jaxws.saml.post-binding.state-time-to-live=90000")
                .run(context -> {
                    assertThat(context).hasNotFailed();
                    assertThat(context).hasSingleBean(MessageContext.class);
                    assertThat(context).hasSingleBean(RequestDispatcherProvider.class);
                    assertThat(context).hasSingleBean(AuthnRequestBuilder.class);
                    assertThat(context).hasSingleBean(SPStateManager.class);
                    SPStateManager state = context.getBean(SPStateManager.class);
                    SamlRedirectBindingFilter redirect = context.getBean(SamlRedirectBindingFilter.class);
                    SamlPostBindingFilter post = context.getBean(SamlPostBindingFilter.class);

                    assertThat(redirect.getStateProvider()).isSameAs(state);
                    assertThat(redirect.getIdpServiceAddress()).isEqualTo("https://idp.example/redirect");
                    assertThat(redirect.getWebAppDomain()).isEqualTo("redirect.example");
                    assertThat(redirect.getStateTimeToLive()).isEqualTo(45000);
                    assertThat(post.getStateProvider()).isSameAs(state);
                    assertThat(post.getIdpServiceAddress()).isEqualTo("https://idp.example/post");
                    assertThat(post.getWebAppDomain()).isEqualTo("post.example");
                    assertThat(post.getStateTimeToLive()).isEqualTo(90000);
                });
    }

    @Test
    void honorsExplicitStateManagerAndIndependentSigningOptions() {
        SPStateManager state = mock(SPStateManager.class);
        contextRunner.withBean(SPStateManager.class, () -> state)
                .withPropertyValues(
                        "cxf.jaxws.saml.enabled=true",
                        "cxf.jaxws.saml.redirect-binding.sign-request=true",
                        "cxf.jaxws.saml.redirect-binding.signature-username=redirect-key",
                        "cxf.jaxws.saml.post-binding.sign-request=true",
                        "cxf.jaxws.saml.post-binding.signature-username=post-key")
                .run(context -> {
                    assertThat(context).hasSingleBean(SPStateManager.class);
                    SamlRedirectBindingFilter redirect = context.getBean(SamlRedirectBindingFilter.class);
                    SamlPostBindingFilter post = context.getBean(SamlPostBindingFilter.class);

                    assertThat(redirect.getStateProvider()).isSameAs(state);
                    assertThat(redirect.isSignRequest()).isTrue();
                    assertThat(redirect.getSignatureUsername()).isEqualTo("redirect-key");
                    assertThat(post.getStateProvider()).isSameAs(state);
                    assertThat(post.isSignRequest()).isTrue();
                    assertThat(post.getSignatureUsername()).isEqualTo("post-key");
                });
    }
}
