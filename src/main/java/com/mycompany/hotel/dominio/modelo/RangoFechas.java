package com.mycompany.hotel.dominio.modelo;

import com.mycompany.hotel.dominio.excepcion.ValidacionDominioException;
import java.time.LocalDate;
import java.time.temporal.ChronoUnit;

/** Value Object: periodo de estadía [fechaInicio, fechaFin) de al menos una noche. */
public record RangoFechas(LocalDate fechaInicio, LocalDate fechaFin) {

    public RangoFechas {
        Requisitos.noNulo(fechaInicio, "La fecha de inicio");
        Requisitos.noNulo(fechaFin, "La fecha de fin");
        if (!fechaFin.isAfter(fechaInicio)) {
            throw new ValidacionDominioException("La fecha de fin debe ser posterior a la fecha de inicio");
        }
    }

    public long noches() {
        return ChronoUnit.DAYS.between(fechaInicio, fechaFin);
    }

    /** Dos periodos se solapan si comparten al menos una noche (el día de salida no cuenta). */
    public boolean solapaCon(RangoFechas otro) {
        Requisitos.noNulo(otro, "El otro periodo");
        return fechaInicio.isBefore(otro.fechaFin) && otro.fechaInicio.isBefore(fechaFin);
    }

    public long diasHasta(LocalDate desde) {
        Requisitos.noNulo(desde, "La fecha de referencia");
        return Math.max(0, ChronoUnit.DAYS.between(desde, fechaInicio));
    }
}
