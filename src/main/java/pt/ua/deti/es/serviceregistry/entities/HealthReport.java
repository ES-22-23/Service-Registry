package pt.ua.deti.es.serviceregistry.entities;

import lombok.Data;
import lombok.Generated;

import java.util.List;

@Data
@Generated
public class HealthReport {

    private final boolean isHealthy;
    private final List<?> additionalProperties;

}