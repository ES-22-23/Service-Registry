package pt.ua.deti.es.serviceregistry.web.entities;

import lombok.Data;
import lombok.Generated;
import lombok.NoArgsConstructor;

import java.util.UUID;

@Data
@NoArgsConstructor
@Generated
public class UnregistrationRequest {

    private String componentUniqueId;

    public UUID getComponentIdAsUUID() {
        return UUID.fromString(componentUniqueId);
    }

}
