package pt.ua.deti.es.serviceregistry.web.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import pt.ua.deti.es.serviceregistry.entities.ComponentType;
import pt.ua.deti.es.serviceregistry.web.entities.RegisteredServicesResponse;
import pt.ua.deti.es.serviceregistry.web.entities.RegistrationRequest;
import pt.ua.deti.es.serviceregistry.web.entities.RegistrationResponse;
import pt.ua.deti.es.serviceregistry.web.entities.UnregistrationRequest;
import pt.ua.deti.es.serviceregistry.web.services.RegistryWebService;

import java.util.UUID;

@RestController
@RequestMapping("/registry")
public class RegistryController {

    private final RegistryWebService registryWebService;

    @Autowired
    public RegistryController(RegistryWebService registryWebService) {
        this.registryWebService = registryWebService;
    }

    @PostMapping("/register")
    public RegistrationResponse registerNewComponent(@RequestBody RegistrationRequest registrationRequest) {

        ComponentType componentType = registrationRequest.getComponentType();
        UUID registeredComponentUniqueId = registryWebService.registerComponent(registrationRequest,
                registryWebService.getUniqueIdForComponent(componentType,
                        registryWebService.getOccupiedIds(),
                        registryWebService.hasAvailableIds(componentType, registryWebService.getFilteredRegisteredComponents(componentType))));

        if (registeredComponentUniqueId != null) {
            return new RegistrationResponse("Service successfully registered.", registeredComponentUniqueId);
        } else {
            return new RegistrationResponse("Unable to register the service.", null);
        }

    }

    @DeleteMapping("/unregister")
    public RegistrationResponse unregisterComponent(@RequestBody UnregistrationRequest unregistrationRequest) {

        boolean successfullyUnregistered = registryWebService.unregisterComponent(unregistrationRequest.getComponentIdAsUUID());

        if (successfullyUnregistered) {
            return new RegistrationResponse("Service successfully un-registered.", unregistrationRequest.getComponentIdAsUUID());
        } else {
            return new RegistrationResponse("Unable to un-registered the service.", null);
        }

    }

    @GetMapping("/services")
    public RegisteredServicesResponse getRegisteredComponents() {
        return new RegisteredServicesResponse("Request Successful!", registryWebService.getAllRegisteredComponents());
    }

}
