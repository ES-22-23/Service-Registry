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

import java.util.*;
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

    public List<UUID> getOccupiedIds() {
        return registeredComponentService.getRegisteredComponents()
                .stream()
                .map(RegisteredComponentDto::getId)
                .collect(Collectors.toList());
    }

    public Optional<UUID> getUniqueIdForComponent(ComponentType componentType, List<UUID> occupiedIds) {

        Optional<UUID> serviceUniqueId;

        switch (componentType) {
            case API:
            case UI:
                serviceUniqueId = Optional.ofNullable(otherServicesUniqueIds).orElse(new ArrayList<>())
                        .stream()
                        .filter(uuid -> !occupiedIds.contains(uuid))
                        .findAny();
                break;
            case CAMERA:
                serviceUniqueId = Optional.ofNullable(availableCamerasUniqueIds).orElse(new ArrayList<>())
                        .stream()
                        .filter(uuid -> !occupiedIds.contains(uuid))
                        .findAny();
                break;
            case ALARM:
                serviceUniqueId = Optional.ofNullable(availableAlarmsUniqueIds).orElse(new ArrayList<>())
                        .stream()
                        .filter(uuid -> !occupiedIds.contains(uuid))
                        .findAny();
                break;
            default:
                serviceUniqueId = Optional.empty();
                break;
        }

        return serviceUniqueId;

    }

    public boolean hasAvailableIds(ComponentType componentType, List<RegisteredComponentDto> filteredRegisteredComponents) {

        switch (componentType) {
            case API:
            case UI:
                return filteredRegisteredComponents.size() < Optional.ofNullable(otherServicesUniqueIds)
                        .orElse(Collections.emptyList()).size();
            case CAMERA:
                return filteredRegisteredComponents.size() < Optional.ofNullable(availableCamerasUniqueIds)
                        .orElse(Collections.emptyList()).size();
            case ALARM:
                return filteredRegisteredComponents.size() < Optional.ofNullable(availableAlarmsUniqueIds)
                        .orElse(Collections.emptyList()).size();
            default:
                return false;
        }

    }

    public Optional<UUID> freeUniqueIdForComponent(ComponentType componentType) {

        Optional<UUID> freedId = Optional.empty();
        Optional<RegisteredComponentDto> componentToBeUnregistered = registeredComponentService.getRegisteredComponents()
                .stream()
                .filter(registeredComponentDto -> registeredComponentDto.getComponentType() == componentType)
                .filter(registeredComponentDto -> registeredComponentDto.getComponentAvailability().getAvailability() == ComponentAvailability.OFFLINE)
                .findAny();

        if (componentToBeUnregistered.isPresent()) {
            if (registeredComponentService.unregisterComponent(componentToBeUnregistered.get().getId())) {
                freedId = Optional.ofNullable(componentToBeUnregistered.get().getId());
                log.info("Freed unique id: {} for component type: {}", freedId.orElse(null), componentType);
            } else {
                log.error("Unable to unregister component: {}", componentToBeUnregistered.get().getId());
            }
        } else {
            log.warn("Unable to free an id for component type: {}. There are no Offline components...", componentType);
        }

        return freedId;

    }

}
