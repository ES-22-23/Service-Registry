package pt.ua.deti.es.serviceregistry.data.models;

import lombok.*;
import org.hibernate.Hibernate;
import pt.ua.deti.es.serviceregistry.data.dto.ComponentAvailabilityDto;
import pt.ua.deti.es.serviceregistry.entities.ComponentAvailability;

import javax.persistence.*;
import java.util.Objects;

@Entity
@Table(name = "services_availability")
@Getter
@Setter
@ToString
@RequiredArgsConstructor
@AllArgsConstructor
@Generated
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

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || Hibernate.getClass(this) != Hibernate.getClass(o)) return false;
        ComponentAvailabilityModel that = (ComponentAvailabilityModel) o;
        return id != null && Objects.equals(id, that.id);
    }

    @Override
    public int hashCode() {
        return getClass().hashCode();
    }

}
