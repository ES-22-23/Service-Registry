package pt.ua.deti.es.serviceregistry.web.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import pt.ua.deti.es.serviceregistry.data.dto.RegisteredComponentDto;
import pt.ua.deti.es.serviceregistry.data.services.RegisteredComponentService;
import pt.ua.deti.es.serviceregistry.web.entities.ServiceType;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
public class RegistryWebService {

    @Value("${Services.Cameras.Ids:}")
    private List<UUID> availableCamerasUniqueIds;

    @Value("${Services.Alarms.Ids:}")
    private List<UUID> availableAlarmsUniqueIds;

    @Value("${Services.Others.Ids:}")
    private List<UUID> otherServicesUniqueIds;

    private final RegisteredComponentService registeredComponentService;

    @Autowired
    public RegistryWebService(RegisteredComponentService registeredComponentService) {
        this.registeredComponentService = registeredComponentService;
    }

    public List<RegisteredComponentDto> getAllRegisteredComponents() {
        return registeredComponentService.getRegisteredComponents();
    }

    public void registerComponent(RegisteredComponentDto registeredComponentDto) {
        System.out.println("UUID: " + registeredComponentDto.getId().toString());
        registeredComponentService.registerComponent(registeredComponentDto);
    }

    public void unregisterComponent(UUID componentUniqueId) {
        registeredComponentService.unregisterComponent(componentUniqueId);
    }

    public Optional<UUID> getUniqueIdForComponent(ServiceType serviceType) {

        Optional<UUID> serviceUniqueId = Optional.empty();

        switch (serviceType) {
            case API:
            case UI:
                serviceUniqueId = otherServicesUniqueIds.stream().findAny();
                break;
            case CAMERA:
                serviceUniqueId = availableCamerasUniqueIds.stream().findAny();
                break;
            case ALARM:
                serviceUniqueId = availableAlarmsUniqueIds.stream().findAny();
                break;
        }

        return serviceUniqueId;

    }

}
