package pt.ua.deti.es.serviceregistry.data.models;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import pt.ua.deti.es.serviceregistry.data.dto.ComponentAddressDto;

import javax.persistence.*;

@Entity
@Table(name = "services_addresses")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
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

}
