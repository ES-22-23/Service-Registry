package pt.ua.deti.es.serviceregistry.web.entities;

import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.UUID;

@Data
@NoArgsConstructor
public class UnregistrationRequest {

    private String componentUniqueId;

    public UUID getComponentIdAsUUID() {
        return UUID.fromString(componentUniqueId);
    }

}
