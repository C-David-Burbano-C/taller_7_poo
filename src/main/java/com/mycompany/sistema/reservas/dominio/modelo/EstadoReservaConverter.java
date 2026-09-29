/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.sistema.reservas.dominio.modelo;

/**
 *
 * @author daferarte
 */

import jakarta.persistence.AttributeConverter;
import jakarta.persistence.Converter;

@Converter(autoApply = false)
public class EstadoReservaConverter implements AttributeConverter<comportamentales.state.EstadoReserva, String> {    
    @Override
    public String convertToDatabaseColumn(comportamentales.state.EstadoReserva estado) {
        if (estado == null) {
            return null;
        }
        if (estado instanceof comportamentales.state.EstadoPendiente) {
            return "PENDIENTE";
        }
        if (estado instanceof comportamentales.state.EstadoConfirmada) {
            return "CONFIRMADA";
        }
        if (estado instanceof comportamentales.state.EstadoCancelada) {
            return "CANCELADA";
        }
        throw new IllegalArgumentException("Estado desconocido: " + estado.getClass().getName());
    }

    @Override
    public comportamentales.state.EstadoReserva convertToEntityAttribute(String dbData) {
        if (dbData == null) {
            return null;
        }
        return switch (dbData.toUpperCase()) {
            case "PENDIENTE" -> new comportamentales.state.EstadoPendiente();
            case "CONFIRMADA" -> new comportamentales.state.EstadoConfirmada();
            case "CANCELADA" -> new comportamentales.state.EstadoCancelada();
            default -> throw new IllegalArgumentException("Valor de base de datos no soportado: " + dbData);
        };
    }
}
