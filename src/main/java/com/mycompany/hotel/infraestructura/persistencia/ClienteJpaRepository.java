package com.mycompany.hotel.infraestructura.persistencia;

import java.util.UUID;
import org.springframework.data.jpa.repository.JpaRepository;

interface ClienteJpaRepository extends JpaRepository<ClienteJpa, UUID> {

    boolean existsByEmail(String email);
}
