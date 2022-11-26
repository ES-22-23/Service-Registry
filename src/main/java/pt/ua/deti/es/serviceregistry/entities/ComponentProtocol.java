package pt.ua.deti.es.serviceregistry.entities;

import lombok.Generated;
import lombok.Getter;
import lombok.ToString;

@Getter
@Generated
public enum ComponentProtocol {

    HTTP("http://", 80), HTTPS("https://", 443);

    private final String protocolPrefix;
    private final int protocolPort;

    ComponentProtocol(String protocolPrefix, int protocolPort) {
        this.protocolPrefix = protocolPrefix;
        this.protocolPort = protocolPort;
    }

}
