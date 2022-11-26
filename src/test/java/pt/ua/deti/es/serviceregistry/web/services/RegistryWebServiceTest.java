package pt.ua.deti.es.serviceregistry.web.services;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import pt.ua.deti.es.serviceregistry.data.dto.ComponentAddressDto;
import pt.ua.deti.es.serviceregistry.data.dto.ComponentAvailabilityDto;
import pt.ua.deti.es.serviceregistry.data.dto.RegisteredComponentDto;
import pt.ua.deti.es.serviceregistry.data.services.RegisteredComponentService;
import pt.ua.deti.es.serviceregistry.entities.ComponentAddress;
import pt.ua.deti.es.serviceregistry.entities.ComponentAvailability;
import pt.ua.deti.es.serviceregistry.entities.ComponentProtocol;
import pt.ua.deti.es.serviceregistry.entities.ComponentType;
import pt.ua.deti.es.serviceregistry.web.entities.RegistrationRequest;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class RegistryWebServiceTest {

    @Mock
    private RegisteredComponentService registeredComponentService;

    @InjectMocks
    private RegistryWebService registryWebService;

    @Test
    void getAllRegisteredComponents() {

        List<RegisteredComponentDto> registeredComponentDtoMockList = List.of(mock(RegisteredComponentDto.class), mock(RegisteredComponentDto.class));

        when(registeredComponentService.getRegisteredComponents()).thenReturn(registeredComponentDtoMockList);

        assertThat(registryWebService.getAllRegisteredComponents())
                .isNotNull()
                .isEqualTo(registeredComponentDtoMockList);

    }

    @Test
    void getFilteredRegisteredComponents() {

        List<RegisteredComponentDto> registeredComponentDtoList = List.of(new RegisteredComponentDto(
                UUID.randomUUID(), "", "",
                ComponentProtocol.HTTP, ComponentType.ALARM, mock(ComponentAddressDto.class),
                mock(ComponentAvailabilityDto.class))
        );

        when(registeredComponentService.getRegisteredComponents()).thenReturn(registeredComponentDtoList);

        assertThat(registryWebService.getFilteredRegisteredComponents(ComponentType.ALARM))
                .isNotNull()
                .hasSize(1)
                .isEqualTo(registeredComponentDtoList);

        assertThat(registryWebService.getFilteredRegisteredComponents(ComponentType.UI))
                .isNotNull()
                .isEmpty();

        assertThat(registryWebService.getFilteredRegisteredComponents(ComponentType.API))
                .isNotNull()
                .isEmpty();

    }

    @Test
    void registerComponent() {

        RegistrationRequest registrationRequest = new RegistrationRequest(
                "", ComponentType.CAMERA, "/health",
                ComponentProtocol.HTTP, mock(ComponentAddress.class)
        );

        assertThat(registryWebService.registerComponent(registrationRequest, Optional.of(UUID.randomUUID())))
                .isNotNull()
                .isInstanceOf(UUID.class);

        verify(registeredComponentService, times(1)).registerComponent(any());

    }

    @Test
    void unregisterComponent() {

        UUID componentUnregisteredId = UUID.randomUUID();

        when(registeredComponentService.unregisterComponent(componentUnregisteredId)).thenReturn(true);

        assertThat(registryWebService.unregisterComponent(componentUnregisteredId))
                .isNotNull()
                .isTrue();

        verify(registeredComponentService, times(1)).unregisterComponent(componentUnregisteredId);

    }

    @Test
    void updateAvailabilityStatus() {

        ComponentAvailability componentAvailabilityStub = ComponentAvailability.ONLINE;
        UUID componentId = UUID.randomUUID();
        RegisteredComponentDto registeredComponentDtoMock = mock(RegisteredComponentDto.class);

        when(registeredComponentDtoMock.getComponentAvailability()).thenReturn(new ComponentAvailabilityDto(
                1L, componentAvailabilityStub, 100L
        ));
        when(registeredComponentService.getRegisteredComponent(componentId)).thenReturn(registeredComponentDtoMock);

        assertThat(registryWebService.updateAvailabilityStatus(componentId, ComponentAvailability.ONLINE))
                .isNotNull()
                .hasFieldOrProperty("componentAvailability");

        assertThat(registryWebService.updateAvailabilityStatus(componentId, ComponentAvailability.ONLINE).getComponentAvailability())
                .isNotNull()
                .hasFieldOrPropertyWithValue("availability", componentAvailabilityStub);

        verify(registeredComponentService, times(2)).getRegisteredComponent(any());
        verify(registeredComponentService, times(2)).updateComponent(any());

    }

}