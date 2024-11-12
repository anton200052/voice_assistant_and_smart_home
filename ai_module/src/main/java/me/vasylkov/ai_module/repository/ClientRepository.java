package me.vasylkov.ai_module.repository;

import me.vasylkov.ai_module.entity.Client;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface ClientRepository extends JpaRepository<Client, Long>  {
    Optional<Client> findClientByUuid(String clientUniqueId);
    void deleteClientByUuid(String clientUniqueId);
}
