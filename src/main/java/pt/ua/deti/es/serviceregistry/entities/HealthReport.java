package pt.ua.deti.es.serviceregistry.entities;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Generated;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@Generated
@NoArgsConstructor
@AllArgsConstructor
public class HealthReport {

    private boolean isHealthy;
    private List<?> additionalProperties;

}