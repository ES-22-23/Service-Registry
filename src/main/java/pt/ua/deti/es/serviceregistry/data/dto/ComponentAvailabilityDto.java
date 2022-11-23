package pt.ua.deti.es.serviceregistry.data.dto;

import lombok.Data;
import pt.ua.deti.es.serviceregistry.data.models.ComponentAvailabilityModel;
import pt.ua.deti.es.serviceregistry.entities.ComponentAvailability;

import java.io.Serializable;

@Data
public class ComponentAvailabilityDto implements DataTransferObject<ComponentAvailabilityModel> {

    private final Long id;
    private final ComponentAvailability componentAvailability;
    private final Long lastTimeOnline;

    @Override
    public ComponentAvailabilityModel toModel() {
        return new ComponentAvailabilityModel(id, componentAvailability, lastTimeOnline);
    }

}