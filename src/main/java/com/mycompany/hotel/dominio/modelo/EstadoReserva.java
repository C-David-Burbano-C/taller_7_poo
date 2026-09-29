package com.mycompany.hotel.dominio.modelo;

/**
 * Ciclo de vida de una reserva. Las reglas de qué transiciones son legales viven aquí,
 * no en switches externos: agregar un estado nuevo no obliga a tocar a Reserva.
 *
 * <pre>PENDIENTE → CONFIRMADA → CANCELADA
 *PENDIENTE → CANCELADA</pre>
 */
public enum EstadoReserva {
    PENDIENTE(true, true, true),
    CONFIRMADA(false, true, true),
    CANCELADA(false, false, false);

    private final boolean permiteConfirmar;
    private final boolean permiteCancelar;
    private final boolean activa;

    EstadoReserva(boolean permiteConfirmar, boolean permiteCancelar, boolean activa) {
        this.permiteConfirmar = permiteConfirmar;
        this.permiteCancelar = permiteCancelar;
        this.activa = activa;
    }

    public boolean permiteConfirmar() {
        return permiteConfirmar;
    }

    public boolean permiteCancelar() {
        return permiteCancelar;
    }

    /** Una reserva activa bloquea la habitación en sus fechas. */
    public boolean estaActiva() {
        return activa;
    }
}
