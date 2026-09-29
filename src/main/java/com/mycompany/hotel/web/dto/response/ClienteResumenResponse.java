package com.mycompany.hotel.web.dto.response;

import java.math.BigDecimal;
import java.util.List;
import java.util.UUID;

/** Vista ejecutiva del huésped. El monto es BigDecimal (no double) para no perder centavos. */
public record ClienteResumenResponse(
        UUID id,
        String nombre,
        String email,
        boolean activo,
        int penalizaciones,
        int totalReservasRealizadas,
        BigDecimal montoTotalGastado,
        List<ReservaItemResponse> reservasRecientes) {
}
