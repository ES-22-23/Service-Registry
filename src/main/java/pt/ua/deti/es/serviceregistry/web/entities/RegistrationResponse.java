package pt.ua.deti.es.serviceregistry.web.entities;

import lombok.Data;

import java.util.UUID;

@Data
public class RegistrationResponse {

    private final String message;
    private final UUID serviceUniqueId;

}
