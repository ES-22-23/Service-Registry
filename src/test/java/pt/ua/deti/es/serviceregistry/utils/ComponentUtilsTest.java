package pt.ua.deti.es.serviceregistry.utils;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import pt.ua.deti.es.serviceregistry.data.dto.ComponentAddressDto;
import pt.ua.deti.es.serviceregistry.data.dto.ComponentAvailabilityDto;
import pt.ua.deti.es.serviceregistry.data.dto.RegisteredComponentDto;
import pt.ua.deti.es.serviceregistry.entities.ComponentProtocol;
import pt.ua.deti.es.serviceregistry.entities.ComponentType;

import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.spy;

class ComponentUtilsTest {

    private RegisteredComponentDto registeredComponentDto;

    @BeforeEach
    void setUp() {

        String healthEndpoint = "/health";
        ComponentAddressDto componentAddress = new ComponentAddressDto(1L, "private", "public");

        registeredComponentDto = new RegisteredComponentDto(UUID.randomUUID(), "", healthEndpoint,
                ComponentProtocol.HTTP, ComponentType.ALARM, componentAddress, mock(ComponentAvailabilityDto.class));

    }

    @Test
    void buildHealthEndpoint() {

        ComponentUtils componentUtils = spy(ComponentUtils.class);

        assertThat(componentUtils.buildHealthEndpoint(registeredComponentDto, true))
                .isNotNull()
                .isEqualTo("http://private:80/health");


        assertThat(componentUtils.buildHealthEndpoint(registeredComponentDto, false))
                .isNotNull()
                .isEqualTo("http://public:80/health");


    }

}