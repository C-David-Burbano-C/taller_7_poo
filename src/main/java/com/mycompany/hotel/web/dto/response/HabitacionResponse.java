package com.mycompany.hotel.web.dto.response;

import com.fasterxml.jackson.annotation.JsonSubTypes;
import com.fasterxml.jackson.annotation.JsonTypeInfo;
import com.mycompany.hotel.dominio.modelo.EstadoHabitacion;
import java.math.BigDecimal;
import java.util.UUID;

/**
 * Contrato de salida polimórfico (sealed). Jackson escribe el discriminador
 * {@code "tipo": "ESTANDAR" | "SUITE"} en cada elemento.
 */
@JsonTypeInfo(use = JsonTypeInfo.Id.NAME, include = JsonTypeInfo.As.PROPERTY, property = "tipo")
@JsonSubTypes({
    @JsonSubTypes.Type(value = HabitacionEstandarResponse.class, name = "ESTANDAR"),
    @JsonSubTypes.Type(value = SuitePresidencialResponse.class, name = "SUITE")
})
public sealed interface HabitacionResponse permits HabitacionEstandarResponse, SuitePresidencialResponse {

    UUID id();

    String numero();

    BigDecimal precioPorNoche();

    int capacidadMaxima();

    EstadoHabitacion estado();
}
