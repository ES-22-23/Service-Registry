package pt.ua.deti.es.serviceregistry.data.dto;

import lombok.Data;
import pt.ua.deti.es.serviceregistry.data.models.RegisteredComponentModel;
import pt.ua.deti.es.serviceregistry.web.entities.ComponentType;

import java.util.UUID;

@Data
public class RegisteredComponentDto implements DataTransferObject<RegisteredComponentModel> {

    private final UUID id;
    private final String serviceName;
    private final String healthEndpoint;
    private final ComponentType componentType;
    private final ComponentAddressDto serviceAddress;

    @Override
    public RegisteredComponentModel toModel() {
        return new RegisteredComponentModel(id, serviceName, healthEndpoint, componentType, serviceAddress.toModel());
    }

}
