package pt.ua.deti.es.serviceregistry.data.models;

import lombok.*;
import pt.ua.deti.es.serviceregistry.data.dto.ComponentAvailabilityDto;
import pt.ua.deti.es.serviceregistry.entities.ComponentAvailability;

import javax.persistence.*;

@Entity
@Getter
@Setter
@ToString
@RequiredArgsConstructor
@AllArgsConstructor
@Table(name = "services_availability")
public class ComponentAvailabilityModel implements DataModel<ComponentAvailabilityDto> {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    @Column(name = "id", nullable = false)
    private Long id;

    @Enumerated(EnumType.STRING)
    @Column(name = "service_availability")
    private ComponentAvailability componentAvailability;

    @Column(name = "last_time_online")
    private Long lastTimeOnline;

    @Override
    public ComponentAvailabilityDto toDTO() {
        return new ComponentAvailabilityDto(id, componentAvailability, lastTimeOnline);
    }

}
