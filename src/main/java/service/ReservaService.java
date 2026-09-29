package service;

import com.mycompany.sistema.reservas.dominio.modelo.RangoFechas;
import com.mycompany.sistema.reservas.dominio.modelo.Reserva;
import java.util.UUID;

/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */

/**
 *
 * @author daferarte
 */
public interface ReservaService {
    Reserva crearReserva(UUID clienteId, UUID habitacionId, RangoFechas periodo);
    Reserva confirmarReserva(UUID reservaId);
    Reserva cancelarReserva(UUID reservaId);
}
