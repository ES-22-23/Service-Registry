package pt.ua.deti.es.serviceregistry.data.dto;

import lombok.Data;
import pt.ua.deti.es.serviceregistry.data.models.ComponentAddressModel;

import java.io.Serializable;

@Data
public class ComponentAddressDto implements DataTransferObject<ComponentAddressModel> {

    private final Long id;
    private final String privateAddress;
    private final String publicAddress;

    @Override
    public ComponentAddressModel toModel() {
        return new ComponentAddressModel(id, privateAddress, publicAddress);
    }

}
