package com.mycompany.hotel.dominio.excepcion;

/** La habitación no puede reservarse (mantenimiento o fechas ya ocupadas). */
public class HabitacionNoDisponibleException extends ReglaNegocioException {

    public HabitacionNoDisponibleException(String mensaje) {
        super(mensaje);
    }
}
