package pt.ua.deti.es.serviceregistry.web.controllers;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import pt.ua.deti.es.serviceregistry.web.entities.RegistrationRequest;
import pt.ua.deti.es.serviceregistry.web.entities.UnregistrationRequest;
import pt.ua.deti.es.serviceregistry.web.services.RegistryWebService;

import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class RegistryControllerTest {

    @Mock
    private RegistryWebService registryWebService;

    @InjectMocks
    private RegistryController registryController;

    @Test
    void registerNewComponent() {

        RegistrationRequest registrationRequest = mock(RegistrationRequest.class);
        UUID componentUniqueId = UUID.randomUUID();

        when(registryWebService.registerComponent(any(), any())).thenReturn(componentUniqueId);

        assertThat(registryController.registerNewComponent(registrationRequest))
                .isNotNull()
                .hasFieldOrPropertyWithValue("message", "Service successfully registered.")
                .hasFieldOrPropertyWithValue("serviceUniqueId", componentUniqueId);

        when(registryWebService.registerComponent(any(), any())).thenReturn(null);

        assertThat(registryController.registerNewComponent(registrationRequest))
                .isNotNull()
                .hasFieldOrPropertyWithValue("message", "Unable to register the service.")
                .hasFieldOrPropertyWithValue("serviceUniqueId", null);

        verify(registryWebService, times(2)).registerComponent(any(), any());

    }

    @Test
    void unregisterComponent() {

        UUID componentUniqueId = UUID.randomUUID();
        UnregistrationRequest unregistrationRequest = new UnregistrationRequest(componentUniqueId.toString());

        when(registryWebService.unregisterComponent(componentUniqueId)).thenReturn(true);

        assertThat(registryController.unregisterComponent(unregistrationRequest))
                .isNotNull()
                .hasFieldOrPropertyWithValue("message", "Service successfully un-registered.")
                .hasFieldOrPropertyWithValue("serviceUniqueId", componentUniqueId);

        when(registryWebService.unregisterComponent(componentUniqueId)).thenReturn(false);

        assertThat(registryController.unregisterComponent(unregistrationRequest))
                .isNotNull()
                .hasFieldOrPropertyWithValue("message", "Unable to un-registered the service.")
                .hasFieldOrPropertyWithValue("serviceUniqueId", null);

    }

    @Test
    void getRegisteredComponents() {

        registryWebService.getAllRegisteredComponents();
        verify(registryWebService, times(1)).getAllRegisteredComponents();

    }

}