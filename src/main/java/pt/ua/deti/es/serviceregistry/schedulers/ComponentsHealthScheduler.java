package pt.ua.deti.es.serviceregistry.schedulers;

import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.scheduling.annotation.Async;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;
import pt.ua.deti.es.serviceregistry.utils.ComponentUtils;
import pt.ua.deti.es.serviceregistry.entities.HealthReport;
import pt.ua.deti.es.serviceregistry.web.services.RegistryWebService;

@Component
@EnableScheduling
@EnableAsync
@Log4j2
public class ComponentsHealthScheduler {

    private final RegistryWebService registryWebService;
    private final ComponentUtils componentUtils;

    @Autowired
    public ComponentsHealthScheduler(RegistryWebService registryWebService, ComponentUtils componentUtils) {
        this.registryWebService = registryWebService;
        this.componentUtils = componentUtils;
    }

    @Async
    @Scheduled(fixedDelayString = "${Schedulers.ComponentsAvailability.Delay}")
    public void checkComponentsAvailability() {

        registryWebService.getAllRegisteredComponents().forEach(registeredComponent -> {

            RestTemplate restTemplate = new RestTemplateBuilder().build();
            String componentHealthEndpoint = componentUtils.buildHealthEndpoint(registeredComponent, false);

            HealthReport componentHealthReport = restTemplate.getForObject(componentHealthEndpoint, HealthReport.class);

            if (componentHealthReport == null) {
                log.warn(String.format("Unable to get Health Report for component %s.", registeredComponent.getComponentName()));
                return;
            }

            if (!componentHealthReport.isRunning()) {

            }

        });

    }

}
