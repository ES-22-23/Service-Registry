package pt.ua.deti.es.serviceregistry.web.services;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;

class HealthServiceTest {

    private HealthService healthService;

    @BeforeEach
    void setUp() {
        this.healthService = new HealthService();
    }

    @Test
    void getHealthReport() {
        assertThat(healthService.getHealthReport()).isNotNull()
                .hasFieldOrPropertyWithValue("healthy", true)
                .hasFieldOrPropertyWithValue("additionalProperties", null);
    }

}