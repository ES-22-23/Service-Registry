package pt.ua.deti.es.serviceregistry.schedulers;

import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.scheduling.annotation.Async;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;
import pt.ua.deti.es.serviceregistry.entities.ComponentAvailability;
import pt.ua.deti.es.serviceregistry.utils.ComponentUtils;
import pt.ua.deti.es.serviceregistry.entities.HealthReport;
import pt.ua.deti.es.serviceregistry.web.services.RegistryWebService;

import java.time.Duration;

@Component
@EnableScheduling
@EnableAsync
@Log4j2
public class ComponentsHealthScheduler {

    private final RegistryWebService registryWebService;
    private final ComponentUtils componentUtils;

    @Value("${Services.Availability.OfflineTimeout}")
    private int availabilityOfflineTimeout;

    @Autowired
    public ComponentsHealthScheduler(RegistryWebService registryWebService, ComponentUtils componentUtils) {
        this.registryWebService = registryWebService;
        this.componentUtils = componentUtils;
    }

    @Async
    @Scheduled(fixedDelayString = "${Services.Availability.HealthCheck.Delay}")
    public void checkComponentsAvailability() {

        registryWebService.getAllRegisteredComponents().forEach(registeredComponent -> {

            RestTemplate restTemplate = new RestTemplateBuilder()
                    .setConnectTimeout(Duration.ofSeconds(1))
                    .setReadTimeout(Duration.ofSeconds(1))
                    .build();

            String componentHealthEndpoint = componentUtils.buildHealthEndpoint(registeredComponent, false);

            HealthReport componentHealthReport;

            try {
                componentHealthReport = restTemplate.getForObject(componentHealthEndpoint, HealthReport.class);
            } catch (RestClientException e) {
                // Ignore Exception. Already treated in the next if statement.
                componentHealthReport = null;
            }

            if (componentHealthReport != null && componentHealthReport.isHealthy()) {
                registryWebService.updateAvailabilityStatus(registeredComponent.getId(), ComponentAvailability.ONLINE);
            } else {

                long lastTimeHealthyTimestamp = registeredComponent.getComponentAvailability().getLastTimeOnline();

                if (registeredComponent.getComponentAvailability().getAvailability() == ComponentAvailability.OFFLINE) {
                    return;
                }

                if (System.currentTimeMillis() - lastTimeHealthyTimestamp > availabilityOfflineTimeout) {
                    registryWebService.updateAvailabilityStatus(registeredComponent.getId(), ComponentAvailability.OFFLINE);
                    log.warn(String.format("Component %s (%s) did not respond for 1 minute - Marked as Offline.", registeredComponent.getComponentName(), registeredComponent.getId()));
                } else {
                    registryWebService.updateAvailabilityStatus(registeredComponent.getId(), ComponentAvailability.NOT_RESPONDING);
                    log.warn(String.format("Component %s (%s) is not responding.", registeredComponent.getComponentName(), registeredComponent.getId()));
                }

            }

        });

    }

}
