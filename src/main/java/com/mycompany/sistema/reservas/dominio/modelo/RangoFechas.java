/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Record.java to edit this template
 */
package com.mycompany.sistema.reservas.dominio.modelo;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;

/**
 *
 * @author daferarte
 */
@Embeddable
public record RangoFechas(
        @Column(name = "fecha_inicio", nullable = false) 
        LocalDateTime fechaInicio, 
        @Column(name = "fecha_fin", nullable = false) 
        LocalDateTime fechaFin
    ) {
    
    public RangoFechas {
        if (fechaInicio == null || fechaFin == null) {
            throw new IllegalArgumentException("Las fechas no pueden ser nulas");
        }
        if (fechaFin.isBefore(fechaInicio)) {
            throw new IllegalArgumentException("La fecha de fin no puede ser anterior a la fecha de inicio");
        }
    }

    public boolean incluye(LocalDateTime fecha) {
        return !fecha.isBefore(fechaInicio) && !fecha.isAfter(fechaFin);
    }
    
    public long duracionEnDias() {
        return ChronoUnit.DAYS.between(fechaInicio, fechaFin);
    }
    
    public long getDias() {
        return duracionEnDias();
    }
}
