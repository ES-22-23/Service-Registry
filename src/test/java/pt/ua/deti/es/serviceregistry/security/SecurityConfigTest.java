package pt.ua.deti.es.serviceregistry.security;

import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

class SecurityConfigTest {

    @Test
    void corsConfigurationSource() {
        assertThat(new SecurityConfig().corsConfigurationSource()).isNotNull();
    }

}