package pt.ua.deti.es.serviceregistry.data.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import pt.ua.deti.es.serviceregistry.data.dto.RegisteredComponentDto;
import pt.ua.deti.es.serviceregistry.data.models.RegisteredComponentModel;
import pt.ua.deti.es.serviceregistry.data.repositories.ComponentAvailabilityRepository;
import pt.ua.deti.es.serviceregistry.data.repositories.RegisteredComponentRepository;
import pt.ua.deti.es.serviceregistry.data.repositories.ComponentAddressRepository;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
public class RegisteredComponentService {

    private final RegisteredComponentRepository registeredComponentRepository;
    private final ComponentAddressRepository componentAddressRepository;
    private final ComponentAvailabilityRepository componentAvailabilityRepository;

    @Autowired
    public RegisteredComponentService(RegisteredComponentRepository registeredComponentRepository, ComponentAddressRepository componentAddressRepository, ComponentAvailabilityRepository componentAvailabilityRepository) {
        this.registeredComponentRepository = registeredComponentRepository;
        this.componentAddressRepository = componentAddressRepository;
        this.componentAvailabilityRepository = componentAvailabilityRepository;
    }

    public void registerComponent(RegisteredComponentDto registeredComponentDto) {

        RegisteredComponentModel registeredComponentModel = registeredComponentDto.toModel();
        componentAddressRepository.save(registeredComponentModel.getComponentAddress());
        componentAvailabilityRepository.save(registeredComponentModel.getComponentAvailability());
        registeredComponentRepository.save(registeredComponentModel);

    }

    public boolean unregisterComponent(UUID serviceUniqueId) {
        if (registeredComponentRepository.existsById(serviceUniqueId)) {
            registeredComponentRepository.deleteById(serviceUniqueId);
            return true;
        }
        return false;
    }

    public List<RegisteredComponentDto> getRegisteredComponents() {
        return registeredComponentRepository.findAll()
                .stream()
                .map(RegisteredComponentModel::toDTO)
                .collect(Collectors.toList());
    }

    public RegisteredComponentDto getRegisteredComponent(UUID componentUniqueId) {
        return registeredComponentRepository.findById(componentUniqueId)
                .map(RegisteredComponentModel::toDTO)
                .orElse(null);
    }

    public void updateComponent(RegisteredComponentDto registeredComponentDto) {

        RegisteredComponentModel registeredComponentModel = registeredComponentDto.toModel();

        componentAddressRepository.save(registeredComponentModel.getComponentAddress());
        componentAvailabilityRepository.save(registeredComponentModel.getComponentAvailability());
        registeredComponentRepository.save(registeredComponentModel);

    }

}
