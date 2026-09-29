package com.mycompany.hotel.aplicacion;

import com.mycompany.hotel.dominio.modelo.CapacidadMaxima;
import com.mycompany.hotel.dominio.modelo.Dinero;
import com.mycompany.hotel.dominio.modelo.Habitacion;
import com.mycompany.hotel.dominio.modelo.HabitacionEstandar;
import com.mycompany.hotel.dominio.modelo.NumeroHabitacion;
import com.mycompany.hotel.dominio.modelo.SuitePresidencial;
import java.math.BigDecimal;

/**
 * Comando de aplicación (independiente de HTTP) para dar de alta una habitación.
 * Cada variante sabe construir su propia entidad: el servicio no necesita un switch por tipo.
 */
public sealed interface ComandoCrearHabitacion {

    Habitacion construir();

    record Estandar(String numero, BigDecimal precioPorNoche, int capacidadMaxima, int camasIndividuales)
            implements ComandoCrearHabitacion {

        @Override
        public Habitacion construir() {
            return HabitacionEstandar.crear(new NumeroHabitacion(numero), Dinero.de(precioPorNoche),
                    new CapacidadMaxima(capacidadMaxima), camasIndividuales);
        }
    }

    record Suite(String numero, BigDecimal precioPorNoche, int capacidadMaxima,
            boolean incluyeMayordomo, boolean jacuzziPrivado) implements ComandoCrearHabitacion {

        @Override
        public Habitacion construir() {
            return SuitePresidencial.crear(new NumeroHabitacion(numero), Dinero.de(precioPorNoche),
                    new CapacidadMaxima(capacidadMaxima), incluyeMayordomo, jacuzziPrivado);
        }
    }
}
