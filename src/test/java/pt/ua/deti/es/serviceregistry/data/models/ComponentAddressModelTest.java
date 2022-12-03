package pt.ua.deti.es.serviceregistry.data.models;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

class ComponentAddressModelTest {

    private Long id;
    private String privateAddress;
    private String publicAddress;

    private ComponentAddressModel componentAddressModel;

    @BeforeEach
    void setUp() {

        this.id = 1L;
        this.privateAddress = "privateAddress";
        this.publicAddress = "publicAddress";

        this.componentAddressModel = new ComponentAddressModel(this.id, this.privateAddress, this.publicAddress);

    }

    @Test
    void toDTO() {

        assertThat(this.componentAddressModel.toDTO())
                .isNotNull()
                .hasOnlyFields("id", "privateAddress", "publicAddress");

        assertThat(this.componentAddressModel.toDTO().getId()).isEqualTo(this.id);
        assertThat(this.componentAddressModel.toDTO().getPrivateAddress()).isEqualTo(this.privateAddress);
        assertThat(this.componentAddressModel.toDTO().getPublicAddress()).isEqualTo(this.publicAddress);


    }

}