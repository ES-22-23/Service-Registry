package pt.ua.deti.es.serviceregistry.web.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import pt.ua.deti.es.serviceregistry.data.dto.ComponentAvailabilityDto;
import pt.ua.deti.es.serviceregistry.data.dto.RegisteredComponentDto;
import pt.ua.deti.es.serviceregistry.data.dto.ComponentAddressDto;
import pt.ua.deti.es.serviceregistry.entities.ComponentAvailability;
import pt.ua.deti.es.serviceregistry.web.entities.RegisteredServicesResponse;
import pt.ua.deti.es.serviceregistry.web.entities.RegistrationRequest;
import pt.ua.deti.es.serviceregistry.web.entities.RegistrationResponse;
import pt.ua.deti.es.serviceregistry.web.entities.UnregistrationRequest;
import pt.ua.deti.es.serviceregistry.web.services.RegistryWebService;

import java.util.Optional;
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

        UUID registeredComponentUniqueId = registryWebService.registerComponent(registrationRequest);

        if (registeredComponentUniqueId != null) {
            return new RegistrationResponse("Service successfully registered.", registeredComponentUniqueId);
        } else {
            return new RegistrationResponse("Unable to register the service.", null);
        }

    }

    @DeleteMapping("/unregister")
    public RegistrationResponse unregisterComponent(@RequestBody UnregistrationRequest unregistrationRequest) {
        registryWebService.unregisterComponent(unregistrationRequest.getComponentIdAsUUID());
        return new RegistrationResponse("Service successfully un-registered.", unregistrationRequest.getComponentIdAsUUID());
    }

    @GetMapping("/services")
    public RegisteredServicesResponse getRegisteredComponents() {
        return new RegisteredServicesResponse("Request Successful!", registryWebService.getAllRegisteredComponents());
    }

}
