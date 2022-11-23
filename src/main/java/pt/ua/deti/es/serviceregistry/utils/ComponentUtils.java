package pt.ua.deti.es.serviceregistry.utils;

import org.springframework.stereotype.Component;
import pt.ua.deti.es.serviceregistry.data.dto.RegisteredComponentDto;

@Component
public class ComponentUtils {

    public String buildHealthEndpoint(RegisteredComponentDto registeredComponentDto, boolean usePrivateAddress) {

        String protocolPrefix = registeredComponentDto.getComponentProtocol().getProtocolPrefix();
        String address = usePrivateAddress ? registeredComponentDto.getComponentAddress().getPrivateAddress() :
                registeredComponentDto.getComponentAddress().getPublicAddress();
        String healthEndpoint = registeredComponentDto.getHealthEndpoint();
        String protocolPort = String.valueOf(registeredComponentDto.getComponentProtocol().getProtocolPort());

        return protocolPrefix + address + ":" + protocolPort + healthEndpoint;

    }

}
