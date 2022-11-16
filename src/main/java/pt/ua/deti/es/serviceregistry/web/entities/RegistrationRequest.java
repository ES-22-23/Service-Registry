package pt.ua.deti.es.serviceregistry.web.entities;

import lombok.Data;

@Data
public class RegistrationRequest {

    private final String serviceName;
    private final ServiceType serviceType;
    private final String serviceHealthEndpoint;
    private final ServiceAddress serviceAddress;

}
