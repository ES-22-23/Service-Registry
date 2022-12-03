package pt.ua.deti.es.serviceregistry.data.services;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import pt.ua.deti.es.serviceregistry.data.dto.RegisteredComponentDto;
import pt.ua.deti.es.serviceregistry.data.models.ComponentAddressModel;
import pt.ua.deti.es.serviceregistry.data.models.ComponentAvailabilityModel;
import pt.ua.deti.es.serviceregistry.data.models.RegisteredComponentModel;
import pt.ua.deti.es.serviceregistry.data.repositories.ComponentAddressRepository;
import pt.ua.deti.es.serviceregistry.data.repositories.ComponentAvailabilityRepository;
import pt.ua.deti.es.serviceregistry.data.repositories.RegisteredComponentRepository;

import java.util.List;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class RegisteredComponentServiceTest {

    @Mock
    private RegisteredComponentRepository registeredComponentRepository;
    @Mock
    private ComponentAddressRepository componentAddressRepository;
    @Mock
    private ComponentAvailabilityRepository componentAvailabilityRepository;

    @InjectMocks
    private RegisteredComponentService registeredComponentService;

    @Test
    void registerComponent() {

        RegisteredComponentDto registeredComponentDtoMock = mock(RegisteredComponentDto.class);

        when(registeredComponentDtoMock.toModel()).thenReturn(mock(RegisteredComponentModel.class));
        when(registeredComponentDtoMock.toModel().getComponentAddress()).thenReturn(mock(ComponentAddressModel.class));
        when(registeredComponentDtoMock.toModel().getComponentAvailability()).thenReturn(mock(ComponentAvailabilityModel.class));

        registeredComponentService.registerComponent(registeredComponentDtoMock);

        verify(this.registeredComponentRepository, times(1)).save(any(RegisteredComponentModel.class));
        verify(this.componentAddressRepository, times(1)).save(any(ComponentAddressModel.class));
        verify(this.componentAvailabilityRepository, times(1)).save(any(ComponentAvailabilityModel.class));

    }

    @Test
    void unregisterExistingComponent() {

        UUID existingComponentId = UUID.randomUUID();

        when(this.registeredComponentRepository.existsById(existingComponentId)).thenReturn(true);

        assertThat(registeredComponentService.unregisterComponent(existingComponentId))
                .isTrue();

        verify(this.registeredComponentRepository, times(1)).deleteById(existingComponentId);

    }

    @Test
    void unregisterNonExistingComponent() {

        UUID existingComponentId = UUID.randomUUID();

        when(this.registeredComponentRepository.existsById(existingComponentId)).thenReturn(false);

        assertThat(registeredComponentService.unregisterComponent(existingComponentId))
                .isFalse();

        verify(this.registeredComponentRepository, times(0)).deleteById(existingComponentId);

    }

    @Test
    void getRegisteredComponents() {

        List<RegisteredComponentModel> mockedComponents = List.of(mock(RegisteredComponentModel.class), mock(RegisteredComponentModel.class));

        when(this.registeredComponentRepository.findAll()).thenReturn(mockedComponents);

        assertThat(this.registeredComponentService.getRegisteredComponents())
                .isNotNull()
                .hasSize(2);

        verify(this.registeredComponentRepository, times(1)).findAll();

    }

    @Test
    void getExistingRegisteredComponent() {

        UUID existingComponentId = UUID.randomUUID();
        RegisteredComponentModel registeredComponentModelMock = mock(RegisteredComponentModel.class);
        RegisteredComponentDto registeredComponentDtoMock = mock(RegisteredComponentDto.class);

        when(this.registeredComponentRepository.findById(existingComponentId)).thenReturn(java.util.Optional.of(registeredComponentModelMock));
        when(registeredComponentModelMock.toDTO()).thenReturn(registeredComponentDtoMock);

        assertThat(this.registeredComponentService.getRegisteredComponent(existingComponentId)).isNotNull()
                .isEqualTo(registeredComponentDtoMock);

        verify(this.registeredComponentRepository, times(1)).findById(existingComponentId);

    }

    @Test
    void updateComponent() {

        RegisteredComponentDto registeredComponentDtoMock = mock(RegisteredComponentDto.class);

        when(registeredComponentDtoMock.toModel()).thenReturn(mock(RegisteredComponentModel.class));
        when(registeredComponentDtoMock.toModel().getComponentAddress()).thenReturn(mock(ComponentAddressModel.class));
        when(registeredComponentDtoMock.toModel().getComponentAvailability()).thenReturn(mock(ComponentAvailabilityModel.class));

        registeredComponentService.registerComponent(registeredComponentDtoMock);

        verify(this.registeredComponentRepository, times(1)).save(any(RegisteredComponentModel.class));
        verify(this.componentAddressRepository, times(1)).save(any(ComponentAddressModel.class));
        verify(this.componentAvailabilityRepository, times(1)).save(any(ComponentAvailabilityModel.class));

    }

}