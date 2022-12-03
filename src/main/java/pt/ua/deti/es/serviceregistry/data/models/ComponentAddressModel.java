package pt.ua.deti.es.serviceregistry.data.models;

import lombok.*;
import org.hibernate.Hibernate;
import pt.ua.deti.es.serviceregistry.data.dto.ComponentAddressDto;

import javax.persistence.*;
import java.util.Objects;

@Entity
@Table(name = "services_addresses")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@ToString
@Generated
public class ComponentAddressModel implements DataModel<ComponentAddressDto> {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    @Column(name = "id", nullable = false)
    private Long id;

    @Column(name = "private_address")
    private String privateAddress;

    @Column(name = "public_address")
    private String publicAddress;

    @Override
    public ComponentAddressDto toDTO() {
        return new ComponentAddressDto(id, privateAddress, publicAddress);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || Hibernate.getClass(this) != Hibernate.getClass(o)) return false;
        ComponentAddressModel that = (ComponentAddressModel) o;
        return id != null && Objects.equals(id, that.id);
    }

    @Override
    public int hashCode() {
        return getClass().hashCode();
    }

}
