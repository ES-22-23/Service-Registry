package pt.ua.deti.es.serviceregistry.schedulers;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import pt.ua.deti.es.serviceregistry.data.dto.ComponentAddressDto;
import pt.ua.deti.es.serviceregistry.data.dto.ComponentAvailabilityDto;
import pt.ua.deti.es.serviceregistry.data.dto.RegisteredComponentDto;
import pt.ua.deti.es.serviceregistry.entities.ComponentProtocol;
import pt.ua.deti.es.serviceregistry.entities.ComponentType;
import pt.ua.deti.es.serviceregistry.utils.ComponentUtils;
import pt.ua.deti.es.serviceregistry.web.services.RegistryWebService;

import java.util.List;
import java.util.UUID;

import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class ComponentsHealthSchedulerTest {

    @Mock
    private ComponentUtils componentUtils;
    @Mock
    private RegistryWebService registryWebService;

    @InjectMocks
    private ComponentsHealthScheduler componentsHealthScheduler;

    @Test
    void checkComponentsAvailability() {

        List<RegisteredComponentDto> registeredComponentDtoStubList = List.of(new RegisteredComponentDto(
                UUID.randomUUID(), "", "",
                ComponentProtocol.HTTP, ComponentType.ALARM, mock(ComponentAddressDto.class),
                mock(ComponentAvailabilityDto.class)));

        when(registryWebService.getAllRegisteredComponents()).thenReturn(registeredComponentDtoStubList);

        componentsHealthScheduler.checkComponentsAvailability();

        verify(componentUtils, times(1)).buildHealthEndpoint(any(), anyBoolean());

    }

}