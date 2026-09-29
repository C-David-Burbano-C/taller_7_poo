package com.mycompany.hotel.dominio.modelo;

import com.mycompany.hotel.dominio.excepcion.ValidacionDominioException;
import java.util.UUID;

/** Habitación estándar: se distingue por su número de camas individuales. */
public final class HabitacionEstandar extends Habitacion {

    private final int camasIndividuales;

    private HabitacionEstandar(NumeroHabitacion numero, Dinero precio, CapacidadMaxima capacidad, int camas) {
        super(numero, precio, capacidad);
        this.camasIndividuales = validarCamas(camas);
    }

    private HabitacionEstandar(UUID id, NumeroHabitacion numero, Dinero precio, CapacidadMaxima capacidad,
            EstadoHabitacion estado, int camas) {
        super(id, numero, precio, capacidad, estado);
        this.camasIndividuales = validarCamas(camas);
    }

    public static HabitacionEstandar crear(NumeroHabitacion numero, Dinero precioPorNoche,
            CapacidadMaxima capacidadMaxima, int camasIndividuales) {
        return new HabitacionEstandar(numero, precioPorNoche, capacidadMaxima, camasIndividuales);
    }

    public static HabitacionEstandar reconstituir(UUID id, NumeroHabitacion numero, Dinero precioPorNoche,
            CapacidadMaxima capacidadMaxima, EstadoHabitacion estado, int camasIndividuales) {
        return new HabitacionEstandar(id, numero, precioPorNoche, capacidadMaxima, estado, camasIndividuales);
    }

    private static int validarCamas(int camas) {
        if (camas < 1) {
            throw new ValidacionDominioException("Una habitación estándar debe tener al menos 1 cama individual");
        }
        return camas;
    }

    @Override
    public TipoHabitacion tipo() {
        return TipoHabitacion.ESTANDAR;
    }

    public int getCamasIndividuales() {
        return camasIndividuales;
    }
}
