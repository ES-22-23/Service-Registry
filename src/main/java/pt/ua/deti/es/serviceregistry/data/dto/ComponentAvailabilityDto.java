package pt.ua.deti.es.serviceregistry.data.dto;

import lombok.Data;
import lombok.Generated;
import pt.ua.deti.es.serviceregistry.data.models.ComponentAvailabilityModel;
import pt.ua.deti.es.serviceregistry.entities.ComponentAvailability;

@Data
@Generated
public class ComponentAvailabilityDto implements DataTransferObject<ComponentAvailabilityModel> {

    private final Long id;
    private final ComponentAvailability availability;
    private final Long lastTimeOnline;

    @Override
    public ComponentAvailabilityModel toModel() {
        return new ComponentAvailabilityModel(id, availability, lastTimeOnline);
    }

}
