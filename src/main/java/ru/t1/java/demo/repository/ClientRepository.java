package ru.t1.java.demo.repository;

import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import ru.t1.java.demo.dto.ClientStatusDto;
import ru.t1.java.demo.model.Client;


import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface ClientRepository extends JpaRepository<Client, Long> {

    Optional<Client> findByClientId(UUID clientId);

    @Query("SELECT new ru.t1.java.demo.dto.ClientStatusDto(c.status, c.clientId) FROM Client c" )
    List<ClientStatusDto> clientsToUnlock(Pageable pageable);

}