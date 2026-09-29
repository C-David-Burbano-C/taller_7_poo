package com.mycompany.hotel.web.dto.request;

/**
 * Jerarquía cerrada de requests de alta de habitaciones. Cada subtipo se expone en su
 * propia ruta ({@code /estandar}, {@code /suites}) pero comparten un único controlador.
 */
public sealed interface CrearHabitacionRequest
        permits CrearHabitacionEstandarRequest, CrearSuitePresidencialRequest {
}
