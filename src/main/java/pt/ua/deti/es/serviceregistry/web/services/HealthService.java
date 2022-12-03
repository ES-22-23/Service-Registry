package pt.ua.deti.es.serviceregistry.web.services;

import org.springframework.stereotype.Service;
import pt.ua.deti.es.serviceregistry.entities.HealthReport;

@Service
public class HealthService {

    public HealthReport getHealthReport() {
        return new HealthReport(true, null);
    }

}