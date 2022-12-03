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

import java.util.ArrayList;
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

        when(registeredComponentService.getRegisteredComponent(componentId)).thenReturn(null);

        assertThat(registryWebService.updateAvailabilityStatus(componentId, ComponentAvailability.ONLINE))
                .isNull();

        verify(registeredComponentService, times(3)).getRegisteredComponent(any());
        verify(registeredComponentService, times(2)).updateComponent(any());

    }

    @Test
    void getUniqueIdForComponent() {

        assertThat(registryWebService.getUniqueIdForComponent(ComponentType.CAMERA, new ArrayList<>()))
                .isNotNull()
                .isNotPresent();

        assertThat(registryWebService.getUniqueIdForComponent(ComponentType.CAMERA, new ArrayList<>()))
                .isNotNull()
                .isNotPresent();

        assertThat(registryWebService.getUniqueIdForComponent(ComponentType.UI, new ArrayList<>()))
                .isNotNull()
                .isNotPresent();

        assertThat(registryWebService.getUniqueIdForComponent(ComponentType.UI, new ArrayList<>()))
                .isNotNull()
                .isNotPresent();

        assertThat(registryWebService.getUniqueIdForComponent(ComponentType.API, new ArrayList<>()))
                .isNotNull()
                .isNotPresent();

        assertThat(registryWebService.getUniqueIdForComponent(ComponentType.API, new ArrayList<>()))
                .isNotNull()
                .isNotPresent();

        assertThat(registryWebService.getUniqueIdForComponent(ComponentType.ALARM, new ArrayList<>()))
                .isNotNull()
                .isNotPresent();

        assertThat(registryWebService.getUniqueIdForComponent(ComponentType.ALARM, new ArrayList<>()))
                .isNotNull()
                .isNotPresent();

    }

    @Test
    void hasAvailableIds() {

        assertThat(registryWebService.hasAvailableIds(ComponentType.CAMERA, new ArrayList<>()))
                .isNotNull()
                .isFalse();

        assertThat(registryWebService.hasAvailableIds(ComponentType.ALARM, new ArrayList<>()))
                .isNotNull()
                .isFalse();

        assertThat(registryWebService.hasAvailableIds(ComponentType.UI, new ArrayList<>()))
                .isNotNull()
                .isFalse();

        assertThat(registryWebService.hasAvailableIds(ComponentType.API, new ArrayList<>()))
                .isNotNull()
                .isFalse();

        assertThat(registryWebService.hasAvailableIds(ComponentType.UNKNOWN, new ArrayList<>()))
                .isNotNull()
                .isFalse();

    }

    @Test
    void getOccupiedIds() {

        UUID componentId = UUID.randomUUID();

        List<RegisteredComponentDto> registeredComponentDtoList = List.of(new RegisteredComponentDto(
                componentId, "", "",
                ComponentProtocol.HTTP, ComponentType.ALARM, mock(ComponentAddressDto.class),
                mock(ComponentAvailabilityDto.class))
        );

        when(registeredComponentService.getRegisteredComponents()).thenReturn(registeredComponentDtoList);

        assertThat(registryWebService.getOccupiedIds())
                .isNotNull()
                .hasSize(1)
                .contains(componentId);

        verify(registeredComponentService, times(1)).getRegisteredComponents();

    }

    @Test
    void freeUniqueIdForComponent_whenDeviceOffline() {

        UUID componentUniqueId = UUID.randomUUID();

        RegisteredComponentDto registeredComponentDtoStub = new RegisteredComponentDto(
                componentUniqueId, "", "",
                ComponentProtocol.HTTP, ComponentType.CAMERA, mock(ComponentAddressDto.class),
                new ComponentAvailabilityDto(1L, ComponentAvailability.OFFLINE, 100L));

        when(registeredComponentService.getRegisteredComponents()).thenReturn(List.of(registeredComponentDtoStub));
        when(registeredComponentService.unregisterComponent(componentUniqueId)).thenReturn(true);

        assertThat(registryWebService.freeUniqueIdForComponent(ComponentType.CAMERA))
                .isNotNull()
                .isPresent()
                .get()
                .isEqualTo(componentUniqueId);

    }

    @Test
    void freeUniqueIdForComponent_whenNoDevicesOffline() {

        UUID componentUniqueId = UUID.randomUUID();

        RegisteredComponentDto registeredComponentDtoStub = new RegisteredComponentDto(
                componentUniqueId, "", "",
                ComponentProtocol.HTTP, ComponentType.CAMERA, mock(ComponentAddressDto.class),
                new ComponentAvailabilityDto(1L, ComponentAvailability.ONLINE, 100L));

        when(registeredComponentService.getRegisteredComponents()).thenReturn(List.of(registeredComponentDtoStub));

        assertThat(registryWebService.freeUniqueIdForComponent(ComponentType.CAMERA))
                .isNotNull()
                .isNotPresent();

    }

    @Test
    void freeUniqueIdForComponent_whenUnableToRegister() {

        UUID componentUniqueId = UUID.randomUUID();

        RegisteredComponentDto registeredComponentDtoStub = new RegisteredComponentDto(
                componentUniqueId, "", "",
                ComponentProtocol.HTTP, ComponentType.CAMERA, mock(ComponentAddressDto.class),
                new ComponentAvailabilityDto(1L, ComponentAvailability.OFFLINE, 100L));

        when(registeredComponentService.getRegisteredComponents()).thenReturn(List.of(registeredComponentDtoStub));
        when(registeredComponentService.unregisterComponent(componentUniqueId)).thenReturn(false);

        assertThat(registryWebService.freeUniqueIdForComponent(ComponentType.CAMERA))
                .isNotNull()
                .isNotPresent();

    }

}