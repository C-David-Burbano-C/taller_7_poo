package com.mycompany.hotel.web.dto.response;

import com.mycompany.hotel.dominio.modelo.EstadoHabitacion;
import java.math.BigDecimal;
import java.util.UUID;

public record HabitacionEstandarResponse(
        UUID id,
        String numero,
        BigDecimal precioPorNoche,
        int capacidadMaxima,
        EstadoHabitacion estado,
        int camasIndividuales) implements HabitacionResponse {
}
