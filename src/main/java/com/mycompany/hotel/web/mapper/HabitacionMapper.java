package com.mycompany.hotel.web.mapper;

import com.mycompany.hotel.aplicacion.ComandoCrearHabitacion;
import com.mycompany.hotel.dominio.modelo.Habitacion;
import com.mycompany.hotel.dominio.modelo.HabitacionEstandar;
import com.mycompany.hotel.dominio.modelo.SuitePresidencial;
import com.mycompany.hotel.web.dto.request.CrearHabitacionEstandarRequest;
import com.mycompany.hotel.web.dto.request.CrearHabitacionRequest;
import com.mycompany.hotel.web.dto.request.CrearSuitePresidencialRequest;
import com.mycompany.hotel.web.dto.response.HabitacionEstandarResponse;
import com.mycompany.hotel.web.dto.response.HabitacionResponse;
import com.mycompany.hotel.web.dto.response.SuitePresidencialResponse;
import org.mapstruct.Mapper;
import org.mapstruct.ObjectFactory;
import org.mapstruct.SubclassMapping;

/**
 * Mapeo polimórfico: MapStruct genera una cadena de {@code instanceof} que elige en tiempo de
 * ejecución la proyección exacta, conservando camasIndividuales / jacuzziPrivado / incluyeMayordomo.
 */
@Mapper(uses = ConversionesDominio.class)
public interface HabitacionMapper {

    // --- Salida: dominio → response -----------------------------------------------------
    @SubclassMapping(source = HabitacionEstandar.class, target = HabitacionEstandarResponse.class)
    @SubclassMapping(source = SuitePresidencial.class, target = SuitePresidencialResponse.class)
    HabitacionResponse toResponse(Habitacion habitacion);

    /** Sólo se ejecuta si aparece un subtipo sin @SubclassMapping: falla en vez de devolver datos truncados. */
    @ObjectFactory
    default HabitacionResponse subtipoNoSoportado(Habitacion habitacion) {
        throw new IllegalArgumentException("Subtipo de habitación sin proyección: " + habitacion.getClass());
    }

    HabitacionEstandarResponse toResponse(HabitacionEstandar habitacion);

    SuitePresidencialResponse toResponse(SuitePresidencial habitacion);

    // --- Entrada: request → comando de aplicación ---------------------------------------
    @SubclassMapping(source = CrearHabitacionEstandarRequest.class, target = ComandoCrearHabitacion.Estandar.class)
    @SubclassMapping(source = CrearSuitePresidencialRequest.class, target = ComandoCrearHabitacion.Suite.class)
    ComandoCrearHabitacion toComando(CrearHabitacionRequest request);

    @ObjectFactory
    default ComandoCrearHabitacion subtipoNoSoportado(CrearHabitacionRequest request) {
        throw new IllegalArgumentException("Subtipo de request sin comando: " + request.getClass());
    }

    ComandoCrearHabitacion.Estandar toComando(CrearHabitacionEstandarRequest request);

    ComandoCrearHabitacion.Suite toComando(CrearSuitePresidencialRequest request);
}
