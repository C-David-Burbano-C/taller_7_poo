package com.mycompany.hotel.dominio.modelo;

/** Estado operativo de una habitación. */
public enum EstadoHabitacion {
    DISPONIBLE(true),
    OCUPADA(true),
    MANTENIMIENTO(false);

    private final boolean reservable;

    EstadoHabitacion(boolean reservable) {
        this.reservable = reservable;
    }

    public boolean esReservable() {
        return reservable;
    }
}
