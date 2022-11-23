package pt.ua.deti.es.serviceregistry.web.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import pt.ua.deti.es.serviceregistry.data.dto.ComponentAddressDto;
import pt.ua.deti.es.serviceregistry.data.dto.ComponentAvailabilityDto;
import pt.ua.deti.es.serviceregistry.data.dto.RegisteredComponentDto;
import pt.ua.deti.es.serviceregistry.data.services.RegisteredComponentService;
import pt.ua.deti.es.serviceregistry.entities.ComponentAvailability;
import pt.ua.deti.es.serviceregistry.entities.ComponentType;
import pt.ua.deti.es.serviceregistry.web.entities.RegistrationRequest;

import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;

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

    public List<RegisteredComponentDto> getFilteredRegisteredComponents(ComponentType componentType) {
        return getAllRegisteredComponents()
                .stream()
                .filter(registeredComponentDto -> registeredComponentDto.getComponentType() == componentType)
                .collect(Collectors.toList());
    }

    public UUID registerComponent(RegistrationRequest registrationRequest) {

        Optional<UUID> uniqueIdForService = getUniqueIdForComponent(registrationRequest.getComponentType());

        ComponentAddressDto componentAddressDto = new ComponentAddressDto(null, registrationRequest.getComponentAddress().getPrivateAddress(), registrationRequest.getComponentAddress().getPublicAddress());
        ComponentAvailabilityDto componentAvailabilityDto = new ComponentAvailabilityDto(null, ComponentAvailability.ONLINE, System.currentTimeMillis());

        return uniqueIdForService.map(uuid -> {

            RegisteredComponentDto serviceToBeRegisteredDto = new RegisteredComponentDto(uuid, registrationRequest.getComponentName(), registrationRequest.getComponentHealthEndpoint(), registrationRequest.getComponentProtocol(), registrationRequest.getComponentType(), componentAddressDto, componentAvailabilityDto);
            registeredComponentService.registerComponent(serviceToBeRegisteredDto);
            return uuid;

        }).orElse(null);

    }

    public void unregisterComponent(UUID componentUniqueId) {
        registeredComponentService.unregisterComponent(componentUniqueId);
    }

    private Optional<UUID> getUniqueIdForComponent(ComponentType componentType) {

        Optional<UUID> serviceUniqueId = Optional.empty();

        List<RegisteredComponentDto> registeredComponentsByType = getFilteredRegisteredComponents(componentType);
        List<UUID> occupiedIds = registeredComponentsByType
                .stream()
                .map(RegisteredComponentDto::getId)
                .collect(Collectors.toList());

        switch (componentType) {
            case API:
            case UI:
                serviceUniqueId = otherServicesUniqueIds
                        .stream()
                        .filter(uuid -> !occupiedIds.contains(uuid))
                        .findAny();
                break;
            case CAMERA:
                serviceUniqueId = availableCamerasUniqueIds
                        .stream()
                        .filter(uuid -> !occupiedIds.contains(uuid))
                        .findAny();
                break;
            case ALARM:
                serviceUniqueId = availableAlarmsUniqueIds
                        .stream()
                        .filter(uuid -> !occupiedIds.contains(uuid))
                        .findAny();
                break;
        }

        return serviceUniqueId;

    }

}
