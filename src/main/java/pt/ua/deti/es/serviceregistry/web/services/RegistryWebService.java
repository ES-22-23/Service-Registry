package pt.ua.deti.es.serviceregistry.web.services;

import lombok.extern.log4j.Log4j2;
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
@Log4j2
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
        return registeredComponentService.getRegisteredComponents()
                .stream()
                .filter(registeredComponentDto -> registeredComponentDto.getComponentType() == componentType)
                .collect(Collectors.toList());
    }

    public UUID registerComponent(RegistrationRequest registrationRequest, Optional<UUID> componentUniqueId) {

        ComponentAddressDto componentAddressDto = new ComponentAddressDto(null, registrationRequest.getComponentAddress().getPrivateAddress(), registrationRequest.getComponentAddress().getPublicAddress());
        ComponentAvailabilityDto componentAvailabilityDto = new ComponentAvailabilityDto(null, ComponentAvailability.ONLINE, System.currentTimeMillis());

        return componentUniqueId.map(uuid -> {

            RegisteredComponentDto serviceToBeRegisteredDto = new RegisteredComponentDto(uuid, registrationRequest.getComponentName(), registrationRequest.getComponentHealthEndpoint(), registrationRequest.getComponentProtocol(), registrationRequest.getComponentType(), componentAddressDto, componentAvailabilityDto);
            registeredComponentService.registerComponent(serviceToBeRegisteredDto);
            return uuid;

        }).orElse(null);

    }

    public boolean unregisterComponent(UUID componentUniqueId) {
        return registeredComponentService.unregisterComponent(componentUniqueId);
    }

    public RegisteredComponentDto updateAvailabilityStatus(UUID componentUniqueId, ComponentAvailability componentAvailability) {

        RegisteredComponentDto registeredComponent = registeredComponentService.getRegisteredComponent(componentUniqueId);

        if (registeredComponent == null) {
            log.warn("Trying to update availability status for a component that is not registered.");
            return null;
        }

        if (componentAvailability == ComponentAvailability.ONLINE) {
            registeredComponent.setComponentAvailability(new ComponentAvailabilityDto(null, componentAvailability, System.currentTimeMillis()));
        } else {
            registeredComponent.setComponentAvailability(new ComponentAvailabilityDto(null, componentAvailability, registeredComponent.getComponentAvailability().getLastTimeOnline()));
        }

        registeredComponentService.updateComponent(registeredComponent);
        return registeredComponent;

    }

    public Optional<UUID> getUniqueIdForComponent(ComponentType componentType) {

        Optional<UUID> serviceUniqueId = Optional.empty();

        if (!hasAvailableIds(componentType, getFilteredRegisteredComponents(componentType))) {

            log.warn("No more available ids for component type: {}. Trying to free an ID...", componentType);

            UUID freedUniqueId = freeUniqueIdForComponent(componentType);

            if (freedUniqueId != null) {
                log.info("Freed unique id: {} for component type: {}", freedUniqueId, componentType);
            } else {
                log.warn("Unable to free an id for component type: {}. There are no Offline components...", componentType);
            }

        }

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

    private boolean hasAvailableIds(ComponentType componentType, List<RegisteredComponentDto> filteredRegisteredComponents) {

        switch (componentType) {
            case API:
            case UI:
                return filteredRegisteredComponents.size() < otherServicesUniqueIds.size();
            case CAMERA:
                return filteredRegisteredComponents.size() < availableCamerasUniqueIds.size();
            case ALARM:
                return filteredRegisteredComponents.size() < availableAlarmsUniqueIds.size();
        }

        return false;

    }

    private UUID freeUniqueIdForComponent(ComponentType componentType) {

        Optional<RegisteredComponentDto> componentThatCanBeUnregistered = getFilteredRegisteredComponents(componentType).stream()
                .filter(registeredComponentDto -> registeredComponentDto.getComponentAvailability().getAvailability() == ComponentAvailability.OFFLINE)
                .findAny();

        if (componentThatCanBeUnregistered.isPresent() && unregisterComponent(componentThatCanBeUnregistered.get().getId())) {
            return componentThatCanBeUnregistered.get().getId();
        }

        return null;

    }

}
