package pt.ua.deti.es.serviceregistry.web.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import pt.ua.deti.es.serviceregistry.data.dto.RegisteredComponentDto;
import pt.ua.deti.es.serviceregistry.data.dto.ComponentAddressDto;
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

        Optional<UUID> uniqueIdForService = registryWebService.getUniqueIdForComponent(registrationRequest.getComponentType());

        return uniqueIdForService.map(uuid -> {

            ComponentAddressDto componentAddressDto = new ComponentAddressDto(null, registrationRequest.getComponentAddress().getPrivateAddress(), registrationRequest.getComponentAddress().getPublicAddress());
            RegisteredComponentDto serviceToBeRegisteredDto = new RegisteredComponentDto(uniqueIdForService.get(), registrationRequest.getComponentName(), registrationRequest.getComponentHealthEndpoint(), registrationRequest.getComponentType(), componentAddressDto);

            registryWebService.registerComponent(serviceToBeRegisteredDto);

            return new RegistrationResponse(
                    String.format("Service %s successfully registered.", registrationRequest.getComponentName()),
                    uuid
            );

        }).orElseGet(() -> new RegistrationResponse(
                String.format("Unable to register service %s.", registrationRequest.getComponentName()),
                null
        ));

    }

    @DeleteMapping("/unregister")
    public RegistrationResponse unregisterComponent(@RequestBody UnregistrationRequest unregistrationRequest) {
        registryWebService.unregisterComponent(unregistrationRequest.getComponentIdAsUUID());
        return new RegistrationResponse("Component successfully un-registered.", unregistrationRequest.getComponentIdAsUUID());
    }

    @GetMapping("/services")
    public RegisteredServicesResponse getRegisteredComponents() {
        return new RegisteredServicesResponse("Request Successful!", registryWebService.getAllRegisteredComponents());
    }

}
