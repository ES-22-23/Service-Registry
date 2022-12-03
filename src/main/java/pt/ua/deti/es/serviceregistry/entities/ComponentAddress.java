package pt.ua.deti.es.serviceregistry.entities;

import com.fasterxml.jackson.annotation.JsonAlias;
import lombok.Data;
import lombok.Generated;

@Data
@Generated
public class ComponentAddress {

    @JsonAlias("public")
    private final String publicAddress;
    @JsonAlias("private")
    private final String privateAddress;

}
