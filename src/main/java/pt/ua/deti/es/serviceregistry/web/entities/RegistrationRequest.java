package pt.ua.deti.es.serviceregistry.web.entities;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;
import pt.ua.deti.es.serviceregistry.entities.ComponentAddress;
import pt.ua.deti.es.serviceregistry.entities.ComponentProtocol;
import pt.ua.deti.es.serviceregistry.entities.ComponentType;

@Data
public class RegistrationRequest {

    @JsonProperty("serviceName")
    private final String componentName;

    @JsonProperty("serviceType")
    private final ComponentType componentType;

    @JsonProperty("serviceHealthEndpoint")
    private final String componentHealthEndpoint;

    @JsonProperty("serviceProtocol")
    private final ComponentProtocol componentProtocol;

    @JsonProperty("serviceAddress")
    private final ComponentAddress componentAddress;

}
