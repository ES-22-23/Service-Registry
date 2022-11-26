package pt.ua.deti.es.serviceregistry.data.dto;

import lombok.Data;
import lombok.Generated;
import pt.ua.deti.es.serviceregistry.data.models.ComponentAddressModel;

@Data
@Generated
public class ComponentAddressDto implements DataTransferObject<ComponentAddressModel> {

    private final Long id;
    private final String privateAddress;
    private final String publicAddress;

    @Override
    public ComponentAddressModel toModel() {
        return new ComponentAddressModel(id, privateAddress, publicAddress);
    }

}
