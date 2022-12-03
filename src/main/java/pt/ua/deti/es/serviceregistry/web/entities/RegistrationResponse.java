package pt.ua.deti.es.serviceregistry.web.entities;

import lombok.Data;
import lombok.Generated;

import java.util.UUID;

@Data
@Generated
public class RegistrationResponse {

    private final String message;
    private final UUID serviceUniqueId;

}
