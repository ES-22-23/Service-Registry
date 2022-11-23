package pt.ua.deti.es.serviceregistry.web.entities;

import lombok.Data;
import pt.ua.deti.es.serviceregistry.data.dto.RegisteredComponentDto;

import java.util.List;

@Data
public class RegisteredServicesResponse {

    private final String message;
    private final List<RegisteredComponentDto> registeredServices;

}
