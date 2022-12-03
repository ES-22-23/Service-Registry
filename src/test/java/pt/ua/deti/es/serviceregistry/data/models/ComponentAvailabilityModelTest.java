package pt.ua.deti.es.serviceregistry.data.models;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import pt.ua.deti.es.serviceregistry.entities.ComponentAvailability;

import static org.assertj.core.api.Assertions.assertThat;

class ComponentAvailabilityModelTest {


    private Long id;
    private ComponentAvailability componentAvailability;
    private Long lastTimeOnline;

    private ComponentAvailabilityModel componentAvailabilityModel;

    @BeforeEach
    void setUp() {

        this.id = 1L;
        this.componentAvailability = ComponentAvailability.ONLINE;
        this.lastTimeOnline = 1L;

        this.componentAvailabilityModel = new ComponentAvailabilityModel(this.id, this.componentAvailability, this.lastTimeOnline);

    }

    @Test
    void toDTO() {

        assertThat(this.componentAvailabilityModel.toDTO())
                .isNotNull()
                .hasOnlyFields("id", "availability", "lastTimeOnline");

        assertThat(this.componentAvailabilityModel.toDTO().getId()).isEqualTo(this.id);
        assertThat(this.componentAvailabilityModel.toDTO().getAvailability()).isEqualTo(this.componentAvailability);
        assertThat(this.componentAvailabilityModel.toDTO().getLastTimeOnline()).isEqualTo(this.lastTimeOnline);

    }

}