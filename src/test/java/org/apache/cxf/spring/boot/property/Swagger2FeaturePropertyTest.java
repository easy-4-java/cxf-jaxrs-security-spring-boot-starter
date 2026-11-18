package org.apache.cxf.spring.boot.property;

import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * Unit tests for {@link Swagger2FeatureProperty}.
 *
 * @author <a href="https://github.com/loong10k">Loong Wan</a>
 * @since 1.0.0
 */
class Swagger2FeaturePropertyTest {

    @Test
    void defaultValues_shouldBeCorrect() {
        Swagger2FeatureProperty property = new Swagger2FeatureProperty();

        assertThat(property.getTitle()).isNull();
        assertThat(property.getDescription()).isNull();
        assertThat(property.getVersion()).isNull();
        assertThat(property.getContact()).isNull();
        assertThat(property.getLicense()).isNull();
        assertThat(property.getLicenseUrl()).isNull();
        assertThat(property.getTermsOfServiceUrl()).isNull();
        assertThat(property.isLicenseWasSet()).isFalse();
        assertThat(property.isRunAsFilter()).isFalse();
        assertThat(property.isActivateOnlyIfJaxrsSupported()).isFalse();
        assertThat(property.getResourcePackage()).isNull();
        assertThat(property.getBasePath()).isNull();
        assertThat(property.getFilterClass()).isNull();
        assertThat(property.getIgnoreRoutes()).isNull();
        assertThat(property.isSupportSwaggerUi()).isFalse();
        assertThat(property.getSwaggerUiVersion()).isNull();
        assertThat(property.getSwaggerUiMavenGroupAndArtifact()).isNull();
        assertThat(property.getSwaggerUiMediaTypes()).isNull();
        assertThat(property.isDynamicBasePath()).isFalse();
        assertThat(property.getHost()).isNull();
        assertThat(property.getSchemes()).isNull();
        assertThat(property.isPrettyPrint()).isFalse();
        assertThat(property.getPropertiesLocation()).isEqualTo("/swagger.properties");
    }

}
