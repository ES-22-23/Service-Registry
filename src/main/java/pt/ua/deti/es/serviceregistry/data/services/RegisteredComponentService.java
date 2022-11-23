package pt.ua.deti.es.serviceregistry.data.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import pt.ua.deti.es.serviceregistry.data.dto.RegisteredComponentDto;
import pt.ua.deti.es.serviceregistry.data.models.RegisteredComponentModel;
import pt.ua.deti.es.serviceregistry.data.repositories.RegisteredComponentRepository;
import pt.ua.deti.es.serviceregistry.data.repositories.ComponentAddressRepository;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
public class RegisteredComponentService {

    private final RegisteredComponentRepository registeredComponentRepository;
    private final ComponentAddressRepository componentAddressRepository;

    @Autowired
    public RegisteredComponentService(RegisteredComponentRepository registeredComponentRepository, ComponentAddressRepository componentAddressRepository) {
        this.registeredComponentRepository = registeredComponentRepository;
        this.componentAddressRepository = componentAddressRepository;
    }

    public void registerComponent(RegisteredComponentDto registeredComponentDto) {

        RegisteredComponentModel registeredComponentModel = registeredComponentDto.toModel();
        componentAddressRepository.save(registeredComponentModel.getServiceAddress());
        registeredComponentRepository.save(registeredComponentModel);

    }

    public void unregisterComponent(UUID serviceUniqueId) {
        System.out.println(registeredComponentRepository.existsById(serviceUniqueId));
        registeredComponentRepository.deleteById(serviceUniqueId);
    }

    public List<RegisteredComponentDto> getRegisteredComponents() {
        return registeredComponentRepository.findAll()
                .stream()
                .map(RegisteredComponentModel::toDTO)
                .collect(Collectors.toList());
    }

}
