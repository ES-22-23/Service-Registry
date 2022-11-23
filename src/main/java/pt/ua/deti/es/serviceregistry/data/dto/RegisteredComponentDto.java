package pt.ua.deti.es.serviceregistry.data.dto;

import lombok.Data;
import pt.ua.deti.es.serviceregistry.data.models.RegisteredComponentModel;
import pt.ua.deti.es.serviceregistry.web.entities.ServiceType;

import java.io.Serializable;
import java.util.UUID;

@Data
public class RegisteredComponentDto implements DataTransferObject<RegisteredComponentModel> {

    private final UUID id;
    private final String serviceName;
    private final String healthEndpoint;
    private final ServiceType serviceType;
    private final ComponentAddressDto serviceAddress;

    @Override
    public RegisteredComponentModel toModel() {
        return new RegisteredComponentModel(id, serviceName, healthEndpoint, serviceType, serviceAddress.toModel());
    }

}
