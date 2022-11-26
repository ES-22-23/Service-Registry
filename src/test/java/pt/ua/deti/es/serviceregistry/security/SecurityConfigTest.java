package pt.ua.deti.es.serviceregistry.security;

import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;

class SecurityConfigTest {

    @Test
    void corsConfigurationSource() {
        assertThat(new SecurityConfig().corsConfigurationSource()).isNotNull();
    }

}