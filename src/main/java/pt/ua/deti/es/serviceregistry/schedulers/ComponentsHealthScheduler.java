package pt.ua.deti.es.serviceregistry.schedulers;

import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.scheduling.annotation.Async;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.web.client.HttpServerErrorException;
import org.springframework.web.client.ResourceAccessException;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.client.UnknownContentTypeException;
import pt.ua.deti.es.serviceregistry.data.dto.RegisteredComponentDto;
import pt.ua.deti.es.serviceregistry.entities.ComponentAvailability;
import pt.ua.deti.es.serviceregistry.entities.ComponentType;
import pt.ua.deti.es.serviceregistry.entities.HealthReport;
import pt.ua.deti.es.serviceregistry.utils.ComponentUtils;
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
                    .setConnectTimeout(Duration.ofSeconds(2))
                    .setReadTimeout(Duration.ofSeconds(2))
                    .build();

            String componentHealthEndpoint = componentUtils.buildHealthEndpoint(registeredComponent, false);

            ResponseEntity<HealthReport> componentHealthReport;

            try {
                componentHealthReport = restTemplate.exchange(componentHealthEndpoint, HttpMethod.GET, null, HealthReport.class);
            } catch (ResourceAccessException e) {
                // Ignore Timeout. Already treated in the next if statement.
                componentHealthReport = null;
            } catch (UnknownContentTypeException e) {
                // Ignore Unknown Content Type Exception.
                componentHealthReport = new ResponseEntity<>(null, HttpStatus.valueOf(e.getRawStatusCode()));
            } catch (HttpServerErrorException e) {
                // Ignore Server Error Exception.
                componentHealthReport = new ResponseEntity<>(null, e.getStatusCode());
            }

            boolean isComponentHealthy;

            if (registeredComponent.getComponentType() == ComponentType.UI) {
                isComponentHealthy = componentHealthReport != null && componentHealthReport.getStatusCode() == HttpStatus.OK;
            } else {
                isComponentHealthy = componentHealthReport != null && componentHealthReport.getStatusCode() == HttpStatus.OK && componentHealthReport.getBody() != null && componentHealthReport.getBody().isHealthy();
            }

            updateComponentStatus(registeredComponent, isComponentHealthy);

        });

    }

    private void updateComponentStatus(RegisteredComponentDto registeredComponent, boolean isHealthy) {

        if (isHealthy) {
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

    }

}
