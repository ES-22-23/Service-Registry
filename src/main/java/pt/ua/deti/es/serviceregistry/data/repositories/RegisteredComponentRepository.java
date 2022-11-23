package pt.ua.deti.es.serviceregistry.data.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import pt.ua.deti.es.serviceregistry.data.models.RegisteredComponentModel;

import java.util.UUID;

@Repository
public interface RegisteredComponentRepository extends JpaRepository<RegisteredComponentModel, UUID> {
}