package com.mycompany.hotel.dominio.modelo;

/** Resultado de cancelar una reserva: la reserva ya cancelada y el recargo aplicado. */
public record ResultadoCancelacion(Reserva reserva, Dinero recargo) {
}
