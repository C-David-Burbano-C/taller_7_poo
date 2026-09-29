package com.mycompany.hotel.infraestructura.persistencia;

import java.util.UUID;
import org.springframework.data.jpa.repository.JpaRepository;

interface HabitacionJpaRepository extends JpaRepository<HabitacionJpa, UUID> {

    boolean existsByNumero(String numero);
}
