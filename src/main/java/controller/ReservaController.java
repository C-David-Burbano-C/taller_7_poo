package controller;

import com.mycompany.sistema.reservas.dominio.modelo.Reserva;
import java.util.UUID;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import service.ReservaService;

/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */

/**
 *
 * @author daferarte
 */
@RestController
@RequestMapping("/api/reservas")
public class ReservaController {

    private final ReservaService reservaService;

    public ReservaController(ReservaService reservaService) {
        this.reservaService = reservaService;
    }

    @PatchMapping("/{id}/confirmar")
    public ResponseEntity<String> confirmar(@PathVariable UUID id) {
        Reserva confirmada = reservaService.confirmarReserva(id);
        return ResponseEntity.ok("Reserva " + confirmada.getId() + " confirmada con éxito");
    }
}
