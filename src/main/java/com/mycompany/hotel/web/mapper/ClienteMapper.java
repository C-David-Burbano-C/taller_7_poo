package com.mycompany.hotel.web.mapper;

import com.mycompany.hotel.aplicacion.ClienteBorrador;
import com.mycompany.hotel.dominio.modelo.Cliente;
import com.mycompany.hotel.web.dto.request.ActualizarClienteRequest;
import com.mycompany.hotel.web.dto.response.ClienteResponse;
import com.mycompany.hotel.web.dto.response.ClienteResumenResponse;
import org.mapstruct.BeanMapping;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;
import org.mapstruct.NullValuePropertyMappingStrategy;

@Mapper(uses = {ReservaMapper.class, ConversionesDominio.class})
public interface ClienteMapper {

    ClienteResponse toResponse(Cliente cliente);

    /**
     * Los agregados (total, monto, historial) los calcula la entidad con Streams;
     * el mapper sólo los proyecta. La lista de reservas usa {@link ReservaMapper#toItem}
     * (proyección plana sin referencia al cliente ⇒ sin recursión).
     */
    @Mapping(target = "totalReservasRealizadas", source = "totalReservas")
    @Mapping(target = "montoTotalGastado", source = "montoTotalGastado", qualifiedByName = "dineroACantidad")
    @Mapping(target = "reservasRecientes", source = "historial")
    ClienteResumenResponse toResumen(Cliente cliente);

    /**
     * PATCH seguro: sólo se copian los campos NO nulos del request. El destino es un borrador
     * que únicamente contiene nombre y email, por lo que id, activo, penalizaciones y reservas
     * son inalcanzables (protección contra mass assignment).
     */
    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    void updateClienteFromDto(ActualizarClienteRequest dto, @MappingTarget ClienteBorrador borrador);
}
