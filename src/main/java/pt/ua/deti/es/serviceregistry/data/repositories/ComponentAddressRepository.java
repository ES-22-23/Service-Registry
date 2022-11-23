package pt.ua.deti.es.serviceregistry.data.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import pt.ua.deti.es.serviceregistry.data.models.ComponentAddressModel;

@Repository
public interface ComponentAddressRepository extends JpaRepository<ComponentAddressModel, Long> {
}