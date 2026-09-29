package com.mycompany.hotel.web.dto.response;

import java.math.BigDecimal;

public record CancelacionResponse(ReservaItemResponse reserva, BigDecimal recargoAplicado) {
}
