package com.mycompany.hotel.web.mapper;

import com.mycompany.hotel.dominio.modelo.Reserva;
import com.mycompany.hotel.dominio.modelo.ResultadoCancelacion;
import com.mycompany.hotel.web.dto.response.CancelacionResponse;
import com.mycompany.hotel.web.dto.response.ReservaItemResponse;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(uses = ConversionesDominio.class)
public interface ReservaMapper {

    @Mapping(target = "idReserva", source = "id")
    @Mapping(target = "numeroHabitacion", source = "habitacion.numero")
    @Mapping(target = "fechaInicio", source = "periodo.fechaInicio")
    @Mapping(target = "fechaFin", source = "periodo.fechaFin")
    ReservaItemResponse toItem(Reserva reserva);

    @Mapping(target = "recargoAplicado", source = "recargo")
    CancelacionResponse toResponse(ResultadoCancelacion resultado);
}
