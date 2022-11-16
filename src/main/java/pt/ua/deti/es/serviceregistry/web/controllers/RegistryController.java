package pt.ua.deti.es.serviceregistry.web.controllers;

import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import pt.ua.deti.es.serviceregistry.web.entities.RegistrationRequest;
import pt.ua.deti.es.serviceregistry.web.entities.RegistrationResponse;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/register")
public class RegistryController {

    @PostMapping()
    public RegistrationResponse registerNewService(@RequestBody RegistrationRequest registrationRequest) {

        List<UUID> availableUniqueIds = new ArrayList<>();
        availableUniqueIds.add(UUID.randomUUID());

        return new RegistrationResponse(
                String.format("Service %s successfully registered.", registrationRequest.getServiceName()),
                availableUniqueIds.stream().findAny().get()
        );

    }

}
