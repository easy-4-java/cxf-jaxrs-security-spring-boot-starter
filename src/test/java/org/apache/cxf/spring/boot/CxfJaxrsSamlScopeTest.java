package org.apache.cxf.spring.boot;

import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;

/**
 * Unit tests for {@link CxfJaxrsSamlProperties.Scope}.
 *
 * @author <a href="https://github.com/loong10k">Loong Wan</a>
 * @since 1.0.0
 */
class CxfJaxrsSamlScopeTest {

    @Test
    void scopeValues_shouldExist() {
        assertThat(CxfJaxrsSamlProperties.Scope.values()).hasSize(2);
        assertThat(CxfJaxrsSamlProperties.Scope.REQUEST_SCOPE).isNotNull();
        assertThat(CxfJaxrsSamlProperties.Scope.SESSION_SCOPE).isNotNull();
    }

    @Test
    void get_shouldReturnScopeString() {
        assertThat(CxfJaxrsSamlProperties.Scope.REQUEST_SCOPE.get()).isEqualTo("request");
        assertThat(CxfJaxrsSamlProperties.Scope.SESSION_SCOPE.get()).isEqualTo("session");
    }

    @Test
    void equalsWithScope_shouldWork() {
        assertThat(CxfJaxrsSamlProperties.Scope.REQUEST_SCOPE.equals(CxfJaxrsSamlProperties.Scope.REQUEST_SCOPE)).isTrue();
        assertThat(CxfJaxrsSamlProperties.Scope.REQUEST_SCOPE.equals(CxfJaxrsSamlProperties.Scope.SESSION_SCOPE)).isFalse();
    }

    @Test
    void equalsWithString_shouldWork() {
        assertThat(CxfJaxrsSamlProperties.Scope.REQUEST_SCOPE.equals("request")).isTrue();
        assertThat(CxfJaxrsSamlProperties.Scope.SESSION_SCOPE.equals("session")).isTrue();
    }

    @Test
    void valueOfIgnoreCase_shouldWork() {
        assertThat(CxfJaxrsSamlProperties.Scope.valueOfIgnoreCase("request")).isEqualTo(CxfJaxrsSamlProperties.Scope.REQUEST_SCOPE);
        assertThat(CxfJaxrsSamlProperties.Scope.valueOfIgnoreCase(new String("ReQuEsT")))
                .isEqualTo(CxfJaxrsSamlProperties.Scope.REQUEST_SCOPE);
        assertThat(CxfJaxrsSamlProperties.Scope.valueOfIgnoreCase(new String("SESSION")))
                .isEqualTo(CxfJaxrsSamlProperties.Scope.SESSION_SCOPE);
        assertThrows(java.util.NoSuchElementException.class,
                () -> CxfJaxrsSamlProperties.Scope.valueOfIgnoreCase("unknown"));
        assertThat(CxfJaxrsSamlProperties.Scope.valueOfIgnoreCase("session")).isEqualTo(CxfJaxrsSamlProperties.Scope.SESSION_SCOPE);
        assertThat(CxfJaxrsSamlProperties.Scope.valueOfIgnoreCase("request")).isEqualTo(CxfJaxrsSamlProperties.Scope.REQUEST_SCOPE);
    }

    @Test
    void valueOf_shouldWork() {
        assertThat(CxfJaxrsSamlProperties.Scope.valueOf("REQUEST_SCOPE")).isEqualTo(CxfJaxrsSamlProperties.Scope.REQUEST_SCOPE);
        assertThat(CxfJaxrsSamlProperties.Scope.valueOf("SESSION_SCOPE")).isEqualTo(CxfJaxrsSamlProperties.Scope.SESSION_SCOPE);
    }

}
