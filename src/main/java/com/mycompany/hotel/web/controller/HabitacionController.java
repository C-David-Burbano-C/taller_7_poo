package com.mycompany.hotel.web.controller;

import com.mycompany.hotel.aplicacion.HabitacionService;
import com.mycompany.hotel.dominio.modelo.Habitacion;
import com.mycompany.hotel.web.dto.request.CrearHabitacionEstandarRequest;
import com.mycompany.hotel.web.dto.request.CrearHabitacionRequest;
import com.mycompany.hotel.web.dto.request.CrearSuitePresidencialRequest;
import com.mycompany.hotel.web.dto.response.HabitacionResponse;
import com.mycompany.hotel.web.mapper.HabitacionMapper;
import jakarta.validation.Valid;
import java.net.URI;
import java.util.List;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/** Un único controlador para toda la jerarquía de habitaciones. */
@RestController
@RequestMapping("/api/habitaciones")
public class HabitacionController {

    private final HabitacionService servicio;
    private final HabitacionMapper mapper;

    public HabitacionController(HabitacionService servicio, HabitacionMapper mapper) {
        this.servicio = servicio;
        this.mapper = mapper;
    }

    @PostMapping("/estandar")
    public ResponseEntity<HabitacionResponse> crearEstandar(@Valid @RequestBody CrearHabitacionEstandarRequest request) {
        return crear(request);
    }

    @PostMapping("/suites")
    public ResponseEntity<HabitacionResponse> crearSuite(@Valid @RequestBody CrearSuitePresidencialRequest request) {
        return crear(request);
    }

    @GetMapping
    public List<HabitacionResponse> listar() {
        return servicio.listar().stream().map(mapper::toResponse).toList();
    }

    private ResponseEntity<HabitacionResponse> crear(CrearHabitacionRequest request) {
        Habitacion creada = servicio.crear(mapper.toComando(request));
        return ResponseEntity.created(URI.create("/api/habitaciones/" + creada.getId()))
                .body(mapper.toResponse(creada));
    }
}
