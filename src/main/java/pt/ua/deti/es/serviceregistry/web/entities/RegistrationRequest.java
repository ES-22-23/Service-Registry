package pt.ua.deti.es.serviceregistry.web.entities;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

@Data
public class RegistrationRequest {

    @JsonProperty("serviceName")
    private final String componentName;

    @JsonProperty("serviceType")
    private final ComponentType componentType;

    @JsonProperty("serviceHealthEndpoint")
    private final String componentHealthEndpoint;

    @JsonProperty("serviceAddress")
    private final ComponentAddress componentAddress;

}
