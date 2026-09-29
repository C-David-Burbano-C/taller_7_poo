package com.mycompany.hotel.web.dto.response;

import com.mycompany.hotel.dominio.modelo.EstadoHabitacion;
import java.math.BigDecimal;
import java.util.UUID;

public record SuitePresidencialResponse(
        UUID id,
        String numero,
        BigDecimal precioPorNoche,
        int capacidadMaxima,
        EstadoHabitacion estado,
        boolean incluyeMayordomo,
        boolean jacuzziPrivado) implements HabitacionResponse {
}
