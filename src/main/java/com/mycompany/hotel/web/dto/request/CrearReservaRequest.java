package com.mycompany.hotel.web.dto.request;

import com.mycompany.hotel.dominio.modelo.PoliticaCancelacion;
import jakarta.validation.constraints.NotNull;
import java.time.LocalDate;
import java.util.UUID;

/** {@code politica} es opcional (por defecto MODERADA). */
public record CrearReservaRequest(
        @NotNull UUID habitacionId,
        @NotNull LocalDate fechaInicio,
        @NotNull LocalDate fechaFin,
        PoliticaCancelacion politica) {
}
