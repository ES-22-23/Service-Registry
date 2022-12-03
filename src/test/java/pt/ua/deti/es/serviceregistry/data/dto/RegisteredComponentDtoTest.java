package pt.ua.deti.es.serviceregistry.data.dto;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import pt.ua.deti.es.serviceregistry.entities.ComponentAvailability;
import pt.ua.deti.es.serviceregistry.entities.ComponentProtocol;
import pt.ua.deti.es.serviceregistry.entities.ComponentType;

import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;

class RegisteredComponentDtoTest {

    private UUID id;
    private String componentName;
    private String healthEndpoint;
    private ComponentProtocol componentProtocol;
    private ComponentType componentType;
    private ComponentAddressDto componentAddress;
    private ComponentAvailabilityDto componentAvailability;

    private RegisteredComponentDto registeredComponentDto;

    @BeforeEach
    void setUp() {

        this.id = UUID.randomUUID();
        this.componentName = "componentName";
        this.healthEndpoint = "healthEndpoint";
        this.componentProtocol = ComponentProtocol.HTTP;
        this.componentType = ComponentType.CAMERA;
        this.componentAddress = new ComponentAddressDto(1L, "host", "privateHost");
        this.componentAvailability = new ComponentAvailabilityDto(1L, ComponentAvailability.ONLINE, System.currentTimeMillis());

        this.registeredComponentDto = new RegisteredComponentDto(this.id, this.componentName, this.healthEndpoint, this.componentProtocol, this.componentType, this.componentAddress, this.componentAvailability);

    }

    @Test
    void toModel() {

        assertThat(this.registeredComponentDto).hasNoNullFieldsOrProperties()
                .hasOnlyFields("id", "componentName", "healthEndpoint", "componentProtocol", "componentType", "componentAddress", "componentAvailability");

        assertThat(this.registeredComponentDto.toModel().getId()).isEqualTo(this.id);
        assertThat(this.registeredComponentDto.toModel().getComponentName()).isEqualTo(this.componentName);
        assertThat(this.registeredComponentDto.toModel().getHealthEndpoint()).isEqualTo(this.healthEndpoint);
        assertThat(this.registeredComponentDto.toModel().getComponentProtocol()).isEqualTo(this.componentProtocol);
        assertThat(this.registeredComponentDto.toModel().getComponentType()).isEqualTo(this.componentType);
        assertThat(this.registeredComponentDto.toModel().getComponentAddress()).isEqualTo(this.componentAddress.toModel());
        assertThat(this.registeredComponentDto.toModel().getComponentAvailability()).isEqualTo(this.componentAvailability.toModel());


    }

}