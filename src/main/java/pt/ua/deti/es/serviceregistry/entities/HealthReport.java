package pt.ua.deti.es.serviceregistry.entities;

import lombok.Data;

import java.util.List;

@Data
public class HealthReport {

    private final boolean isHealthy;
    private final List<?> additionalProperties;

}