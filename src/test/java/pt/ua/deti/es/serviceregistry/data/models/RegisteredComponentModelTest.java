package pt.ua.deti.es.serviceregistry.data.models;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import pt.ua.deti.es.serviceregistry.entities.ComponentProtocol;
import pt.ua.deti.es.serviceregistry.entities.ComponentType;

import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;

class RegisteredComponentModelTest {

    private UUID id;
    private String componentName;
    private String healthEndpoint;
    private ComponentProtocol componentProtocol;
    private ComponentType componentType;
    private ComponentAddressModel componentAddress;
    private ComponentAvailabilityModel componentAvailability;

    private RegisteredComponentModel registeredComponentModel;

    @BeforeEach
    void setUp() {
        this.id = UUID.randomUUID();
        this.componentName = "componentName";
        this.healthEndpoint = "healthEndpoint";
        this.componentProtocol = ComponentProtocol.HTTP;
        this.componentType = ComponentType.UI;
        this.componentAddress = new ComponentAddressModel();
        this.componentAvailability = new ComponentAvailabilityModel();

        this.registeredComponentModel = new RegisteredComponentModel(this.id, this.componentName, this.healthEndpoint, this.componentProtocol, this.componentType, this.componentAddress, this.componentAvailability);
    }

    @Test
    void toDTO() {

        assertThat(this.registeredComponentModel.toDTO())
                .isNotNull()
                .hasOnlyFields("id", "componentName", "healthEndpoint", "componentProtocol", "componentType", "componentAddress", "componentAvailability");

        assertThat(this.registeredComponentModel.toDTO().getId()).isEqualTo(this.id);
        assertThat(this.registeredComponentModel.toDTO().getComponentName()).isEqualTo(this.componentName);
        assertThat(this.registeredComponentModel.toDTO().getHealthEndpoint()).isEqualTo(this.healthEndpoint);
        assertThat(this.registeredComponentModel.toDTO().getComponentProtocol()).isEqualTo(this.componentProtocol);
        assertThat(this.registeredComponentModel.toDTO().getComponentType()).isEqualTo(this.componentType);
        assertThat(this.registeredComponentModel.toDTO().getComponentAddress()).isEqualTo(this.componentAddress.toDTO());
        assertThat(this.registeredComponentModel.toDTO().getComponentAvailability()).isEqualTo(this.componentAvailability.toDTO());

    }

}