package com.mycompany.hotel.web.dto.response;

import com.mycompany.hotel.dominio.modelo.EstadoReserva;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.UUID;

/** Proyección plana de una reserva: NO referencia de vuelta al cliente (evita ciclos). */
public record ReservaItemResponse(
        UUID idReserva,
        String numeroHabitacion,
        LocalDate fechaInicio,
        LocalDate fechaFin,
        EstadoReserva estado,
        BigDecimal costoTotal) {
}
