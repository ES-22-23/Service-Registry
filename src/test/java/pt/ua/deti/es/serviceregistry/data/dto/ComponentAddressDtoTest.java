package pt.ua.deti.es.serviceregistry.data.dto;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import pt.ua.deti.es.serviceregistry.data.models.ComponentAddressModel;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(MockitoExtension.class)
class ComponentAddressDtoTest {

    private Long id;
    private String publicAddress;
    private String privateAddress;

    private ComponentAddressDto componentAddressDto;

    @BeforeEach
    void setUp() {

        this.id = 1L;
        this.publicAddress = "0.0.0.0";
        this.privateAddress = "0.0.0.0";

        this.componentAddressDto = new ComponentAddressDto(id, privateAddress, publicAddress);

    }

    @Test
    void toModel() {

        ComponentAddressModel componentAddressModel = componentAddressDto.toModel();

        assertThat(componentAddressModel)
                .isNotNull()
                .hasFieldOrPropertyWithValue("id", id)
                .hasFieldOrPropertyWithValue("publicAddress", publicAddress)
                .hasFieldOrPropertyWithValue("privateAddress", privateAddress);
    }

}