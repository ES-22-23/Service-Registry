package pt.ua.deti.es.serviceregistry.data.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import pt.ua.deti.es.serviceregistry.data.models.RegisteredComponentModel;
import pt.ua.deti.es.serviceregistry.entities.ComponentProtocol;
import pt.ua.deti.es.serviceregistry.entities.ComponentType;

import java.util.UUID;

@Data
@AllArgsConstructor
public class RegisteredComponentDto implements DataTransferObject<RegisteredComponentModel> {

    private UUID id;
    private String componentName;
    private String healthEndpoint;
    private ComponentProtocol componentProtocol;
    private ComponentType componentType;
    private ComponentAddressDto componentAddress;
    private ComponentAvailabilityDto componentAvailability;

    @Override
    public RegisteredComponentModel toModel() {
        return new RegisteredComponentModel(id, componentName, healthEndpoint, componentProtocol, componentType, componentAddress.toModel(), componentAvailability.toModel());
    }

}
