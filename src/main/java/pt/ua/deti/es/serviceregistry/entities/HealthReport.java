package pt.ua.deti.es.serviceregistry.entities;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Generated;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@Generated
@AllArgsConstructor
@NoArgsConstructor
public class HealthReport {

    @JsonProperty("isHealthy")
    private boolean isHealthy;
    @JsonProperty("additionalProperties")
    private List<?> additionalProperties;

}