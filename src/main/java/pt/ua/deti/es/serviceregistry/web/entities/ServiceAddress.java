package pt.ua.deti.es.serviceregistry.web.entities;

import lombok.Data;

@Data
public class ServiceAddress {

    private final String publicAddress;
    private final String privateAddress;

}
