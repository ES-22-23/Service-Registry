package pt.ua.deti.es.serviceregistry.data.models;

import lombok.*;
import org.hibernate.Hibernate;
import org.hibernate.annotations.Type;
import pt.ua.deti.es.serviceregistry.data.dto.RegisteredComponentDto;
import pt.ua.deti.es.serviceregistry.web.entities.ComponentType;

import javax.persistence.*;
import java.util.Objects;
import java.util.UUID;

@Entity
@Getter
@Setter
@ToString
@RequiredArgsConstructor
@AllArgsConstructor
@Table(name="registered_services")
public class RegisteredComponentModel implements DataModel<RegisteredComponentDto> {

    @Id
    @Type(type="org.hibernate.type.UUIDCharType")
    @Column(name = "id", nullable = false)
    private UUID id;

    @Column(name = "service_name", nullable = false)
    private String serviceName;

    @Column(name = "health_endpoint")
    private String healthEndpoint;

    @Enumerated(EnumType.STRING)
    @Column(name = "service_type", nullable = false)
    private ComponentType componentType;

    @OneToOne(optional = false, orphanRemoval = true)
    @JoinColumn(name = "service_address_id", nullable = false)
    private ComponentAddressModel serviceAddress;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || Hibernate.getClass(this) != Hibernate.getClass(o)) return false;
        RegisteredComponentModel that = (RegisteredComponentModel) o;
        return id != null && Objects.equals(id, that.id);
    }

    @Override
    public int hashCode() {
        return getClass().hashCode();
    }

    @Override
    public RegisteredComponentDto toDTO() {
        return new RegisteredComponentDto(id, serviceName, healthEndpoint, componentType, serviceAddress.toDTO());
    }

}
