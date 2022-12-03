package pt.ua.deti.es.serviceregistry.data.dto;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import pt.ua.deti.es.serviceregistry.entities.ComponentAvailability;

import static org.assertj.core.api.Assertions.assertThat;

class ComponentAvailabilityDtoTest {

    private Long id;
    private ComponentAvailability componentAvailability;
    private Long lastTimeOnline;

    private ComponentAvailabilityDto componentAvailabilityDto;

    @BeforeEach
    void setUp() {
        this.id = 1L;
        this.componentAvailability = ComponentAvailability.ONLINE;
        this.lastTimeOnline = System.currentTimeMillis();

        this.componentAvailabilityDto = new ComponentAvailabilityDto(this.id, this.componentAvailability, this.lastTimeOnline);

    }

    @Test
    void toModel() {

        assertThat(this.componentAvailabilityDto).hasNoNullFieldsOrProperties()
                        .hasOnlyFields("id", "availability", "lastTimeOnline");

        assertThat(this.componentAvailabilityDto.toModel().getId()).isEqualTo(this.id);
        assertThat(this.componentAvailabilityDto.toModel().getComponentAvailability()).isEqualTo(this.componentAvailability);
        assertThat(this.componentAvailabilityDto.toModel().getLastTimeOnline()).isEqualTo(this.lastTimeOnline);

    }

}