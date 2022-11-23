package pt.ua.deti.es.serviceregistry.data.dto;

import lombok.Data;
import pt.ua.deti.es.serviceregistry.data.models.RegisteredComponentModel;
import pt.ua.deti.es.serviceregistry.entities.ComponentProtocol;
import pt.ua.deti.es.serviceregistry.entities.ComponentType;

import java.util.UUID;

@Data
public class RegisteredComponentDto implements DataTransferObject<RegisteredComponentModel> {

    private final UUID id;
    private final String componentName;
    private final String healthEndpoint;
    private final ComponentProtocol componentProtocol;
    private final ComponentType componentType;
    private final ComponentAddressDto componentAddress;
    private final ComponentAvailabilityDto componentAvailability;

    @Override
    public RegisteredComponentModel toModel() {
        return new RegisteredComponentModel(id, componentName, healthEndpoint, componentProtocol, componentType, componentAddress.toModel(), componentAvailability.toModel());
    }

}
