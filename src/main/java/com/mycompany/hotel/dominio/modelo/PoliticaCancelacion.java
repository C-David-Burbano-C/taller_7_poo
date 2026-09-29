package com.mycompany.hotel.dominio.modelo;

/**
 * Política de cancelación: cada constante define su propia fórmula de recargo
 * (porcentaje del total si se cancela con menos de {@code diasMinimos} días de anticipación).
 * Agregar una política nueva no requiere modificar a quien la consume.
 */
public enum PoliticaCancelacion {
    FLEXIBLE(0, 0),
    MODERADA(2, 20),
    ESTRICTA(7, 50);

    private final int diasMinimos;
    private final int porcentajeRecargo;

    PoliticaCancelacion(int diasMinimos, int porcentajeRecargo) {
        this.diasMinimos = diasMinimos;
        this.porcentajeRecargo = porcentajeRecargo;
    }

    public Dinero recargo(Dinero total, long diasRestantes) {
        Requisitos.noNulo(total, "El total de la reserva");
        return diasRestantes < diasMinimos ? total.porcentaje(porcentajeRecargo) : Dinero.cero();
    }
}
