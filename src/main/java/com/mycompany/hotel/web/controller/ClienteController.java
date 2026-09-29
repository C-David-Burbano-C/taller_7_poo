package com.mycompany.hotel.web.controller;

import com.mycompany.hotel.aplicacion.ClienteService;
import com.mycompany.hotel.aplicacion.ReservaService;
import com.mycompany.hotel.dominio.modelo.Cliente;
import com.mycompany.hotel.web.dto.request.ActualizarClienteRequest;
import com.mycompany.hotel.web.dto.request.CrearClienteRequest;
import com.mycompany.hotel.web.dto.request.CrearReservaRequest;
import com.mycompany.hotel.web.dto.response.CancelacionResponse;
import com.mycompany.hotel.web.dto.response.ClienteResponse;
import com.mycompany.hotel.web.dto.response.ClienteResumenResponse;
import com.mycompany.hotel.web.dto.response.ReservaItemResponse;
import com.mycompany.hotel.web.mapper.ClienteMapper;
import com.mycompany.hotel.web.mapper.ReservaMapper;
import jakarta.validation.Valid;
import java.net.URI;
import java.util.UUID;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/clientes")
public class ClienteController {

    private final ClienteService clientes;
    private final ReservaService reservas;
    private final ClienteMapper clienteMapper;
    private final ReservaMapper reservaMapper;

    public ClienteController(ClienteService clientes, ReservaService reservas,
            ClienteMapper clienteMapper, ReservaMapper reservaMapper) {
        this.clientes = clientes;
        this.reservas = reservas;
        this.clienteMapper = clienteMapper;
        this.reservaMapper = reservaMapper;
    }

    @PostMapping
    public ResponseEntity<ClienteResponse> crear(@Valid @RequestBody CrearClienteRequest request) {
        Cliente creado = clientes.registrar(request.nombre(), request.email());
        return ResponseEntity.created(URI.create("/api/clientes/" + creado.getId()))
                .body(clienteMapper.toResponse(creado));
    }

    @GetMapping("/{id}")
    public ClienteResponse obtener(@PathVariable UUID id) {
        return clienteMapper.toResponse(clientes.obtener(id));
    }

    @GetMapping("/{id}/resumen")
    public ClienteResumenResponse resumen(@PathVariable UUID id) {
        return clienteMapper.toResumen(clientes.obtener(id));
    }

    /** Actualización parcial: sólo se modifican los campos enviados. */
    @PatchMapping("/{id}")
    public ClienteResponse actualizar(@PathVariable UUID id, @RequestBody ActualizarClienteRequest request) {
        Cliente actualizado = clientes.actualizarDatos(id, borrador -> clienteMapper.updateClienteFromDto(request, borrador));
        return clienteMapper.toResponse(actualizado);
    }

    // --- Reservas del cliente ------------------------------------------------------------

    @PostMapping("/{id}/reservas")
    public ResponseEntity<ReservaItemResponse> reservar(@PathVariable UUID id,
            @Valid @RequestBody CrearReservaRequest request) {
        var reserva = reservas.reservar(id, request.habitacionId(), request.fechaInicio(),
                request.fechaFin(), request.politica());
        return ResponseEntity.created(URI.create("/api/clientes/" + id + "/reservas/" + reserva.getId()))
                .body(reservaMapper.toItem(reserva));
    }

    @PostMapping("/{id}/reservas/{reservaId}/confirmar")
    public ReservaItemResponse confirmar(@PathVariable UUID id, @PathVariable UUID reservaId) {
        return reservaMapper.toItem(reservas.confirmar(id, reservaId));
    }

    @PostMapping("/{id}/reservas/{reservaId}/cancelar")
    public CancelacionResponse cancelar(@PathVariable UUID id, @PathVariable UUID reservaId) {
        return reservaMapper.toResponse(reservas.cancelar(id, reservaId));
    }
}
