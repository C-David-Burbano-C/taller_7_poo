package com.mycompany.hotel.infraestructura.persistencia;

import com.mycompany.hotel.dominio.modelo.EstadoReserva;
import java.time.LocalDate;
import java.util.Collection;
import java.util.UUID;
import org.springframework.data.jpa.repository.JpaRepository;

interface ReservaJpaRepository extends JpaRepository<ReservaJpa, UUID> {

    /** Solapamiento de [inicio, fin) con reservas cuyo estado bloquea la habitación. */
    boolean existsByHabitacion_IdAndEstadoInAndFechaInicioLessThanAndFechaFinGreaterThan(
            UUID habitacionId, Collection<EstadoReserva> estados, LocalDate fin, LocalDate inicio);
}
