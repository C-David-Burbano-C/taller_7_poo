package com.mycompany.hotel.aplicacion;

import com.mycompany.hotel.dominio.excepcion.EntidadNoEncontradaException;
import com.mycompany.hotel.dominio.modelo.Cliente;
import com.mycompany.hotel.dominio.modelo.Habitacion;
import com.mycompany.hotel.dominio.modelo.PoliticaCancelacion;
import com.mycompany.hotel.dominio.modelo.RangoFechas;
import com.mycompany.hotel.dominio.modelo.Reserva;
import com.mycompany.hotel.dominio.modelo.ResultadoCancelacion;
import com.mycompany.hotel.dominio.puertos.ClienteRepository;
import com.mycompany.hotel.dominio.puertos.DisponibilidadHabitaciones;
import com.mycompany.hotel.dominio.puertos.HabitacionRepository;
import java.time.Clock;
import java.time.LocalDate;
import java.util.UUID;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/** Orquesta reservas entre los agregados Cliente y Habitacion. */
@Service
public class ReservaService {

    private final ClienteRepository clientes;
    private final HabitacionRepository habitaciones;
    private final DisponibilidadHabitaciones disponibilidad;
    private final Clock reloj;

    public ReservaService(ClienteRepository clientes, HabitacionRepository habitaciones,
            DisponibilidadHabitaciones disponibilidad, Clock reloj) {
        this.clientes = clientes;
        this.habitaciones = habitaciones;
        this.disponibilidad = disponibilidad;
        this.reloj = reloj;
    }

    @Transactional
    public Reserva reservar(UUID clienteId, UUID habitacionId, LocalDate inicio, LocalDate fin,
            PoliticaCancelacion politica) {
        Cliente cliente = cargarCliente(clienteId);
        Habitacion habitacion = habitaciones.buscarPorId(habitacionId)
                .orElseThrow(() -> new EntidadNoEncontradaException("Habitación", habitacionId));
        Reserva reserva = cliente.reservar(habitacion, new RangoFechas(inicio, fin),
                politica == null ? PoliticaCancelacion.MODERADA : politica, disponibilidad);
        clientes.guardar(cliente);
        return reserva;
    }

    @Transactional
    public Reserva confirmar(UUID clienteId, UUID reservaId) {
        Cliente cliente = cargarCliente(clienteId);
        Reserva reserva = cliente.confirmarReserva(reservaId);
        clientes.guardar(cliente);
        return reserva;
    }

    @Transactional
    public ResultadoCancelacion cancelar(UUID clienteId, UUID reservaId) {
        Cliente cliente = cargarCliente(clienteId);
        ResultadoCancelacion resultado = cliente.cancelarReserva(reservaId, LocalDate.now(reloj));
        clientes.guardar(cliente);
        return resultado;
    }

    private Cliente cargarCliente(UUID id) {
        return clientes.buscarPorId(id).orElseThrow(() -> new EntidadNoEncontradaException("Cliente", id));
    }
}
