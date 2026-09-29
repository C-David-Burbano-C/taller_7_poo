package com.mycompany.hotel.infraestructura.persistencia;

import com.mycompany.hotel.dominio.modelo.EstadoReserva;
import com.mycompany.hotel.dominio.modelo.Habitacion;
import com.mycompany.hotel.dominio.modelo.RangoFechas;
import com.mycompany.hotel.dominio.puertos.DisponibilidadHabitaciones;
import java.util.Arrays;
import java.util.List;
import org.springframework.stereotype.Component;

@Component
class DisponibilidadHabitacionesJpa implements DisponibilidadHabitaciones {

    private final ReservaJpaRepository reservas;

    DisponibilidadHabitacionesJpa(ReservaJpaRepository reservas) {
        this.reservas = reservas;
    }

    @Override
    public boolean estaLibre(Habitacion habitacion, RangoFechas periodo) {
        List<EstadoReserva> bloqueantes = Arrays.stream(EstadoReserva.values())
                .filter(EstadoReserva::estaActiva).toList();
        return !reservas.existsByHabitacion_IdAndEstadoInAndFechaInicioLessThanAndFechaFinGreaterThan(
                habitacion.getId(), bloqueantes, periodo.fechaFin(), periodo.fechaInicio());
    }
}
