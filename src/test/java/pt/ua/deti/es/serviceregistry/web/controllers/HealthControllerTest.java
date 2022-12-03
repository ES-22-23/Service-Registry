package pt.ua.deti.es.serviceregistry.web.controllers;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import pt.ua.deti.es.serviceregistry.entities.HealthReport;
import pt.ua.deti.es.serviceregistry.web.services.HealthService;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class HealthControllerTest {

    @Mock
    private HealthService healthService;

    @InjectMocks
    private HealthController healthController;

    @Test
    void getHealthStatus() {

        HealthReport healthReport = mock(HealthReport.class);

        when(healthService.getHealthReport()).thenReturn(healthReport);

        assertThat(healthController.getHealthStatus()).isNotNull()
                .isEqualTo(healthReport);

        verify(healthService, times(1)).getHealthReport();

    }

}