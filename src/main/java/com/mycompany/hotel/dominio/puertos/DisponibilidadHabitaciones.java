package com.mycompany.hotel.dominio.puertos;

import com.mycompany.hotel.dominio.modelo.Habitacion;
import com.mycompany.hotel.dominio.modelo.RangoFechas;

/** Puerto (definido por el dominio): ¿está libre la habitación en ese periodo? */
@FunctionalInterface
public interface DisponibilidadHabitaciones {

    boolean estaLibre(Habitacion habitacion, RangoFechas periodo);
}
